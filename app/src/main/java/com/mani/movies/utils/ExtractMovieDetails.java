package com.mani.movies.utils;


import android.util.Log;

import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractMovieDetails {
    private static String HOST_URL = "http://api.themoviedb.org/3";
    private static String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    private static String POPULAR_MOVIES_URL = HOST_URL + "/movie/popular";
    private static String RATED_MOVIES_URL = HOST_URL + "/movie/top_rated";

    private static String TAG = ExtractMovieDetails.class.getSimpleName();

    public static List<MovieDetails> getPopularMovies() {
        HttpRequest httpRequest = new HttpRequest(POPULAR_MOVIES_URL);
        try {
            return parseJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<MovieDetails> getRatedMovies() {
        HttpRequest httpRequest = new HttpRequest(RATED_MOVIES_URL);
        try {
            return parseJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    static String getMovieDetails(String movieId) {
        String requestUrl = HOST_URL + "/movie/" + movieId;
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseMovieDetailJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    static List<ReviewDetails> getReviews(String movieId) {
        String requestUrl = HOST_URL + "/movie/" + movieId + "/reviews";
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseReviewJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    static List<TrailerDetails> getVideos(String movieId) {
        String requestUrl = HOST_URL + "/movie/" + movieId + "/videos";
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseVideoJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    private static String parseMovieDetailJson(String str) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            return jsonObject.optString(KeyConstants.keyRunTime, "");
        } catch (JSONException e) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    private static List<TrailerDetails> parseVideoJson(String str) {

        List<TrailerDetails> trailerDetailsList = new ArrayList<>();
        try {
            return parseVideoJson((new JSONObject(str)).getJSONArray(KeyConstants.keyResults));

        } catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG, e1.getMessage());
        }
        return trailerDetailsList;
    }

    public static List<TrailerDetails> parseVideoJson(JSONArray jsonArray) {
        List<TrailerDetails> trailerDetailsList = new ArrayList<>();

        JSONObject videoJson = new JSONObject();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                videoJson = jsonArray.getJSONObject(i);
                if (videoJson.optString(KeyConstants.keyType, "").equalsIgnoreCase(KeyConstants.matchTrailer)) {
                    TrailerDetails trailerDetails = new TrailerDetails(
                            videoJson.optString(KeyConstants.keyName, "Name Unavailable"),
                            videoJson.optString(KeyConstants.keyVideoKey)
                    );
                    trailerDetailsList.add(trailerDetails);
                }

            } catch (JSONException e) {
                Log.e(TAG, "Json error->" + videoJson.toString());
                Log.e(TAG, e.getMessage());
            }
        }
        return trailerDetailsList;
    }

    private static List<ReviewDetails> parseReviewJson(String str) {

        List<ReviewDetails> reviewDetailsList = new ArrayList<>();
        try {
            return parseReviewJson((new JSONObject(str)).getJSONArray(KeyConstants.keyResults));

        } catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG, e1.getMessage());
        }
        return reviewDetailsList;
    }

    public static List<ReviewDetails> parseReviewJson(JSONArray jsonArray) {
        List<ReviewDetails> reviewDetailsList = new ArrayList<>();

        JSONObject reviewJson = new JSONObject();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                reviewJson = jsonArray.getJSONObject(i);
                ReviewDetails reviewDetails = new ReviewDetails(
                        reviewJson.getString(KeyConstants.keyAuthor),
                        reviewJson.getString(KeyConstants.keyContent)
                );
                reviewDetailsList.add(reviewDetails);

            } catch (JSONException e) {
                Log.e(TAG, "Json error->" + reviewJson.toString());
                Log.e(TAG, e.getMessage());
            }
        }
        return reviewDetailsList;
    }

    private static List<MovieDetails> parseJson(String str) {
        Log.d(TAG, "API Response --> " + str);

        List<MovieDetails> movieDetailsList = new ArrayList<>();
        try {
            JSONArray resultArray = (new JSONObject(str)).getJSONArray(KeyConstants.keyResults);
            JSONObject movieJson = new JSONObject();
            for (int i = 0; i < resultArray.length(); i++) {
                try {
                    movieJson = resultArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails(
                            IMAGE_URL + movieJson.getString(KeyConstants.keyMoviePosterUrl),
                            movieJson.getString(KeyConstants.keyTitle),
                            IMAGE_URL + movieJson.getString(KeyConstants.keyThumbnail),
                            movieJson.getString(KeyConstants.keyOverview),
                            movieJson.getString(KeyConstants.keyRating),
                            movieJson.getString(KeyConstants.keyReleaseDate),
                            movieJson.getString(KeyConstants.keyMovieId));

                    movieDetailsList.add(movieDetails);
                } catch (JSONException e) {
                    Log.e(TAG, "Json error->" + movieJson.toString());
                    Log.e(TAG, e.getMessage());
                }
            }
        } catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG, e1.getMessage());
        }

        return movieDetailsList;

    }
}
