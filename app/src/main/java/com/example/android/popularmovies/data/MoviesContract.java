package com.example.android.popularmovies.data;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "favorites";

    public static final String KEY_ID = "_id";

    public static final String TABLE_NAME = "favorites";

    public static final String COL_BACKDROP_PATH = "backdrop_path";
    public static final String COL_MOVIE_ID = "id";
    public static final String COL_ORIGINAL_TITLE = "original_title";
    public static final String COL_OVERVIEW = "overview";
    public static final String COL_POSTER_PATH = "poster_path";
    public static final String COL_RELEASE_DATE = "release_date";
    public static final String COL_TITLE = "title";
    public static final String COL_VOTE_AVERAGE = "vote_average";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

    public static Uri buildMoviesUri(long id)
    {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }

    public static String getIdFromUri(Uri uri)
    {
        return uri.getPathSegments().get(1);
    }


}
