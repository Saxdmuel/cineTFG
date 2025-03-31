package com.example.cine;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentBorrarActor extends AppCompatDialogFragment {

    public static final String TAG = FragmentBorrarActor.class.getSimpleName();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_crear_actor, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtNombreActor = v.findViewById(R.id.edtNombreActor);
        Button btnBorrarActor = v.findViewById(R.id.btnBorrarActorF);

        Bundle bundle = getArguments();
        String titulo = bundle.getString("titulo");
        btnBorrarActor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(edtNombreActor.getText().toString()+"   "+titulo);
                Conexion.borrarActor(getContext(),edtNombreActor.getText().toString(),titulo);
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