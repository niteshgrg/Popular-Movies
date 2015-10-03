package com.example.android.popularmovies.model;

import java.util.ArrayList;


public class VideoPOJO {

    private String id;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public ArrayList<com.example.android.popularmovies.model.Trailers> results;

    public ArrayList<com.example.android.popularmovies.model.Trailers> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<com.example.android.popularmovies.model.Trailers> results) {
        this.results = results;
    }
}
