package com.mani.movies.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mani.movies.R;
import com.mani.movies.adapters.ImageAdapter;
import com.mani.movies.adapters.MovieViewModel;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.utils.ExtractMovieDetails;

import java.util.ArrayList;
import java.util.List;

import static com.mani.movies.activity.MainActivity.MenuSelection.favorite;
import static com.mani.movies.activity.MainActivity.MenuSelection.popular;
import static com.mani.movies.activity.MainActivity.MenuSelection.rated;

public class MainActivity extends AppCompatActivity {

    public enum MenuSelection {
        popular, rated, favorite
    }

    private static MenuSelection selection = popular;
    private String defaultSelectionString = "";
    ImageView errorImageView;
    GridView gridView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultSelectionString = getString(R.string.most_popular);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        errorImageView = findViewById(R.id.error_image);
        updateSelectionFromSharedPref();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = findViewById(R.id.movie_grid);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem popular = menu.findItem(R.id.most_popular);
        MenuItem rated = menu.findItem(R.id.highest_rated);
        MenuItem favorite = menu.findItem(R.id.favorite);
        switch (selection) {
            case rated:
                rated.setEnabled(false);
                popular.setEnabled(true);
                favorite.setEnabled(true);
                break;
            case popular:
                popular.setEnabled(false);
                rated.setEnabled(true);
                favorite.setEnabled(true);
                break;
            default:
                popular.setEnabled(true);
                rated.setEnabled(true);
                favorite.setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progressBar.setVisibility(View.VISIBLE);
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            selection = popular;
            (new CallApiBasedOnSelection()).execute(selection);
            updateSharedPrefBasedOnSelection(getString(R.string.most_popular));
        } else if (id == R.id.highest_rated) {
            selection = rated;
            (new CallApiBasedOnSelection()).execute(selection);
            updateSharedPrefBasedOnSelection(getString(R.string.highest_rated));
        } else {
            selection = favorite;
            updateSharedPrefBasedOnSelection(getString(R.string.my_favorite));
            queryDb();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSelectionFromSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String selectedValue = sharedPrefs.getString(
                getString(R.string.key_selection),
                defaultSelectionString
        );
        if (selectedValue != null) {
            if (selectedValue.equalsIgnoreCase(getString(R.string.highest_rated))) {
                selection = rated;
                (new CallApiBasedOnSelection()).execute(selection);
            } else if (selectedValue.equalsIgnoreCase(getString(R.string.most_popular))) {
                selection = popular;
                (new CallApiBasedOnSelection()).execute(selection);
            } else {
                selection = favorite;
                queryDb();
            }
        }
    }

    private void updateSharedPrefBasedOnSelection(String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.key_selection), newValue);
        editor.apply();
    }

    private void updateUI(List<MovieDetails> movieDetails) {
        progressBar.setVisibility(View.GONE);
        if (movieDetails == null || movieDetails.isEmpty()) {
            errorImageView.setVisibility(View.VISIBLE);
        } else {
            ImageAdapter imageAdapter = new ImageAdapter(movieDetails, this);
            gridView.setAdapter(imageAdapter);
        }
    }

    public void queryDb() {
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getMovieDetails().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetails> movieDetails) {
                updateUI(movieDetails);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private class CallApiBasedOnSelection extends AsyncTask<MenuSelection, Void, List<MovieDetails>> {
        @Override
        protected List<MovieDetails> doInBackground(MenuSelection... menuSelections) {

            switch (menuSelections[0]) {
                case popular:
                    return ExtractMovieDetails.getPopularMovies();
                case rated:
                    return ExtractMovieDetails.getRatedMovies();
                default:
                    return new ArrayList<>();
            }
        }


        @Override
        protected void onPostExecute(List<MovieDetails> movieDetails) {
            super.onPostExecute(movieDetails);
            updateUI(movieDetails);
        }
    }
}
