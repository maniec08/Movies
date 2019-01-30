package com.mani.movies.datastruct;

public class ReviewDetails {

    private String reviewContent;
    private String reviewAuthor;

    public ReviewDetails(String reviewAuthor, String reviewContent) {
        this.reviewContent = reviewContent;
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }
}
