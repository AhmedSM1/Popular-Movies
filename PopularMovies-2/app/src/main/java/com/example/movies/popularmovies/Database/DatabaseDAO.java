package com.example.movies.popularmovies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.example.movies.popularmovies.Model.Movie;

import java.util.List;

@Dao
public interface DatabaseDAO {
  @Query("SELECT * FROM favourite_movies")
   List<Movie> getAllMovies();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertMovie(Movie movie);
  @Delete
  void deleteMovie(Movie movie);

  @Query("SELECT * FROM favourite_movies WHERE id = :id")
  LiveData<Movie> getSingleMovie(int id);
}
