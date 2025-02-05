package com.example.cine;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaseDatos {
    String poster;
    public BaseDatos(){

    }

    public void crearUsuario(){

    }
    public String cartelesAPI(String titulo, String anio){ //metodo que conecta con la API y devuelve los carteles

        Thread hiloCarteles= new Thread(new Runnable() { //hay que hacer hilo para conectar a internet
            @Override
            public void run() {
                //conexion http
                try {
                    URL api = new URL("https://www.omdbapi.com/?apikey=d69d499d&t="+titulo+"&y=anio");
                    HttpURLConnection httpConexion = (HttpURLConnection) api.openConnection();
                    httpConexion.setRequestProperty("User-Agent","Cine-proyectoDam"); //añadimos un encabezado apra que la API identifice esta app
                    //creamos los canales para recoger la resspuesta
                    InputStream respuesta = httpConexion.getInputStream();
                    InputStreamReader respuestaReader = new InputStreamReader(respuesta,"UTF-8");

                    JsonReader jsReader = new JsonReader(respuestaReader); // JsonReader es una clase de Android SDK que facilita la lectura de json
                    jsReader.beginObject(); // metodo para empezar a procesar el objeto JsonReader
                    while (jsReader.hasNext()){
                        String key = jsReader.nextName(); //recoge la "key" en la que esta el JsonReader
                        if(key.equals("Poster")){
                            poster = jsReader.nextString(); //metodo que recoge el string de la Key en la que se encuentra el JsonReader
                            System.out.println("dentro "+poster);
                            break;
                        }else{
                            jsReader.skipValue(); //pasa a la siguiente key
                        }
                    }
                    jsReader.close();
                    httpConexion.disconnect();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hiloCarteles.start();
        try {
            hiloCarteles.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("fuera "+poster);
        return poster;
    }

}
