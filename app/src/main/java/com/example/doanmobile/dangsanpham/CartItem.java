package com.example.doanmobile.dangsanpham;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productID);
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeString(imageURL);
        dest.writeInt(quantity);
    }

    protected CartItem(Parcel in) {
        productID = in.readInt();
        title = in.readString();
        price = in.readDouble();
        imageURL = in.readString();
        quantity = in.readInt();
    }

    public static final Parcelable.Creator<CartItem> CREATOR = new Parcelable.Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

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
