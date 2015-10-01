package com.example.android.popularmovies.model;

import java.util.ArrayList;

public class ReviewPOJO {

    private String id;

    private ArrayList<com.example.android.popularmovies.model.Reviews> results;

    public ArrayList<com.example.android.popularmovies.model.Reviews> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<com.example.android.popularmovies.model.Reviews> results) {
        this.results = results;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }
}
