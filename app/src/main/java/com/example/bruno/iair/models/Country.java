package com.example.bruno.iair.models;

/**
 * Created by tiago on 11/8/17.
 */

public class Country {
     /*DEFINE VARS*/
     private String name;
     private String id;

    public Country(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
