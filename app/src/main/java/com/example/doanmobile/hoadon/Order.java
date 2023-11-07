package com.example.doanmobile.hoadon;

import java.util.Date;

public class Order {
    private static int nextorderID = 1;
    private int orderID;
    private int userID;
    private Date orderDate;
    private String diaChi;
    private double tongTien;
    private String htThanhToan;

    public  Order(){

    }
    public Order(int orderID, int userID, Date orderDate, String diaChi, double tongTien, String htThanhToan) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.htThanhToan = htThanhToan;
    }

    public int getOrderID() {
        return orderID;
    }

    public int getUserID() {
        return userID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public double getTongTien() {
        return tongTien;
    }

    public String getHtThanhToan() {
        return htThanhToan;
    }


}