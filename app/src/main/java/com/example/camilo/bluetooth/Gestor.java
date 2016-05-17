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
import java.util.ArrayList;

public class Gestor {

    private File sdCard, directory, file = null;
    private static final int READ_BLOCK_SIZE = 100;
    private Temperatura str;
    public int peso;
    public int edad;
    public int altura;
    public float impedanciaMedia;
    public int Vmedio;
    public int mostresV;
    public int mostresZ;
    public long tiempo;

    public Gestor(Temperatura info){

        this.str=info;

    }


    public void Guardar(String nombre){

        String  p = str.getTemperatura();


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
    public void GuardarTemporal(String nombre,double x){

        double  p =x;


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



    public ArrayList leer(String nombre) {
        ArrayList a=new ArrayList();
        double p;
        int i=0;

        if (Environment.getExternalStorageState().equals("mounted")) {
            sdCard = Environment.getExternalStorageDirectory();
            directory = new File(sdCard.getAbsolutePath() + "/PAE_PRUEBA");
            file = new File(directory, nombre);


            try {

                BufferedReader isr = new BufferedReader(new FileReader(file));


                String linea = "";

                while ((linea = isr.readLine()) != null) {
                   linea=linea.replaceAll("\\s+","");
                    Log.e(".Gestor","valor de linea"+linea);
                    p=Double.parseDouble(linea);

                    a.add(i,p);
                    i++;
                }

                isr.close();

            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(".java cargar", "no pudo entrar al cargar!!!!!");
            }
        }
        return a;

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

    public double[] filter(int tipo){

        //tipo =0 es HR , tipo =1 es Respiracion

        ArrayList x=leer("Tension.txt");
        int i=4;
        if (tipo == 0){
        double y1[]=new double[ x.size()];
        double a1=0.017;
        double a2=0;
        double a3=-0.035;
        double a4=0;
        double a5=0.017;

        double b1=1;
        double b2=-3.8749;
        double b3=5.6356;
        double b4=-3.6464;
        double b5=0.8856;
            while(i<x.size()){

                y1[i] = (double) x.get(i) *a1 + (double) x.get(i-1)*a2 +(double) x.get(i-2)*a3 + (double) x.get(i-3)*a4 + (double) x.get(i-4)*a5 - y1[i-1]*b2 - y1[i-2]*b3 - y1[i-3]*b4 - y1[i-4]*b5;
                GuardarTemporal("camilo2.txt",y1[i]);
                i++;
            }
            Log.e(".Filtro","valor de y  "+y1[2945]);

            return y1;


        }
        else{

       /* double y2[]=null;
        double a1=5.3717/10000;
        double a2=0;
        double a3=-0.0011;
        double a4=0;
        double a5=5.3717/10000;

        double b1=1;
        double b2=-3.9333;
        double b3=5.8022;
        double b4=-3.8044;
        double b5=0.9355;
            while(x[i]>0){

            y2[i] = (x[i]*b1 + x[i-1]*b2 + x[i-2]*b3 + x[i-3]*b4 + x[i-4]*b5 - y2[i-1]*a2 - y2[i-2]*a3 - y2[i-3]*a4 - y2[i-4]*a5)/a1;
                i++;
            }

*/
            return null;
        }
        /*
        a(1)*y(n)=b(1)x(n)+b2 x(n-1);

*/

    }
    public int picos(){

/*
 int t= 50s;

 if ( picos >= numero){

   HR.picos<RR.picos


 }


*/
        return picos();
    }

}
