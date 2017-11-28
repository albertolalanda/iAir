package com.example.bruno.iair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Boolean.FALSE;

public class CityActivity extends AppCompatActivity {


    private TextView txtCity;
    private CheckBox chkFavorite;
    private City thisCity;
    private LinkedList<City> cities;
    private String urlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        urlString = "https://api.thingspeak.com/channels/365072/feeds.json?api_key=ZJAGHCE3DO174L1Z";


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");
        thisCity = findThisCity(city);

        try {
            thisCity.updateData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtCity = findViewById(R.id.txtCityName);
        chkFavorite = findViewById(R.id.chkFavorite);

        txtCity.setText(city);

        if(DashBoardActivity.isCityFavorite(city)){
            chkFavorite.setChecked(true);
            chkFavorite.setEnabled(false);
        }

        chkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkFavorite.isChecked()){
                    DashBoardActivity.updateFavorite(city);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(FALSE);
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

    public City findThisCity(String name) {
        for(City c : cities){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

}
