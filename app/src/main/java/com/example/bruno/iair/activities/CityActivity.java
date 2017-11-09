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
import android.widget.TextView;

import com.example.bruno.iair.R;

public class CityActivity extends AppCompatActivity {


    private TextView txtCity;
    private CheckBox chkFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");

        txtCity = findViewById(R.id.txtCityName);
        chkFavorite = findViewById(R.id.chkFavorite);

        txtCity.setText(city);

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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnBack:
                this.finish();
                break;
            case R.id.btnCity:
                Intent appInfo = new Intent(CityActivity.this, CityListActivity.class);
                startActivity(appInfo);
            default:
        }

        return true;
    }

}
