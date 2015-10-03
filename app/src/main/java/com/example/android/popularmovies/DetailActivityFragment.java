package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.API.MoviesInterface;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDbHelper;
import com.example.android.popularmovies.model.VideoPOJO;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private String BASE_URL = "http://image.tmdb.org/t/p/w500/";
    private MoviesDbHelper mMoviesHelper;

    private ImageView mBackdrop;
    private TextView mTitle;
    private TextView mRatings;
    private TextView mReleaseDate;
    private TextView mOverview;
    private CheckBox mFavorite;
    private Bitmap coverImage;
    private Bitmap posterImage;
    ArrayList<String> trailerList;
    ArrayAdapter<String> adapter;

    VideoPOJO mVideoPOJO;
    private String id;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("id");
        final String backdropPath = extras.getString("backdrop_path");
        final String posterPath =extras.getString("poster_path");
        final String title = extras.getString("title");
        final String rating = "Rating: " + extras.getString("ratings");
        final String releaseDate = "Release Date: " + extras.getString("release_date");
        final String overview = "Overview: " + extras.getString("overview");

        trailerList = new ArrayList<String>();

        mMoviesHelper = new MoviesDbHelper(getActivity());

        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_trailers, R.id.trailer, trailerList);


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_trailers);

        listView.setAdapter(adapter);

        mBackdrop = (ImageView) rootView.findViewById(R.id.coverPoster_id);
        String url = BASE_URL + backdropPath;
        Log.e(LOG_TAG, "Detail poster url: " + url);

        Picasso.with(getActivity())
                .load(url)
                .into(new Target() {
                    @Override
                    public void onPrepareLoad(Drawable drawable) {

                    }

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    /* Save the bitmap or do something with it here */
                        coverImage = bitmap;
                        Log.e(LOG_TAG, " bitmap of image" + bitmap);
                        //Set it in the ImageView
                        mBackdrop.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable args0) {

                    }
                });



        Log.e(LOG_TAG, " bitmap of image" + coverImage);

        getTrailers();

        mTitle = (TextView) rootView.findViewById(R.id.title);
        mTitle.setText(title);

        mRatings = (TextView) rootView.findViewById(R.id.ratings);
        mRatings.setText(rating);

        mReleaseDate = (TextView) rootView.findViewById(R.id.release_date);
        mReleaseDate.setText(releaseDate);

        mOverview = (TextView) rootView.findViewById(R.id.overview);
        mOverview.setText(overview);

        mFavorite = (CheckBox) rootView.findViewById(R.id.favourites);

        final InteractingDatabase interact = new InteractingDatabase(getActivity());

        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    long movieId = interact.addMovie(id, title, posterPath, backdropPath, overview, releaseDate, rating);
                    final Context context = getActivity();


                    String url = BASE_URL + posterPath;

                    Picasso.with(context)
                            .load(url)
                            .into(new Target() {
                                @Override
                                public void onPrepareLoad(Drawable drawable) {

                                }

                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    /* Save the bitmap or do something with it here */
                                    posterImage = bitmap;
                                    mMoviesHelper.saveImage(context, id, posterImage, MoviesContract.COL_POSTER_PATH, posterPath);
                                    mMoviesHelper.saveImage(context, id, coverImage, MoviesContract.COL_BACKDROP_PATH, backdropPath);

                                }

                                @Override
                                public void onBitmapFailed(Drawable args0) {

                                }
                            });



                    CharSequence text = "Adding to Favorites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else
                {
                    Context context = getActivity();
                    CharSequence text = "Removing from Favorites";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        return rootView;
    }

    public void getTrailers()
    {
        String ApiKey = "c8ea7e0252da1993f1dec16ac38c4157";
        String API = "http://api.themoviedb.org";

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MoviesInterface moviesApi = restAdapter.create(MoviesInterface.class);

        moviesApi.getVideos(id, ApiKey, new Callback<VideoPOJO>() {

            public void success(VideoPOJO videoPOJO, Response response) {

                mVideoPOJO = videoPOJO;
                Log.e(LOG_TAG, "trailers get " + videoPOJO.getResults().size());
                for(int i = 0; i < videoPOJO.getResults().size(); i++)
                {
                    trailerList.add("Trailer " + (i+1));
                    Log.e(LOG_TAG, trailerList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
