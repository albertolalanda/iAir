package com.example.bruno.iair;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    private ListView listOfCities;
    private ArrayAdapter<String> cAdapter;
    private LinearLayout layOutInfo;

    public static TextView cityText;
    private TextView condDescr;
    public static TextView temp;
    private TextView press;
    private TextView windSpeed;
    private TextView windDeg;

    private TextView hum;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String city = "Lisbon";

       // showCityData(city);


        layOutInfo = findViewById(R.id.layOutTInfo);
        cityText = (TextView) findViewById(R.id.cityText);
        condDescr = (TextView) findViewById(R.id.condDescr);
        temp = (TextView) findViewById(R.id.temp);
        hum = (TextView) findViewById(R.id.hum);
        press = (TextView) findViewById(R.id.press);
        windSpeed = (TextView) findViewById(R.id.windSpeed);
        windDeg = (TextView) findViewById(R.id.windDeg);
        imgView = (ImageView) findViewById(R.id.condIcon);

//
//        MAIN_MENU
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        listOfCities = findViewById(R.id.lst_cities);
        final String[] cities = {"Lisboa", "Porto", "Leiria"};
        listOfCities.setAdapter(cAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities));
        listOfCities.setTextFilterEnabled(true);

        listOfCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

                Intent appInfo = new Intent(MainActivity.this, CityActivity.class);
                String data = listOfCities.getAdapter().getItem(position).toString();
                appInfo.putExtra("city", data);
                startActivity(appInfo);

            }
        });

    }

   /* public void showCityData(String cityID){
        Downloadtask task = new Downloadtask();
        task.execute("http://api.waqi.info/feed/"+ cityID +"/?token=c926f68c35165c6286fa37999cf236d5e0dc7662");
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_itens, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.btnSearch).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        MenuItem cityItem = (MenuItem) menu.findItem(R.id.btnCity).getActionView();


        return super.onCreateOptionsMenu(menu);



    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnCity:
                Intent appInfo = new Intent(MainActivity.this, CityListActivity.class);
                startActivity(appInfo);
                break;
            default:
        }

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            listOfCities.clearTextFilter();
            //layOutInfo.setVisibility(View.VISIBLE);
            //listOfCities.setVisibility(View.INVISIBLE);
        } else {
            listOfCities.setVisibility(View.VISIBLE);
            //layOutInfo.setVisibility(View.GONE);
            //showCityData("Lisbon");
            listOfCities.setFilterText(newText.toString());
        }

        return true;
    }

    private class ItemClicked {
    }
}
