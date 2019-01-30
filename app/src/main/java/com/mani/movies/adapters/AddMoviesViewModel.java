package com.mani.movies.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.db.AppDb;

import java.util.List;

public class AddMoviesViewModel extends ViewModel {
    private LiveData<List<MovieDetails>> movieDetailsList;

    AddMoviesViewModel(AppDb appDb, List<String> ids) {
        movieDetailsList = appDb.movieDao().getMovieDetails(ids);
    }

    public LiveData<List<MovieDetails>> getMovieDetailsList() {
        return movieDetailsList;
    }
}
