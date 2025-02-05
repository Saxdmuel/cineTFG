package com.example.cine;

import static java.security.AccessController.getContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends Fragment {

    public Registro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //ir a inicio
        TextView txtInicio = view.findViewById(R.id.txtLogin);
        txtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.login);
            }
        });

        //boton cancelar

        Button btnCancelar = view.findViewById(R.id.btnCancelarRegistro);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.inicio);   //voy a inicio
            }
        });

        //registar usuario
        EditText usuario= view.findViewById(R.id.edtUsuario);
        EditText password= view.findViewById(R.id.edtPass);
        EditText email= view.findViewById(R.id.edtEmail);
        EditText repEmail= view.findViewById(R.id.edtRepEmail);
        EditText repPassword= view.findViewById(R.id.edtRepPass);
        Button btnRegistrar =view.findViewById(R.id.btnAceptarRegistro);
        //boton que acepta el registro
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //compruebo si E-mail y repetir E-mail  coinciden
                if(email.getText().toString().equals(repEmail.getText().toString())){

                    //compruebo si contraseña y repetir contraseña  coinciden
                    if(password.getText().toString().equals(repPassword.getText().toString())){

                        if(password.getText().toString().length() >= 8){ //compruebo que la contraseña tenga 8 o mas caracteres
                            if(Conexion.usuarioExiste(usuario.getText().toString(),getContext())){
                                //si todo esta bien creo el usuario
                                //metodo que crea el usuario
                                Conexion.crearUsuario(usuario.getText().toString(),password.getText().toString(),email.getText().toString(),"usuario");
                                Toast.makeText(getContext(),"Usuario creado con exito", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(view).navigate(R.id.login);
                            }
                        }else{ //creo y muestro error
                            Toast.makeText(getContext(),"La contraseña debe tener 8 o mas caracteres ", Toast.LENGTH_LONG).show();
                        }
                    }else{//creo y muestro error
                        Toast.makeText(getContext(),"Contraseña y repetir contraseña no coinciden ", Toast.LENGTH_LONG).show();
                    }
                }else{//creo y muestro error
                    Toast.makeText(getContext(),"E-mail y repetir E-mail no coinciden ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}