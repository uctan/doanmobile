package com.example.doanmobile.chat;

public class ChatHistoryModel {
    private String userID;
    private String shopName;

    public ChatHistoryModel(String userID, String shopName) {
        this.userID = userID;
        this.shopName = shopName;
    }

    public String getUserID() {
        return userID;
    }

    public String getShopName() {
        return shopName;
    }
}