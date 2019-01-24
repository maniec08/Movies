package com.mani.movies.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mani.movies.R;
import com.mani.movies.adapters.ReviewRecyclerAdapter;
import com.mani.movies.adapters.TrailerRecyclerAdapter;
import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;

import java.util.List;

public class ApiRequest {

    public static Activity apiRequestActivity = new Activity();

    public static class Review extends AsyncTask<String, String, List<ReviewDetails>> {

        @Override
        protected List<ReviewDetails> doInBackground(String... strings) {
            return ExtractMovieDetails.getReviews(strings[0]);
        }

        @Override
        protected void onPostExecute(List<ReviewDetails> reviewDetailsList) {
            updateReviewRecyclerView(reviewDetailsList);
        }


    }

    private static void updateReviewRecyclerView(List<ReviewDetails> reviewDetailsList) {
        RecyclerView reviewRecyclerView = apiRequestActivity.findViewById(R.id.reviews_recycler_view);
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(apiRequestActivity));
        reviewRecyclerView.setAdapter(new ReviewRecyclerAdapter(apiRequestActivity, reviewDetailsList));
    }


    private static void updateTrailerRecyclerView(List<TrailerDetails> reviewDetailsList) {
        RecyclerView trailorRecyclerView = apiRequestActivity.findViewById(R.id.trailer_recycler_view);
        trailorRecyclerView.setLayoutManager(new LinearLayoutManager(apiRequestActivity));
        trailorRecyclerView.setAdapter(new TrailerRecyclerAdapter(apiRequestActivity, reviewDetailsList));
    }

    public static class Videos extends AsyncTask<String, String, List<TrailerDetails>> {

        @Override
        protected List<TrailerDetails> doInBackground(String... strings) {
            return ExtractMovieDetails.getVideos(strings[0]);
        }

        @Override
        protected void onPostExecute(List<TrailerDetails> trailerDetailsList) {
            updateTrailerRecyclerView(trailerDetailsList);
        }
    }

    public static class MovieInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return ExtractMovieDetails.getMovieDetails(strings[0]);
        }

        @Override
        protected void onPostExecute(String duration) {
            TextView textView = apiRequestActivity.findViewById(R.id.duration_tv);
            textView.setText(String.format(apiRequestActivity.getString(R.string.duration_mins), duration));
        }
    }
}
