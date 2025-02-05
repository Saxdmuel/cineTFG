package com.example.cine;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class Sala extends Fragment {
    Boolean[] arrayAsientos;

    List<String> asientosCambiar = new ArrayList<>();
    ResultSet rs;
    String titulo;
    String horario;
    String dia;
    String sala;
    Button btnComprarEntradas;
    LinearLayout ly;
    LinearLayout ly2;
    LinearLayout ly3;
    ImageView[] asientos = new ImageView[] {};
    ImageView asiento;
    public Sala() {
        // Required empty public constructor
    }


    public static Sala newInstance() {
        Sala fragment = new Sala();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sala, container, false);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("keySala", this, new FragmentResultListener() {
            @Override


            //leo los datos del Fragment Inicio
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                sala = bundle.getString("sala");
                horario= bundle.getString("horario");
                dia= bundle.getString("dia");
                titulo= bundle.getString("titulo");
                Conexion cn = new Conexion();
                System.out.println(sala);
                System.out.println(horario);
                System.out.println(dia);
                arrayAsientos = cn.buscarAsientos(sala,horario,dia); //metodo que busca los asientos de la sala para un dia y unn horario
                System.out.println("sala "+arrayAsientos[0]);


                ly = view.findViewById(R.id.Linear);
                ly2 = view.findViewById(R.id.Linear2);
                ly3 = view.findViewById(R.id.Linear3);

                btnComprarEntradas = view.findViewById(R.id.btnComprarEntradas);
                asientos(); //metodo que rellena los asientos y los selecciona

                //boton para comprar entradas
                btnComprarEntradas.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(asientosCambiar.size()==0){ //compruebo si hay algun asiento seleccionado sino doy error
                            Toast toast = Toast.makeText(getContext(), "Selecciona algun asiento", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else{
                            for (int i = 0; i< asientosCambiar.size(); i++){
                                //cambia el estado de los asientos selecionados
                                arrayAsientos[Integer.parseInt(asientosCambiar.get(i))]=false;
                                Conexion.cambiarAsientos(sala,horario,dia,arrayAsientos); //metodo que cambia los asientos
                                SQLite lite = new SQLite(getContext());
                                lite.insertarEntradasLite(getContext(),titulo,asientosCambiar.size());
                            }
                        }
                    }
                });
            }
        });

        //llenamos la sala de asientos dependiendo del numero
        TextView twInicio = view.findViewById(R.id.txtInicioSala);
        twInicio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.inicio); //ir a inicio
            }
        });
    }

    //metodo que rellena los asientos
    public void asientos() {
            //añado los asientos al lyout
            for (int i = 0; i < arrayAsientos.length; i++) {
                LinearLayout row = new LinearLayout(this.getContext());
                asiento = new ImageView(this.getContext());

                asiento.setImageDrawable(getResources().getDrawable(R.drawable.asiento));

                asiento.setId(i);
                asiento.callOnClick();
                asiento.setTop(i);
                asiento.setTag(i);
                if (!arrayAsientos[i]) { //pongo el color dependiendo de si esta ya comprado o no
                    asiento.setColorFilter(Color.RED);
                } else {
                    asiento.setTag("libre");
                }
                //selecciona o deselecciona los asientos y pone en rojo los selecionados
                asiento.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView cambio = (ImageView) v;
                        try {
                            if (cambio.getTag().equals("libre")) { //si esta libre
                                if (cambio.getColorFilter() == null) { //si no tiene color, es decir el ususario no lo ha selecionado antes
                                    System.out.println(cambio.getId());
                                    cambio.setColorFilter(Color.RED); //cambio el color al selecionado
                                    asientosCambiar.add(Integer.toString(cambio.getId())); //lo añade a la lista de asientos a cambiar
                                } else { //si ha sido selecionado antes por el usuario
                                    cambio.setColorFilter(null); //quito el color del asiento
                                    asientosCambiar.remove(Integer.toString(cambio.getId())); //borro el asiento de la lista a cambiar
                                }
                            } else { //muestra error si se intenta selecionar un asiento ya comprado
                                Toast toast = Toast.makeText(getContext(), "Asiento ocupado", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (Exception e) {

                            System.out.println(e.getMessage());
                        }
                    }
                });
                //añade los asientos en orden
                row.addView(asiento);
                if (i < 10) {
                    ly.addView(row);
                } else if (i >= 10 && i < 20) {
                    ly2.addView(row);
                } else {
                    ly3.addView(row);
                }

            }
        }
}