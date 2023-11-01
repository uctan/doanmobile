package com.example.doanmobile.chat;

public class ChatHistoryModel {
    private int userID;
    private int shopID;
    private String tenDayDu;

    private String shopName;
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getTenDayDu() {
        return tenDayDu;
    }

    public void setTenDayDu(String tenDayDu) {
        this.tenDayDu = tenDayDu;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }



    public ChatHistoryModel(int userID, int shopID, String tenDayDu, String shopName) {
        this.userID = userID;
        this.shopID = shopID;
        this.tenDayDu = tenDayDu;
        this.shopName = shopName;
    }
    public ChatHistoryModel(){

    }

}