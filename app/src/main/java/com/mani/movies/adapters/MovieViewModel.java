package com.mani.movies.adapters;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.db.AppDb;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<MovieDetails>> movieDetails;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieDetails = AppDb.getInstance(this.getApplication()).movieDao().getStoredMovieDetails();
    }

    public LiveData<List<MovieDetails>> getMovieDetails() {
        return movieDetails;
    }
}
