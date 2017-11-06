package com.example.bruno.iair.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;

import java.util.LinkedList;

public class CityListActivity extends AppCompatActivity {
    private ListView listOfCities;
    private ArrayAdapter<City> cAdapter;
    private CheckBox ch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);


        ch = findViewById(R.id.checkBoxFavorite);




        //        MAIN_MENU
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.countrySpinner);
        final String[] countries = {"Portugal","Spain","France","USA"};
        ArrayAdapter<String> cAdapter2;
        spinner.setAdapter(cAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries));

        listOfCities = findViewById(R.id.cityList);
        listOfCities.setAdapter(cAdapter = new ArrayAdapter<City>(this, R.layout.item_city,R.id.textViewCityName,DashBoardActivity.getCities()));


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
