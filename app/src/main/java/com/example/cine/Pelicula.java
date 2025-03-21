package com.example.cine;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Pelicula extends Fragment {

    TextView tvDia;
    boolean centDia = true;
    boolean centHora = true;

    TextView tvHora;
    List<Date> horas = new ArrayList<>();
    int IDHora;
    int IDdia = 1;
    TextView dia;
    List<Date> dias = new ArrayList<>();
    String sala;
    String horario;
    Pelicula pelicula;
    String nombrePelicula;
    String anio;
    String duracion;
    String descripcion;
    String cartel;
    String trailer;
    public Pelicula() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pelicula, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitulo = view.findViewById(R.id.TVpeliculaTitulo);
        TextView tvAnio = view.findViewById(R.id.TVanio);
        TextView tvDuracion = view.findViewById(R.id.TVduracion);
        TextView tvDescripcion = view.findViewById(R.id.TVdescripcion);
        ImageView ivCartel = view.findViewById(R.id.IWPeliculaCartel);

        //leo el bundle
        getParentFragmentManager().setFragmentResultListener("keyPelicula", this, new FragmentResultListener() {
            @Override
            //leo los datos del Fragment Inicio y relleno el texto con ellos
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                nombrePelicula = bundle.getString("nombrePelicula");
                tvTitulo.setText(nombrePelicula);

                cartel=bundle.getString("cartel");
                Picasso.get().load(cartel).into(ivCartel); //metodo para rellenar el ImageView


                anio = bundle.getString("anio");

                tvAnio.setText(anio);

                duracion = bundle.getString("duracion");
                tvDuracion.setText(duracion);

                descripcion = bundle.getString("descripcion");
                tvDescripcion.setText(descripcion);

                sala = bundle.getString("sala");

                trailer = bundle.getString("trailer");

            }
        });

        //boton de comprar
        Button btnComprar =view.findViewById(R.id.btnComprar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //creo otro bundle para pasarle la sala a cargar
                Bundle bundle = new Bundle();
                bundle.putString("sala",sala);
                bundle.putString("horario",horario);
                bundle.putString("dia", String.valueOf(IDdia));
                bundle.putString("titulo",nombrePelicula);
                getParentFragmentManager().setFragmentResult("keySala",bundle); //paso el bundle
                Navigation.findNavController(v).navigate(R.id.sala); //voy a sala
            }
        });
        //creo y relleno el calendario para elegir el dia
        dias = Conexion.fechas(); //metodo que busca las fechas
        LinearLayout ly = view.findViewById(R.id.Hly);
        //rellebi el lyout con los dias
        for (int i = 0; i<dias.size();i++ ){

            // convierto el dia de la lista a un LocalDate
            LocalDate localDate = dias.get(i).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            //obtengo el dia del mes
            String txtDia = Integer.toString(localDate.getDayOfMonth());
            //obtrengo el nombre del mes
            String txtMes = localDate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es","ES"));

            //doy formato a la fecha
            String txtFecha = txtDia+"\n"+txtMes;

            //creo un textView para cada dia
            dia = new TextView(this.getContext());
            dia.setText(txtFecha);
            dia.setTextSize(20);
            dia.setTextColor(Color.WHITE);
            dia.setId(i+1);
            dia.setTop(i+1);
            ly.addView(dia); // lo añado al lyour

            //agrego un onClick a cada dia
            dia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (centDia){ //comprueba si ya se ha elegido un dia
                        tvDia = (TextView) v;
                        tvDia.setBackgroundColor(Color.RED); //cambia el color del dia elegido
                        IDdia =tvDia.getId();
                        centDia = false;
                    }
                    else {
                        tvDia.setBackgroundColor(Color.TRANSPARENT); //cambia el color del dia previamente seleccionado
                        tvDia = (TextView) v;
                        tvDia.setBackgroundColor(Color.RED); //cambia el color del dia seleccionado
                        IDdia =tvDia.getId();
                        System.out.println(tvDia.getId());
                    }
                }
            });
        }
        //relleno los horarios

        TextView hora1 =  view.findViewById(R.id.tvHora1);
        TextView hora2 =  view.findViewById(R.id.tvHora2);
        TextView hora3 =  view.findViewById(R.id.tvHora3);

        List<String> listaHorarios = new ArrayList<>();
        listaHorarios = Conexion.buscarHorarios(IDdia); //metodo que devuelve la lista de los horarios para un dia
        System.out.println(listaHorarios.size());

        hora1.setText(listaHorarios.get(0)); //pongo la 1ª hora
        hora2.setText(listaHorarios.get(1)); //pongo la 2ª hora
        hora3.setText(listaHorarios.get(2)); //porgo la 3ª hora

        //botones para elegir el horario
        hora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horario = (String) hora1.getText();
                horarioClick(hora1.getId(),v); //metodo que cambia de color el horario seleccionado
            }
        });
        hora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horario = (String) hora2.getText();
                horarioClick(hora2.getId(),v); //metodo que cambia de color el horario seleccionado
            }
        });
        hora3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horario = (String) hora3.getText();
                horarioClick(hora3.getId(),v); //metodo que cambia de color el horario seleccionado
            }
        });

        //ir inicio
        TextView tvInicio = view.findViewById(R.id.TVInicio);
        tvInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.inicio);
            }
        });

        //ir snacks
        TextView tvSnacks = view.findViewById(R.id.TVsnacks);
        tvSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.snack);
            }
        });

        Button btnTrailer = view.findViewById(R.id.btnTrailer);
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTrailer fTrailer = new FragmentTrailer(trailer);
                fTrailer.show(getParentFragmentManager(),FragmentTrailer.TAG);
            }
        });
    }
    //metodo que cambia de color el horario seleccionado
    public void horarioClick(int id,View v){
        if (centHora){
            tvHora = (TextView) v;
            tvHora.setBackgroundColor(Color.RED); //cambia el color del dia selecionado
            IDHora =tvHora.getId();
            centHora = false;
        }
        else {
            tvHora.setBackgroundColor(Color.TRANSPARENT); //cambia el color del dia selecionado previamente
            tvHora = (TextView) v;
            tvHora.setBackgroundColor(Color.RED); //cambia el color del dia selecionado
            IDHora =tvHora.getId();
        }
    }
    public void setPelicula (Pelicula pelicula){
        this.pelicula=pelicula;
    }
}