package com.mani.movies.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.adapters.ReviewRecyclerAdapter;
import com.mani.movies.adapters.TrailerRecyclerAdapter;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.db.AppDb;
import com.mani.movies.db.AppDbExecutors;
import com.mani.movies.utils.DateUtils;
import com.mani.movies.utils.DetailsApiRequest;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {

    public static MovieDetails movieDetails;
    public MovieDetails movieDetailsDb;
    private String movieId;
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
        setContentView(R.layout.movie_details);
        Intent intent = getIntent();
        movieDetails = intent.getParcelableExtra(getString(R.string.key_movie_details));
        movieId = movieDetails.getMovieId();
        thumbnail = findViewById(R.id.thumbnail_iv);
        rating = findViewById(R.id.rating_tv);
        released = findViewById(R.id.released_on_tv);
        overview = findViewById(R.id.overview_tv);
        duration = findViewById(R.id.duration_tv);
        favButton = findViewById(R.id.favorite_button);
        reviewRecyclerView = findViewById(R.id.reviews_recycler_view);
        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        movieDb = AppDb.getInstance(getApplicationContext());
        favButton.setOnClickListener(favoriteButtonClickListener());
        updateUi();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(getString(R.string.key_movie_details), movieDetails);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movieDetails = savedInstanceState.getParcelable(getString(R.string.key_movie_details));
    }


    @Override
    public void onResume() {
        sendApiCalls(movieId);
        super.onResume();
    }
    private void setUpFavoriteButton() {
        getMovieDetailsFromDb();
    }

    private void setUpFavoriteButton(boolean idFound) {
        if (idFound) {
            favButton.setText(R.string.remove_favorite_button);
        } else {
            favButton.setText(R.string.mark_as_favorite_button);
        }
    }

    private View.OnClickListener favoriteButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    String buttonText = favButton.getText().toString();
                    if (buttonText.equalsIgnoreCase(getString(R.string.mark_as_favorite_button))) {
                        favButton.setText(R.string.remove_favorite_button);
                        updateDb();
                    } else {
                        favButton.setText(R.string.mark_as_favorite_button);
                        deleteDb();
                    }
                }
            }
        };
    }

    private void sendApiCalls(String movieId) {
        DetailsApiRequest.apiRequestActivity = this;
        if (movieDetails.getDuration() == null || movieDetails.getDuration().isEmpty()) {
            new DetailsApiRequest.MovieInfo().execute(movieId);
        } else {
            duration.setText(String.format(getString(R.string.duration_mins), movieDetails.getDuration()));
        }
        if (movieDetails.getTrailerDetailsList() == null || movieDetails.getTrailerDetailsList().isEmpty()) {
            new DetailsApiRequest.Videos().execute(movieId);
        }else {

            trailerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            trailerRecyclerView.setAdapter(new TrailerRecyclerAdapter(this, movieDetails.getTrailerDetailsList()));
        }
        if (movieDetails.getReviewDetailsList() == null ||  movieDetails.getReviewDetailsList().isEmpty()) {
            new DetailsApiRequest.Review().execute(movieId);
        }else {
            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewRecyclerView.setAdapter(new ReviewRecyclerAdapter(this, movieDetails.getReviewDetailsList()));
        }

    }

    private void getMovieDetailsFromDb() {
        AppDbExecutors.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                setUpFavoriteButton(isMoviePresent());
            }
        });
    }
    private boolean isMoviePresent(){
        try {
            MovieDetails movieDetails = movieDb.movieDao().getMovieDetails(movieId);
            return movieDetails != null && movieDetails.getMovieId() != null;
        }
        catch (Exception e){
            return false;
        }

    }
    private void updateDb() {
        AppDbExecutors.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                if (isMoviePresent()) {
                    movieDb.movieDao().updateTask(movieDetails);
                } else {
                    movieDb.movieDao().insertTask(movieDetails);
                }
            }
        });
    }

    private void deleteDb() {
        final Context context = this;
        AppDbExecutors.getInstance().getDiskIo().execute(new Runnable() {
            @Override
            public void run() {
                movieDb.movieDao().deleteTask(movieDetails);
            }
        });
    }

    private void updateUi() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movieDetails.getTitle());
        Picasso.with(this)
                .load(movieDetails.getThumbnail())
                .error(R.drawable.image_uavailable)
                .into(thumbnail);
        if(!(movieDetails.getRating() ==null || movieDetails.getRating().isEmpty())) {
            String ratingText = movieDetails.getRating() + "/10";
        rating.setText(ratingText);
        }
        if(!(movieDetails.getReleaseDate()== null ||movieDetails.getReleaseDate().isEmpty())) {
            released.setText(DateUtils.getYear(movieDetails.getReleaseDate()));
        }
        if(!(movieDetails.getOverview()== null ||movieDetails.getOverview().isEmpty())) {
            overview.setText(movieDetails.getOverview());
        }
        setUpFavoriteButton();
    }

}
