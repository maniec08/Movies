package com.mani.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;
import com.mani.movies.utils.ApiRequest;
import com.mani.movies.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailsActivity extends AppCompatActivity {

    public static List<TrailerDetails> trailerDetailsList = new ArrayList<>();
    public static List<ReviewDetails> reviewDetailsList = new ArrayList<>();
    private MovieDetails movieDetails;
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
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Intent intent = getIntent();
        movieDetails = (MovieDetails) intent.getSerializableExtra(getString(R.string.key_movie_details));
        sendApiCalls(movieDetails.getMovieId());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movieDetails.getTitle());
        thumbnail = findViewById(R.id.thumbnail_iv);
        rating = findViewById(R.id.rating_tv);
        released = findViewById(R.id.released_on_tv);
        overview = findViewById(R.id.overview_tv);
        duration = findViewById(R.id.duration_tv);
        button = findViewById(R.id.favorite_button);
        button.setOnClickListener(favoriteButtonClickListener());
        updateUi();
    }
    private View.OnClickListener favoriteButtonClickListener(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(v instanceof Button){
                    String buttonText =((Button) v).getText().toString();
                    if(buttonText.equalsIgnoreCase(getString(R.string.mark_as_favorite_button).trim())){
                        ((Button) v).setText(R.string.remove_favorite_button);
                    }
                    else{
                        ((Button) v).setText(R.string.mark_as_favorite_button);
                    }
                }
            }
        };
    }
    private void sendApiCalls(String movieId) {
       ApiRequest.apiRequestActivity = this;
        new ApiRequest.MovieInfo().execute(movieId);
        new ApiRequest.Videos().execute(movieId);
        new ApiRequest.Review().execute(movieId);
    }

    private void updateUi() {
        Picasso.with(this)
                .load(movieDetails.getThumbnail())
                .error(R.color.colorgrey)
                .into(thumbnail);
        String ratingText = movieDetails.getRating() +"/10";
        rating.setText(ratingText);
        released.setText(DateUtils.getYear(movieDetails.getReleaseDate()));
        overview.setText(movieDetails.getOverview());
    }

}
