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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


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
    private CheckBox mFavorite;
    private Bitmap coverImage;
    private Bitmap posterImage;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        final String id = extras.getString("id");
        final String backdropPath = extras.getString("backdrop_path");
        final String posterPath =extras.getString("poster_path");
        final String title = extras.getString("title");
        final String rating = "Rating: " + extras.getString("ratings");
        final String releaseDate = "Release Date: " + extras.getString("release_date");
        final String overview = "Overview: " + extras.getString("overview");


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
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
                    Context context = getActivity();

                    MoviesProvider provider = new MoviesProvider();

                    if(coverImage != null && id != null && backdropPath != null) {
                        Log.e(LOG_TAG, "id =" + id + "backDropPath = " + backdropPath + "coverimage = " + coverImage);
                        provider.callSaveImage(id, coverImage, MoviesContract.COL_BACKDROP_PATH, backdropPath);

                    }
                    else
                    {
                        Log.e(LOG_TAG, "coverImage empty");
                    }
                    String url = BASE_URL + posterPath;

                    Picasso.with(getActivity())
                            .load(url)
                            .into(new Target() {
                                @Override
                                public void onPrepareLoad(Drawable drawable) {

                                }

                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                    /* Save the bitmap or do something with it here */
                                    posterImage = bitmap.copy(bitmap.getConfig(), true);

                                }

                                @Override
                                public void onBitmapFailed(Drawable args0) {

                                }
                            });

                    provider.callSaveImage(id, posterImage, MoviesContract.COL_POSTER_PATH, posterPath);

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

}
