package com.example.android.popularmovies.API;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by nitesh on 7/13/15.
 */
public interface MoviesInterface {
    @GET("/3/discover/movie")
    public void getMovies(@Query("sort_by") String sort_Type, @Query("api_key") String keyVal, Callback<com.example.android.popularmovies.model.MoviePOJO> pojo);

    @GET("/3/movie/{id}/videos")
    public void getVideos(@Path("id") String id, @Query("api_key") String keyVal, Callback<com.example.android.popularmovies.model.VideoPOJO> pojo);

    @GET("/3/movie/{id}/reviews")
    public void getReviews(@Path("id") String id, @Query("api_key") String keyVal, Callback<com.example.android.popularmovies.model.ReviewPOJO> pojo);

}
