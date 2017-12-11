package com.example.bruno.iair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.iair.R;
import com.example.bruno.iair.models.City;

import java.util.LinkedList;

/**
 * Created by lalanda on 29-11-2017.
 */

public class EventsDataActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LinkedList<City> cities;
    private Button send;
    private City city;
    private String username;
    private RadioGroup radioEvent;
    private RadioButton radioFire;
    private RadioButton radioFlood;
    private RadioButton radioTsunami;
    private RadioButton radioEarthQuake;
    private RadioButton radioOther;
    private TextView textViewOtherEvent;
    private EditText editTextOtherEvent;
    private String eventName;
    private String messageName;
    private LinearLayout layoutFire;
    private LinearLayout layoutFlood;
    private LinearLayout layoutEarthquake;
    private LinearLayout layoutTsunami;
    private EditText editTextMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_data);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        username=DashBoardActivity.username;

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        final Spinner spinner = findViewById(R.id.citySensorSpinner);

        ArrayAdapter<City> cAdapter2 = new ArrayAdapter<City>(this, android.R.layout.simple_list_item_1, cities);
        spinner.setAdapter(cAdapter2);

        spinner.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        String favoriteCity = intent.getStringExtra("city");

        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(favoriteCity)){
                spinner.setSelection(i);
            }
        }

        send = findViewById(R.id.sendBtn);
        send.setEnabled(false);

        textViewOtherEvent = findViewById(R.id.textViewOtherEvent);
        editTextOtherEvent = findViewById(R.id.editTextOtherEvent);
        editTextOtherEvent.setEnabled(false);
        editTextMessage = findViewById(R.id.editMessage);
        editTextMessage.setEnabled(false);

        radioEvent = (RadioGroup) findViewById(R.id.radioEvent);
        radioEvent.clearCheck();

        layoutFire = (LinearLayout) findViewById(R.id.layoutFire);
        layoutFlood = (LinearLayout) findViewById(R.id.layoutFlood);
        layoutEarthquake = (LinearLayout) findViewById(R.id.layoutEarthquake);
        layoutTsunami = (LinearLayout) findViewById(R.id.layoutTsunami);

        radioFire = (RadioButton) findViewById(R.id.radioButtonFire);
        radioFlood = (RadioButton) findViewById(R.id.radioButtonFlood);
        radioEarthQuake = (RadioButton) findViewById(R.id.radioButtonEarthQuake);
        radioTsunami = (RadioButton) findViewById(R.id.radioButtonTsunami);
        radioOther = (RadioButton) findViewById(R.id.radioButtonOther);

        textViewOtherEvent.setText("Outro: ");


        layoutFire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(true);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Fire";
                editTextMessage.setText("There is a Fire in " + city.getName());
            }
        });
        radioFire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(true);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Fire";
                editTextMessage.setText("There is a Fire in " + city.getName());
            }
        });
        layoutFlood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(true);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Flood";
                editTextMessage.setText("There is a Flood in " + city.getName());
            }
        });
        radioFlood.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(true);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Flood";
                editTextMessage.setText("There is a Flood in " + city.getName());
            }
        });
        layoutEarthquake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(true);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Earthquake";
                editTextMessage.setText("There is a Earthquake in " + city.getName());
            }
        });
        radioEarthQuake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(true);
                radioTsunami.setChecked(false);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Earthquake";
                editTextMessage.setText("There is a Earthquake in " + city.getName());
            }
        });
        layoutTsunami.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(true);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Tsunami";
                editTextMessage.setText("There is a Tsunami in " + city.getName());
            }
        });
        radioTsunami.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(true);
                radioOther.setChecked(false);
                editTextOtherEvent.setEnabled(false);
                send.setEnabled(true);
                editTextMessage.setEnabled(true);
                eventName = "Tsunami";
                editTextMessage.setText("There is a Tsunami in " + city.getName());
            }
        });
        radioOther.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                radioFire.setChecked(false);
                radioFlood.setChecked(false);
                radioEarthQuake.setChecked(false);
                radioTsunami.setChecked(false);
                radioOther.setChecked(true);
                editTextOtherEvent.setEnabled(true);
                send.setEnabled(true);
                editTextMessage.setText(editTextOtherEvent.getText().toString());
                if(!editTextOtherEvent.getText().toString().trim().isEmpty()){
                    editTextMessage.setEnabled(true);
                }else{
                    editTextMessage.setEnabled(false);
                }
            }
        });

        editTextOtherEvent.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextMessage.setText(s);
                if(!editTextOtherEvent.getText().toString().trim().isEmpty()){
                    editTextMessage.setEnabled(true);
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioOther.isChecked() && send.isEnabled() && (editTextOtherEvent.getText().toString().trim().isEmpty() || editTextOtherEvent.getText().toString()==null)) {
                    Toast.makeText(EventsDataActivity.this, "Please write or choose the right event", Toast.LENGTH_LONG).show();
                }else{
                    if(send.isEnabled()){
                        if (radioOther.isChecked()){
                            eventName = editTextOtherEvent.getText().toString();
                        }
                        messageName = editTextMessage.getText().toString();
                        //collect and send data + gps coordenates from device ...
                        //  field1: "City",
                        //  field2: "Event",
                        //  field3: "Name",
                        String urlString = "https://api.thingspeak.com/update?api_key=6IC9U3VE1R4R44UI&field1=" + city.getName()
                                + "&field2=" + eventName
                                + "&field3=" + username
                                + "&field4=" + messageName;

                        // Instantiate the RequestQueue.
                        RequestQueue queue = Volley.newRequestQueue(EventsDataActivity.this);
                        String url = urlString;

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // Display the first 500 characters of the response string or not ...
                                        Toast.makeText(EventsDataActivity.this, "Data Sent", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EventsDataActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(false);

        MenuItem item = menu.findItem(R.id.btnCity);
        item.setVisible(false);

        MenuItem settings = menu.findItem(R.id.btnSettings);
        settings.setVisible(false);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        city = (City) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
