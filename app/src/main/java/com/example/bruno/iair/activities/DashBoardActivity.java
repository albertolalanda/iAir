package com.example.bruno.iair.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;

import java.util.LinkedList;

public class DashBoardActivity extends AppCompatActivity {

    public static LinkedList<City> cities;



    private City favoriteCity;
    private TextView cityName;
    private TextView cityTemperature;
    private TextView cityTemperatureData;
    private TextView cityHumidity;
    private TextView cityHumidityData;
    private TextView cityOzone;
    private TextView cityOzoneData;
    private TextView cityCarbonMonoxide;
    private TextView cityCarbonMonoxideData;
    private TextView cityNitrogenDioxide;
    private TextView cityNitrogenDioxideData;
    private CheckBox checkFavorite;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);


    cities = new LinkedList<City>();
    cityName = findViewById(R.id.textViewCityName);
    cityTemperature = findViewById(R.id.textViewCityTemperature);
    cityTemperatureData = findViewById(R.id.textViewTemperatureData);
    cityHumidity = findViewById(R.id.textViewCityHumidity);
    cityHumidityData = findViewById(R.id.textViewHumidityData);
    cityOzone = findViewById(R.id.textViewCityOzone);
    cityOzoneData = findViewById(R.id.textViewOzoneData);
    cityCarbonMonoxide = findViewById(R.id.textViewCarbonMonoxide);
    cityCarbonMonoxideData = findViewById(R.id.textViewCarbonMonoxideData);
    cityNitrogenDioxide = findViewById(R.id.textViewNitrogenDioxide);
    cityNitrogenDioxideData = findViewById(R.id.textViewNitrogenMonoxideData);
    checkFavorite = findViewById(R.id.checkBoxFavorite);

        populateCities();
        cities.get(1).setFavorite(true);

        favoriteCity = City.getFavoriteCity(cities);

        cityName.setText(favoriteCity.getName());
        cityTemperature.setText("Temperature: ");
        cityTemperatureData.setText(favoriteCity.getTemperature() + " ºC");
        cityHumidity.setText("Humidity: ");
        cityHumidityData.setText(favoriteCity.getHumidity() + " %");
        cityOzone.setText("Ozone: ");
        cityOzoneData.setText(favoriteCity.getOzoneO3() + " ppm");
        cityCarbonMonoxide.setText("Carbon Monoxide: ");
        cityCarbonMonoxideData.setText(favoriteCity.getCarbonMonoxideCO() + " ppm");
        cityNitrogenDioxide.setText("Nitrogen Dioxide: ");
        cityNitrogenDioxideData.setText(favoriteCity.getNitrogenDioxideNO2() + " ppm");

        if (favoriteCity.isFavorite()){
            checkFavorite.setChecked(true);
        }




    }

    public static LinkedList<City> getCities() {
        return cities;
    }

    public static void setCities(LinkedList<City> cities) {
        DashBoardActivity.cities = cities;
    }


    public void populateCities(){
        cities.add(new City("Lisbon", "PT", 30.22, -8.23, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("Leiria", "PT", 30.22, -8.23, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("Porto", "PT", 30.22, -8.23, 30.0, 18.43, 60.55, 60.55, 60.55));

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.btnBack);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnCity:
                Intent appInfo = new Intent(DashBoardActivity.this, CityListActivity.class);
                startActivity(appInfo);
                break;
            default:
        }

        return true;
    }

    public static void updateFavorite(String city) {
        for (City c:cities){
            if(c.isFavorite()){
                c.setFavorite(false);
            }
        }
        for (City c:cities){
            if(c.getName()==city){
                c.setFavorite(true);
            }
        }
    }
}
