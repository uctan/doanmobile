package com.example.doanmobile.dangkynguoibanvip;

public class Nguoibanvip {
    private static  int nextnguoibanvipID = 1;
    private int nguoibanvipID;
    private int shopID;
    private Number price;

    public Nguoibanvip(){

    }

    public Nguoibanvip(int nguoibanvipID, int shopID, Number price) {
        this.nguoibanvipID = nextnguoibanvipID++;
        this.shopID = shopID;
        this.price = price;
    }

    public int getNguoibanvipID() {
        return nguoibanvipID;
    }

    public void setNguoibanvipID(int nguoibanvipID) {
        this.nguoibanvipID = nguoibanvipID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }
}
