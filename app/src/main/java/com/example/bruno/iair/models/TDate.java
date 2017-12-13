package com.example.bruno.iair.models;

/**
 * Created by tiago on 11/24/17.
 */

public class TDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public TDate(String date) {
        parseDate(date);
    }

    public TDate() {

    }

    public void parseDate(String date){
        this.year = Integer.parseInt(date.substring(0,4));
        this.month = Integer.parseInt(date.substring(5,7));
        this.day = Integer.parseInt(date.substring(8,10));
        this.hour = Integer.parseInt(date.substring(11,13));
        this.minute = Integer.parseInt(date.substring(14,16));
        this.second = Integer.parseInt(date.substring(17,19));
    }

    @Override
    public String toString() {
        String minuten = ""+minute;
       if(minute<10){
           minuten="0"+minute;
       }

        return day + "/" + month + "/" + year + " " + hour + ":" + minuten;
    }
    public String toStringCondensed(){
        return day + "/" + month + "/" + year;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }
}
