package com.chuckr_udacity.popularmoviespartone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;


import com.chuckr_udacity.popularmoviespartone.adapter.MovieAdapter;
import com.chuckr_udacity.popularmoviespartone.models.Movie;
import com.chuckr_udacity.popularmoviespartone.models.MovieList;
import com.chuckr_udacity.popularmoviespartone.rest.MovieApi;
import com.chuckr_udacity.popularmoviespartone.rest.MovieInterface;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Uses Listener for Sort Options
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {



    //used in error logs
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        //Create Sort Options Drop Down
        Spinner spinner =  findViewById(R.id.mySpinner);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

        final RecyclerView recyclerView = findViewById(R.id.movieDbList);
        //recyclerView.setSaveEnabled(true);
        //Use Grid Layout manager to display Posters in Grids
        final StaggeredGridLayoutManager movieGridLayoutManger = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        //No need to add or remove movies while running
        recyclerView.setHasFixedSize(true);
        //Defines the Parameters for the API call to movieDB
        MovieInterface movieService = MovieApi.getClient().create(MovieInterface.class);



        //Get Names of Sort Options and store to an array
        String[] choices = getResources().getStringArray(R.array.sort_choices);
        //Store API Key
        String apiKey = getString(R.string.API_key);
        //Reviewer: Please Enter API Key in secrets.xml for moviedb
        if (apiKey.equals(""))
            Toast.makeText(this, "Please Enter API Key in Secrets.xml", Toast.LENGTH_SHORT).show();
        //Only Run if API key is available
        else {
            //Choose a retrofit call based on sort option
            Call<MovieList> call;
            if ("Popular Movies".equals(choices[position]))
                call = movieService.getPopularMovies(apiKey);
            else if ("Top Rated Movies".equals(choices[position]))
                call = movieService.getTopRatedMovies(apiKey);
            else
                call = movieService.getTopRatedMovies(apiKey);

            //Send the network call using Retrofit to background task
            call.enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                    //If Successful Network Call to API bind the Recycleview to MovieAdaptor
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(movies, R.layout.movie_list_item, getApplicationContext()));
                    recyclerView.setLayoutManager(movieGridLayoutManger);
                }

                @Override
                public void onFailure(Call<MovieList> call, Throwable t) {
                    //If not log the Error
                    Log.e(TAG, t.toString());
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Required by Listener
    }


}
