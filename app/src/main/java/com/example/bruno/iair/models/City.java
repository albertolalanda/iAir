package com.example.bruno.iair.models;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.example.bruno.iair.services.HumiditySensor;

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
    private SensorManager mSensorManager;
    private Sensor mHumiditySensor;
    private Sensor mTemperatureSensor;



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

    public static JSONObject getJSONObjectFromURL(String urlString, String cityName) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */ );
        urlConnection.setConnectTimeout(15000 /* milliseconds */ );
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();

        String jsonString = sb.toString();
        System.out.println("JSON: " + jsonString);

        return new JSONObject(jsonString);
    }

    public void updateData(String urlString){
        try{
            JSONObject jsonObject = this.getJSONObjectFromURL(urlString, this.name);
            System.out.println(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field1"));

            this.setOzoneO3(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field1")));
            this.setNitrogenDioxideNO2(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field2")));
            this.setCarbonMonoxideCO(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field3")));
            this.setTemperature(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field4")));
            this.setHumidity(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field5")));


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public void updateDataFromSensors(){
        try{
            int TYPE_RELATIVE_HUMIDITY;

            this.setOzoneO3(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field1")));
            this.setNitrogenDioxideNO2(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field2")));
            this.setCarbonMonoxideCO(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field3")));
            this.setTemperature(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field4")));
            this.setHumidity(Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field5")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/


    public String getAQI(){
        double averagePPM=(double)((this.getOzoneO3() + this.getNitrogenDioxideNO2() + this.getCarbonMonoxideCO()) /3);
        if(averagePPM<=54)
            return "Good";
        if(averagePPM<=154)
            return "Moderate";
        if(averagePPM<=254)
            return "Unhealthy for Sensitive Groups";
        if(averagePPM<=354)
            return "Unhealthy";
        if(averagePPM<=424)
            return "Very Unhealthy";
        if(averagePPM<=604||averagePPM>604)
            return "Hazardous";
        return "Error";
    }

    public String getColorAQI(){
        double averagePPM=(double)((this.getOzoneO3() + this.getNitrogenDioxideNO2() + this.getCarbonMonoxideCO()) /3);
        if(averagePPM<=54)
            return "#4dff4d";
        if(averagePPM<=154)
            return "#ffff33";
        if(averagePPM<=254)
            return "#ffaf1a";
        if(averagePPM<=354)
            return "#ff1a1a";
        if(averagePPM<=424)
            return "#e600e6";
        if(averagePPM<=604||averagePPM>604)
            return "#cc0000";
        return "#FFFFFF";
    }
}
