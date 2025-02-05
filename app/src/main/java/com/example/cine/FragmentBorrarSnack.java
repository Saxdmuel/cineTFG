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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentBorrarSnack extends AppCompatDialogFragment {

    public static final String TAG = FragmentBorrarSnack.class.getSimpleName();
    public FragmentBorrarSnack() {

    }
    //creo el dialog donde se introduiciran los datos del snack a borrar
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_borrar_snack, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtNombreSnack = v.findViewById(R.id.edtNSBorrar);
        Button btnBorrar = v.findViewById(R.id.btnBorrarS);

        //boton que borra el snakc
        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conexion.borrarSnack(edtNombreSnack.getText().toString(),getContext()); //metodo que borra el snack
                dialog.dismiss();
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