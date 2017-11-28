package com.example.bruno.iair.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;

import java.util.Iterator;
import java.util.LinkedList;

public class SelectFavoriteCityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public LinkedList<City> filteredCities;
    public LinkedList<Country> countries;
    private ListView listOfCities;
    public String selectedCountry;
    public LinkedList<City> cities;
    public int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_favorite_city);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        cities = DashBoardActivity.getCities();
        filteredCities = (LinkedList<City>) cities.clone();

        countries = new LinkedList<Country>();
        countries = DashBoardActivity.getCountries();

        Spinner spinner = findViewById(R.id.countrySpinner);
        ArrayAdapter<Country> cAdapter2 = new ArrayAdapter<Country>(this, android.R.layout.simple_list_item_1, countries);
        spinner.setAdapter(cAdapter2);

        spinner.setOnItemSelectedListener(this);

        listOfCities = findViewById(R.id.cityList);
        listOfCities.setTextFilterEnabled(true);
        listOfCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                String data = listOfCities.getAdapter().getItem(position).toString();
                DashBoardActivity.updateFavorite(data);
                setResult(RESULT_OK);
                finish();
            }
        });

        Button btnCurrentLocation=findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                DashBoardActivity.updateFavorite("GPS");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        selectedCountry = item;
        filteredCities = (LinkedList<City>) cities.clone();

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

        ArrayAdapter<City> cAdapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName, filteredCities) {

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
                r.setVisibility(View.GONE);

                return v;
            }

        };
        listOfCities.setAdapter(cAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
