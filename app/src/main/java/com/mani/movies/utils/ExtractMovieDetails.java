package com.mani.movies.utils;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import com.mani.movies.datastruct.MovieDetails;
import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;

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

    public static String getMovieDetails(String movieId){
        String requestUrl = HOST_URL + "/movie/"+movieId ;
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseMovieDetailJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    public static List<ReviewDetails> getReviews(String movieId){
        String requestUrl = HOST_URL + "/movie/"+movieId +"/reviews";
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseReviewJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }

    public static List<TrailerDetails> getVideos(String movieId){
        String requestUrl = HOST_URL + "/movie/"+movieId +"/videos";
        HttpRequest httpRequest = new HttpRequest(requestUrl);
        try {
            return parseVideoJson(httpRequest.makeHttpRequest());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return new ArrayList<>();
    }
    private static String parseMovieDetailJson(String str){
        String keyRunTime = "runtime";
        try{
            JSONObject jsonObject = new JSONObject(str);
            return jsonObject.optString(keyRunTime, "");
        } catch (JSONException e) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG,e.getMessage());
        }
        return "";
    }
    private static List<TrailerDetails> parseVideoJson(String str){
        String keyResults = "results";
        String keyName = "name";
        String keyVideoKey = "key";
        String keySite = "site";
        String keyType = "type";
        String matchTrailer = "Trailer";
        List<TrailerDetails> trailerDetailsList = new ArrayList<>();
        try{
            JSONArray resultArray = (new JSONObject(str)).getJSONArray(keyResults);
            JSONObject videoJson = new JSONObject();
            for (int i=0;i<resultArray.length();i++){
                try {
                    videoJson = resultArray.getJSONObject(i);
                    if(videoJson.optString(keyType,"").equalsIgnoreCase(matchTrailer)) {
                        TrailerDetails trailerDetails = new TrailerDetails(
                                videoJson.optString(keyName, "Name Unavailable"),
                                videoJson.optString(keyVideoKey),
                                videoJson.optString(keySite)
                        );
                        trailerDetailsList.add(trailerDetails);
                    }

                }
                catch (JSONException e){
                    Log.e(TAG, "Json error->" + videoJson.toString());
                    Log.e(TAG,e.getMessage());
                }
            }
        }catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG,e1.getMessage());
        }
        return trailerDetailsList;
    }

    private static List<ReviewDetails> parseReviewJson(String str){
        String keyResults = "results";
        String keyAuthor = "author";
        String keyContent = "content";
        List<ReviewDetails> reviewDetailsList = new ArrayList<>();
        try{
            JSONArray resultArray = (new JSONObject(str)).getJSONArray(keyResults);
            JSONObject reviewJson = new JSONObject();
            for (int i=0;i<resultArray.length();i++){
                try {
                    reviewJson = resultArray.getJSONObject(i);
                    ReviewDetails reviewDetails = new ReviewDetails(
                            reviewJson.getString(keyAuthor),
                            reviewJson.getString(keyContent)
                    );
                    reviewDetailsList.add(reviewDetails);

                }
                catch (JSONException e){
                    Log.e(TAG, "Json error->" + reviewJson.toString());
                    Log.e(TAG,e.getMessage());
                }
            }
        }catch (JSONException e1) {
            Log.e(TAG, "Json parse error->" + str);
            Log.e(TAG,e1.getMessage());
        }
        return reviewDetailsList;
    }

    private static List<MovieDetails> parseJson(String str){
          String keyResults = "results";
          String keyMoviePosterUrl = "poster_path";
          String keyTitle = "title";
          String keyThumbnail = "backdrop_path";
          String keyOverview = "overview";
          String keyRating = "vote_average";
          String keyReleaseDate = "release_date";
          String keyMovieId = "id";
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
                            movieJson.getString(keyReleaseDate),
                            movieJson.getString(keyMovieId));

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
