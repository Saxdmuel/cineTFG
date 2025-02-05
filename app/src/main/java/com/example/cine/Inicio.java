package com.example.cine;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Inicio extends Fragment{
    String permisos;
    public SharedPreferences carro;
    RecyclerView rv;
    RecyclerView.Adapter rva;
    RecyclerView.LayoutManager lm;
    ImageView imagenD;

    List<String> nombres = new ArrayList<>();
    List<String> anios = new ArrayList<>();
    List<String> descripciones = new ArrayList<>();
    List<String> duraciones = new ArrayList<>();
    List<String> carteles = new ArrayList<>();

    List<String> salas = new ArrayList<>();

    String nombrePelicula;
    String cartel;
    String anio;
    String descripcion;
    String duracion;
    TextView titulo;
    Pelicula pelicula;
    static Conexion conexion;
    static ResultSet rs;
    public Inicio() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creamos un objeto comision para establecer conexion con la base de datos
        conexion= new Conexion();
        conexion.conectarSql();
        Boolean centinela = true;
        while(centinela){
            if(conexion.getEstadoConexion()){
                rs=conexion.verPeliculasConexion();
                centinela= false;
            }
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_inicio, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SQLite lite = new SQLite(getContext());
        String usuario = lite.usuarioConectado(getContext());//metodo que devuelve el usuario conectado
        permisos = Conexion.buscarPermisos(usuario); //metodo que devuelve los permisos del usuario

        Button btnCrearPelicula;
        Button btnBorrarPelicula;
        if(permisos.equals("administrador")){
            btnCrearPelicula = view.findViewById(R.id.btnCrearPelicula);
            btnBorrarPelicula = view.findViewById(R.id.btnBorrarPelicula);
            //hacemos los botones visibles si es cuenta administrador
            btnBorrarPelicula.setVisibility(View.VISIBLE);
            btnCrearPelicula.setVisibility(View.VISIBLE);

            //boton que abre el dialog de crear pelicula
            btnCrearPelicula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentCrearPelicula fcp = new FragmentCrearPelicula(); //creo el fragmento del dialog
                    fcp.show(getParentFragmentManager(),FragmentCrearPelicula.TAG); //muestro el fragmento
                }
            });
            //boton que abre el dialog de borrar pelicula
            btnBorrarPelicula.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentBorrarPelicula fbp = new FragmentBorrarPelicula(); //creo el fragmento del dialog
                    fbp.show(getParentFragmentManager(),FragmentCrearPelicula.TAG);//muestro el fragmento
                }
            });
        }

        //metodo para ir a la pelicula seleccionada
         try{
             //relleno las listar con los datos de la consulta para luego saber cual seleciono
            while (rs.next()){
                nombres.add(rs.getString("nombrepelicula"));
                anios.add(rs.getString("year"));
                descripciones.add(rs.getString("descripcion"));
                duraciones.add(rs.getString("duracion"));
                salas.add(rs.getString("sala"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        LinearLayout ly = view.findViewById(R.id.Linear);
        BaseDatos bs = new BaseDatos();

        for (int i = 0; i < nombres.size(); i++) { //recorro la lista para ir creando las imagenes

            imagenD = new ImageView(this.getContext());

            cartel=bs.cartelesAPI((String) nombres.get(i), anios.get(i)); //metodo que busca el cartel de la API
            Picasso.get().load(cartel).into(imagenD);   //rellena el ImageView con el cartel
            carteles.add(cartel); //lo meto en la lista de carteles

            //añado los parametros de cada imagen para luego identificarlas
            imagenD.setId(i);
            imagenD.setTop(i);


            ly.addView(imagenD); //añado la imagen al layout para mostarla

            //le pongo un onClick a cada imagen que luego me llevara a esa pelicula
            imagenD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView im = (ImageView) v;

                    irPelicula(im.getId()); //metodo para ir a la pelicula seleccionada
                }
            });
        }

        //ir a snack

        TextView txtSnack = view.findViewById(R.id.txtSnackCarrito);
        //boton para ir a snack
        txtSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.snack); //voy a snack
            }
        });

        // ir carrito
        ImageView carrito = view.findViewById(R.id.IVcarrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.carrito);
            }
        });

        //cerrar sesión
        TextView tvCerrarsesion = view.findViewById(R.id.TVcerrarSesion);
        tvCerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite lite = new SQLite(getContext());
                lite.cerrarSesion(getContext());
                Navigation.findNavController(view).navigate(R.id.login);
            }
        });
    }
    public ResultSet getRs(){
        return rs;
    }
    public Pelicula getPelicula(){
        return pelicula;
    }

    //metodo que va a la pelicula seleccionada
    public void irPelicula(int id){
    //creo en bundle y le pongo los parametros
        Bundle bundle = new Bundle();
        bundle.putString("nombrePelicula",nombres.get(id));
        bundle.putString("cartel",carteles.get(id));
        bundle.putString("anio",anios.get(id));
        bundle.putString("descripcion",descripciones.get(id));
        bundle.putString("duracion",duraciones.get(id));
        bundle.putString("sala",salas.get(id));
        getParentFragmentManager().setFragmentResult("keyPelicula",bundle); //le paso el bundle al siguiente frgament

        Navigation.findNavController(getView()).navigate(R.id.pelicula); //voy a la pelicula
    }

}