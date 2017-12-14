package com.example.bruno.iair.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.AirQualityData;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.SensorsData;
import com.example.bruno.iair.models.TDate;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static java.lang.Boolean.FALSE;

public class CityHistoryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView cityName;
    private City thisCity;
    private LinkedList<City> cities;
    private TableLayout DataTable;
    private TableLayout TempTable;
    private TableLayout HumTable;
    private GraphView AQIGraph;
    private GraphView TempGraph;
    private GraphView HumGraph;
    private Spinner spinnerStartDate;
    private Spinner spinnerEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_history);

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        cityName = findViewById(R.id.textViewCityName);

        DataTable = findViewById(R.id.DataTable);
        TempTable = findViewById(R.id.TempTable);
        HumTable = findViewById(R.id.HumTable);

        AQIGraph = findViewById(R.id.AQIGraph);
        TempGraph = findViewById(R.id.TempGraph);
        HumGraph = findViewById(R.id.HumGraph);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        Intent intent = getIntent();
        final String city = intent.getStringExtra("city");
        thisCity = findThisCity(city);

        cityName.setText(thisCity.getName());

        ArrayList<TDate> dates = getListOfDates();

        ArrayAdapter<TDate> adapter = new ArrayAdapter<TDate>(
                this, android.R.layout.simple_spinner_item, dates);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStartDate = (Spinner) findViewById(R.id.spinnerStartDate);
        spinnerStartDate.setAdapter(adapter);
        spinnerStartDate.setOnItemSelectedListener(this);
        spinnerEndDate = (Spinner) findViewById(R.id.spinnerEndDate);
        spinnerEndDate.setAdapter(adapter);
        spinnerEndDate.setOnItemSelectedListener(this);
        spinnerEndDate.setSelection(dates.size()-1);

        populateTablesAndGraphics(parseString(spinnerStartDate.getSelectedItem().toString()),parseString(spinnerEndDate.getSelectedItem().toString()));

    }

    public City findThisCity(String name) {
        for(City c : cities){
            if(c.getName().equals(name)){
                return c;
            }
        }
        return null;
    }

    public void populateTablesAndGraphics(TDate Sdate, TDate Edate){

        List<DataPoint> listO3 = new ArrayList<DataPoint>();
        List<DataPoint> listCO = new ArrayList<DataPoint>();
        List<DataPoint> listNO2 = new ArrayList<DataPoint>();
        List<DataPoint> listAQI = new ArrayList<DataPoint>();
        List<Date> dateList = new ArrayList<Date>();
        for (AirQualityData a : thisCity.getAirQualityHistory()){
            System.out.println("Entrou na funcao");
            if(a.getDate().getYear()>=Sdate.getYear() && a.getDate().getMonth()>=Sdate.getMonth() && a.getDate().getDay()>=Sdate.getDay()
               && a.getDate().getYear()<=Edate.getYear() && a.getDate().getMonth()<=Edate.getMonth() && a.getDate().getDay()<=Edate.getDay()) {
                System.out.println("Entrou no if");
                populateRowDataTable(a);
                if (a.getDate().getYear() != 1971) {
                    GregorianCalendar cal = new GregorianCalendar(a.getDate().getYear(), a.getDate().getMonth() - 1, a.getDate().getDay());
                    Date d = cal.getTime();
                    dateList.add(d);
                    listO3.add(new DataPoint(d, a.getOzoneO3()));
                    listCO.add(new DataPoint(d, a.getCarbonMonoxideCO()));
                    listNO2.add(new DataPoint(d, a.getNitrogenDioxideNO2()));
                    listAQI.add(new DataPoint(d, a.getAveragePPM()));
                }
            }
        }

        DataPoint[] dataPointArrayO3 = new DataPoint[listO3.size()];
        listO3.toArray(dataPointArrayO3);
        LineGraphSeries<DataPoint> O3series = new LineGraphSeries<>(dataPointArrayO3);
        // styling series
        O3series.setTitle("O3");
        O3series.setColor(Color.BLUE);
        O3series.setDrawDataPoints(true);
        O3series.setDataPointsRadius(10);
        O3series.setThickness(8);
        AQIGraph.addSeries(O3series);

        DataPoint[] dataPointArrayCO = new DataPoint[listCO.size()];
        listCO.toArray(dataPointArrayCO);
        LineGraphSeries<DataPoint> COseries = new LineGraphSeries<>(dataPointArrayCO);
        // styling series
        COseries.setTitle("CO");
        COseries.setColor(Color.BLACK);
        COseries.setDrawDataPoints(true);
        COseries.setDataPointsRadius(10);
        COseries.setThickness(8);
        AQIGraph.addSeries(COseries);

        DataPoint[] dataPointArrayNO2 = new DataPoint[listNO2.size()];
        listNO2.toArray(dataPointArrayNO2);
        LineGraphSeries<DataPoint> NO2series = new LineGraphSeries<>(dataPointArrayNO2);
        // styling series
        NO2series.setTitle("NO2");
        NO2series.setColor(Color.GREEN);
        NO2series.setDrawDataPoints(true);
        NO2series.setDataPointsRadius(10);
        NO2series.setThickness(8);
        AQIGraph.addSeries(NO2series);

        DataPoint[] dataPointArrayAQI = new DataPoint[listAQI.size()];
        listAQI.toArray(dataPointArrayAQI);
        LineGraphSeries<DataPoint> AQIseries = new LineGraphSeries<>(dataPointArrayAQI);
        // styling series
        AQIseries.setTitle("AQI");
        AQIseries.setColor(Color.RED);
        AQIseries.setDrawDataPoints(true);
        AQIseries.setDataPointsRadius(10);
        AQIseries.setThickness(8);
        AQIGraph.addSeries(AQIseries);


        AQIGraph.getLegendRenderer().setVisible(true);
        AQIGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set date label formatter
        AQIGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE)));
        AQIGraph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        AQIGraph.getViewport().setMinX(dateList.get(0).getTime());
        AQIGraph.getViewport().setMaxX(dateList.get(dateList.size()-1).getTime());
        AQIGraph.getViewport().setXAxisBoundsManual(true);

        // set manual y bounds to have nice steps
        //AQIGraph.getViewport().setMinY(0);
        //AQIGraph.getViewport().setMaxY(1000);
        //AQIGraph.getViewport().setYAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        AQIGraph.getGridLabelRenderer().setHumanRounding(false);


        List<DataPoint> listTemp = new ArrayList<DataPoint>();
        List<DataPoint> listHum = new ArrayList<DataPoint>();
        List<Date> dateList2 = new ArrayList<Date>();
        for (SensorsData d : thisCity.getSensorsDataHistory()){
            populateRowTempTable(d);
            populateRowHumTable(d);
            if(d.getDate().getYear()!=1971) {
                GregorianCalendar cal = new GregorianCalendar(d.getDate().getYear(), d.getDate().getMonth() - 1, d.getDate().getDay());
                Date date = cal.getTime();
                dateList2.add(date);
                listTemp.add(new DataPoint(date, d.getTemp()));
                listHum.add(new DataPoint(date, d.getHum()));
            }
        }

        DataPoint[] dataPointArrayTemp = new DataPoint[listTemp.size()];
        listTemp.toArray(dataPointArrayTemp);
        LineGraphSeries<DataPoint> Tempseries = new LineGraphSeries<>(dataPointArrayTemp);
        // styling series
        Tempseries.setTitle("Temperature");
        Tempseries.setColor(Color.RED);
        Tempseries.setDrawDataPoints(true);
        Tempseries.setDataPointsRadius(10);
        Tempseries.setThickness(8);
        TempGraph.addSeries(Tempseries);

        TempGraph.getLegendRenderer().setVisible(true);
        TempGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set date label formatter
        TempGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE)));
        TempGraph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        TempGraph.getViewport().setMinX(dateList2.get(0).getTime());
        TempGraph.getViewport().setMaxX(dateList2.get(dateList2.size()-1).getTime());
        TempGraph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        TempGraph.getGridLabelRenderer().setHumanRounding(false);

        DataPoint[] dataPointArrayHum = new DataPoint[listHum.size()];
        listHum.toArray(dataPointArrayHum);
        LineGraphSeries<DataPoint> Humseries = new LineGraphSeries<>(dataPointArrayHum);
        // styling series
        Humseries.setTitle("Humidity");
        Humseries.setColor(Color.BLUE);
        Humseries.setDrawDataPoints(true);
        Humseries.setDataPointsRadius(10);
        Humseries.setThickness(8);
        HumGraph.addSeries(Humseries);

        HumGraph.getLegendRenderer().setVisible(true);
        HumGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set date label formatter
        HumGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), DateFormat.getDateInstance(DateFormat.SHORT, Locale.FRANCE)));
        HumGraph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        HumGraph.getViewport().setMinX(dateList2.get(0).getTime());
        HumGraph.getViewport().setMaxX(dateList2.get(dateList2.size()-1).getTime());
        HumGraph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        HumGraph.getGridLabelRenderer().setHumanRounding(false);
    }

    public void populateRowDataTable(AirQualityData a){

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView date = new TextView(this);
        date.setPadding(10,10,10,10);
        if(a.getDate().getYear()==1971)
            date.setText("Date");
        else
            date.setText(a.getDate().toString());
        tr.addView(date);

        TextView aqi = new TextView(this);
        aqi.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            aqi.setText("AQI");
        else
            aqi.setText(String.format("%.2f",a.getAveragePPM())+"");
        tr.addView(aqi);

        TextView o3 = new TextView(this);
        o3.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            o3.setText("O3");
        else
            o3.setText(a.getOzoneO3()+"");
        tr.addView(o3);

        TextView co = new TextView(this);
        co.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            co.setText("CO");
        else
            co.setText(a.getCarbonMonoxideCO()+"");
        tr.addView(co);

        TextView no2 = new TextView(this);
        no2.setPadding(30,10,10,10);
        if(a.getDate().getYear()==1971)
            no2.setText("NO2");
        else
            no2.setText(a.getNitrogenDioxideNO2()+"");
        tr.addView(no2);

        DataTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void populateRowTempTable(SensorsData d){

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView date = new TextView(this);
        date.setPadding(10,10,10,10);
        if(d.getDate().getYear()==1971)
            date.setText("Date");
        else
            date.setText(d.getDate().toString());
        tr.addView(date);

        TextView temp = new TextView(this);
        temp.setPadding(30,10,10,10);
        if(d.getDate().getYear()==1971)
            temp.setText("Temperature");
        else
            temp.setText(d.getTemp()+"");
        tr.addView(temp);

        TempTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    public void populateRowHumTable(SensorsData d){

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView date = new TextView(this);
        date.setPadding(10,10,10,10);
        if(d.getDate().getYear()==1971)
            date.setText("Date");
        else
            date.setText(d.getDate().toString());
        tr.addView(date);

        TextView hum = new TextView(this);
        hum.setPadding(30,10,10,10);
        if(d.getDate().getYear()==1971)
            hum.setText("Humidity");
        else
            hum.setText(d.getHum()+"");
        tr.addView(hum);

        HumTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem btnSearch = menu.findItem(R.id.btnSearch);
        btnSearch.setVisible(FALSE);

        MenuItem btnSettings = menu.findItem(R.id.btnSettings);
        btnSettings.setVisible(FALSE);

        MenuItem item = menu.findItem(R.id.btnCity);
        item.setVisible(false);

        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item

        System.out.println(spinnerStartDate.getSelectedItem().toString());
        System.out.println(spinnerStartDate.getSelectedItem().toString().substring(0,2));
        System.out.println(spinnerStartDate.getSelectedItem().toString().substring(3,5));
        System.out.println(spinnerStartDate.getSelectedItem().toString().substring(6,10));





    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

    public ArrayList<TDate> getListOfDates(){
        ArrayList<TDate> dates = new ArrayList<TDate>();
        for(AirQualityData d : thisCity.getAirQualityHistory()){
            if(d.getDate().getYear()!=1971)
                dates.add(d.getDate());
        }
        return dates;
    }

    public TDate parseString(String s){
        TDate date = new TDate();
        date.setDay(Integer.parseInt(s.substring(0,2)));
        date.setMonth(Integer.parseInt(s.substring(3,5)));
        date.setYear(Integer.parseInt(s.substring(6,10)));
        return date;
    }

}
