package com.mani.movies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.adapters.AddMoviesViewModel;
import com.mani.movies.adapters.AddMoviesViewModelFactory;
import com.mani.movies.adapters.ReviewRecyclerAdapter;
import com.mani.movies.adapters.TrailerRecyclerAdapter;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;
import com.mani.movies.db.AppDb;
import com.mani.movies.db.AppDbExecutors;
import com.mani.movies.utils.DateUtils;
import com.mani.movies.utils.ExtractMovieDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_ID_KEY = "movie_id_for_detail";
    public MovieDetails movieDetails;
    private String movieId;

    //TODO: Fix the gradle error with Androidx to use @Bindview
    // @BindView(R.id.thumbnail_iv)
    ImageView thumbnail;

    // @BindView(R.id.rating_tv)
    TextView rating;
    // @BindView(R.id.released_on_tv)
    TextView released;
    // @BindView(R.id.preview_tv)
    TextView overview;
    TextView duration;
    Button favButton;
    AppDb movieDb;
    RecyclerView reviewRecyclerView;
    RecyclerView trailerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieDb = AppDb.getInstance(this);
        setContentView(R.layout.movie_details);
        Intent intent = getIntent();
        movieDetails = intent.getParcelableExtra(getString(R.string.key_movie_details));
        movieId =intent.getStringExtra("movie_id_for_detail");
        thumbnail = findViewById(R.id.thumbnail_iv);
        rating = findViewById(R.id.rating_tv);
        released = findViewById(R.id.released_on_tv);
        overview = findViewById(R.id.overview_tv);
        duration = findViewById(R.id.duration_tv);
        favButton = findViewById(R.id.favorite_button);
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        favButton.setOnClickListener(favoriteButtonClickListener());
        updateMovieDetailsFromDb();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("movie_id_for_detail", movieId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieId = savedInstanceState.getString(MOVIE_ID_KEY);
    }


    private View.OnClickListener favoriteButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    String buttonText = favButton.getText().toString();
                    if (buttonText.equalsIgnoreCase(getString(R.string.mark_as_favorite_button))) {
                        favButton.setText(R.string.remove_favorite_button);
                        updateFavSelection(true);
                    } else {
                        favButton.setText(R.string.mark_as_favorite_button);
                        updateFavSelection(false);
                    }
                }
            }
        };
    }

    public void updateMovieDetailsFromDb() {
        //Re-using the existing View model
        List<String> movieIds = new ArrayList<>();
        movieIds.add(movieId);
        AddMoviesViewModelFactory factory = new AddMoviesViewModelFactory(movieDb, movieIds);
        final AddMoviesViewModel viewModel = ViewModelProviders.of(this, factory)
                .get(AddMoviesViewModel.class);
        viewModel.getMovieDetailsList().observe(this, new Observer<List<MovieDetails>>() {
            @Override
            public void onChanged(@Nullable List<MovieDetails> movieDetails) {
                updateUi(movieDetails);
            }
        });
    }

    private void updateUi(List<MovieDetails> movieDetailsList) {
        if(movieDetailsList == null || movieDetailsList.isEmpty()) {
            return;
        }
        updateUi(movieDetailsList.get(0));
    }

    private void updateUi(MovieDetails movieDetails) {
        this.movieDetails = movieDetails;
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movieDetails.getTitle());
        Picasso.with(this)
                .load(movieDetails.getThumbnail())
                .error(R.drawable.image_uavailable)
                .into(thumbnail);
        if (!(movieDetails.getReleaseDate() == null || movieDetails.getReleaseDate().isEmpty())) {
            released.setText(movieDetails.getReleaseDate());
        }
        if (!(movieDetails.getDuration() == null || movieDetails.getDuration().isEmpty())) {
            duration.setText(String.format(getString(R.string.duration_mins), movieDetails.getDuration()));
        }
        if (!(movieDetails.getRating() == null || movieDetails.getRating().isEmpty())) {
            String ratingText = movieDetails.getRating() + "/10";
            rating.setText(ratingText);
        }
        setUpFavoriteButton(movieDetails.isFavorite());

        if (!(movieDetails.getOverview() == null || movieDetails.getOverview().isEmpty())) {
            overview.setText(movieDetails.getOverview());
        }
        updateTrailerRecyclerView(movieDetails.getTrailerDetailsList());
        updateReviewRecyclerView(movieDetails.getReviewDetailsList());
    }

    private void setUpFavoriteButton(boolean isFav) {
        if (isFav) {
            favButton.setText(R.string.remove_favorite_button);
        } else {
            favButton.setText(R.string.mark_as_favorite_button);
        }
    }

    private void updateTrailerRecyclerView(String str) {
        if(str == null || str.isEmpty()){
            return;
        }
        List<TrailerDetails> trailerDetailsList = new ArrayList<>();
        try {
            trailerDetailsList = ExtractMovieDetails.parseVideoJson(new JSONArray(str), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (trailerDetailsList == null || trailerDetailsList.isEmpty()) {
            trailerDetailsList = new ArrayList<>();
            TrailerDetails trailerDetails = new TrailerDetails(getString(R.string.trailer_unavailble), "");
            trailerDetailsList.add(trailerDetails);
        }
        trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailerRecyclerView.setAdapter(new TrailerRecyclerAdapter(this, trailerDetailsList));
    }

    private void updateReviewRecyclerView(String str) {
        if(str == null || str.isEmpty()){
            return;
        }
        List<ReviewDetails> reviewDetailsList = new ArrayList<>();
        try {
            reviewDetailsList = ExtractMovieDetails.parseReviewJson(new JSONArray(str));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (reviewDetailsList == null || reviewDetailsList.isEmpty()) {
            reviewDetailsList = new ArrayList<>();
            ReviewDetails reviewDetails = new ReviewDetails("",
                    getString(R.string.review_unavailable));
            reviewDetailsList.add(reviewDetails);
        }
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setAdapter(new ReviewRecyclerAdapter(this, reviewDetailsList));
    }
    public void updateFavSelection(final boolean isFav) {
        AppDbExecutors.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                movieDb.movieDao().updateFavoriteSelection(isFav, movieId);
            }
        });
    }
}