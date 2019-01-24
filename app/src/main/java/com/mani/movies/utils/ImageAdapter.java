package com.mani.movies.utils;

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
                MovieDetails movieDetails = movieDetailsList.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(context.getString(R.string.key_movie_details), movieDetails);
                context.startActivity(intent);
            }
        });
        Picasso.with(context)
                .load(movieDetailsList.get(position).getMoviePosterUrl()).error(R.color.colorgrey)
                .into(imageView);

        return imageView;
    }
}
