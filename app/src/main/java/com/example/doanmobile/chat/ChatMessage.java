package com.example.doanmobile.chat;

import java.util.Date;

public class ChatMessage {
    private int userID;
    private int shopID;
    private String mess;
    private Date dateObj;
    private String datetime;

    private long timestamp;

    private String tenDayDu;



    private String shopName;

    public ChatMessage() {
    }



    public ChatMessage(int userID, int shopID, String mess, Date dateObj, String datetime, long timestamp, String tenDayDu, String shopName) {
        this.userID = userID;
        this.shopID = shopID;
        this.mess = mess;
        this.dateObj = dateObj;
        this.datetime = datetime;
        this.timestamp = timestamp;
        this.tenDayDu=tenDayDu;
        this.shopName=shopName;
    }

    // Getter methods
    public int getUserID() {
        return userID;
    }

    public int getShopID() {
        return shopID;
    }

    public String getMess() {
        return mess;
    }

    public Date getDateObj() {
        return dateObj;
    }

    public String getDatetime() {
        return datetime;
    }



    public long getTimestamp() {
        return timestamp;
    }


    public String getTenDayDu() {return tenDayDu;}

    public String getShopName() {return shopName;}

    // Setter methods
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public void setDateObj(Date dateObj) {
        this.dateObj = dateObj;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }



    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTenDayDu(String tenDayDu) {this.tenDayDu = tenDayDu;}

    public void setShopName(String shopName) {this.shopName = shopName;}
}
