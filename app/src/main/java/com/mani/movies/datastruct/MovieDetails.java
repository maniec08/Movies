package com.mani.movies.datastruct;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.mani.movies.db.DataConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
@TypeConverters(DataConverter.class)
public class MovieDetails implements Parcelable {
    @PrimaryKey
    @NonNull
    private String movieId;
    private String title;
    private String moviePosterUrl;
    private String thumbnail;
    private String overview;
    private String rating;
    private String releaseDate;
    private String duration;
    private List<ReviewDetails> reviewDetailsList = new ArrayList<>();
    private List<TrailerDetails> trailerDetailsList = new ArrayList<>();

    public List<ReviewDetails> getReviewDetailsList() {
        return reviewDetailsList;
    }

    public List<TrailerDetails> getTrailerDetailsList() {
        return trailerDetailsList;
    }
    public void setReviewDetailsList(List<ReviewDetails> reviewDetailsList) {
        this.reviewDetailsList = reviewDetailsList;
    }

    public void setTrailerDetailsList(List<TrailerDetails> trailerDetailsList) {
        this.trailerDetailsList = trailerDetailsList;
    }

    public MovieDetails(String moviePosterUrl, String title, String thumbnail, String overview, String rating, String releaseDate, String movieId) {
        this.moviePosterUrl = moviePosterUrl;
        this.title = title;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movieId);
        dest.writeString(this.title);
        dest.writeString(this.duration);
        dest.writeString(this.moviePosterUrl);
        dest.writeString(this.overview);
        dest.writeString(this.rating);
        dest.writeString(this.releaseDate);
        dest.writeString(this.thumbnail);
    }

    private MovieDetails(Parcel parcel) {
        movieId = parcel.readString();
        title = parcel.readString();
        duration = parcel.readString();
        moviePosterUrl = parcel.readString();
        overview = parcel.readString();
        rating = parcel.readString();
        releaseDate = parcel.readString();
        thumbnail = parcel.readString();
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel parcel) {
            return new MovieDetails(parcel);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };
}
