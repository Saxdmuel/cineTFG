package com.example.cine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Snack extends Fragment {
    String permisos;
    RecyclerView rw;
    public Snack() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_snack, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SQLite lite = new SQLite(getContext());
        String usuario = lite.usuarioConectado(getContext());//metodo que devuelve el usuario conectado
        permisos = Conexion.buscarPermisos(usuario); //metodo que devuelve los permisos del usuario

        if (permisos.equals("administrador")){ //comprueba si el usuario es administrador
            Button btnCrearSnack = view.findViewById(R.id.btnCrearSnack);
            Button btnBorrarSnack = view.findViewById(R.id.btnBorrarSnack);
            Button btnCambiarSnack = view.findViewById(R.id.btnCambiarSnack);

            //pongo los botones visibles para el administrador
            btnCrearSnack.setVisibility(View.VISIBLE);
            btnBorrarSnack.setVisibility(View.VISIBLE);
            btnCambiarSnack.setVisibility(View.VISIBLE);

            //boton crear snacks
            btnCrearSnack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentCrearSnack fcs = new FragmentCrearSnack(); //creo un fragment de crear snack
                    fcs.show(getParentFragmentManager(),FragmentCrearSnack.TAG); //muestro el fragment
                }
            });
            //boton borrar snack
            btnBorrarSnack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentBorrarSnack fbs = new FragmentBorrarSnack(); //creo un fragment de borrar snack
                    fbs.show(getParentFragmentManager(),FragmentBorrarPelicula.TAG); //muestro el fragment
                }
            });
            //boton cambiar snack
            btnCambiarSnack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentEditarSnack fes = new FragmentEditarSnack(); //creo un fragment de editar snack
                    fes.show(getParentFragmentManager(),FragmentEditarSnack.TAG); //muestro el fragment

                }
            });
        }
        //leo el bundle
        getParentFragmentManager().setFragmentResultListener("keySnacks", this, new FragmentResultListener() {

            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

            }

        });
        TextView tvInicio = view.findViewById(R.id.TVinicio);
        ImageView ivCarrito = view.findViewById(R.id.IVcarrito);
        Button btnComprarSnacks = view.findViewById(R.id.btnComprarSnacks);


        List <String> listaNombres = new ArrayList<>();
        listaNombres= Conexion.buscarSnacks(); //relleno la lista con los snacks de la base de datos
        List <String> listaPrecios = new ArrayList<>();
        listaPrecios= Conexion.buscarPreciosSnacks(); //relleno la lista con de los precuios de la base de datos



        Adaptador adaptador = new Adaptador(listaNombres,listaPrecios);  //creo un adaptador para rellenar el Recycler
        rw = view.findViewById(R.id.RecyclerView);
        LinearLayoutManager lym = new LinearLayoutManager(this.getContext()); //le asigno un lyout al RecyclerView
        rw.setLayoutManager(lym);
        rw.setAdapter(adaptador); //le asigno el adaptqador al RecyvlerView


        List<String> finalListaNombres = listaNombres; //se crea una igual porque sino da fallo al haberla usado antes
        List<String> finalListaPrecios = listaPrecios;

        //boton comprar snacks
        btnComprarSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i<adaptador.listaCantidad.size(); i++){
                    //leo la cantidad a comprar de cada snack
                    int cantidad = Integer.parseInt(adaptador.listaCantidad.get(i).getText().toString());
                    if (cantidad > 0){
                        //metodo que inserta el snack en SQLite
                        lite.insertarSnacksLite(getContext(), finalListaNombres.get(i), Integer.parseInt(finalListaPrecios.get(i)),cantidad);
                    }
                }
            }
        });

        //ir a inicio
        tvInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.inicio);
            }
        });

        //ir a carrito
        ivCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.carrito);
            }
        });
    }
}