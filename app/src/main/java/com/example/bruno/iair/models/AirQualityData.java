package com.example.bruno.iair.models;

/**
 * Created by tiago on 11/27/17.
 */

public class AirQualityData {

    /*DEFINE VARS*/
    private double ozoneO3;
    private double carbonMonoxideCO;
    private double nitrogenDioxideNO2;
    private TDate date;

    public AirQualityData(double ozoneO3, double carbonMonoxideCO, double nitrogenDioxideNO2, TDate date) {
        this.ozoneO3 = ozoneO3;
        this.carbonMonoxideCO = carbonMonoxideCO;
        this.nitrogenDioxideNO2 = nitrogenDioxideNO2;
        this.date = date;
    }

    public double getOzoneO3() {
        return ozoneO3;
    }

    public double getCarbonMonoxideCO() {
        return carbonMonoxideCO;
    }

    public double getNitrogenDioxideNO2() {
        return nitrogenDioxideNO2;
    }


    public TDate getDate() {
        return date;
    }

}
