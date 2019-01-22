package com.mani.movies.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.mani.movies.R;
import com.mani.movies.utils.ExtractMovieDetails;
import com.mani.movies.utils.ImageAdapter;
import com.mani.movies.utils.MovieDetails;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private enum MenuSelection {
        popular, rated
    }

    private List<MovieDetails> movieDetailsList = new ArrayList<>();
    private static MenuSelection selection = MenuSelection.popular;
    private String defaultSelectionString = "";

    GridView gridView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultSelectionString = getString(R.string.most_popular);
        updateSelectionFromSharedPref();
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = findViewById(R.id.movie_grid);
        (new CallApiBasedOnSelection()).execute(selection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        if (!movieDetailsList.isEmpty()) {
            updateUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem popular = menu.findItem(R.id.most_popular);
        MenuItem rated = menu.findItem(R.id.most_rated);
        if (selection == MenuSelection.rated) {
            rated.setEnabled(false);
            popular.setEnabled(true);
        } else {
            popular.setEnabled(false);
            rated.setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        progressBar.setVisibility(View.VISIBLE);
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            selection = MenuSelection.popular;
            updateSharedPrefBasedOnSelection(getString(R.string.most_popular));
        } else {
            selection = MenuSelection.rated;
            updateSharedPrefBasedOnSelection(getString(R.string.most_rated));
        }

        (new CallApiBasedOnSelection()).execute(selection);
        return super.onOptionsItemSelected(item);
    }

    private void updateSelectionFromSharedPref() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String selectedValue = sharedPrefs.getString(
                getString(R.string.key_selection),
                defaultSelectionString
        );
        if (selectedValue != null && selectedValue.equalsIgnoreCase(getString(R.string.most_rated))) {
            selection = MenuSelection.rated;
        } else {
            selection = MenuSelection.popular;
        }
    }

    private void updateSharedPrefBasedOnSelection(String newValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(getString(R.string.key_selection), newValue);
        editor.apply();
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        ImageAdapter imageAdapter = new ImageAdapter(movieDetailsList, this);
        gridView.setAdapter(imageAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private class CallApiBasedOnSelection extends AsyncTask<MenuSelection, Void, List<MovieDetails>> {
        @Override
        protected List<MovieDetails> doInBackground(MenuSelection... menuSelections) {
            if (menuSelections[0] == MenuSelection.popular) {
                return ExtractMovieDetails.getPopularMovies();
            }
            return ExtractMovieDetails.getRatedMovies();
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movieDetails) {
            super.onPostExecute(movieDetails);
            movieDetailsList = movieDetails;
            updateUI();
        }
    }
}
