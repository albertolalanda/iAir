package com.example.bruno.iair.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.AirQualityData;
import com.example.bruno.iair.models.City;

import java.util.LinkedList;

public class CityHistoryActivity extends AppCompatActivity {

    private TextView cityName;
    private City thisCity;
    private LinkedList<City> cities;
    private TableLayout DataTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_history);
        cityName = findViewById(R.id.textViewCityName);

        DataTable = findViewById(R.id.DataTable);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");
        thisCity = findThisCity(city);

        cityName.setText(thisCity.getName());

        populateTables();

    }

    public City findThisCity(String name) {
        for(City c : cities){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    public void populateTables(){

        for (AirQualityData a : thisCity.getAirQualityHistory()){
            populateRowDataTable(a);

        }
    }

    public void populateRowDataTable(AirQualityData a){

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView date = new TextView(this);
        date.setPadding(10,10,10,10);
        if(a.getDate().getYear()==1971)
            date.setText("Date");
        else
            date.setText(a.getDate().toStringCondensed());
        tr.addView(date);

        TextView aqi = new TextView(this);
        aqi.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            aqi.setText("AQI");
        else
            aqi.setText(String.format("%.2f",a.getAveragePPM())+"");
        tr.addView(aqi);

        TextView o3 = new TextView(this);
        o3.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            o3.setText("O3");
        else
            o3.setText(a.getOzoneO3()+"");
        tr.addView(o3);

        TextView co2 = new TextView(this);
        co2.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            co2.setText("CO2");
        else
            co2.setText(a.getCarbonMonoxideCO()+"");
        tr.addView(co2);

        TextView no2 = new TextView(this);
        no2.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            no2.setText("NO2");
        else
            no2.setText(a.getNitrogenDioxideNO2()+"");
        tr.addView(no2);

        DataTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }
}
