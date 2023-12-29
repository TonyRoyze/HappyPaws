package com.example.happypaws.Classes;

public class Review {
    private int id;
    private int postId, forId, fromId;
    private double rating;
    private String review, status;

    public Review(int id, int postId, int forId, int fromId, String status, double rating, String review) {
        this.id = id;
        this.postId = postId;
        this.forId = forId;
        this.fromId = fromId;
        this.rating = rating;
        this.review = review;
        this.status = status;
    }

    public Review(int id, int postId, int forId, int fromId, String status) {
        this.id = id;
        this.postId = postId;
        this.forId = forId;
        this.fromId = fromId;
        this.status = status;

        this.rating = 0.0;
        this.review = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getForId() {
        return forId;
    }

    public void setForId(int forId) {
        this.forId = forId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
