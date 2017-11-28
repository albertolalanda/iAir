package com.example.bruno.iair.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.bruno.iair.models.Event;
import com.example.bruno.iair.models.TDate;
import com.example.bruno.iair.services.GPSTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.LinkedList;

import static android.view.View.GONE;
import static java.lang.Boolean.FALSE;

public class DashBoardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static LinkedList<City> cities;
    public static LinkedList<Country> countries;
    private static final int REQUEST_FAV = 1;


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
    private TextView cityAirQualityDate;
    private TextView cityNitrogenDioxideData;
    private ListView listViewOfEvents;
    private CheckBox checkFavorite;
    private LinearLayout layoutInfo;
    private ListView listViewOfCities;
    private SearchView searchView;
    private ArrayAdapter<City> adapter;
    private ArrayAdapter<Event> adapterEvents;
    public static String citiesURL;
    public static String countriesURL;

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

        citiesURL = "https://api.thingspeak.com/channels/371900/feeds.json?api_key=ADDXWHYRJNAY95LZ";
        countriesURL = "https://api.thingspeak.com/channels/369386/feeds.json?api_key=EH9WYNAGVS2EDGNS";


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
        cityAirQualityDate = findViewById(R.id.textViewAirQualityDate);
        checkFavorite = findViewById(R.id.checkBoxFavorite);
        layoutInfo = findViewById(R.id.layoutInfoo);
        listViewOfCities = findViewById(R.id.cityList);
        adapter = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cities);
        listViewOfCities.setAdapter(adapter);
        listViewOfCities.setTextFilterEnabled(true);


        try {
            populateCountries();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            populateCities();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cities.get(1).setFavorite(true);

        favoriteCity = City.getFavoriteCity(cities);

        if(favoriteCity.getName().equals("GPS")){
            favoriteCity=currentLocation();
        }

        try {
            favoriteCity.updateData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cityName.setText(favoriteCity.getName());
        linearLayoutAQI.setBackgroundColor(Color.parseColor(favoriteCity.getColorAQI()));
        cityAQI.setText("Air Quality is " + favoriteCity.getAQI());
        cityTemperature.setText("Temperature: ");
        cityTemperatureData.setText(favoriteCity.getTemperature() + " ºC");
        cityHumidity.setText("Humidity: ");
        cityHumidityData.setText(favoriteCity.getHumidity() + " %");
        cityOzone.setText("O3: ");
        cityOzoneData.setText(favoriteCity.getOzoneO3() + "");
        cityCarbonMonoxide.setText("CO2: ");
        cityCarbonMonoxideData.setText(favoriteCity.getCarbonMonoxideCO() + "");
        cityNitrogenDioxide.setText("NO2: ");
        cityNitrogenDioxideData.setText(favoriteCity.getNitrogenDioxideNO2() + "");
        cityAirQualityDate.setText(favoriteCity.getDate());


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
                startActivityForResult(appInfo,REQUEST_FAV);
            }
        });


        listViewOfEvents = findViewById(R.id.LVEventList);
        adapterEvents = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, favoriteCity.getEvents());
        listViewOfEvents.setAdapter(adapterEvents);
        listViewOfEvents.setTextFilterEnabled(true);

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

    public void populateCountries() throws IOException, JSONException {
        JSONObject jsonObject = getJSONObjectFromURL(countriesURL);
        //System.out.println("******Countries******");
        // Last entry id:
        int last = jsonObject.getJSONObject("channel").getInt("last_entry_id");

        for (int i=0; i<last; i++){
            System.out.println(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"));
            System.out.println(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field2"));
            countries.add(new Country(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"),jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field2")));
        }

        //System.out.println(countries);
    }

    public Country findCountryWithID(String id){
        for (Country c : countries ) {
            if(c.getId().equals(id)){
                return c;
            }
        }
        return null;
    }

    public void populateCities() throws IOException, JSONException {
        JSONObject jsonObject = getJSONObjectFromURL(citiesURL);
        //System.out.println("******Cities******");
        // Last entry id:
        int last = jsonObject.getJSONObject("channel").getInt("last_entry_id");

        for (int i=0; i<last; i++){
            cities.add(new City(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field1"), findCountryWithID(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field2")), Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field3")), Double.parseDouble(jsonObject.getJSONArray("feeds").getJSONObject(i).getString("field4"))));
        }

        //System.out.println(cities);



    }


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
            try {
                atualizarLista();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void atualizarLista() throws IOException, JSONException {
        favoriteCity = City.getFavoriteCity(cities);
        if(favoriteCity.getName().equals("GPS")){
            favoriteCity=currentLocation();
        }
        favoriteCity.updateData();
        linearLayoutAQI.setBackgroundColor(Color.parseColor(favoriteCity.getColorAQI()));
        cityAQI.setText("Air Quality is " + favoriteCity.getAQI());
        cityName.setText(favoriteCity.getName());
        cityTemperature.setText("Temperature: ");
        cityTemperatureData.setText(favoriteCity.getTemperature() + " ºC");
        cityHumidity.setText("Humidity: ");
        cityHumidityData.setText(favoriteCity.getHumidity() + " %");
        cityOzone.setText("O3: ");
        cityOzoneData.setText(favoriteCity.getOzoneO3() + "");
        cityCarbonMonoxide.setText("CO: ");
        cityCarbonMonoxideData.setText(favoriteCity.getCarbonMonoxideCO() + "");
        cityNitrogenDioxide.setText("NO2: ");
        cityNitrogenDioxideData.setText(favoriteCity.getNitrogenDioxideNO2() + "");


        listViewOfEvents = findViewById(R.id.LVEventList);
        adapterEvents = new ArrayAdapter<Event>(this, android.R.layout.simple_list_item_1, favoriteCity.getEvents());
        listViewOfEvents.setAdapter(adapterEvents);
        listViewOfEvents.setTextFilterEnabled(true);

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

}
