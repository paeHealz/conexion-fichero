package com.example.camilo.bluetooth;

/**
 * Created by Camilo on 21/04/2016.
 */
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.UUID;

/**
 * Created by Roque on 4/03/14.
 */
public class MiAsyncTask extends AsyncTask<BluetoothDevice, Temperatura, Void>
{
    private static final String TAG = "MiAsyncTask";

    //Identificador unico universal del puerto bluetooth en android (UUID)
    private static final String UUID_SERIAL_PORT_PROFILE = "00001101-0000-1000-8000-00805F9B34FB";
    private Temperatura temperatura = new Temperatura();
    private BluetoothSocket mSocket = null;
    private BufferedReader mBufferedReader = null;
    private MiCallback callback;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private boolean recibiendo = false;
    private InputStream aStream = null;
    private InputStreamReader aReader = null;
    private int contadorConexiones = 0;
    private Gestor Impedancia,Tension;

    public interface MiCallback {
        void onTaskCompleted();

        void onCancelled();

        void onTemperaturaUpdate(Temperatura p);
    }

    public MiAsyncTask(MiCallback CALLBACK) {
        callback = CALLBACK;

    }
    @Override
    protected Void doInBackground(BluetoothDevice... devices) {

        final BluetoothDevice device = devices[0];

//Realizamos la conexion al disp.blueetoth. A veces la conexion falla aunque el dispositivo
//este presente. Asi que si falla, y la tarea no ha sido cancelada, lo reintentamos.
        while (!isCancelled()) {

            if (!recibiendo) {
                recibiendo = conectayRecibeBT(device);
            }

        }

        cierra();
        return null;
    }

    private boolean conectayRecibeBT(BluetoothDevice device) {
//Abrimos la conexión con el dispositivo.
        boolean ok = true;


        try {
            contadorConexiones++;

            mSocket = device.createRfcommSocketToServiceRecord(getSerialPortUUID());
            mSocket.connect();
            aStream = mSocket.getInputStream();
            aReader = new InputStreamReader(aStream);
            mBufferedReader = new BufferedReader(aReader);
            Impedancia=new Gestor(temperatura);
            Tension = new Gestor(temperatura);
            String nombre="Tension.txt";
            int contadorV=1;
            int contadorZ=1;



            temperatura.setInformacion("Sin datos...");
            publishProgress(temperatura);
/*Mientras no se cancele la tarea asincrona (cuando se destruya la actividad)
se interroga al canal de comunicación por la temperatura*/
            Long comenzament= System.currentTimeMillis();
            int t=0;


            while (!isCancelled()) {

                if(t==0) {
                    comenzament = System.currentTimeMillis();
                    t=1;

                }

                try {

                    Log.e(".java, asin", "entra en primer try para recojer datos");
                    String aString = mBufferedReader.readLine();
                    if ((aString != null) && (!aString.isEmpty())) {
                        temperatura.setInformacion(sdf.format(new Date()));
                        try {

                            Log.e(".java, asin","entra en segundo try para recojer datos");
                            String[] s = aString.split(",");


                                if (s[0].equalsIgnoreCase("c1") && !s[1].isEmpty()) {
                                    temperatura.setTemperatura(s[1]);
                                    Impedancia.Guardar("calibracion1.txt");
                                }

                                if (s[0].equalsIgnoreCase("c2") && !s[1].isEmpty()) {

                                    temperatura.setTemperatura(s[1]);
                                    Impedancia.Guardar("calibracion2.txt");


                                }

                                if (s[0].equalsIgnoreCase("Z") && !s[1].isEmpty()) {

                                    temperatura.setTemperatura(s[1]);
                                    Impedancia.Guardar("Impedancia.txt");
                                    Impedancia.mostresZ=contadorZ;
                                    long fin=System.currentTimeMillis();
                                    long tiempo = (fin - comenzament)/1000;
                                    Tension.tiempo = tiempo;
                                    contadorZ++;

                                } else if (s[0].equalsIgnoreCase("V") && !s[1].isEmpty()) {

                                    temperatura.setTemperatura(s[1]);
                                    Tension.Guardar(nombre);
                                    Tension.mostresV = contadorV;
                                    long fin = System.currentTimeMillis();
                                    long tiempo = (fin - comenzament)/1000;
                                    Tension.tiempo = tiempo;
                                    contadorV++;

                                }

                                publishProgress(temperatura);

                        } catch (Exception e) {
//Si falla el formateo de los datos, no hacemos nada. Mostramos la excepción en la consola para
//observar el error.
                            e.printStackTrace();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

//Una vez la tarea se ha cancelado, cerramos la conexión con el dispositivo bluetooth.
            temperatura.setInformacion("Cerrando conexion BT");

        } catch (IOException e) {
            ok = false;
            e.printStackTrace();
            temperatura.setInformacion("Error conectando con dispositivo bt, reintento " + contadorConexiones + "... Si este error se repite, reinicie el arduino.");
            publishProgress(temperatura);
            cierra();

        }

        return ok;
    }

    private void cierra() {
        close(mBufferedReader);
        close(aReader);
        close(aStream);
        close(mSocket);
    }

    private UUID getSerialPortUUID() {
        return UUID.fromString(UUID_SERIAL_PORT_PROFILE);
    }

    private void close(Closeable aConnectedObject) {
        if (aConnectedObject == null) return;
        try {
            aConnectedObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aConnectedObject = null;
    }

    @Override
    protected void onProgressUpdate(Temperatura... values) {
        super.onProgressUpdate(values);
        callback.onTemperaturaUpdate(values[0]);
    }

    @Override
    protected void onCancelled() {
        callback.onCancelled();
    }
}
