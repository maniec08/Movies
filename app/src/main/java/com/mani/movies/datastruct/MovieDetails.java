package com.mani.movies.datastruct;

import android.os.Parcelable;

import java.io.Serializable;

public class MovieDetails implements Serializable {
    private String moviePosterUrl;
    private String title;
    private String thumbnail;
    private String overview;
    private String rating;
    private String releaseDate;
    private String duration;

    public String getMovieId() {
        return movieId;
    }

    private String movieId;

    public MovieDetails(String moviePosterUrl, String title, String thumbnail, String overview, String rating, String releaseDate, String movieId) {
        this.moviePosterUrl = moviePosterUrl;
        this.title = title;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.movieId = movieId;
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


}
