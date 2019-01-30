package com.mani.movies.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;
import com.mani.movies.db.AppDb;
import com.mani.movies.db.DataConverter;

import java.util.List;

public class DetailsApiRequest {

    public static Context detailsActivityApiRequest;

    public static class Review extends AsyncTask<String, String, List<ReviewDetails>> {
        String movieId = "";

        @Override
        protected List<ReviewDetails> doInBackground(String... strings) {
            movieId = strings[0];

            List<ReviewDetails> reviewDetailsList= ExtractMovieDetails.getReviews(movieId);
            AppDb.getInstance(detailsActivityApiRequest).movieDao().updateReviewDetails(
                    DataConverter.convertReviewListToString(reviewDetailsList), movieId);
            return  reviewDetailsList;
        }

        @Override
        protected void onPostExecute(final List<ReviewDetails> reviewDetailsList) {

        }
    }


    public static class Videos extends AsyncTask<String, String, List<TrailerDetails>> {
        String movieId = "";
        @Override
        protected List<TrailerDetails> doInBackground(String... strings) {
            movieId = strings[0];
            List<TrailerDetails> trailerDetailsList =  ExtractMovieDetails.getVideos(strings[0]);
            AppDb.getInstance(detailsActivityApiRequest).movieDao().updateTrailerDetails(
                    DataConverter.convertVideoListToString(trailerDetailsList), movieId);
            return trailerDetailsList;
        }

        @Override
        protected void onPostExecute(final List<TrailerDetails> trailerDetailsList) {
        }
    }

    public static class MovieInfo extends AsyncTask<String, String, String> {
        String movieId = "";

        @Override
        protected String doInBackground(String... strings) {
            movieId = strings[0];
            String duration =  ExtractMovieDetails.getMovieDetails(strings[0]);
            AppDb.getInstance(detailsActivityApiRequest).movieDao().updateMovieDuration(duration,movieId);
            return duration;
        }

        @Override
        protected void onPostExecute(final String duration) {

        }
    }

}

