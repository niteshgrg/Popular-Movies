package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.android.popularmovies.data.MoviesContract;

/**
 * Created by nitesh on 9/18/15.
 */
public class InteractingDatabase {

    private Context mContext;

    public InteractingDatabase(Context context)
    {
        mContext = context;
    }

    public long addMovie(String id, String title, String poster_path, String backdrop_path, String overview, String release_date, String vote_average)
    {
        long movieId;

        Cursor movieCursor = mContext.getContentResolver().query(
                MoviesContract.CONTENT_URI,
                new String[]{MoviesContract.KEY_ID},
                MoviesContract.COL_MOVIE_ID + " = ?",
                new String[]{id},
                null
        );
        if(movieCursor.moveToFirst())
        {
            int movieIdIndex = movieCursor.getColumnIndex(MoviesContract.KEY_ID);
            movieId = movieCursor.getLong(movieIdIndex);
        }
        else
        {
            ContentValues values = new ContentValues();

            values.put(MoviesContract.COL_MOVIE_ID, id);
            values.put(MoviesContract.COL_TITLE, title);
            values.put(MoviesContract.COL_POSTER_PATH, poster_path);
            values.put(MoviesContract.COL_BACKDROP_PATH, backdrop_path);
            values.put(MoviesContract.COL_OVERVIEW, overview);
            values.put(MoviesContract.COL_RELEASE_DATE, release_date);
            values.put(MoviesContract.COL_VOTE_AVERAGE, vote_average);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MoviesContract.CONTENT_URI,
                    values
            );

            movieCursor.close();

            movieId = ContentUris.parseId(insertedUri);

        }


        return movieId;
    }
}
