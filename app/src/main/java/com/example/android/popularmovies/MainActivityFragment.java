package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.popularmovies.API.MoviesInterface;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDbHelper;
import com.example.android.popularmovies.model.MoviePOJO;
import com.example.android.popularmovies.model.Results;
import com.example.android.popularmovies.model.addMovieList;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private String sortBy;
    ImageAdapter adapter;
    GridViewAdapter localAdapter;
    public ArrayList<Results> movies_info;
    SharedPreferences shared;
    ArrayList<Bitmap> posterImagesBitmap;
    private MoviesDbHelper mMoviesHelper;
    GridView gridview;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        shared = PreferenceManager.getDefaultSharedPreferences(getActivity());

        sortBy = shared.getString(getString(R.string.pref_key),
                getString(R.string.sort_by_default_value));


        gridview = (GridView) rootView.findViewById(R.id.gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString("id", movies_info.get(i).getId().toString());
                extras.putString("backdrop_path", movies_info.get(i).getBackdrop_path());
                extras.putString("poster_path", movies_info.get(i).getPoster_path());
                extras.putString("title", movies_info.get(i).getTitle());
                extras.putString("ratings", movies_info.get(i).getVote_average().toString());
                extras.putString("release_date", movies_info.get(i).getRelease_date());
                extras.putString("overview", movies_info.get(i).getOverview());
                intent.putExtras(extras);

                startActivity(intent);

            }
        });


        return rootView;
    }

    public void getMovies() {

        if(sortBy.equals("favorites"))
        {

            Log.e(LOG_TAG, "we are in favorites block");
            Cursor movieCursor = getActivity().getContentResolver().query(
                    MoviesContract.CONTENT_URI,
                    new String[]{MoviesContract.COL_MOVIE_ID, MoviesContract.COL_POSTER_PATH},
                    null,
                    null,
                    null
            );

            while (movieCursor.moveToNext())
            {
                int movieColumnIndex = movieCursor.getColumnIndex(MoviesContract.COL_MOVIE_ID);
                String coloumn_id = movieCursor.getString(movieColumnIndex);
                posterImagesBitmap.add(mMoviesHelper.getImage(coloumn_id, MoviesContract.COL_POSTER_PATH));
            }

            movieCursor.close();
            localAdapter.notifyDataSetChanged();

        }
        else {
            String ApiKey = "c8ea7e0252da1993f1dec16ac38c4157";
            String API = "http://api.themoviedb.org";

            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).build();
            MoviesInterface moviesApi = restAdapter.create(MoviesInterface.class);
            Log.d(LOG_TAG, "Sort by: " + sortBy);

            movies_info.clear();

            moviesApi.getMovies(sortBy, ApiKey, new Callback<MoviePOJO>() {

                public void success(MoviePOJO moviePOJO, Response response) {
                    addMovieList.get(getActivity()).setResultsArrayList(moviePOJO.getResults());
                    movies_info = addMovieList.get(getActivity()).getResultsArrayList();
                    adapter.updateContent(new ArrayList<Results>(movies_info));
                }

                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.e(LOG_TAG, "We are in onResume");
        sortBy = shared.getString(getString(R.string.pref_key),
                getString(R.string.sort_by_default_value));

        mMoviesHelper = new MoviesDbHelper(getActivity());

        if(!(sortBy.equals("favorites"))) {
            movies_info = new ArrayList<Results>();

            adapter = new ImageAdapter(getActivity(), movies_info);

            gridview.setAdapter(adapter);
        }
        else
        {
            posterImagesBitmap = new ArrayList<Bitmap> ();

            localAdapter = new GridViewAdapter(getActivity(), posterImagesBitmap);

            gridview.setAdapter(localAdapter);
        }
        getMovies();
    }
}
