package com.example.bruno.iair;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bruno on 01/11/2017.
 */

public class Downloadtask extends AsyncTask<String,Void,String> {
    String result = "";
    URL url;
    HttpURLConnection urlConnection = null;
    @Override
    protected String doInBackground(String... strings) {

        try {
            url = new URL(strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);

            int data = inReader.read();

            while (data != -1){
                char current = (char) data;

                result += current;

                data = inReader.read();
            }

            return result;
        } catch (Exception e) {
            e.getMessage();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            JSONObject jsonObject = new JSONObject(result);
            String weatherInfo = jsonObject.getString("weather");

            JSONObject weatherDatas = new JSONObject(jsonObject.getString("main"));

            double tempInt = Double.parseDouble(weatherDatas.getString("temp"));

            MainActivity.temp.setText(String.valueOf(tempInt) + "C");

            MainActivity.cityText.setText(jsonObject.getString("name"));

            JSONArray jsonArray = new JSONArray(weatherInfo);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPart = jsonArray.getJSONObject(i);


            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
