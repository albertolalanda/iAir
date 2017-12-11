package com.example.bruno.iair.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import static com.example.bruno.iair.activities.DashBoardActivity.getJSONObjectFromURL;
import static java.lang.Boolean.FALSE;

public class CityActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{


    private TextView txtCity;
    private CheckBox chkFavorite;
    private City thisCity;
    private LinkedList<City> cities;

    private LinearLayout linearLayoutAQI;
    private TextView cityAQI;
    private TextView cityTemperatureData;
    private TextView cityHumidityData;
    private TextView cityOzoneData;
    private TextView cityCarbonMonoxideData;
    private TextView cityAirQualityDate;
    private TextView cityNitrogenDioxideData;
    private ListView listViewOfEvents;
    private ArrayAdapter<Event> adapterEvents;
    private SwipeRefreshLayout swipeRefresh;
    private TextView noEventsTextview;

    private JSONObject jsonObjectAirQualityData;
    private JSONObject jsonObjectDataCitySensors;
    private JSONObject jsonObjectCityEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");
        thisCity = findThisCity(city);

        txtCity = findViewById(R.id.txtCityName);
        chkFavorite = findViewById(R.id.chkFavorite);

        txtCity.setText(city);

        if(DashBoardActivity.isCityFavorite(city)){
            chkFavorite.setChecked(true);
            chkFavorite.setEnabled(false);
        }

        chkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkFavorite.isChecked()){
                    DashBoardActivity.updateFavorite(city);
                }
            }
        });

        cityAQI = findViewById(R.id.textViewAQI);
        linearLayoutAQI = findViewById(R.id.linearLayoutAQI);
        cityTemperatureData = findViewById(R.id.textViewTemperatureData);
        cityHumidityData = findViewById(R.id.textViewHumidityData);
        cityOzoneData = findViewById(R.id.textViewOzoneData);
        cityCarbonMonoxideData = findViewById(R.id.textViewCarbonMonoxideData);
        cityNitrogenDioxideData = findViewById(R.id.textViewNitrogenMonoxideData);
        cityAirQualityDate = findViewById(R.id.textViewAirQualityDate);
        noEventsTextview = findViewById(R.id.noEventTextView);

        linearLayoutAQI.setBackgroundColor(Color.parseColor(thisCity.getColorAQI()));
        cityAQI.setText("Air Quality is " + thisCity.getAQI());
        cityOzoneData.setText(thisCity.getOzoneO3() + "");
        cityCarbonMonoxideData.setText(thisCity.getCarbonMonoxideCO() + "");
        cityNitrogenDioxideData.setText(thisCity.getNitrogenDioxideNO2() + "");
        cityTemperatureData.setText(thisCity.getTemperature() + " ºC");
        cityHumidityData.setText(thisCity.getHumidity() + " %");

        listViewOfEvents = findViewById(R.id.LVEventList);
        LinkedList<Event> events = thisCity.getEvents();
        if(events.isEmpty()){
            noEventsTextview.setVisibility(View.VISIBLE);
            listViewOfEvents.setVisibility(View.GONE);
        }else{
            noEventsTextview.setVisibility(View.GONE);
            listViewOfEvents.setVisibility(View.VISIBLE);
            adapterEvents = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, events);
            listViewOfEvents.setAdapter(adapterEvents);
            listViewOfEvents.setTextFilterEnabled(true);
        }
        cityAirQualityDate.setText(thisCity.getDate());

        swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        Button infoHistoryBtn = findViewById(R.id.infoHistoryBtn);
        infoHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appInfo = new Intent(CityActivity.this, CityHistoryActivity.class);
                String data = thisCity.getName();
                appInfo.putExtra("city", data);
                startActivity(appInfo);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(FALSE);

        MenuItem menuItmCity = menu.findItem(R.id.btnCity);
        menuItmCity.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnBack:
                setResult(RESULT_OK);
                this.finish();
                break;
            case R.id.btnSettings:
                Intent settingsIntent = new Intent(CityActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
        }

        return true;
    }

    public City findThisCity(String name) {
        for(City c : cities){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    private void atualizarListaRefresh() throws IOException, JSONException {

        try {
            String airDataURL = "https://api.thingspeak.com/channels/373908/feeds.json?api_key=IRDG2HB6BC8VG461";
            jsonObjectAirQualityData = getJSONObjectFromURL(airDataURL);
            String sensorsURL = "https://api.thingspeak.com/channels/373891/feeds.json?api_key=VC0UA9ODEMHK7APY";
            jsonObjectDataCitySensors = getJSONObjectFromURL(sensorsURL);
            String eventsURL = "https://api.thingspeak.com/channels/371908/feeds.json?api_key=1SED3ZW7C4B1A8J2";
            jsonObjectCityEvents = getJSONObjectFromURL(eventsURL);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(City c:DashBoardActivity.getCities()){
            c.updateData(jsonObjectAirQualityData,jsonObjectDataCitySensors,jsonObjectCityEvents);
        }
        linearLayoutAQI.setBackgroundColor(Color.parseColor(thisCity.getColorAQI()));
        cityAQI.setText("Air Quality is " + thisCity.getAQI());
        cityOzoneData.setText(thisCity.getOzoneO3() + "");
        cityCarbonMonoxideData.setText(thisCity.getCarbonMonoxideCO() + "");
        cityNitrogenDioxideData.setText(thisCity.getNitrogenDioxideNO2() + "");
        cityTemperatureData.setText(thisCity.getTemperature() + " ºC");
        cityHumidityData.setText(thisCity.getHumidity() + " %");

        listViewOfEvents = findViewById(R.id.LVEventList);
        LinkedList<Event> events = thisCity.getEvents();
        if(events.isEmpty()){
            noEventsTextview.setVisibility(View.VISIBLE);
            listViewOfEvents.setVisibility(View.GONE);
        }else{
            noEventsTextview.setVisibility(View.GONE);
            listViewOfEvents.setVisibility(View.VISIBLE);
            adapterEvents = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, events);
            listViewOfEvents.setAdapter(adapterEvents);
            listViewOfEvents.setTextFilterEnabled(true);
        }
        cityAirQualityDate.setText(thisCity.getDate());

    }

    @Override
    public void onRefresh() {
        try {
            atualizarListaRefresh();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefresh.setRefreshing(false);
            }
        }, 1500);
    }
}
