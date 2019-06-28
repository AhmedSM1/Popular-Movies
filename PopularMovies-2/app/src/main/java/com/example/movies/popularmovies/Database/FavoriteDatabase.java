package com.example.movies.popularmovies.Database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.movies.popularmovies.Model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "favourite_movies";
    private static FavoriteDatabase database;


    public abstract DatabaseDAO databaseDAO();
    public static FavoriteDatabase getDatabase (Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, FavoriteDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
        }
        return database;
    }
}











