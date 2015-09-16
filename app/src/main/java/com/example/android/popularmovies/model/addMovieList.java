package com.example.android.popularmovies.model;

import android.content.Context;

import java.util.ArrayList;


public class addMovieList {

    private ArrayList<com.example.android.popularmovies.model.Results> mResultsArrayList = new ArrayList<com.example.android.popularmovies.model.Results>();
    static addMovieList sMovieDataList;
    Context mContext;

    addMovieList(Context context) {

        mContext = context;
    }

    public static addMovieList get(Context context) {
        if (sMovieDataList == null) {
            sMovieDataList = new addMovieList(context);
        }
        return sMovieDataList;
    }

    public ArrayList<com.example.android.popularmovies.model.Results> getResultsArrayList() {

        return mResultsArrayList;
    }

    public void setResultsArrayList(ArrayList<com.example.android.popularmovies.model.Results> resultsList) {
        for (Results Results : resultsList)
            mResultsArrayList.add(Results);
    }

    public void setSingleResult(com.example.android.popularmovies.model.Results result) {

        mResultsArrayList.add(result);
    }

    public com.example.android.popularmovies.model.Results getSingleResultByPosition(int pos) {

        return mResultsArrayList.get(pos);
    }

    public void clearList() {

        mResultsArrayList.clear();
    }
}
