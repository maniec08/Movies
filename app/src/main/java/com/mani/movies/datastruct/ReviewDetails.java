package com.mani.movies.datastruct;

public class ReviewDetails {
    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public ReviewDetails(String reviewAuthor,String reviewContent ) {
        this.reviewContent = reviewContent;
        this.reviewAuthor = reviewAuthor;
    }

    private String reviewContent;
    private String reviewAuthor;
}
