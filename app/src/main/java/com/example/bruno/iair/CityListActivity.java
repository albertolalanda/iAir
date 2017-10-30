package com.example.bruno.iair;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import static java.lang.Boolean.FALSE;

public class CityListActivity extends AppCompatActivity {
    private ListView listOfCities;
    private ArrayAdapter<String> cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        //        MAIN_MENU
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Spinner spinner = findViewById(R.id.countrySpinner);
        final String[] countrys = {"Portugal","Spain","France","USA"};
        spinner.setAdapter(cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countrys));

        listOfCities = findViewById(R.id.cityList);
        final String[] cities = {"Lisboa", "Porto", "Leiria"};
        listOfCities.setAdapter(cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities));
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
        menuInflater.inflate(R.menu.menu_itens, menu);
        MenuItem item = menu.findItem(R.id.btnCity);
        item.setVisible(false);
        return true;
    }

}
