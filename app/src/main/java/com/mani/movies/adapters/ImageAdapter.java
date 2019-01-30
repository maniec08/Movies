package com.mani.movies.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.mani.movies.R;
import com.mani.movies.activity.DetailsActivity;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.utils.DetailsApiRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private List<MovieDetails> movieDetailsList;
    private Context context;

    public ImageAdapter(List<MovieDetails> movieDetails, Context contextReceived) {
        super();
        movieDetailsList = movieDetails;
        context = contextReceived;
    }

    @Override
    public int getCount() {
        return movieDetailsList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        final MovieDetails movieDetails = movieDetailsList.get(position);

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridLayout.LayoutParams());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!movieDetails.getMovieId().isEmpty()) {
                    sendApiCalls(movieDetails.getMovieId());
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("movie_id_for_detail", movieDetails.getMovieId());
                    intent.putExtra(context.getString(R.string.key_movie_details), movieDetails);
                    context.startActivity(intent);
                }
            }
        });

        if (movieDetailsList.get(position).getMoviePosterUrl().isEmpty()) {
            Picasso.with(context)
                    .load(R.drawable.image_uavailable)
                    .into(imageView);
        } else {
            Picasso.with(context)
                    .load(movieDetailsList.get(position).getMoviePosterUrl())
                    .error(R.drawable.image_uavailable)
                    .into(imageView);
        }

        return imageView;
    }

    private void sendApiCalls(String movieId) {
        DetailsApiRequest.detailsActivityApiRequest = context;
        new DetailsApiRequest.MovieInfo().execute(movieId);
        new DetailsApiRequest.Videos().execute(movieId);
        new DetailsApiRequest.Review().execute(movieId);
    }
}
