package com.example.bruno.iair.models;

import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by tiago on 11/27/17.
 */

public class SensorsData {

    /*DEFINE VARS*/
    private double temp;
    private double hum;

    public SensorsData(double temp, double hum) {
        this.temp = temp;
        this.hum = hum;
    }

    public double getTemp() {
        return temp;
    }

    public double getHum() {
        return hum;
    }
}
