package com.example.bruno.iair.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.iair.R;
import com.example.bruno.iair.models.AirQualityData;
import com.example.bruno.iair.models.City;
import com.example.bruno.iair.models.SensorsData;
import com.example.bruno.iair.models.TDate;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static java.lang.Boolean.FALSE;

public class AllCitiesHistoryActivity extends AppCompatActivity{

    private Calendar calendar;
    private TextView date1View;
    private TextView date2View;
    private int year, month, day;
    private LinkedList<City> cities;
    private GraphView AQIBarGraph;
    private TDate date1;
    private TDate date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cities_history);

        //        MAIN_MENU
        Toolbar myToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        cities = new LinkedList<City>();
        cities = DashBoardActivity.getCities();

        // datepicker _________________________
        date1View = findViewById(R.id.textDate1);
        date2View = findViewById(R.id.textDate2);
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        date1 = new TDate();
        date2 = new TDate();

        try {
            showDate1(year, month+1, day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            showDate2(year, month+1, day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // graficos _________________________

        AQIBarGraph = findViewById(R.id.ppmBarGraph);

    }

    @SuppressWarnings("deprecation")
    public void setDate1(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    public void setDate2(View view) {
        showDialog(998);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        }else if(id == 998){
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    try {
                        showDate1(arg1, arg2+1, arg3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    try {
                        showDate2(arg1, arg2+1, arg3);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            };

    private void showDate1(int year, int month, int day) throws ParseException {
        date1View.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date1.setYear(year);
        date1.setMonth(month);
        date1.setDay(day);
        populateTablesAndGraphics(date1,date2);
    }
    private void showDate2(int year, int month, int day) throws ParseException {
        date2View.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date2.setYear(year);
        date2.setMonth(month);
        date2.setDay(day);
        populateTablesAndGraphics(date1,date2);
    }

    public void populateTablesAndGraphics(TDate Sdate, TDate Edate) throws ParseException {
        AQIBarGraph = findViewById(R.id.ppmBarGraph);
        AQIBarGraph.removeAllSeries();

        final List<Double> position = new ArrayList<>();
        double i = 1;

        // add empty value _______________________________
        List<DataPoint> listAQI = new ArrayList<>();
        listAQI.add(new DataPoint(0, 0.0));
        DataPoint[] dataPointArrayAQI = new DataPoint[listAQI.size()];
        listAQI.toArray(dataPointArrayAQI);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPointArrayAQI);
        series.setDrawValuesOnTop(false);
        AQIBarGraph.addSeries(series);

        for(City city :cities){
            position.add(i);

            listAQI = new ArrayList<>();
            double auxAQI = city.getOverallAverageAQI(Sdate,Edate);
            listAQI.add(new DataPoint(i, auxAQI));

            dataPointArrayAQI = new DataPoint[listAQI.size()];
            listAQI.toArray(dataPointArrayAQI);

            series = new BarGraphSeries<>(dataPointArrayAQI);

            if(auxAQI<=150) {
                //series.setTitle(i+". "+city.getName()+": Good");
                series.setColor(Color.GREEN);
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.BLACK);
            }else if(auxAQI<=300){
                //series.setTitle(i+". "+city.getName()+": Moderate");
                series.setColor(Color.YELLOW);
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.BLACK);
            }else if(auxAQI<= 450 || auxAQI > 450){
                //series.setTitle(i+". "+city.getName()+": Bad");
                series.setColor(Color.RED);
                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.BLACK);
            }else if(auxAQI==0.0){
                series.setDrawValuesOnTop(false);
            }

            AQIBarGraph.addSeries(series);

            i++;
        }

        // add empty value _______________________________
        listAQI = new ArrayList<>();
        listAQI.add(new DataPoint(i+1, 0.0));
        dataPointArrayAQI = new DataPoint[listAQI.size()];
        listAQI.toArray(dataPointArrayAQI);
        series = new BarGraphSeries<>(dataPointArrayAQI);
        series.setDrawValuesOnTop(false);
        AQIBarGraph.addSeries(series);

        AQIBarGraph.getLegendRenderer().setVisible(false);
        AQIBarGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        // set manual y bounds to have nice steps
        AQIBarGraph.getViewport().setYAxisBoundsManual(true);
        AQIBarGraph.getViewport().setMinY(0.0);
        AQIBarGraph.getViewport().setMaxY(500.0);

        // set manual x bounds to have nice steps
        AQIBarGraph.getViewport().setXAxisBoundsManual(true);
        AQIBarGraph.getViewport().setMinX(0.0);
        AQIBarGraph.getViewport().setMaxX(4.0);

        AQIBarGraph.getViewport().setScalableY(true);

        AQIBarGraph.getGridLabelRenderer().setHumanRounding(true);

        AQIBarGraph.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    for (int i=0;i<position.size();i++){
                        if(Math.abs(value) == position.get(i)){
                            return cities.get(i).getName();
                        }
                    }
                }else{
                    return ""+Math.round(value);
                }

                return "";
            }

            @Override
            public void setViewport(Viewport viewport) {

            }
        });
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
}
