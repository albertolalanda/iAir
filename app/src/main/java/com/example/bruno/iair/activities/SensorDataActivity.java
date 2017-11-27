package com.example.bruno.iair.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;
import com.example.bruno.iair.services.GPSTracker;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SensorDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinkedList<City> cities;
    private CheckBox all;
    private CheckBox temp;
    private CheckBox hum;
    private Button send;
    private float tempData;
    private float humData;
    private City city;
    private double lon;
    private double lat;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.citySensorSpinner);

        ArrayAdapter<City> cAdapter2 = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cities);
        spinner.setAdapter(cAdapter2);

        spinner.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        String favoriteCity = intent.getStringExtra("city");

        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(favoriteCity)){
                spinner.setSelection(i);
            }
        }

        getGpsCoordenates();

        temp = findViewById(R.id.tempCheckBox);
        hum = findViewById(R.id.humCheckBox);
        send = findViewById(R.id.sendBtn);
        send.setEnabled(false);
        all = findViewById(R.id.allCheckBox);

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(all.isChecked()){
                    temp.setChecked(true);
                    hum.setChecked(true);
                    send.setEnabled(true);
                }else{
                    temp.setChecked(false);
                    hum.setChecked(false);
                    send.setEnabled(false);
                }
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all.isChecked() && !temp.isChecked()){
                    all.setChecked(false);
                    send.setEnabled(true);
                }else if(!temp.isChecked() && !hum.isChecked()){
                    send.setEnabled(false);
                }else if(temp.isChecked()){
                    send.setEnabled(true);
                }
            }
        });

        hum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all.isChecked() && !hum.isChecked()){
                    all.setChecked(false);
                }else if(!temp.isChecked() && !hum.isChecked()){
                    send.setEnabled(false);
                }else if(hum.isChecked()){
                    send.setEnabled(true);
                }
            }
        });

        PackageManager manager = getPackageManager();
        boolean hasTempSensor = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
        boolean hasHumSensor = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY);

        if (!hasTempSensor){
            temp.setEnabled(false);
            tempData=Float.NaN;
        }else{
            tempData=DashBoardActivity.getTemp();
            TextView tempText = findViewById(R.id.tempTextView);
            tempText.setText(String.valueOf(tempData));
        }

        if(!hasHumSensor){
            hum.setEnabled(false);
            humData=Float.NaN;
        }else{
            humData=DashBoardActivity.getHum();
            TextView humText = findViewById(R.id.humTextView);
            humText.setText(String.valueOf(humData));
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(send.isEnabled()){
                    //collect and send data + gps coordenates from device ...
                    if(lon == Double.MIN_VALUE && lat == Double.MIN_VALUE){
                        Toast.makeText(SensorDataActivity.this, "GPS must be active to be able to send data", Toast.LENGTH_SHORT).show();
                    }else{
                        //  field1: "City",
                        //  field2: "Latitude",
                        //  field3: "Longitude",
                        //  field4: "Temperature",
                        //  field5: "Humidity",
                        //  field6: "UserID"
                        String urlString = "https://api.thingspeak.com/update?api_key=ZX0IJFYM4610O2TY&field1=" + city.getName()
                                + "&field2=" + lat
                                + "&field3=" + lon
                                + "&field4=" + tempData
                                + "&field5=" + humData
                                + "&field6=110" /*+ username*/;

                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(SensorDataActivity.this);
                        String url = urlString;

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string or not ...
                                        Toast.makeText(SensorDataActivity.this, "Data Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(SensorDataActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(false);

        MenuItem item = menu.findItem(R.id.btnCity);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnBack:
                this.finish();
                break;
            default:
        }

        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = (City) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getGpsCoordenates(){
        lat=DashBoardActivity.getLat();
        lon=DashBoardActivity.getLon();
    }
}
