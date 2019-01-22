package com.mani.movies.utils;

import java.io.Serializable;

public class MovieDetails implements Serializable {
    private String moviePosterUrl;
    private String title;
    private String thumbnail;
    private String overview;
    private String rating;
    private String releaseDate;

    MovieDetails(String moviePosterUrl, String title, String thumbnail, String overview, String rating, String releaseDate) {
        this.moviePosterUrl = moviePosterUrl;
        this.title = title;
        this.thumbnail = thumbnail;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    String getMoviePosterUrl() {
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


}
