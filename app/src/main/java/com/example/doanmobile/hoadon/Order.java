package com.example.doanmobile.hoadon;

import java.util.Date;

public class Order {
    private int orderID;
    private int userID;
    private Date orderDate;
    private String diaChi;
    private double tongTien;
    private String htThanhToan;
    private  String phuongthucvanchuyen;
    private  double phivanchuyen;
    private  String trangthai;

    public  Order(){

    }
    public Order(int orderID, int userID, Date orderDate, String diaChi, double tongTien, String htThanhToan,double phivanchuyen,String phuongthucvanchuyen,String trangthai) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.htThanhToan = htThanhToan;
        this.phivanchuyen = phivanchuyen;
        this.phuongthucvanchuyen = phuongthucvanchuyen;
        this.trangthai = trangthai;
    }

    public String getPhuongthucvanchuyen() {
        return phuongthucvanchuyen;
    }

    public void setPhuongthucvanchuyen(String phuongthucvanchuyen) {
        this.phuongthucvanchuyen = phuongthucvanchuyen;
    }

    public double getPhivanchuyen() {
        return phivanchuyen;
    }

    public void setPhivanchuyen(double phivanchuyen) {
        this.phivanchuyen = phivanchuyen;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
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