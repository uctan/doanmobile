package com.example.doanmobile.dangsanpham;

public class CartItem {
    private int productID;
    private String title;
    private double price;
    private String imageURL;
    private int quantity;

    public CartItem(int productID, String title, double price, String imageURL, int quantity) {
        this.productID = productID;
        this.title = title;
        this.price = price;
        this.imageURL = imageURL;
        this.quantity=quantity;
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {

        return price* this.quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
