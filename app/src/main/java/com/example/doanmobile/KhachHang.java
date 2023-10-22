package com.example.doanmobile;

public class KhachHang {

    private int userID;
    private String matKhau;
    private String email;
    private String tenDayDu;
    private String soDienThoai;
    private boolean isKhachHang;
    private boolean isNguoiBan;
    private boolean isNguoiBanVip;

    // Constructors
    public KhachHang() {} // Thêm constructor mặc định


    public KhachHang(int userID, String matKhau, String email, String tenDayDu, String soDienThoai,
                     boolean isKhachHang, boolean isNguoiBan, boolean isNguoiBanVip) {
        this.userID = userID;
        this.matKhau = matKhau;
        this.email = email;
        this.tenDayDu = tenDayDu;
        this.soDienThoai = soDienThoai;
        this.isKhachHang = isKhachHang;
        this.isNguoiBan = isNguoiBan;
        this.isNguoiBanVip = isNguoiBanVip;
    }

    // Thêm getter và setter cho userID
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTenDayDu() {
        return tenDayDu;
    }

    public void setTenDayDu(String tenDayDu) {
        this.tenDayDu = tenDayDu;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isKhachHang() {
        return isKhachHang;
    }

    public void setKhachHang(boolean khachHang) {
        isKhachHang = khachHang;
    }

    public boolean isNguoiBan() {
        return isNguoiBan;
    }

    public void setNguoiBan(boolean nguoiBan) {
        isNguoiBan = nguoiBan;
    }

    public boolean isNguoiBanVip() {
        return isNguoiBanVip;
    }

    public void setNguoiBanVip(boolean nguoiBanVip) {
        isNguoiBanVip = nguoiBanVip;
    }
}