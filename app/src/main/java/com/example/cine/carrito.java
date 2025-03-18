package com.example.cine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class carrito extends Fragment {


    public carrito() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_carrito, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SQLite lite = new SQLite(getContext());
        List<String> listaArticulos = lite.obtenerCarrito(getContext()); //metodo que llena la lista con el carrito
        LinearLayout ly = view.findViewById(R.id.lyCarrito);

        //creo los textView dependiendo de la cantidad de articulos comprados
        for(int i = 0; i<listaArticulos.size();i++) {
            TextView textView = new TextView(getContext());
            if (textView.getParent() != null) {
                ((ViewGroup) textView.getParent()).removeView(textView);
            }
            textView.setText(listaArticulos.get(i));
            ly.addView(textView); //añado los textView al layout
        }
        TextView tvInicio = view.findViewById(R.id.TVinicio);
        TextView tvSnacks = view.findViewById(R.id.txtSnackCarrito);
        TextView tvSumatorio = view.findViewById(R.id.tvSumatorio);
        String total =lite.sumarPrecios(getContext())+"€"; //metodo que suma los precios de los articulos comprados
        tvSumatorio.setText(total); //muestra el total


        Button botonComprar = view.findViewById(R.id.btnComprarCarrito);
        Button botonBorrar = view.findViewById(R.id.btnBorrarCarrito);
        //boton para ir a inicio
        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listaArticulos.size()==0){ //compruebo si el carro esta vacio, si lo esta doy error
                    Toast toast = Toast.makeText(getContext(), "El carro esta vacio", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    try {
                        String articulos = "";
                        for (int i=0;i< listaArticulos.size();i++){

                            articulos = articulos +"\n"+listaArticulos.get(i);
                        }
                        articulos= articulos+"\nTotal = "+total;
                        String destinatario = Conexion.buscarEmail(lite.usuarioConectado(getContext()));
                        EnviarCorreo.enviarCorreo("salmeronCine@gmail.com","kpzt hlfg isyf wwns",
                                destinatario,"cine",articulos);
                        Navigation.findNavController(view).navigate(R.id.inicio);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }

                }
            }
        });
        //boton que borra el carrito
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite.vaciarCarrito(getContext()); //metodo que borra el carrito de la BBDD SQLite

                // Obtiene el administrador de fragmentos para iniciar una transacción.
                // Esto se utiliza para reiniciar o reemplazar el fragmento actual.
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                //cambia el fragmento actual por uno nuevo
                transaction.replace(R.id.fragmentContainerView2, new carrito());

                //realiza los cambios
                transaction.commit();
            }
        });


        //ir a inicio
        tvInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.inicio);
            }
        });
        //ir a snacks
        tvSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.snack);
            }
        });

    }
}