package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private String BASE_URL = "http://image.tmdb.org/t/p/w500/";

    private ImageView mBackdrop;
    private TextView mTitle;
    private TextView mRatings;
    private TextView mReleaseDate;
    private TextView mOverview;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        String id = extras.getString("id");
        String backdropPath = extras.getString("backdrop_path");
        String title = extras.getString("title");
        String rating = "Rating: " + extras.getString("ratings");
        String releaseDate = "Release Date: " + extras.getString("release_date");
        String overview = "Overview: " + extras.getString("overview");


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mBackdrop = (ImageView) rootView.findViewById(R.id.coverPoster_id);
        String url = BASE_URL + backdropPath;
        Log.e(LOG_TAG,"Detail poster url: " + url);
        Picasso.with(getActivity()).load(url).into(mBackdrop);

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mTitle.setText(title);

        mRatings = (TextView) rootView.findViewById(R.id.ratings);
        mRatings.setText(rating);

        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mReleaseDate.setText(releaseDate);

        mOverview = (TextView) rootView.findViewById(R.id.overview);
        mOverview.setText(overview);

        return rootView;
    }
}
