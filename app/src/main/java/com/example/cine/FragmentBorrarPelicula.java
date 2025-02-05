package com.example.cine;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentBorrarPelicula  extends AppCompatDialogFragment {

    public static final String TAG = FragmentBorrarPelicula.class.getSimpleName();
    public FragmentBorrarPelicula() {
    }
    //creo el dialog donde se pondran los datos de la pelicula a borrar
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_borrar_pelicula,null);

        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtTituloBorrar = v.findViewById(R.id.edtTituloBorrar);
        EditText edtYearBorrar = v.findViewById(R.id.edtYearBorrar);
        Button botonBorrar = v.findViewById(R.id.btnBorrarPelicula);

        //boton que borra la pelicula
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = edtTituloBorrar.getText().toString();
                Integer year = Integer.parseInt(edtYearBorrar.getText().toString());

                //Boolean bol = Conexion.comprobarPelicula(titulo,year);
                Conexion.borrarPelicula(titulo,year,getContext()); //metodo que borra la pelicula
                dialog.dismiss(); //cerramos el dialogo

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
            int ancho = (int) (getResources().getDisplayMetrics().widthPixels);
            int largo = (int) (getResources().getDisplayMetrics().heightPixels *0.5);
            dialog.getWindow().setLayout(ancho,largo);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }
}