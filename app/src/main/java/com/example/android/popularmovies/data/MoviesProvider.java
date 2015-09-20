package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by nitesh on 8/22/15.
 */
public class MoviesProvider extends ContentProvider{

    static final int MOVIES = 100;
    static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mMoviesHelper;
    private Bitmap bitmap;


    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return matcher;
    }

    private Cursor getMovieById(Uri uri, String[] projection, String selection, String sortOrder)
    {
        String id = MoviesContract.getIdFromUri(uri);
        return mMoviesHelper.getReadableDatabase().query(
                MoviesContract.TABLE_NAME,
                projection,
                selection,
                new String[] {id},
                null,
                null,
                sortOrder

        );
    }

    @Override
    public boolean onCreate() {
        mMoviesHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri))
        {
            case MOVIES: {

                retCursor = mMoviesHelper.getReadableDatabase().query(
                        MoviesContract.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                        );
                break;
            }
            case MOVIES_WITH_ID: {
                retCursor = getMovieById(uri, projection, selection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;

    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case MOVIES:
                return MoviesContract.CONTENT_TYPE;
            case MOVIES_WITH_ID:
                return MoviesContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match)
        {
            case MOVIES:
            {
                long _id = db.insert(MoviesContract.TABLE_NAME, null, values);

                if(_id > 0)
                {
                    retUri = MoviesContract.buildMoviesUri(_id);
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into : " + uri);
                }
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if(null == selection)
        {
            selection = "1";
        }
        switch (match) {

            case MOVIES: {
                rowsDeleted = db.delete(MoviesContract.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
            {
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
            }

        }
        if(rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match)
        {
            case MOVIES:
            {
                rowsUpdated = db.update(MoviesContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if(rowsUpdated != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values)
    {
        final SQLiteDatabase db = mMoviesHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case MOVIES:
            {
                db.beginTransaction();
                int returnCount = 0;
                try
                {
                    for(ContentValues value : values)
                    {
                        long _id = db.insert(MoviesContract.TABLE_NAME, null, value);
                        if(_id != -1)
                        {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }
                finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
            {
                return super.bulkInsert(uri, values);
            }
        }

    }

    public void callSaveImage(Context context, String id, Bitmap picture, String coloumn, String path)
    {
        mMoviesHelper.saveImage(context, id, picture, coloumn, path);
    }

    public Bitmap callGetImage(String id, String coloumn)
    {
        return mMoviesHelper.getImage(id, coloumn);
    }
}
