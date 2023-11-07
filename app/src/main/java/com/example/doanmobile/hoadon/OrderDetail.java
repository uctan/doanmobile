package com.example.doanmobile.hoadon;

public class OrderDetail {
    private static int nextorderdetailID = 1;
    private int detailID;
    private int soLuong;
    private int orderID;
    private int productID;
    private double giaSanPham;

    public  OrderDetail(){
    }

    public OrderDetail(int detailID, int soLuong, int orderID, int productID, double giaSanPham) {
        this.detailID = nextorderdetailID++;
        this.soLuong = soLuong;
        this.orderID = orderID;
        this.productID = productID;
        this.giaSanPham = giaSanPham;
    }

    public int getDetailID() {
        return detailID;
    }

    public void setDetailID(int detailID) {
        this.detailID = detailID;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(double giaSanPham) {
        this.giaSanPham = giaSanPham;
    }
}






