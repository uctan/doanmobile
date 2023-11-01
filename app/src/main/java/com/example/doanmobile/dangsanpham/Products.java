package com.example.doanmobile.dangsanpham;

public class Products {
    private int productID;
    private int shopID;
    private int categoryID;
    private String title;
    private String description;
    private double price;
    private String imageURL;
    private int likeCount;

    public Products(){}

    public Products(int productID, int shopID, int categoryID, String title, String description, double price, String imageURL, int likeCount) {
        this.productID = productID;
        this.shopID = shopID;
        this.categoryID = categoryID;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
        this.likeCount = likeCount;
    }
    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
