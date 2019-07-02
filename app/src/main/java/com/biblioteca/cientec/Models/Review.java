package com.biblioteca.cientec.Models;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    private int userId;
    private int bookId;
    private String review;
    private int rating;
    private String updatedAt;

    public Review() {
        this.userId = -1;
        this.bookId = -1;
        this.review = "";
        this.rating = -1;
        this.updatedAt = "";
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
