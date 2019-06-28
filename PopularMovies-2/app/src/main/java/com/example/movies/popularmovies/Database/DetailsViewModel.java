package com.example.movies.popularmovies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.movies.popularmovies.Model.Movie;

public class DetailsViewModel extends ViewModel {
    private LiveData<Movie> movieLiveData;
    public DetailsViewModel(FavoriteDatabase favoriteDatabase, int movieID) {
        this.movieLiveData = favoriteDatabase.databaseDAO().getSingleMovie(movieID);
    }

    public LiveData<Movie> getMovieLiveData() {
        return movieLiveData;
    }
}
