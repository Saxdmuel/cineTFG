package com.example.cine;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentCrearSnack extends AppCompatDialogFragment {

    public static final String TAG = FragmentCrearSnack.class.getSimpleName();
    public FragmentCrearSnack() {

    }
    //creo el dialog donde se pondran los datos del snack a crear
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_snack, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtNombreSnack = v.findViewById(R.id.edtNSCrear);
        EditText edtPrecioSnack = v.findViewById(R.id.edtPSCrear);
        Button btnCrear = v.findViewById(R.id.btnCrear);

        //boton que crea el snack
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombreSnack.getText().equals(null)){ //comprueba que se haya introducido nombre y da error si no
                    Toast error = Toast.makeText(getContext(),"Falta el nombre por rellenar",Toast.LENGTH_LONG);
                    error.show();
                } else if (edtPrecioSnack.getText().equals(null)) { //comprueba que se haya introducido precio y da error si no
                    Toast error = Toast.makeText(getContext(),"Falta Precio por rellenar",Toast.LENGTH_LONG);
                    error.show();
                }else{ //si esta todos los datos introducidos
                    try {
                        //metodo que crea el snack
                        Conexion.crearSnack(edtNombreSnack.getText().toString(),Integer.parseInt(edtPrecioSnack.getText().toString()));
                        dialog.dismiss(); //cierra el dialg
                    }catch (NumberFormatException e){ //comprueba que el precio introducido sea un numero
                        Toast error = Toast.makeText(getContext(),"El precio debe ser un numero",Toast.LENGTH_LONG);
                        error.show();
                    }
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
            int ancho = (int) (getResources().getDisplayMetrics().widthPixels);
            int largo = (int) (getResources().getDisplayMetrics().heightPixels *0.5);
            dialog.getWindow().setLayout(ancho,largo);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        }
    }
}