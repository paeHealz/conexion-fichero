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
    File sdCard, directory, file = null;
    static final int READ_BLOCK_SIZE = 100000;
    Temperatura str;
    double FatMass=0;
    double TotalWater=0;
    double ExtraCelular=0;
    double IntrCelular=0;
    double BodyMass=0;
    double BioImpedance=0;

    public Controlador(){


    }

    public double TBW(){

        double tbw;

        tbw=0.6*((gestor.altura*gestor.altura)/BioImpedance);


        return 0;
    }

    public double Hidratacion(){
       double alto= gestor.altura/100;
        ExtraCelular=((alto*alto)/BioImpedance)+2.7;

        return 0;
    }

    public double FFM(){

        double total=TBW();
        double free;

        free=total/0.73;

        return free;
    }


}
