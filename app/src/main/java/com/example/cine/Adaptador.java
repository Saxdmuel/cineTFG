package com.example.cine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends  RecyclerView.Adapter <Adaptador.ViewHolder> {
    private static List<String> listaNombres;
    private static List<String> listaPrecios;
    public List<EditText> listaCantidad = new ArrayList<>();
    public Adaptador (List<String> listaNombres, List<String> listaPrecios){
        this.listaNombres=listaNombres;
        this.listaPrecios=listaPrecios;
    }
    // Clase interna ViewHolder que se encarga de mantener las referencias a las vistas.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNombre;
        public TextView tvPrecio;
        public EditText etCantidad;
        public  ViewHolder(View view){ // Constructor del ViewHolder que recibe la vista y vincula las referencias.
            super(view);
            tvNombre = view.findViewById(R.id.tvName);
            etCantidad = view.findViewById(R.id.EtnSnack);
            tvPrecio = view.findViewById(R.id.tvPrecioA);
        }
    }

    @NonNull
    @Override
    // Método llamado para inflar el layout de cada elemento de la lista.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    // Método que enlaza los datos de cada posición con las vistas correspondientes.
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tvNombre.setText(listaNombres.get(position)); // Asignar el nombre del producto al TextView correspondiente.
        holder.tvPrecio.setText(listaPrecios.get(position)+"€");// Asignar el precio del producto al TextView correspondiente
        listaCantidad.add(holder.etCantidad); // Añado el EditText para la cantidad a la lista pública listaCantidad.
    }


    @Override
    public int getItemCount() {
        return listaNombres.size();
    }

}
