package com.example.movies.popularmovies.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.movies.popularmovies.API.Client;
import com.example.movies.popularmovies.API.Services;
import com.example.movies.popularmovies.Model.Movie;
import com.example.movies.popularmovies.Model.MoviesReply;
import com.example.movies.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieViewModel extends AndroidViewModel{
    private static final String API_KEY = "";
    FavoriteDatabase database;

    MutableLiveData<List <Movie>> movieLiveData = new MutableLiveData<>();
    public MovieViewModel(@NonNull Application application) {
        super(application);
        database = FavoriteDatabase.getDatabase(this.getApplication());
        Log.d("Loading","Getting the data from the database");

    }
    public LiveData<List<Movie>> getMovies(boolean fav, String sort) {
        if (fav ) {
            new FavoriteAsyncTask().execute();
            return movieLiveData;


        } else if (fav == false) {

            Client client = new Client();
            Services services = client.getClient().create(Services.class);
            //assign a call instence so we can use it to get popular movie or top rated

            Call<MoviesReply> moviesCall;
            if (sort.equals(getApplication().getResources().getString(R.string.sort_popularity))) {
                Log.d("Calling", "Most Popular method is getting called");
                moviesCall = services.getPopularMovies(API_KEY);

            } else if (sort.equals(getApplication().getResources().getString(R.string.sort_top_rated))) {
                moviesCall = services.getTopRatedMovies(API_KEY);
            } else moviesCall = services.getPopularMovies(API_KEY);

            moviesCall.enqueue(new Callback<MoviesReply>() {
                @Override
                public void onResponse(Call<MoviesReply> moviesCall, Response<MoviesReply> response) {
                    MoviesReply moviesReply = response.body();
                    movieLiveData.postValue(moviesReply.getMovies());
                    Log.d("onRespone", "getting the response "+response.body().getMovies().get(0).getId());
                }

                @Override
                public void onFailure(Call<MoviesReply> moviesCall, Throwable t) {
                    Log.d("error", "Couldnt get the movies");

                }
            });


            return movieLiveData;


        }
        return movieLiveData;
    }

    public class FavoriteAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            movieLiveData.postValue(database.databaseDAO().getAllMovies());
            return null;
        }
    }
}