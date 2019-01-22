package com.mani.movies.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractMovieDetails  {
    private static String HOST_URL = "http://api.themoviedb.org/3";
    private static String IMAGE_URL = "http://image.tmdb.org/t/p/w342/";
    private static String POPULAR_MOVIES_URL =HOST_URL+ "/movie/popular";
    private static String RATED_MOVIES_URL = HOST_URL+"/movie/top_rated";
    private static String TAG = ExtractMovieDetails.class.getSimpleName();

    public static List<MovieDetails> getPopularMovies(){
        HttpRequest httpRequest = new HttpRequest(POPULAR_MOVIES_URL);
        try {
            return parseJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<MovieDetails> getRatedMovies(){
        HttpRequest httpRequest = new HttpRequest(RATED_MOVIES_URL);
        try {
            return parseJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    private static List<MovieDetails> parseJson(String str){
          String keyResults = "results";
          String keyMoviePosterUrl = "poster_path";
          String keyTitle = "title";
          String keyThumbnail = "backdrop_path";
          String keyOverview = "overview";
          String keyRating = "vote_average";
          String keyReleaseDate = "release_date";
        List<MovieDetails> movieDetailsList = new ArrayList<>();
        try {
            JSONArray resultArray = (new JSONObject(str)).getJSONArray(keyResults);
            JSONObject movieJson = new JSONObject();
            for (int i=0;i<resultArray.length();i++){
                try {
                    movieJson = resultArray.getJSONObject(i);
                    MovieDetails movieDetails = new MovieDetails(
                            IMAGE_URL + movieJson.getString(keyMoviePosterUrl),
                            movieJson.getString(keyTitle),
                            IMAGE_URL +movieJson.getString(keyThumbnail),
                            movieJson.getString(keyOverview),
                            movieJson.getString(keyRating),
                            movieJson.getString(keyReleaseDate));
                    movieDetailsList.add(movieDetails);
                }
                catch (JSONException e){
                    Log.e(TAG, "Json error->" + movieJson.toString());
                    Log.e(TAG,e.getMessage());
                }
            }
        } catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG,e1.getMessage());
        }

        return movieDetailsList;

    }
}
