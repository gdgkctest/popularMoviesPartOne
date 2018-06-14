package com.chuckr_udacity.popularmoviespartone.rest;

import com.chuckr_udacity.popularmoviespartone.models.Movie;
import com.chuckr_udacity.popularmoviespartone.models.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {
        //Defines the Parameters for MovieDB api

        //Get Popular Movies
        @GET("popular")
        Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);
        //Get Top Rated Movies
        @GET("top_rated")
        Call<MovieList> getTopRatedMovies(@Query("api_key") String apiKey);
        //Get Movie Details
        @GET("{id}")
        Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
