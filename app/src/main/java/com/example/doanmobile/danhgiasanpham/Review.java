package com.example.doanmobile.danhgiasanpham;

public class Review {
    private static int nextReviewID = 1;
    private int reviewID;
    private int productID;
    private int userID;
    private float rating;
    private String tinnhan;
    public Review(){}

    public Review(int reviewID, int productID, int userID, float rating, String tinnhan) {
        this.reviewID = nextReviewID++;
        this.productID = productID;
        this.userID = userID;
        this.rating = rating;
        this.tinnhan = tinnhan;
    }

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getTinnhan() {
        return tinnhan;
    }

    public void setTinnhan(String tinnhan) {
        this.tinnhan = tinnhan;
    }
}
