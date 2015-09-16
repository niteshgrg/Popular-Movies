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

    private ArrayList<Trailers> trailer;

    public ArrayList<Trailers> getResults() {
        return this.trailer;
    }

    public void setResults(ArrayList<Trailers> trailer) {
        this.trailer = trailer;
    }
}
