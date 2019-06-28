package com.example.movies.popularmovies.Database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private FavoriteDatabase database;
    private int movieID;

    public DetailsViewModelFactory(FavoriteDatabase database, int movieID) {
        this.database = database;
        this.movieID = movieID;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(database,movieID);
    }
}
