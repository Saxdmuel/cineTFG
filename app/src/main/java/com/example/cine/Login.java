package com.example.cine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Fragment {

    static Conexion conexion;

    public Login() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conexion = new Conexion();
        try {
            conexion.conectarSql(); //concecto con la base de datos
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        Boolean centinela = true;
        while (centinela) { //espero a estar conectado
            if (conexion.getEstadoConexion()) {
                centinela = false;
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        System.out.println("LOGIN");
        SQLite lite = new SQLite(getContext());

        String nombreUsuario = lite.usuarioConectado(getContext()); //metodo que devuelve el usuario conectado previamente
        if(nombreUsuario !="vacio"){ //si hau algun usuario conectado
            String permisos = Conexion.buscarPermisos(nombreUsuario); //busco los permisos
            Bundle bundle = new Bundle();
            bundle.putString("permisos",permisos);
            getParentFragmentManager().setFragmentResult("keyPermisos",bundle); //le paso el bundle al fragment
            Navigation.findNavController(getView()).navigate(R.id.inicio); //voy a inicio
        }else{
            Button botonLogin = view.findViewById(R.id.btnLogin);
            Button botonRegistrar = view.findViewById(R.id.btnRegistrar);
            EditText textoUsuario = view.findViewById(R.id.EdtUsuario);
            EditText textoPass = view.findViewById(R.id.EdtPassword);

            //boton para logear
            botonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //metodo que comprueba si existe el usuario y da error si no
                    Boolean centUsuario =Conexion.buscarUsuario(textoUsuario.getText().toString());
                    Boolean centPass=false;
                    if(centUsuario){ //si existe el ususario compruebo si la contraseña le corresponde
                        //metodo que comprueba si la contraseña es correcta y da error si no
                        centPass = Conexion.buscarPass(textoPass.getText().toString(),textoUsuario.getText().toString());
                    }
                    else {
                        errorLogin(); //metodo que muestra el error
                    }
                    if (centPass){ //si usuario y contraseña son correctos

                        String permisos = Conexion.buscarPermisos(textoUsuario.getText().toString()); //establezco los permisos
                        SQLite lite = new SQLite(getContext());
                        lite.insertarUsuario(getContext(),textoUsuario.getText().toString()); //inserte el usuario para recordarlo
                        //creo el bundle y le pongo los parametros
                        Bundle bundle = new Bundle();
                        bundle.putString("permisos",permisos);
                        getParentFragmentManager().setFragmentResult("keyPermisos",bundle); //le paso el bundle al fragment
                        Navigation.findNavController(v).navigate(R.id.inicio); //voy a inicio
                    }
                    else{
                        errorLogin(); //metodo que muestra el error
                    }
                }
            });
            //boton que lleva a registar
            botonRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(R.id.registro); //voy a registrar
                }
            });
        }
        }

    //metodo que muestra el error
    public void errorLogin(){
        Toast error =  Toast.makeText(getContext(),"Usuario o contraseña icorrectos",Toast.LENGTH_LONG);
        error.show();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction(); //reinicio el fragment
        transaction.replace(R.id.fragmentContainerView2, new Login());
        transaction.commit();
    }
    public void irInicio(){

    }
}