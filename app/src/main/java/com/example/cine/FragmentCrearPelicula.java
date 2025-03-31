package com.example.cine;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentCrearPelicula extends AppCompatDialogFragment {
    public static final String TAG = FragmentCrearPelicula.class.getSimpleName();
    public FragmentCrearPelicula() {
    }

    //creo el dialog donde se pondran los datos de la pelicula a crear
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_pelicula, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtTitulo = v.findViewById(R.id.edtNombreActor);
        EditText edtYear = v.findViewById(R.id.edtYear);
        EditText edtDescripcion = v.findViewById(R.id.edtDescripción);
        EditText edtDuracion = v.findViewById(R.id.edtDuración);
        EditText edtSala = v.findViewById(R.id.edtSala);
        EditText edtTrailer = v.findViewById(R.id.edtTrailer);
        Button botonAceptar = v.findViewById(R.id.btnCrearActorF);

        //boton que crea la peluicula
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String titulo =edtTitulo.getText().toString();String year =edtYear.getText().toString(); String descripcion = edtDescripcion.getText().toString();
                    String duracion = edtDuracion.getText().toString(); String sala = edtSala.getText().toString(); String trailer = edtTrailer.getText().toString();

                    //comprueba que se hayan rellenado todos los campos
                    if (titulo.equals(null)||year.equals(null)||descripcion.equals(null)||duracion.equals(null)||sala.equals("")){
                        Toast error = Toast.makeText(getContext(),"Faltan campos por rellenar",Toast.LENGTH_LONG);
                        error.show();
                    }else{
                        //metodo que crea la pelicula
                        Conexion.crearPelicula(titulo,Integer.parseInt(year),descripcion,Integer.parseInt(duracion),sala,trailer);
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        });

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override //sobreescribo onStart para cambiar el tamaño del dialog
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null ){
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );
            //pongo el tamaño del dialog
            int ancho = (int) (getResources().getDisplayMetrics().widthPixels *0.9);
            int largo = (int) (getResources().getDisplayMetrics().heightPixels *0.8);
            dialog.getWindow().setLayout(ancho,largo);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }
}