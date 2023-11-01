package com.example.doanmobile.yeuthichsanpham;

public class Favorites {
    private int favoriteID;
    private int userID;
    private int productID;

    public Favorites() {
    }
    public Favorites(int favoriteID, int userID, int productID) {
        this.favoriteID = favoriteID;
        this.userID = userID;
        this.productID = productID;
    }


    public int getFavoriteID() {
        return favoriteID;
    }

    public void setFavoriteID(int favoriteID) {
        this.favoriteID = favoriteID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }
}
