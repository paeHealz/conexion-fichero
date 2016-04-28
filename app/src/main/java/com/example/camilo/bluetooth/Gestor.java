package com.example.camilo.bluetooth;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.camilo.bluetooth.Temperatura;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Camilo on 28/04/2016.
 */
public class Gestor {

    File sdCard, directory, file = null;
    static final int READ_BLOCK_SIZE = 100;
    Temperatura str;


    public Gestor(Temperatura info){

        this.str=info;

    }

    public void Guardar( ){
      String p = str.getTemperatura();

        if (Environment.getExternalStorageState().equals("mounted")){
            sdCard = Environment.getExternalStorageDirectory();
            try{
                directory = new File(sdCard.getAbsolutePath()
                        + "/PAE_PRUEBA");
                directory.mkdirs();
                file = new File(directory, "Pae.txt");
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


    public void Cargar() {

        if (Environment.getExternalStorageState().equals("mounted")) {
            sdCard = Environment.getExternalStorageDirectory();
            directory = new File(sdCard.getAbsolutePath() + "/PAE_PRUEBA");
            file = new File(directory, "Pae.txt");
            //  String p= file;

            try {

                //FileInputStream fis = openFileInput(file.getName());
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));

                char[] inputBuffer = new char[READ_BLOCK_SIZE];
                String s = "";

                int charRead;
                while ((charRead = isr.read(inputBuffer)) > 0) {
                    // Convertimos los char a String
                    String readString = String.copyValueOf(inputBuffer, 0, charRead);
                    s += readString;

                    inputBuffer = new char[READ_BLOCK_SIZE];
                }


                isr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(".java cargar", "no pudo entrar al cargar!!!!!");
            }
        }
    }


}
