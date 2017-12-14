package com.example.bruno.iair.models;

/**
 * Created by tiago on 11/24/17.
 */

public class Event {
    /*DEFINE VARS*/
    private String city;
    private String type;
    private String userId;
    private String message;
    private TDate date;

    public Event(String city, String type, String userId, String message, TDate date) {
        this.city = city;
        this.type = type;
        this.userId = userId;
        this.message = message;
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return date + " | " + type + " | " + userId;
    }
}
