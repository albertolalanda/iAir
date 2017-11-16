package com.example.bruno.iair.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationManager;


import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.services.GPSTracker;
import com.example.bruno.iair.models.Country;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Boolean.FALSE;

public class CityListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int REQUEST_FAV = 1;
    private ListView listOfCities;
    private ArrayAdapter<City> cAdapter;
    public int selectedPosition;
    public LinkedList<City> cities;
    public LinkedList<City> filteredCities;
    public LinkedList<Country> countries;
    public String selectedCountry;
    private CheckBox gpsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        filteredCities = (LinkedList<City>) cities.clone();

        countries = new LinkedList<Country>();
        countries = DashBoardActivity.getCountries();

        /*for(City city:filteredCities){
            if(city.isFavorite()){
                selectedPosition=filteredCities.indexOf(city);
            }
        }*/

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.countrySpinner);

        ArrayAdapter<Country> cAdapter2;
        spinner.setAdapter(cAdapter2 = new ArrayAdapter<Country>(this, android.R.layout.simple_list_item_1, countries));


        spinner.setOnItemSelectedListener(this);

        listOfCities = findViewById(R.id.cityList);
        /*cAdapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName, filteredCities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.item_city, null);
                    RadioButton r = v.findViewById(R.id.radioFavorite);
                }
                TextView tv = v.findViewById(R.id.textViewCityName);
                tv.setText(cities.get(position).toString());
                RadioButton r = v.findViewById(R.id.radioFavorite);
                r.setChecked(position == selectedPosition);
                r.setTag(position);

                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer)view.getTag();
                        notifyDataSetChanged();
                        DashBoardActivity.updateFavorite(filteredCities.get(selectedPosition).toString());
                    }
                });
                return v;
            }

        };
        listOfCities.setAdapter(cAdapter);*/

        listOfCities.setTextFilterEnabled(true);


        listOfCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Intent appInfo = new Intent(CityListActivity.this, CityActivity.class);
                String data = listOfCities.getAdapter().getItem(position).toString();
                appInfo.putExtra("city", data);
                startActivityForResult(appInfo,REQUEST_FAV);
            }
        });

        Button btnCurrentLocation=findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                try {
                    City city = CityListActivity.this.currentLocation();
                    Intent appInfo = new Intent(CityListActivity.this, CityActivity.class);
                    appInfo.putExtra("city", city.getName());
                    startActivityForResult(appInfo,REQUEST_FAV);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        gpsBtn = findViewById(R.id.checkBoxFavGps);

        if(DashBoardActivity.isCityFavorite("GPS")){
            gpsBtn.setChecked(true);
            gpsBtn.setEnabled(false);
        }

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gpsBtn.isChecked()){
                    DashBoardActivity.updateFavorite("GPS");
                    atualizarLista();
                    gpsBtn.setEnabled(false);
                }
            }
        });


    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        selectedCountry = item;
        System.out.println(item);
        System.out.println(filteredCities);
        filteredCities = (LinkedList<City>) cities.clone();

        System.out.println(cities);
        Iterator<City> iterator = filteredCities.iterator();

        while (iterator.hasNext()){
            City cc = iterator.next();
            if (cc.getCountry().getName() != selectedCountry){
                iterator.remove();    // You can do the modification here.
            }
        }

        int i=0;
        for(City city:filteredCities){
            if(city.isFavorite()){
                i++;
                selectedPosition=filteredCities.indexOf(city);
            }
        }

        if(i==0){
            selectedPosition=-1;
        }

        cAdapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName, filteredCities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.item_city, null);
                    RadioButton r = v.findViewById(R.id.radioFavorite);
                }
                TextView tv = v.findViewById(R.id.textViewCityName);
                tv.setText(filteredCities.get(position).toString());
                RadioButton r = v.findViewById(R.id.radioFavorite);
                r.setChecked(position == selectedPosition);
                r.setTag(position);

                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("WTF HERE1!");
                        selectedPosition = (Integer)view.getTag();
                        notifyDataSetChanged();
                        DashBoardActivity.updateFavorite(filteredCities.get(selectedPosition).toString());
                        gpsBtn = findViewById(R.id.checkBoxFavGps);
                        System.out.println("WTF HERE2!");
                        if (!gpsBtn.isEnabled()){
                            gpsBtn.setChecked(false);
                            gpsBtn.setEnabled(true);
                        }
                    }
                });
                return v;
            }

        };
        listOfCities.setAdapter(cAdapter);
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

        Toast.makeText(CityListActivity.this, "Nearest City is " + nearestCity.getName() + " " +roundOff(kmNearest) +"km away...", Toast.LENGTH_LONG).show();




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



    /*private boolean isLocationEnabled() {
        return LocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || LocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }*/

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



    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(FALSE);
        MenuItem item = menu.findItem(R.id.btnCity);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnBack:
                setResult(RESULT_OK);
                this.finish();
                break;
            default:
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Activity result");
        if (resultCode==RESULT_OK && requestCode==REQUEST_FAV) {
            System.out.println("OK");
            atualizarLista();
        }
    }

    private void atualizarLista() {
        System.out.println("atualizar metudo");
        Iterator<City> iterator = filteredCities.iterator();

        while (iterator.hasNext()){
            City cc = iterator.next();
            if (cc.getCountry().getName() != selectedCountry){
                iterator.remove();    // You can do the modification here.
            }
        }
        System.out.println(filteredCities);
        int i=0;
        for(City city:filteredCities){
            if(city.isFavorite()){
                i++;
                selectedPosition=filteredCities.indexOf(city);
            }
        }

        if(i==0){
            selectedPosition=-1;
        }

        cAdapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName, filteredCities) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.item_city, null);
                    RadioButton r = v.findViewById(R.id.radioFavorite);
                }
                TextView tv = v.findViewById(R.id.textViewCityName);
                tv.setText(filteredCities.get(position).toString());
                RadioButton r = v.findViewById(R.id.radioFavorite);
                r.setChecked(position == selectedPosition);
                r.setTag(position);

                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer)view.getTag();
                        notifyDataSetChanged();
                        DashBoardActivity.updateFavorite(filteredCities.get(selectedPosition).toString());
                        gpsBtn = findViewById(R.id.checkBoxFavGps);
                        System.out.println("WTF HERE2!");
                        if (!gpsBtn.isEnabled()){
                            gpsBtn.setChecked(false);
                            gpsBtn.setEnabled(true);
                        }
                    }
                });
                return v;
            }

        };
        listOfCities.setAdapter(cAdapter);
    }

}
