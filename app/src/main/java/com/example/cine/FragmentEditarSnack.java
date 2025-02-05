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

public class FragmentEditarSnack extends AppCompatDialogFragment {
    public static final String TAG = FragmentEditarSnack.class.getSimpleName();
    public FragmentEditarSnack() {
    }

    //creamos el dialog donde se pondran los datos del snack a editar
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_editar_snack, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext()).setView(v).create();

        EditText edtNombre = v.findViewById(R.id.edtNSEditar);
        EditText edtPrecio = v.findViewById(R.id.edtPSEditar);
        Button btnEditar = v.findViewById(R.id.btnEditarSnack);

        //boton que edita el snack
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNombre.getText().toString().equals("")){ //comprueba si el nombre esta vacio y da error
                    Toast error = Toast.makeText(getContext(),"Falta el nombre por rellenar",Toast.LENGTH_SHORT);
                    error.show();
                } else if (edtPrecio.getText().toString().equals("")) {//comprueba si el precio esta vacio y da error
                    Toast error = Toast.makeText(getContext(),"Falta Precio por rellenar",Toast.LENGTH_SHORT);
                    error.show();
                }else{  //si todo esta introducido
                    try {
                        //metodo que edita el snack
                        Conexion.editarSnack(edtNombre.getText().toString(),Integer.parseInt(edtPrecio.getText().toString()),getContext());
                        dialog.dismiss();
                    } catch (NumberFormatException e){ //da error si el precio no es un numero
                        Toast error = Toast.makeText(getContext(),"El precio debe ser un numero",Toast.LENGTH_SHORT);
                        error.show();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
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