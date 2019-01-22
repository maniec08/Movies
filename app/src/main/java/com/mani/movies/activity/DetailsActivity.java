package com.mani.movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.utils.MovieDetails;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends AppCompatActivity {

    private MovieDetails movieDetails;
    // @BindView(R.id.thumbnail_iv)
    ImageView thumbnail;

    // @BindView(R.id.rating_tv)
    TextView rating;
    // @BindView(R.id.released_on_tv)
    TextView released;
    // @BindView(R.id.preview_tv)
    TextView overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Intent intent = getIntent();
        movieDetails = (MovieDetails) intent.getSerializableExtra(getString(R.string.key_movie_details));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(movieDetails.getTitle());
        thumbnail = findViewById(R.id.thumbnail_iv);
        rating = findViewById(R.id.rating_tv);
        released = findViewById(R.id.released_on_tv);
        overview = findViewById(R.id.overview_tv);

        updateUi();

    }

    private void updateUi() {
        Picasso.with(this)
                .load(movieDetails.getThumbnail())
                .into(thumbnail);
        String ratingText = getString(R.string.rating_label) + movieDetails.getRating();
        String releaseDateText = getString(R.string.released_on_label) + movieDetails.getReleaseDate();
        rating.setText(ratingText);
        released.setText(releaseDateText);
        overview.setText(movieDetails.getOverview());
    }
}
