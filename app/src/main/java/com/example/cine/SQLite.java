package com.example.cine;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {//esta clase crea una tabla integrada en la app
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "cine.db";
    public SQLite(@Nullable Context context) {
        super(context, DATABASE_NAME,null ,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE carrito (id INTEGER PRIMARY KEY AUTOINCREMENT, articulo TEXT, precio INTEGER, cantidad INTEGER)");
        db.execSQL("CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT)");
    }
    public void insertarUsuario(Context context, String nombre){
        try {
            //instanciamos la base de datos
            SQLite sqLite = new SQLite(context);
            SQLiteDatabase db = sqLite.getWritableDatabase();

            //creo un objeto ContenValues para almacenar los datos
            ContentValues valores = new ContentValues();
            valores.put("nombre",nombre);

            long newRowId= db.insert("usuario",null,valores); //inserto y guardo un log para comprobar errores
            // se usa insert en vez de exect porque  evita inyecciones
            db.close();
            // Verificar si el registro se añadió correctamente
            if (newRowId != -1) {
                Log.d("SQLite", "Registro añadido con ID: " + newRowId);
            } else {
                Log.e("SQLite", "Error al insertar registro");
            }
        }catch (Exception e){
            Log.e("SQLiteError", "Error en al insertar: " + e.getMessage());
        }


    }
    public void insertarSnacksLite (Context context,String articulo, int precio,int cantidad){

            //instanciamos la base de datos
            SQLite sqLite = new SQLite(context);
            SQLiteDatabase db = sqLite.getWritableDatabase();

            //creo un objeto ContenValues para almacenar los datos
            ContentValues valores = new ContentValues();
            valores.put("articulo",articulo);
            valores.put("precio",precio);
            valores.put("cantidad",cantidad);

            long newRowId= db.insert("carrito",null,valores); //inserto y guardo un log para comprobar errores
            // se usa insert en vez de exect porque  evita inyecciones
            db.close();
            // Verificar si el registro se añadió correctamente
            if (newRowId != -1) {
                Log.d("SQLite", "Registro añadido con ID: " + newRowId);
            } else {
                Log.e("SQLite", "Error al insertar registro");
            }



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  List<String> obtenerCarrito(Context context){
        SQLite lite = new SQLite(context);
        SQLiteDatabase db = lite.getReadableDatabase();
        List<String> listaCarrito = new ArrayList<>();
        String[] columnas = {"articulo","precio","cantidad"}; // creamos un array de strings con las columnas que quiero leer

        //ejecutamos la consulta
        Cursor cursor = db.query(
                "carrito", //tabla
                columnas,       //Columnas a recuperar
                null,null,null,null,null
        );

        while (cursor.moveToNext()){
            int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
            int precio = cantidad * cursor.getInt(cursor.getColumnIndexOrThrow("precio"));
            System.out.println(cantidad+"   "+precio);
            String linea = cursor.getString(cursor.getColumnIndexOrThrow("articulo"))+" X  "+cantidad+"   "+precio+"€";
            listaCarrito.add(linea);
        }
        return  listaCarrito;
    }

    public static void vaciarCarrito(Context context){
        SQLite lite = new SQLite(context);
        SQLiteDatabase db = lite.getReadableDatabase();

        db.execSQL("DELETE FROM carrito"); //vacia la tabal carrito
        db.execSQL("VACUUM"); //reinicia los contadores

        db.close();
    }

    public String sumarPrecios(Context context) {
        int totalPrecio = 0;
            SQLite lite = new SQLite(context);
            SQLiteDatabase db = lite.getReadableDatabase();

            // Ejecutar la consulta para sumar los valores
            String sql = "SELECT precio, cantidad FROM carrito";
            Cursor cursor = db.rawQuery(sql, null); //creo el cursor para moverme por los datos

            // Obtener el resultado

            while (cursor.moveToNext()) {
                int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                int precio = cantidad *  cursor.getInt(cursor.getColumnIndexOrThrow("precio"));
                totalPrecio = totalPrecio + precio;
            }
            cursor.close();
            db.close();

        return  Integer.toString(totalPrecio);
    }

    public void insertarEntradasLite(Context context,String titulo,int cantidad) {
        //instanciamos la base de datos
        SQLite sqLite = new SQLite(context);
        SQLiteDatabase db = sqLite.getWritableDatabase();

        //creo un objeto ContenValues para almacenar los datos
        ContentValues valores = new ContentValues();
        valores.put("articulo",titulo);
        valores.put("precio",10);
        valores.put("cantidad",cantidad);

        long newRowId= db.insert("carrito",null,valores); //inserto y guardo un log para comprobar errores
        // se usa insert en vez de exect porque  evita inyecciones
        db.close();
        // Verificar si el registro se añadió correctamente
        if (newRowId != -1) {
            Log.d("SQLite", "Registro añadido con ID: " + newRowId);
        } else {
            Log.e("SQLite", "Error al insertar registro");
        }

    }

    public String usuarioConectado(Context context) {
        String usuario = "vacio";
        Log.d("usuario", "usuario principio: " + usuario);

        try {
            SQLite sqLite = new SQLite(context);
            SQLiteDatabase db = sqLite.getWritableDatabase();

            // Verificar si la tabla 'usuario' existe
            if (!existeTablaUsuario(db)) {
                Log.e("SQLiteError", "La tabla 'usuario' no existe.");
                return "vacio";
            }

            // Ejecutar la consulta
            String sql = "SELECT * FROM usuario LIMIT 1";
            Cursor cursor = db.rawQuery(sql, null);

            // Verificar si el cursor tiene resultados
            if (cursor.moveToFirst()) {
                usuario = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                System.out.println("usuario if: " + usuario);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e("SQLiteError", "Error en la base de datos: " + e.getMessage());
            usuario = "vacio";
        }
        System.out.println("usuario final: " + usuario);
        return usuario;
    }
    private boolean existeTablaUsuario(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='usuario'", null
        );
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    //metodo para "cerrar sesion"
    public void cerrarSesion(Context context) {
        SQLite sqLite = new SQLite(context);
        SQLiteDatabase db = sqLite.getWritableDatabase();

        db.delete("usuario",null,null); //borra el contenido de la tabla usuario

    }
}
