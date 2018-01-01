package com.example.bruno.iair.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;

import com.example.bruno.iair.activities.DashBoardActivity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private LinkedList<Event> events;
    private LinkedList<AirQualityData> airQualityHistory;
    private LinkedList<SensorsData> sensorsDataHistory;



    private boolean isFavorite;

    public City() {

    }

    public City(String name, Country country, double latitude, double longitude) {
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavorite = false;
        this.events = new LinkedList<Event>();
        this.airQualityHistory = new LinkedList<AirQualityData>();
        this.sensorsDataHistory = new LinkedList<SensorsData>();
        this.sensorsDataHistory.add(new SensorsData(0,0,new TDate("1971-05-16T01:03:48Z")));
        this.airQualityHistory.add(new AirQualityData(0,0,0,new TDate("1971-05-16T01:03:48Z")));
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
        return sensorsDataHistory.get(sensorsDataHistory.size() - 1).getTemp();
    }

    public double getHumidity() {
        return sensorsDataHistory.get(sensorsDataHistory.size() - 1).getHum();
    }

    public double getOzoneO3() {
        return airQualityHistory.get(airQualityHistory.size() - 1).getOzoneO3();
    }

    public double getCarbonMonoxideCO() {
        return airQualityHistory.get(airQualityHistory.size() - 1).getCarbonMonoxideCO();
    }

    public double getNitrogenDioxideNO2() {
        return airQualityHistory.get(airQualityHistory.size() - 1).getNitrogenDioxideNO2();
    }

    public String getDate() {
        return airQualityHistory.get(airQualityHistory.size() - 1).getDate().toString();
    }




    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {
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

    public void updateData(JSONObject jsonObjectAirQualityData, JSONObject jsonObjectDataCitySensors, JSONObject jsonObjectCityEvents) throws IOException, JSONException {
        updateAirQualityHistory(jsonObjectAirQualityData);
        updateCityEvents(jsonObjectCityEvents);
        updateDataFromCitySensors(jsonObjectDataCitySensors);
    }

    public void updateAirQualityHistory(JSONObject jsonObject){
        try{


            //System.out.println(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field1"));

            //System.out.println("******"+ this.name +"******");
            // Last entry id:
            int last = jsonObject.getJSONObject("channel").getInt("last_entry_id");

            for (int i=0; i<last; i++){
                //System.out.println("---->" + jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"));
                if(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1").equals(this.name)){
                    airQualityHistory.add(new AirQualityData(
                            Double.parseDouble( jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field2") ),
                            Double.parseDouble( jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field4") ),
                            Double.parseDouble( jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field3") ),
                            new TDate(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("created_at"))
                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDataFromCitySensors(JSONObject jsonObject) throws IOException, JSONException {
        try{


            //System.out.println(jsonObject.getJSONArray("feeds").getJSONObject(0).getString("field1"));

            //System.out.println("******"+ this.name +"******");
            // Last entry id:
            int last = jsonObject.getJSONObject("channel").getInt("last_entry_id");

            for (int i=0; i<last; i++){
                //System.out.println("---->" + jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"));
                if(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1").equals(this.name)){
                    sensorsDataHistory.add(new SensorsData(
                            Double.parseDouble( jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field4") ),
                            Double.parseDouble( jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field5") ),
                            new TDate(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("created_at"))
                    ));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateCityEvents(JSONObject jsonObject) throws IOException, JSONException {

        //System.out.println("******Events******");
        // Last entry id:
        int last = jsonObject.getJSONObject("channel").getInt("last_entry_id");
        //System.out.println(jsonObject.getJSONObject("channel").getString("last_entry_id"));
        LinkedList<Event> events = new LinkedList<Event>();

        for (int i=0; i<last; i++){
            if(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1").equals(this.name)){
                events.add(new Event(
                        jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"),
                        jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field2"),
                        jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field3"),
                        jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field4"),
                        new TDate(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("created_at"))
                ));
            }
        }

        this.setEvents(events);
    }


    public String getAQI(){
        double averagePPM=(double)((this.getOzoneO3() + this.getNitrogenDioxideNO2() + this.getCarbonMonoxideCO()) /3);
        /*if(averagePPM<=54)
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
            return "Hazardous";*/
        if(averagePPM<=150)
            return "Good";
        if(averagePPM<=300)
            return "Moderate";
        if(averagePPM<=450 || averagePPM>450)
            return "Unhealthy";
        return "Error. this wasn't supposed to happen!";
    }

    public String getColorAQI(){
        double averagePPM=(double)((this.getOzoneO3() + this.getNitrogenDioxideNO2() + this.getCarbonMonoxideCO()) /3);
        /*if(averagePPM<=54)
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
            return "#cc0000";*/
        if(averagePPM<=150)
            return "#82FF82";
        if(averagePPM<=300)
            return "#FFFF70";
        if(averagePPM<=450 || averagePPM>450)
            return "#FF4747";
        return "#FFFFFF";
    }

    public LinkedList<Event> getEvents() {
        return events;
    }

    public void setEvents(LinkedList<Event> events) {
        this.events = events;
    }

    public LinkedList<AirQualityData> getAirQualityHistory() {
        return airQualityHistory;
    }

    public LinkedList<SensorsData> getSensorsDataHistory() {
        return sensorsDataHistory;
    }


}
