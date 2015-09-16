package com.example.android.popularmovies.model;


import java.util.ArrayList;


public class MoviePOJO {
    private ArrayList<com.example.android.popularmovies.model.Results> results;

    public ArrayList<com.example.android.popularmovies.model.Results> getResults() {
        return this.results;
    }

    public void setResults(ArrayList<com.example.android.popularmovies.model.Results> results) {
        this.results = results;
    }
}