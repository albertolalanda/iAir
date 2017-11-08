package com.example.bruno.iair.models;

import java.util.LinkedList;

/**
 * Created by bruno on 04/11/2017.
 */

public class City {

    /*DEFINE VARS*/
    private String name;
    private Country country;
    private double latitude;
    private double longitude;
    private double temperature;
    private double humidity;
    private double ozoneO3;
    private double carbonMonoxideCO;
    private double nitrogenDioxideNO2;



    private boolean isFavorite;

    public City() {

    }

    public City(String name, Country country, double latitude, double longitude, double temperature, double humidity, double ozoneO3, double carbonMonoxideCO, double nitrogenDioxideNO2) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.humidity = humidity;
        this.ozoneO3 = ozoneO3;
        this.carbonMonoxideCO = carbonMonoxideCO;
        this.nitrogenDioxideNO2 = nitrogenDioxideNO2;
        this.isFavorite = false;
    }
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    @Override
    public String toString() {
        return name;
    }

    public static City getFavoriteCity(LinkedList<City> cities) {
        for(City c: cities){
            if (c.isFavorite() == true){
                return c;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getOzoneO3() {
        return ozoneO3;
    }

    public void setOzoneO3(double ozoneO3) {
        this.ozoneO3 = ozoneO3;
    }

    public double getCarbonMonoxideCO() {
        return carbonMonoxideCO;
    }

    public void setCarbonMonoxideCO(double carbonMonoxideCO) {
        this.carbonMonoxideCO = carbonMonoxideCO;
    }

    public double getNitrogenDioxideNO2() {
        return nitrogenDioxideNO2;
    }

    public void setNitrogenDioxideNO2(double nitrogenDioxideNO2) {
        this.nitrogenDioxideNO2 = nitrogenDioxideNO2;
    }
}
