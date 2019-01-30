package com.mani.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.adapters.ImageAdapter;
import com.mani.movies.adapters.MovieViewModel;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.db.AppDb;
import com.mani.movies.utils.ExtractMovieDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mani.movies.activity.MainActivity.MenuSelection.favorite;
import static com.mani.movies.activity.MainActivity.MenuSelection.popular;
import static com.mani.movies.activity.MainActivity.MenuSelection.rated;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTION = "selection";
    public static final String MOVIE = "movie";
    public static final String POPULAR = "popular";
    public static final String RATED = "rated";
    public static final String FAVORITE = "favorite";

    public enum MenuSelection {
        popular, rated, favorite
    }

    private static MenuSelection selection = MenuSelection.popular;
    Context context;
    String defaultSelectionString;
    GridView gridView;
    ImageAdapter apiImageAdapter;
    ImageAdapter favImageAdapter;
    ArrayList<MovieDetails> apiMovieDetails;
    ArrayList<MovieDetails> favMovieDetails;
    ProgressBar progressBar;
    TextView userInfoMessage;
    AppDb appDb;
    Toolbar toolbar;
    ArrayList<String> movieIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        appDb = AppDb.getInstance(context);
        defaultSelectionString = getString(R.string.most_popular);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        toolbar = findViewById(R.id.toolbar);
        userInfoMessage = findViewById(R.id.error_message);
        setSupportActionBar(toolbar);
        gridView = findViewById(R.id.movie_grid);
        updateFavMovieDetailsFromDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selection == MenuSelection.popular || selection == MenuSelection.rated) {
            if (movieIds == null || movieIds.isEmpty()) {
                (new CallApiBasedOnSelection()).execute(selection);
            } else {
                updateUI();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (selection == MenuSelection.popular || selection == MenuSelection.rated) {
            savedInstanceState.putParcelableArrayList(MOVIE, apiMovieDetails);

        }
        savedInstanceState.putString(SELECTION, selection.toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        switch (savedInstanceState.getString(SELECTION, POPULAR)) {
            case RATED:
                selection = MenuSelection.rated;
                break;
            case FAVORITE:
                selection = MenuSelection.favorite;
                break;
            default:
                selection = MenuSelection.popular;
        }
        if (selection == MenuSelection.popular || selection == MenuSelection.rated) {
            apiMovieDetails = savedInstanceState.getParcelableArrayList(MOVIE);
            setMovieIds(apiMovieDetails);
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
        } else if (id == R.id.highest_rated) {
            selection = rated;
            (new CallApiBasedOnSelection()).execute(selection);
        } else {
            selection = favorite;
            updateUIForFav();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitleBasedOnsElection() {
        if (selection == popular) {
            toolbar.setTitle(getString(R.string.most_popular));
        } else if (selection == rated) {
            toolbar.setTitle(getString(R.string.highest_rated));
        } else {
            toolbar.setTitle(getString(R.string.my_favorite));
        }
    }

    private void updateUI() {
        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        if (selection == MenuSelection.rated || selection == MenuSelection.popular) {
            updateTitleBasedOnsElection();
            if (apiMovieDetails == null || apiMovieDetails.isEmpty()) {
                userInfoMessage.setText(getString(R.string.error_loading_movie));
                userInfoMessage.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                userInfoMessage.setVisibility(View.GONE);
                apiImageAdapter = new ImageAdapter(apiMovieDetails, this);
                gridView.setAdapter(apiImageAdapter);
            }
        }
    }

    private void updateUIForFav() {
        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        if (selection == MenuSelection.favorite) {
            updateTitleBasedOnsElection();
            if (favMovieDetails == null || favMovieDetails.isEmpty()) {
                userInfoMessage.setText(getString(R.string.fav_movie_not_selected));
                userInfoMessage.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
            } else {
                userInfoMessage.setVisibility(View.GONE);
                favImageAdapter = new ImageAdapter(favMovieDetails, this);
                gridView.setAdapter(favImageAdapter);
            }
        }
    }

    public void updateFavMovieDetailsFromDb() {
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getFavMovieDetails().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetails> movieDetails) {
                favMovieDetails = (ArrayList) movieDetails;
                favImageAdapter = new ImageAdapter(movieDetails, context);
                updateUIForFav();
            }
        });
        movieViewModel.setMovieDetails(AppDb.getInstance(this.getApplication()).movieDao().getFavMovieDetails());
    }

    private void setMovieIds(List<MovieDetails> movieDetailsList) {
        if (movieDetailsList == null) {
            return;
        }
        movieIds = new ArrayList<>();
        for (MovieDetails movieDetails : movieDetailsList) {
            movieIds.add(movieDetails.getMovieId());
        }
    }

    private class CallApiBasedOnSelection extends AsyncTask<MainActivity.MenuSelection, Void, List<MovieDetails>> {
        @Override
        protected List<MovieDetails> doInBackground(MainActivity.MenuSelection... menuSelections) {
            apiMovieDetails = new ArrayList<>();
            List<MovieDetails> movieDetailsApiList = new ArrayList<>();
            switch (menuSelections[0]) {
                case popular:
                    movieDetailsApiList = ExtractMovieDetails.getPopularMovies();
                    break;
                case rated:
                    movieDetailsApiList = ExtractMovieDetails.getRatedMovies();
                    break;
                default:
                    break;
            }
            setMovieIds(movieDetailsApiList);
            //Merge and insert the details. Needs DB refactor if API returns more number of rows
            List<MovieDetails> movieDetailsListDb = appDb.movieDao().getMovieDetails(movieIds.toArray(new String[0]));
            appDb.movieDao().insertMovies(updateMovieList(movieDetailsApiList, movieDetailsListDb));
            return movieDetailsApiList;
        }

        private MovieDetails getMergedMovieDetails(MovieDetails movieDetailsDb, MovieDetails movieDetailsApi) {
            movieDetailsDb.setMoviePosterUrl(movieDetailsApi.getMoviePosterUrl());
            movieDetailsDb.setThumbnail(movieDetailsApi.getThumbnail());
            movieDetailsDb.setTitle(movieDetailsApi.getTitle());
            movieDetailsDb.setOverview(movieDetailsApi.getOverview());
            movieDetailsDb.setRating(movieDetailsApi.getRating());
            movieDetailsDb.setReleaseDate(movieDetailsApi.getReleaseDate());
            return movieDetailsDb;
        }

        private List<MovieDetails> updateMovieList(final List<MovieDetails> movieDetailsListApi, final List<MovieDetails> movieDetailsListDb) {
            List<MovieDetails> newMoviesList = new ArrayList<>();
            Map<String, MovieDetails> movieDetailsMap = new HashMap<>();
            for (MovieDetails movieDetails : movieDetailsListDb) {
                movieDetailsMap.put(movieDetails.getMovieId(), movieDetails);
            }
            for (MovieDetails movieDetailsApi : movieDetailsListApi) {
                if (movieDetailsMap.containsKey(movieDetailsApi.getMovieId())) {
                    newMoviesList.add(getMergedMovieDetails(
                            movieDetailsMap.get(movieDetailsApi.getMovieId()), movieDetailsApi));
                } else {
                    newMoviesList.add(movieDetailsApi);
                }
            }
            return newMoviesList;
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movieDetails) {
            super.onPostExecute(movieDetails);
            apiMovieDetails = (ArrayList) movieDetails;
            updateUI();
        }
    }
}
