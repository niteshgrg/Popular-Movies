package com.example.android.popularmovies.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorites.db";

    final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.TABLE_NAME + " (" +

            // Unique keys will be auto-generated in either case.
            MoviesContract.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            // the ID of the location entry associated with this weather data
            MoviesContract.COL_BACKDROP_PATH + " TEXT NOT NULL, " +
            MoviesContract.COL_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
            MoviesContract.COL_ORIGINAL_TITLE + " TEXT NOT NULL, " +
            MoviesContract.COL_OVERVIEW + " TEXT NOT NULL," +

            MoviesContract.COL_POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.COL_RELEASE_DATE + " TEXT NOT NULL, " +

            MoviesContract.COL_TITLE + " TEXT NOT NULL, " +
            MoviesContract.COL_VOTE_AVERAGE + " TEXT NOT NULL;";



    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TABLE_NAME);
        onCreate(db);

    }
}
