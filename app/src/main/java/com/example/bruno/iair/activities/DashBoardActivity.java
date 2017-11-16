package com.example.bruno.iair.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.MenuItemCompat;
import android.provider.Settings;
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

import java.util.LinkedList;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;

public class DashBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static LinkedList<City> cities;
    public static LinkedList<Country> countries;
    private static final int REQUEST_FAV = 1;


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
    private LinearLayout layoutInfo;
    private ListView listViewOfCities;
    private SearchView searchView;
    private ArrayAdapter<City> adapter;
    private String urlString;

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



        cities = new LinkedList<City>();
        countries = new LinkedList<Country>();
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
        layoutInfo = findViewById(R.id.layoutInfoo);
        listViewOfCities = findViewById(R.id.cityList);
        adapter = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cities);
        listViewOfCities.setAdapter(adapter);
        listViewOfCities.setTextFilterEnabled(true);


        populateCountries();
        populateCities();
        cities.get(1).setFavorite(true);

        favoriteCity = City.getFavoriteCity(cities);
        if(favoriteCity.getName().equals("GPS")){
            favoriteCity=currentLocation();
        }

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

        if (favoriteCity.isFavorite()) {
            checkFavorite.setChecked(true);
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (null != searchView) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
        }

        listViewOfCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent appInfo = new Intent(DashBoardActivity.this, CityActivity.class);
                String data = listViewOfCities.getAdapter().getItem(position).toString();
                appInfo.putExtra("city", data);
                startActivity(appInfo);
            }
        });

    }





    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(DashBoardActivity.this, "tosta", Toast.LENGTH_SHORT).show();
                listViewOfCities.setVisibility(GONE);
                layoutInfo.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }


    public static LinkedList<City> getCities() {
        return cities;
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
        MenuItem btnBack = menu.findItem(R.id.btnBack);
        btnBack.setVisible(FALSE);
        searchView = (SearchView) menu.findItem(R.id.btnSearch).getActionView();
        setupSearchView();

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                listViewOfCities.setVisibility(View.VISIBLE);
                layoutInfo.setVisibility(View.GONE);

            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                listViewOfCities.setVisibility(GONE);
                layoutInfo.setVisibility(View.VISIBLE);

            }
        });








        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnCity:
                Intent appInfo = new Intent(DashBoardActivity.this, CityListActivity.class);
                startActivityForResult(appInfo,REQUEST_FAV);
                break;
            case R.id.btnSearch:
                layoutInfo.setVisibility(GONE);
                listViewOfCities.setVisibility(View.VISIBLE);
                break;
            case R.id.btnBack:
                Toast.makeText(this, "BACK", Toast.LENGTH_SHORT).show();
                listViewOfCities.setVisibility(GONE);
                layoutInfo.setVisibility(View.VISIBLE);

                break;

            default:
                layoutInfo.setVisibility(View.VISIBLE);
                listViewOfCities.setVisibility(GONE);
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
    public static boolean isCityFavorite(String city) {
        for(City c : cities){
            if(c.getName().equals(city)){
                return c.isFavorite();
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)){
            listViewOfCities.clearTextFilter();
        } else {

            listViewOfCities.setFilterText(newText);
        }
        return true;
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
                    kmAux = getDistanceFromLatLonInKm(lat, lon, city.getLatitude(), city.getLongitude());
                    if (kmNearest > kmAux || kmNearest == 0){
                        kmNearest = kmAux;
                        nearestCity = city;
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
        }
    }

    private void atualizarLista() {
        favoriteCity = City.getFavoriteCity(cities);
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
}
