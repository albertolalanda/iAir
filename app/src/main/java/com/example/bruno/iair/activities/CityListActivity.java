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

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;

import java.util.LinkedList;

public class CityListActivity extends AppCompatActivity {
    private ListView listOfCities;
    private ArrayAdapter<City> cAdapter;
    public int selectedPosition;
    public LinkedList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        for(City city:cities){
            if(city.isFavorite()){
                selectedPosition=cities.indexOf(city);
            }
        }

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.countrySpinner);
        final String[] countries = {"Portugal","Spain","France","USA"};
        ArrayAdapter<String> cAdapter2;
        spinner.setAdapter(cAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries));

        listOfCities = findViewById(R.id.cityList);
        cAdapter = new ArrayAdapter<City>(this, R.layout.item_city, R.id.textViewCityName, cities) {



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
                        DashBoardActivity.updateFavorite(cities.get(selectedPosition).toString());
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
