package com.example.camilo.bluetooth;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Camilo on 28/04/2016.
 */
public class Gestor {

    private File sdCard, directory, file = null;
    private static final int READ_BLOCK_SIZE = 100;
    private Temperatura str;
    public int peso;
    public int edad;
    public int altura;
    public int impedanciaMedia;
    public int hr;

    public Gestor(Temperatura info){

        this.str=info;

    }
    public Gestor(int i){

        hr=i;

    }

    public void Guardar(String nombre){
      String p = str.getTemperatura();

        if (Environment.getExternalStorageState().equals("mounted")){
            sdCard = Environment.getExternalStorageDirectory();
            try{
                directory = new File(sdCard.getAbsolutePath()
                        + "/PAE_PRUEBA");
                directory.mkdirs();
                file = new File(directory, nombre);
                FileOutputStream fout = new FileOutputStream(file,true);
                OutputStreamWriter osw = new OutputStreamWriter(fout);
                osw.append(p+"\n");
                osw.flush();
                osw.close();

            }catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }


    public void leer(String nombre) {
        int valor;
        int impedancia=0;
        int i=1;

        if (Environment.getExternalStorageState().equals("mounted")) {
            sdCard = Environment.getExternalStorageDirectory();
            directory = new File(sdCard.getAbsolutePath() + "/PAE_PRUEBA");
            file = new File(directory, nombre);


            try {

                //FileInputStream fis = openFileInput(file.getName());
                BufferedReader isr = new BufferedReader(new FileReader(file));


                String linea = "";

                while ((linea = isr.readLine()) != null) {

                    if(nombre=="Impedancia.txt"){

                        valor=Integer.parseInt(linea);
                        impedancia= (impedancia+valor)/i;

                    }

                }

                isr.close();
                impedanciaMedia=impedancia;
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(".java cargar", "no pudo entrar al cargar!!!!!");
            }
        }
    }

    public void fijarPeso(int mPeso){

        peso=mPeso;


    }
    public void fijarAltura(int maltura){

        altura=maltura;

    }
    public void fijarEdad(int mEdad){

        edad=mEdad;

    }


}
