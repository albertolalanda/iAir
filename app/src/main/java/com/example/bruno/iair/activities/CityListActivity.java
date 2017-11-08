package com.example.bruno.iair.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.Country;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CityListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ListView listOfCities;
    private ArrayAdapter<City> cAdapter;
    public int selectedPosition;
    public LinkedList<City> cities;
    public LinkedList<City> filteredCities;
    public LinkedList<Country> countries;
    public String selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        filteredCities = (LinkedList<City>) cities.clone();

        countries = new LinkedList<Country>();
        countries = DashBoardActivity.getCountries();

        for(City city:filteredCities){
            if(city.isFavorite()){
                selectedPosition=filteredCities.indexOf(city);
            }
        }

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.countrySpinner);

        ArrayAdapter<Country> cAdapter2;
        spinner.setAdapter(cAdapter2 = new ArrayAdapter<Country>(this, android.R.layout.simple_list_item_1, countries));

        /*
        selectedCountry = spinner.getSelectedItem().toString();

        for (City ct : filteredCities){
            if(ct.getCountry().getName() != selectedCountry){
                filteredCities.remove(ct);
            }
        }
        */
        spinner.setOnItemSelectedListener(this);

        listOfCities = findViewById(R.id.cityList);
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
                    }
                });
                return v;
            }

        };
        listOfCities.setAdapter(cAdapter);


        listOfCities.setTextFilterEnabled(true);


        listOfCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Intent appInfo = new Intent(CityListActivity.this, CityActivity.class);
                String data = listOfCities.getAdapter().getItem(position).toString();
                appInfo.putExtra("city", data);
                startActivity(appInfo);
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
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName,filteredCities);
        listOfCities.setAdapter(adapter);
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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



}
