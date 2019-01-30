package com.mani.movies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mani.movies.datastruct.MovieDetails;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM MovieDetails WHERE movieId IN(:ids)")
    LiveData<List<MovieDetails>> getMovieDetails(List<String> ids);

    @Query("SELECT * FROM MovieDetails WHERE movieId IN(:ids)")
    List<MovieDetails> getMovieDetails(String[] ids);

    @Query("SELECT * FROM MovieDetails WHERE isFavorite = 1")
    LiveData<List<MovieDetails>> getFavMovieDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<MovieDetails> movieDetails);

    @Query("UPDATE MovieDetails set reviewDetailsList=:reviewDetails WHERE movieId =:id")
    void updateReviewDetails(String reviewDetails, String id);

    @Query("UPDATE MovieDetails set trailerDetailsList=:trailerDetails WHERE movieId =:id")
    void updateTrailerDetails(String trailerDetails, String id);

    @Query("UPDATE MovieDetails set duration=:duration WHERE movieId =:id")
    void updateMovieDuration(String duration, String id);

    @Query("UPDATE MovieDetails set isFavorite=:isFav WHERE movieId =:id")
    void updateFavoriteSelection(boolean isFav, String id);
}
