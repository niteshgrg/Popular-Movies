package com.example.android.popularmovies.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.android.popularmovies.DetailActivityFragment;

import java.io.File;
import java.io.FileOutputStream;

public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    static final String DATABASE_NAME = "favorites.db";

    final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesContract.TABLE_NAME + " (" +

            // Unique keys will be auto-generated in either case.
            MoviesContract.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

            // the ID of the location entry associated with this weather data
            MoviesContract.COL_BACKDROP_PATH + " TEXT NOT NULL, " +
            MoviesContract.COL_MOVIE_ID + " INTEGER NOT NULL UNIQUE, " +
            MoviesContract.COL_ORIGINAL_TITLE + " TEXT NULL, " +
            MoviesContract.COL_OVERVIEW + " TEXT NOT NULL," +

            MoviesContract.COL_POSTER_PATH + " TEXT NOT NULL, " +
            MoviesContract.COL_RELEASE_DATE + " TEXT NOT NULL, " +

            MoviesContract.COL_TITLE + " TEXT NOT NULL, " +
            MoviesContract.COL_VOTE_AVERAGE + " TEXT NOT NULL);";



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

    public void saveImage(Context context, String id, Bitmap picture, String coloumn, String path)
    {
        path = path.replace("/", "");
        Log.d(LOG_TAG, path);
        String imagePath = "";
        File internalStorage = context.getDir("MovieImages", Context.MODE_PRIVATE);
        File imageFilePath = new File(internalStorage, path);
        imagePath = imageFilePath.toString();
        Log.d(LOG_TAG, imagePath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFilePath);
            picture.compress(Bitmap.CompressFormat.JPEG, 85 , fos);
            fos.close();
        }
        catch (Exception ex) {
            Log.i("DATABASE", "Problem updating picture", ex);
            imagePath = "";
        }

        SQLiteDatabase db = getWritableDatabase();

        ContentValues newPictureValue = new ContentValues();
        newPictureValue.put(coloumn,
                imagePath);

        db.update(MoviesContract.TABLE_NAME,
                newPictureValue,
                MoviesContract.COL_MOVIE_ID + "=?",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public Bitmap getImage(String id, String coloumn) {
        String picturePath = getPicturePath(id, coloumn);
        if (picturePath == null || picturePath.length() == 0)
            return (null);

        Log.d(LOG_TAG, picturePath);


        Bitmap picture = BitmapFactory.decodeFile(picturePath);

        return (picture);
    }

    private String getPicturePath(String id, String coloumn) {
        // Gets the database in the current database helper in read-only mode
        SQLiteDatabase db = getReadableDatabase();

        // After the query, the cursor points to the first database row
        // returned by the request
        Cursor reportCursor = db.query(MoviesContract.TABLE_NAME,
                new String[]{coloumn},
                MoviesContract.COL_MOVIE_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        reportCursor.moveToFirst();

        db.close();

        // Get the path of the picture from the database row pointed by
        // the cursor using the getColumnIndex method of the cursor.
        String picturePath = reportCursor.getString(reportCursor.
                getColumnIndex(coloumn));


        return (picturePath);
    }

}
