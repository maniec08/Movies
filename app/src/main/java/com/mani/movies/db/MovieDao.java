package com.mani.movies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mani.movies.datastruct.MovieDetails;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM MovieDetails WHERE movieId = :id")
    MovieDetails getMovieDetails(String id);

    @Query("SELECT * FROM MovieDetails")
    LiveData<List<MovieDetails>> getStoredMovieDetails();

    @Insert
    void insertTask(MovieDetails movieDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(MovieDetails movieDetails);

    @Delete
    void deleteTask(MovieDetails MovieDetails);
}
