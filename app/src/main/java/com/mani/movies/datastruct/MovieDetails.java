package com.mani.movies.datastruct;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.mani.movies.db.DataConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private boolean isFavorite;
    private String reviewDetailsList;
    private String trailerDetailsList;

    public String getReviewDetailsList() {
        return reviewDetailsList;
    }

    public void setReviewDetailsList(String reviewDetailsList) {
        this.reviewDetailsList = reviewDetailsList;
    }

    public String getTrailerDetailsList() {
        return trailerDetailsList;
    }

    public void setTrailerDetailsList(String trailerDetailsList) {
        this.trailerDetailsList = trailerDetailsList;
    }

    @Ignore
    public MovieDetails(@NonNull String movieId, String moviePosterUrl){
        this.moviePosterUrl = moviePosterUrl;
        this.movieId = movieId;
    }

    public MovieDetails(String moviePosterUrl, String title, String thumbnail, String overview, String rating, String releaseDate, @NonNull String movieId) {
        this.moviePosterUrl = moviePosterUrl;
        this.title = title;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
    }

    @NonNull
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
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
        movieId = Objects.requireNonNull(parcel.readString());
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
