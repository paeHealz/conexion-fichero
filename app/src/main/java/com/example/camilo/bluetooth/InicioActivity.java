package com.example.camilo.bluetooth;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;

public class InicioActivity extends Activity implements MiAsyncTask.MiCallback {
    private static final String TAG = "InicioActivity";
    static final int READ_BLOCK_SIZE = 100;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final String NOMBRE_DISPOSITIVO_BT = "HC-06";//Nombre de neustro dispositivo bluetooth.
    private TextView tvTemperatura;
    private TextView tvInformacion;
    private MiAsyncTask tareaAsincrona;
    private EditText textBox;
    File sdCard, directory, file = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*Inicializamos la activity e inflamos el layout*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*Obtenemos las referencias a los dos text views que usaremos para "pintar" la temperatura*/
        tvTemperatura = (TextView) findViewById(R.id.texto_temp);//Mostrará la temperatura
        tvInformacion = (TextView) findViewById(R.id.textView_estado_BT);//Mostrará la hora a la que fue registrada.
        textBox = (EditText) findViewById(R.id.txtText1);//inicia la caja vacia del text edit
    }

    @Override
    protected void onResume() {
/* El metodo on resume es el adecuado para inicialzar todos aquellos procesos que actualicen la interfaz de usuario
Por lo tanto invocamos aqui al método que activa el BT y crea la tarea asincrona que recupera los datos*/

        super.onResume();
        descubrirDispositivosBT();
    }
    private void descubrirDispositivosBT() {
/*
Este método comprueba si nuestro dispositivo dispone de conectividad bluetooh.
En caso afirmativo, si estuviera desctivada, intenta activarla.
En caso negativo presenta un mensaje al usuario y sale de la aplicación.
*/
//Comprobamos que el dispositivo tiene adaptador bluetooth
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        tvInformacion.setText("Comprobando bluetooth");

        if (mBluetoothAdapter != null) {

//El dispositivo tiene adapatador BT. Ahora comprobamos que bt esta activado.

            if (mBluetoothAdapter.isEnabled()) {
//Esta activado. Obtenemos la lista de dispositivos BT emparejados con nuestro dispositivo android.

                tvInformacion.setText("Obteniendo dispositivos emparejados, espere...");
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//Si hay dispositivos emparejados
                if (pairedDevices.size() > 0) {
/*
Recorremos los dispositivos emparejados hasta encontrar el
adaptador BT del arduino, en este caso se llama HC-06
*/

                    BluetoothDevice arduino = null;

                    for (BluetoothDevice device : pairedDevices) {
                        if (device.getName().equalsIgnoreCase(NOMBRE_DISPOSITIVO_BT)) {
                            arduino = device;
                        }
                    }

                    if (arduino != null) {
                        tareaAsincrona = new MiAsyncTask(this);
                        tareaAsincrona.execute(arduino);
                    } else {
//No hemos encontrado nuestro dispositivo BT, es necesario emparejarlo antes de poder usarlo.
//No hay ningun dispositivo emparejado. Salimos de la app.
                        Toast.makeText(this, "No hay dispositivos emparejados", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
//No hay ningun dispositivo emparejado. Salimos de la app.
                    Toast.makeText(this, "No hay dispositivos emparejados", Toast.LENGTH_LONG).show();
                    finish();

                }
            } else {
                muestraDialogoConfirmacionActivacion();
            }
        } else {

            Toast.makeText(this, "El dispositivo no soporta comunicación por Bluetooth", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onStop() {
     super.onStop();
        if (tareaAsincrona != null) {
            tareaAsincrona.cancel(true);
        }
    }

    @Override
    public void onTaskCompleted() {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onTemperaturaUpdate(Temperatura p) {

        tvTemperatura.setText(p.getTemperatura());
        tvInformacion.setText(p.getInformacion());

    }
    private void muestraDialogoConfirmacionActivacion() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Activar Bluetooth")
                .setMessage("BT esta desactivado. ¿Desea activarlo?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//Intentamos activarlo con el siguiente intent.
                        tvInformacion.setText("Activando BT");
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//Salimos de la app
                        finish();
                    }
                })
                .show();
    }

    public void onClickGuardar(View v){

        int a=edad();
        Gestor g=new Gestor(a);
        g.fijarAltura(a);
        g.fijarEdad(a + 1);
        g.fijarPeso(10);

    }

    public void onClickCargar(View v) {

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

                // Establecemos en el EditText el texto que hemos leido
                textBox.setText(s);

                // Mostramos un Toast con el proceso completado
                Toast.makeText(getBaseContext(), "Cargado", Toast.LENGTH_SHORT).show();

                isr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e(".java cargar", "no pudo entrar al cargar!!!!!");
            }
        }
    }

    public int edad(){

        String s= textBox.getText().toString();
        int numero=Integer.parseInt(s);
        return numero;

    }

}
