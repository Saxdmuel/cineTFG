package com.example.cine;

import static java.security.AccessController.getContext;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Editable;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

public class Conexion{
    static ResultSet rsHora;
    String hora_id;
    static ResultSet rsHorarios;
    static ResultSet rsDias;
    Boolean[] arrayAsientos;
    static ResultSet rsPeliculas;
    static ResultSet rsSnacks;
    static ResultSet rsSalas;
    private final String ip = "45.6.48.124";

    private static Connection conexion;
    private final String baseDatos = "cine";
    private final int puerto = 5432;
    private final String usuario = "postgres";
    private final String pass = "saxdmuel";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private static boolean estadoConexion = false;

    public Conexion() {
        this.url = String.format(this.url, this.ip, this.puerto, this.baseDatos);
    }

    //metodo que añade peliculas a la BBDD
    public static void crearPelicula(String titulo, Integer year, String descripcion, Integer duracion, String sala) {
        String sql = "INSERT INTO peliculas(nombrepelicula,year,descripcion,duracion,sala) "
                + "VALUES(?,?,?,?,?);";
        try {
            //creo el PreparedStatement y le añado los parametros
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, titulo);
            ps.setInt(2, year);
            ps.setString(3, descripcion);
            ps.setInt(4,duracion);
            ps.setString(5,sala);
            ps.executeUpdate(); //metodo que ejecuta el sql uso executeUpdate porque se que la consulta es INSERT
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    //metodo que borra peliculas de la BBDD
    public static void borrarPelicula(String titulo, int year, Context context) {
        String sql = "delete from peliculas where nombrepelicula = '"+titulo+"' and year = "+year;
        try{
            //creo el PreparedStatement
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int filasActualizadas= ps.executeUpdate(); //ejecuto el sql y almaceno la cantidad filas eliminadas
            if (filasActualizadas <= 0){ //si las filas actualizadas son 0 es error
                //creo el error y lo muestro
                Toast error = Toast.makeText(context,"titulo o año incorrectos",Toast.LENGTH_LONG);
                error.show();
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //metodo que borra snacks de la BBDD
    public static void borrarSnack(String nombre, Context context) {
        String sql = "delete from snack_bar where nombresnack = '"+nombre+"'";
        try{
            //creo el PreparedStatement
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int filasActualizadas = ps.executeUpdate(); //ejecuto el sql y almaceno la cantidad filas eliminadas
            if (filasActualizadas <= 0){//si las filas actualizadas son 0 es error
                //creo el error y lo muestro
                Toast error = Toast.makeText(context,"No existe el snack",Toast.LENGTH_LONG);
                error.show();
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //metodo que crea snacks
    public static void crearSnack(String nombre, Integer precio) {
        String sql = "INSERT INTO snack_bar(nombresnack,precio) "
                + "VALUES(?,?);";
        try {
            //creo el PreparedStatement y le añado los parametros
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setInt(2, precio);
            ps.executeUpdate();//ejecuto el sql
            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //metedo que edita los snacks ya creados
    public static void editarSnack(String nombre, int precio, Context context) {
        String sql = "UPDATE snack_bar SET precio = ? WHERE nombresnack = ?";

        try {
            PreparedStatement pst; //creo el PreparedStatement y le añado los parametros
            pst = conexion.prepareStatement(sql);
            pst.setInt(1,precio);
            pst.setString(2,nombre);
            int filasActualizadas = pst.executeUpdate();//ejecuto el sql y almaceno la cantidad filas editadas
            pst.close();
            if (filasActualizadas<=0){//si las filas actualizadas son 0 es error
                //creo el error y lo muestro
                Toast error = Toast.makeText(context,"El precio debe ser un numero",Toast.LENGTH_LONG);
                error.show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean usuarioExiste(String usuario,Context context) {
        boolean existe = false;
        try{
            String query = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
            PreparedStatement ps = conexion.prepareStatement(query); // uso preparedStatement para evitar inyecciones
            ps.setString(1, usuario); //pongo el parametro usuario
            ResultSet resultSet = ps.executeQuery(); //ejecuto y  guardo el resultado
            if (resultSet.next()) { //compruebo si hay usuario
                existe = !(resultSet.getInt(1) > 0);  // si es mayor a 0 quiere decir que existe el usuario
            }
            if(!existe){ //si no existe muestro el error
                Toast error = Toast.makeText(context,"El usuario ya existe",Toast.LENGTH_LONG);
                error.show();
            }
            ps.close(); //cierro el PreparedStatement
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return existe;
    }

    //metodo que conecta con la BBDD
    public  void conectarSql() {

        AsyncTask.execute(new Runnable() { //ejecutamos el metodo en un hilo separado para evitar errores y evitar congelar la UI
            @Override
            public void run() {
                try {
                    Class.forName("org.postgresql.Driver"); //cargo el controlador de la BBDD
                    conexion = DriverManager.getConnection(url, usuario, pass); //creo la conexion
                    estadoConexion = true;  //controlo si la conexion ha sido exitosa
                } catch (Exception e) {
                    estadoConexion = false;//controlo si ha habido algun error en la conexion
                    e.printStackTrace();
                }
            }
        });

    }

    //metodo quue crea un usuario
    public static void crearUsuario(String nombre, String password, String email, String permisos) {
        String sql = "INSERT INTO usuarios(nombreusuario,password,email,permisos) "
                + "VALUES(?,?,?,?);";

        try {
            //creo el PreparedStatement y le añado los parametros
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nombre);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4,permisos);
            System.out.println(ps);
            ps.executeUpdate(); //ejecuto el sql
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //asiento que busca las peliculas disponibles
    public static ResultSet verPeliculasConexion() {
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloPeliculas = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Statement st = conexion.createStatement(); // Creamos un objeto Statement para ejecutar la consulta SQL.
                    rsPeliculas = st.executeQuery("select * from peliculas"); //ejecuto la consulta y la guardo
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hiloPeliculas.start();
        try {
            hiloPeliculas.join(); //uso join para asegurar que el hilo termina antes de de devolver el RS
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsPeliculas;
    }
    //metodo que busca los asientos de una sala
    public  Boolean[] buscarAsientos (String sala,String horario, String dia){
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloSala = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    //ejecuto la consulta y la guardo
                    rsHora = st.executeQuery("select id from horarios where sala='"+sala+"' and hora ='"+horario+"' and dia_id = "+dia);
                    if (rsHora.next()){
                        hora_id = rsHora.getString("id");
                        System.out.println("hora id   "+hora_id);
                    }
                    //ejecuto la consulta y la guardo
                    rsSalas = st.executeQuery("select * from salas where nombre_sala='"+sala+"' and horario_id = '"+hora_id+"'");
                    if(rsSalas.next()){
                        System.out.println(rsSalas.getString("id"));
                    }
                    //meto los asientos en un array
                    arrayAsientos= (Boolean[]) rsSalas.getArray("asientos").getArray();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hiloSala.start();
        try {
            hiloSala.join(); //uso join para asegurarme de que acabe el hilo antes de devolver el array
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayAsientos;
    }
    //metodo que busca los snakcs
    public static List<String> buscarSnacks (){
        List<String> listaSnacks = new ArrayList<>();
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloSala = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    rsSnacks = st.executeQuery("select nombresnack from snack_bar"); //ejecutamos la sentencia y la guardo
                    while(rsSnacks.next()){
                        listaSnacks.add(rsSnacks.getString("nombresnack")); //lleno la lista con el RS
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hiloSala.start();
        try {
            hiloSala.join(); //uso join para asegurarme de que acabe el hilo antes de devolver el array
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSnacks;
    }
    //metodo que busca los precios de los snacks
    public static List<String> buscarPreciosSnacks() {
        List<String> listaSnacks = new ArrayList<>();
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloSala = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    rsSnacks = st.executeQuery("select precio from snack_bar");//ejecutamos la sentencia y la guardo
                    while(rsSnacks.next()){
                        listaSnacks.add(rsSnacks.getString("precio"));//lleno la lista con el RS
                    }

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        hiloSala.start();
        try {
            hiloSala.join();//uso join para asegurarme de que acabe el hilo antes de devolver el array
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSnacks;
    }
    //metodo que cambia el asiento comprado
    public static void cambiarAsientos (String nombre_sala, String horario, String dia,Boolean[] arrayAsientos){
        System.out.println(horario);
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloAsientos = new Thread(new Runnable() {
            int hora_id = 0;
            @Override
            public void run() {
                try {

                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    //ejecutamos la sentencia y la guardo
                    rsHora = st.executeQuery("select id from horarios where sala='"+nombre_sala+"' and hora ='"+horario+"' and dia_id = "+dia);
                    if (rsHora.next()){
                        hora_id = rsHora.getInt("id");
                        System.out.println("hora id   "+hora_id);
                    }

                    Array array = conexion.createArrayOf("BOOLEAN",arrayAsientos);
                    String sql = "UPDATE salas SET asientos = ? WHERE nombre_sala = ? and horario_id = ?";

                    PreparedStatement pst = conexion.prepareStatement(sql); //Creamos un objeto PreparedStatement para ejecutar la consulta SQL
                    pst.setArray(1,conexion.createArrayOf("BOOLEAN",arrayAsientos));
                    pst.setString(2,nombre_sala);
                    pst.setInt(3,hora_id);
                    pst.executeUpdate(); //ejecuto el SQL

                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            }
        });
        hiloAsientos.start();
        try {
            hiloAsientos.join();//uso join para asegurarme de que acabe el hilo antes de segir
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //metodo que busca las fechas de una sala
    public static List<Date> fechas () {
        List<Date> dias = new ArrayList<>();
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloFechas = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql = "select dia from dias order by dia";
                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    //ejecutamos la sentencia y la guardo
                    rsDias = st.executeQuery(sql);
                    while (rsDias.next()){
                        dias.add(rsDias.getDate("dia")); //relleno la lista  con el RS
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hiloFechas.start();
        try {
            hiloFechas.join(); //uso join para que acabe el hilo antes de seguir
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dias;
    }
    //metodo que busca los horarios de una sala
    public static List<String> buscarHorarios(int diaID) {
        List<String> listaHorarios = new ArrayList<>();
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloHorarios = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String sql = "select hora from horarios where dia_id = "+diaID+" order by hora";
                    Statement st = conexion.createStatement();// Creamos un objeto Statement para ejecutar la consulta SQL
                    rsHorarios = st.executeQuery(sql);//ejecutamos la sentencia y la guardo

                    while (rsHorarios.next()){
                        listaHorarios.add(rsHorarios.getString("hora")); //relleno la lista  con el RS
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        try {
            hiloHorarios.start();
            hiloHorarios.join();//uso join para que acabe el hilo antes de seguir
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        return listaHorarios;
    }
    //metodo que comprueba si existe el usuario
    public static Boolean buscarUsuario(String usuario) {
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        final Boolean[] centinelaUsuario = {false}; //sa hace  array final para evitar inconsistencias (requisito)
        Thread hiloUsuario = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String sql = "select nombreusuario from usuarios where nombreusuario = '"+usuario+"'";
                    Statement st = conexion.createStatement(); // Creamos un objeto Statement para ejecutar la consulta SQL
                    ResultSet rs= st.executeQuery(sql);//ejecutamos la sentencia y la guardo
                    if (!rs.isBeforeFirst()){
                        centinelaUsuario[0] = false;
                    } else {
                        centinelaUsuario[0] = true;
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        try {
            hiloUsuario.start();
            hiloUsuario.join(); //uso join para que acabe el hilo antes de seguir
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(centinelaUsuario[0]);
        return centinelaUsuario[0];
    }
    //metodo que comprueba si la contraseña es correcta
    public static boolean buscarPass(String pass, String usuario) {
        final Boolean[] centinelaPass = {false}; //sa hace  array final para evitar inconsistencias (requisito)
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloPass = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("CONTRASEÑA");
                    String sql = "select password from usuarios where nombreusuario = '"+usuario+"' and password = '"+pass+"'";
                    Statement st = conexion.createStatement(); // Creamos un objeto Statement para ejecutar la consulta SQL
                    ResultSet rs= st.executeQuery(sql); //ejecutamos la sentencia y la guardo
                    // Verificamos si el ResultSet contiene filas. Si no hay filas, significa que no existe
                    // un usuario con el nombre de usuario y la contraseña proporcionados.
                    if (!rs.isBeforeFirst()){
                        centinelaPass[0] = false; // No se encontró un usuario con esas credenciales
                    } else {
                        centinelaPass[0] = true; // Credenciales correctas
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        try {
            hiloPass.start();
            hiloPass.join();//uso join para que acabe el hilo antes de seguir
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(centinelaPass);
        return centinelaPass[0];
    }
    //metodo para saber los permisos del usuario
    public static String buscarPermisos(String usuario) {
        final String[] permisos = new String[1];//sa hace  array final para evitar inconsistencias (requisito)
        // Creamos un hilo para ejecutar la consulta en segundo plano.
        // Esto se hace porque, de lo contrario, el `ResultSet` podría no completarse correctamente antes de llamarlo
        Thread hiloPermisos = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String sql = "select permisos from usuarios where nombreusuario = '"+usuario+"'";
                    Statement st = conexion.createStatement(); // Creamos un objeto Statement para ejecutar la consulta SQL
                    ResultSet rs= st.executeQuery(sql); //ejecutamos la sentencia y la guardo
                    rs.next();
                    permisos[0] =rs.getString("permisos"); //relleno los permisos
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        try {
            hiloPermisos.start();
            hiloPermisos.join(); //uso join para que acabe el hilo antes de seguir
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return permisos[0];
    }

    public  Connection getConexion() {
        return conexion;
    }
    public  boolean getEstadoConexion() {
        return estadoConexion;
    }
}

