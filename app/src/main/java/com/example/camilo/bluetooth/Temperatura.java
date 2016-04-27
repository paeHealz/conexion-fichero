package com.example.camilo.bluetooth;

/**
 * Created by Camilo on 21/04/2016.
 */
public class Temperatura {

    private String informacion = "";
    private String temperatura = "";
    private String nombreDispositivo = "";

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getNombreDispositivo() {
        return nombreDispositivo;
    }

    public void setNombreDispositivo(String nodo) {
        this.nombreDispositivo = nodo;
    }
}
