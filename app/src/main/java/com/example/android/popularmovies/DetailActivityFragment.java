package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.API.MoviesInterface;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.data.MoviesDbHelper;
import com.example.android.popularmovies.model.ReviewPOJO;
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

    static final String DETAIL_URI = "URI";

    private String BASE_URL = "http://image.tmdb.org/t/p/w500/";
    private MoviesDbHelper mMoviesHelper;
    private ShareActionProvider mShareActionProvider;

    private ImageView mBackdrop;
    private TextView mTitle;
    private TextView mRatings;
    private TextView mReleaseDate;
    private TextView mOverview;
    private CheckBox mFavorite;
    private Bitmap coverImage;
    private Bitmap posterImage;
    ListView listViewTrailers;
    ListView listViewReviews;
    ArrayList<String> trailerList;
    ArrayList<String> reviewList;
    ArrayAdapter<String> adapterTrailers;
    ArrayAdapter<String> adapterReviews;

    VideoPOJO mVideoPOJO;
    ReviewPOJO mReviewPOJO;
    private String id;

    public DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Bundle extras;
        if (getActivity().getIntent().getExtras() != null) {
            Intent intent = getActivity().getIntent();
            extras = intent.getExtras();

        } else {
            extras = getArguments();
            //Log.e(LOG_TAG, "extras detail: " + extras.getString("id"));
        }

        final String backdropPath, posterPath, title, rating, releaseDate, overview;

        if (extras != null) {

            id = extras.getString("id");
            backdropPath = extras.getString("backdrop_path");
            posterPath = extras.getString("poster_path");
            title = extras.getString("title");
            rating = extras.getString("ratings") + "/10";
            releaseDate = extras.getString("release_date");
            overview = extras.getString("overview");


            trailerList = new ArrayList<String>();
            reviewList = new ArrayList<String>();

            mMoviesHelper = new MoviesDbHelper(getActivity());

            adapterTrailers = new ArrayAdapter<String>(getActivity(),
                    R.layout.list_item_trailers, R.id.trailer, trailerList);

            adapterReviews = new ArrayAdapter<String>(getActivity(), R.layout.list_item_reviews, R.id.list_item_reviews, reviewList);

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

            //Log.e(LOG_TAG, " bitmap of image" + coverImage);

            listViewTrailers = (ListView) rootView.findViewById(R.id.list_view_trailers);

            listViewTrailers.setAdapter(adapterTrailers);

            getTrailers();


            listViewReviews = (ListView) rootView.findViewById(R.id.list_view_reviews);

            listViewReviews.setAdapter(adapterReviews);

            getReviews();


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
                    if (isChecked) {

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
                    } else {
                        Context context = getActivity();
                        CharSequence text = "Removing from Favorites";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });


            listViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                    // Do something in response to the click
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + mVideoPOJO.getResults().get(position).getKey())));
                }
            });
        }

            return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + mVideoPOJO.getResults().get(0).getKey());
        return shareIntent;
    }


    public void getTrailers() {
        String ApiKey = "c8ea7e0252da1993f1dec16ac38c4157";
        String API = "http://api.themoviedb.org";

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MoviesInterface moviesApi = restAdapter.create(MoviesInterface.class);

        moviesApi.getVideos(id, ApiKey, new Callback<VideoPOJO>() {

            public void success(VideoPOJO videoPOJO, Response response) {

                mVideoPOJO = videoPOJO;
                int numTrailers = videoPOJO.getResults().size();
                for (int i = 0; i < numTrailers; i++) {
                    trailerList.add("Trailer " + (i + 1));
                }
                ViewGroup.LayoutParams params = listViewTrailers.getLayoutParams();
                View listItem = View.inflate(getActivity(), R.layout.list_item_trailers, null);
                listItem.measure(0, 0);
                params.height = (numTrailers * listItem.getMeasuredHeight()) + (listViewTrailers.getDividerHeight() * numTrailers);
                listViewTrailers.setLayoutParams(params);
                //Log.e(LOG_TAG, "height of trailer listview " + params.height);
                adapterTrailers.notifyDataSetChanged();

                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getReviews() {
        String ApiKey = "c8ea7e0252da1993f1dec16ac38c4157";
        String API = "http://api.themoviedb.org";

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(API).setLogLevel(RestAdapter.LogLevel.FULL).build();
        MoviesInterface moviesApi = restAdapter.create(MoviesInterface.class);

        moviesApi.getReviews(id, ApiKey, new Callback<ReviewPOJO>() {

            public void success(ReviewPOJO reviewPOJO, Response response) {

                mReviewPOJO = reviewPOJO;
                Log.e(LOG_TAG, "Reviews get " + reviewPOJO.getResults().size());
                int numReviews = reviewPOJO.getResults().size();
                for (int i = 0; i < numReviews; i++) {
                    reviewList.add(reviewPOJO.getResults().get(i).getContent());
                }

                adapterReviews.notifyDataSetChanged();


                ListAdapter listAdapter = listViewReviews.getAdapter();

                int numberOfItems = listAdapter.getCount();

                // Get total height of all items.
                int totalItemsHeight = 0;
                for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                    View listItem = listAdapter.getView(itemPos, null, listViewReviews);
                    listItem.measure(View.MeasureSpec.makeMeasureSpec(listViewReviews.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

                    Log.e(LOG_TAG, "item height " + listItem.getMeasuredHeight());
                    totalItemsHeight += listItem.getMeasuredHeight();
                }

                // Get total height of all item dividers.
                int totalDividersHeight = listViewReviews.getDividerHeight() *
                        (numberOfItems - 1);

                // Set list height.
                ViewGroup.LayoutParams params = listViewReviews.getLayoutParams();
                params.height = totalItemsHeight + totalDividersHeight;
                Log.e(LOG_TAG, "height of review listview " + params.height);
                listViewReviews.setLayoutParams(params);
                listViewReviews.requestLayout();
            }

            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}