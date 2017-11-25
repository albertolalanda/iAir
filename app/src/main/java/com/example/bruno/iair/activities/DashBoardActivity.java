package com.example.bruno.iair.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.MenuItemCompat;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;
import com.example.bruno.iair.services.GPSTracker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;

public class DashBoardActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static LinkedList<City> cities;
    public static LinkedList<Country> countries;
    private static final int REQUEST_FAV = 1;

    public static String username;
    private City favoriteCity;
    private TextView cityName;
    private LinearLayout linearLayoutAQI;
    private TextView cityAQI;
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
    private String urlString;
    private SwipeRefreshLayout swipeRefresh;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        urlString = "https://api.thingspeak.com/channels/365072/feeds.json?api_key=ZJAGHCE3DO174L1Z&results=2";

        SharedPreferences sharedPrefs = getSharedPreferences("username", MODE_PRIVATE);
        if (!sharedPrefs.contains("user")){
            username = generateUsername();
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("user", username);
            editor.commit();
        }
        else{
            username = sharedPrefs.getString("user", null);
        }

        swipeRefresh = findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);





        cities = new LinkedList<City>();
        countries = new LinkedList<Country>();
        cityName = findViewById(R.id.textViewCityName);
        cityAQI = findViewById(R.id.textViewAQI);
        linearLayoutAQI = findViewById(R.id.linearLayoutAQI);
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

        populateCountries();
        populateCities();
        //cities.get(1).setFavorite(true);

        favoriteCity = City.getFavoriteCity(cities);
        if(favoriteCity!=null){
            if(favoriteCity.getName().equals("GPS")){
                favoriteCity=currentLocation();
            }

            favoriteCity.updateData(urlString);

            cityName.setText(favoriteCity.getName());
            linearLayoutAQI.setBackgroundColor(Color.parseColor(favoriteCity.getColorAQI()));
            cityAQI.setText("Air Quality is " + favoriteCity.getAQI());
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

            if (favoriteCity.isFavorite()) {
                checkFavorite.setChecked(true);
            }
        }else{
            Intent appInfo = new Intent(DashBoardActivity.this, SelectFavoriteCityActivity.class);
            startActivityForResult(appInfo,REQUEST_FAV);
        }
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "oi", Toast.LENGTH_SHORT).show();
        atualizarLista();
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefresh.setRefreshing(false);
            }
        }, 1500);
    }

    public static LinkedList<City> getCities() {
        LinkedList<City> filteredCities = (LinkedList<City>) cities.clone();
        Iterator<City> iterator = filteredCities.iterator();

        while (iterator.hasNext()){
            City cc = iterator.next();
            if (cc.getName().equals("GPS")){
                iterator.remove();    // You can do the modification here.
            }
        }
        return filteredCities;
    }
    public static LinkedList<Country> getCountries() {
        return countries;
    }

    public static void setCities(LinkedList<City> cities) {
        DashBoardActivity.cities = cities;
    }

    public void populateCountries(){
        countries.add(new Country("Portugal", "PT"));
        countries.add(new Country("Spain", "ES"));
    }

    public Country findCountryWithID(String id){
        for (Country c : countries ) {
            if(c.getId()==id){
                return c;
            }
        }
        return null;
    }

    public void populateCities(){
        cities.add(new City("Lisbon", findCountryWithID("PT"), 38.7223263, -9.1392714, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("Leiria", findCountryWithID("PT"), 39.7495331, -8.807683, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("Porto", findCountryWithID("PT"), 41.1579438, -8.6291053, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("Barcelona", findCountryWithID("ES"), 30.22, -8.23, 30.0, 18.43, 60.55, 60.55, 60.55));
        cities.add(new City("GPS", new Country("GPS", "GPS"), 30.22, -8.23, 30.0, 18.43, 60.55, 60.55, 60.55));
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.btnSearch);
        searchItem.setVisible(false);

        MenuItem btnBack = menu.findItem(R.id.btnBack);
        btnBack.setVisible(FALSE);


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnCity:
                Intent appInfo = new Intent(DashBoardActivity.this, CityListActivity.class);
                startActivityForResult(appInfo,REQUEST_FAV);
                break;
            case R.id.btnSettings:
                Intent settingsIntent = new Intent(DashBoardActivity.this, SettingsActivity.class);
                startActivityForResult(settingsIntent, REQUEST_FAV);
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
            if(c.getName().equals(city)){
                c.setFavorite(true);
            }
        }
    }
    public static boolean isCityFavorite(String city) {
        for(City c : cities){
            if(c.getName().equals(city)){
                return c.isFavorite();
            }
        }
        return false;
    }


    private City currentLocation(){
        GPSTracker gps = new GPSTracker(this);

        City nearestCity = null;
        double kmAux = 0;
        double kmNearest = 0;
        // check if GPS location can get Location
        if (gps.canGetLocation()) {

            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.d("Your Location", "latitude:" + gps.getLatitude()
                        + ", longitude: " + gps.getLongitude());

                double lon = gps.getLongitude();
                double lat = gps.getLatitude();

                for(City city:cities){
                    if(!city.getName().equals("GPS")){
                        kmAux = getDistanceFromLatLonInKm(lat, lon, city.getLatitude(), city.getLongitude());
                        if (kmNearest > kmAux || kmNearest == 0){
                            kmNearest = kmAux;
                            nearestCity = city;
                        }
                    }
                }
            }
        }else{
            showAlert();
        }

        //Toast.makeText(DashBoardActivity.this, "Nearest City is " + nearestCity.getName() + " " +roundOff(kmNearest) +"km away...", Toast.LENGTH_LONG).show();




        return nearestCity;

    }
    public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2)
    {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }



    public double deg2rad(double deg) {
        return (deg * (Math.PI/180));
    }

    public double roundOff(double input){
        return (double) Math.round(input * 100) / 100;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==REQUEST_FAV) {
            atualizarLista();
        }else if(resultCode==RESULT_CANCELED){
            finish();
        }
    }

    private void atualizarLista() {
        favoriteCity = City.getFavoriteCity(cities);
        if(favoriteCity.getName().equals("GPS")){
            favoriteCity=currentLocation();
        }
        System.out.println(favoriteCity);
        favoriteCity.updateData(urlString);
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
    }

    public static String generateUsername(){
        Random ran = new Random();
        return "user" + ran.nextInt();
    }

}
