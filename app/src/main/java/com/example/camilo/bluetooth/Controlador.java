package com.example.camilo.bluetooth;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Camilo on 28/04/2016.
 */
public class Controlador {
    Gestor gestor;
    double FatMass=0;
    double TotalWater=0;
    double ExtraCelular=0;
    double IntrCelular=0;
    double BodyMass=0;

    public Controlador(){

        gestor.leer("HeartRate.txt");
        gestor.leer("Impedancia.txt");


    }

    public double TBW(){

        double tbw;

        tbw=0.6*((gestor.altura*gestor.altura)/gestor.impedanciaMedia);


        return tbw;
    }

    public double Hidratacion(){
       double alto= gestor.altura/100;
        ExtraCelular=((alto*alto)/gestor.impedanciaMedia)+2.7;

        return ExtraCelular;
    }

    public double FFM(){

        double total=TBW();
        double free;

        free=total/0.73;

        return free;
    }


}
