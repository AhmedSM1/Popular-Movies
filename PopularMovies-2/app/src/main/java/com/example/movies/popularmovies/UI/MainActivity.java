package com.example.movies.popularmovies.UI;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movies.popularmovies.API.Client;
import com.example.movies.popularmovies.API.Services;
import com.example.movies.popularmovies.Adapters.MovieAdapter;
import com.example.movies.popularmovies.Database.MovieViewModel;
import com.example.movies.popularmovies.Model.Movie;
import com.example.movies.popularmovies.Model.MoviesReply;
import com.example.movies.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
    private List<Movie> movies;
    private MovieAdapter adapter;
    MutableLiveData<List<Movie>> movieLiveData = new MutableLiveData<>();
    public static final String LOG_TAG = MovieAdapter.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        if (getSortValue().equals(getString(R.string.sort_by_favorites))) {
            viewModel.getMovies(true, "").observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }

                }
            }); }
        else if (getSortValue().equals(getString(R.string.sort_popularity))){
            viewModel.getMovies(false, getString(R.string.sort_popularity) ).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            }); }else if (getSortValue().equals(getString(R.string.sort_top_rated))){
            viewModel.getMovies(false, getString(R.string.sort_top_rated)).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

        Toast.makeText(this, "Sorting by   " + getSortValue(),Toast.LENGTH_LONG).show();
        if (movies != null) {
            Log.d("Amazing", "The sort method is working");
        } else {
            Log.d("Shame", "The sort method DOES NOT WORK ");
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // Calling the Adapter object and setting it to the recycler view.
        adapter = new MovieAdapter(this, movies);
        if (movies != null) {
            Log.d("Great", "Adapter list doesnt = null");
        } else {
            Log.d("Error", "the adapter list is null");
        }
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Sorting by   " + getSortValue(),Toast.LENGTH_LONG).show();
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        if (getSortValue().equals(getString(R.string.sort_by_favorites))) {
            viewModel.getMovies(true, "").observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }

                }
            }); }else if (getSortValue().equals(getString(R.string.sort_top_rated))){
            viewModel.getMovies(false, getString(R.string.sort_top_rated) ).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            }); }else if (getSortValue().equals(getString(R.string.sort_popularity))){
            viewModel.getMovies(false, getString(R.string.sort_popularity)).observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> moviesModels) {
                    movies.clear();
                    if (moviesModels != null) {
                        movies.addAll(moviesModels);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        } }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    private String getSortValue() {

        SharedPreferences sharedPreferences ;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("MainActivity","the preference is "+sharedPreferences.getString(getString(R.string.sort_by_key), getString(R.string.sort_popularity)));
        return sharedPreferences.getString(getString(R.string.sort_by_key), getString(R.string.sort_popularity));
    }
}
