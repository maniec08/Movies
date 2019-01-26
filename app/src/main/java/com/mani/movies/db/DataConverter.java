package com.mani.movies.db;

import android.arch.persistence.room.TypeConverter;

import com.mani.movies.datastruct.ReviewDetails;
import com.mani.movies.datastruct.TrailerDetails;
import com.mani.movies.utils.ExtractMovieDetails;
import com.mani.movies.utils.KeyConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DataConverter {

    @TypeConverter
    public static List<ReviewDetails> toReviewList(String str){
        try {
            return ExtractMovieDetails.parseReviewJson(new JSONArray(str));
        } catch (JSONException e) {
           return new ArrayList<>();
        }
    }

    @TypeConverter
    public static List<TrailerDetails> toVideoList(String str){
        try {
            return ExtractMovieDetails.parseVideoJson(new JSONArray(str));
        } catch (JSONException e) {
            return new ArrayList<>();
        }
    }

    @TypeConverter
    public static String convertReviewListToString(List<ReviewDetails> reviewDetailsList){
        try {
            JSONArray jsonArray = new JSONArray();
            for (ReviewDetails reviewDetails : reviewDetailsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(KeyConstants.keyContent, reviewDetails.getReviewContent());
                jsonObject.put(KeyConstants.keyAuthor, reviewDetails.getReviewAuthor());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
        catch(Exception e){
            return "";
        }

    }

    @TypeConverter
    public static String convertVideoListToString(List<TrailerDetails> trailerDetailsList){
        try {
            JSONArray jsonArray = new JSONArray();
            for (TrailerDetails trailerDetails : trailerDetailsList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(KeyConstants.keyName, trailerDetails.getName());
                jsonObject.put(KeyConstants.keyVideoKey, trailerDetails.getKey());
                jsonArray.put(jsonObject);
            }
            return jsonArray.toString();
        }
        catch(Exception e){
            return "";
        }

    }
}
