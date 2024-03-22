package com.example.doanmobile.dangkynguoibanvip;

public interface NguoibanvipBuilder {
    NguoibanvipBuilder setNguoibanvipID(int nguoibanvipID);
    NguoibanvipBuilder setShopID(int shopID);
    NguoibanvipBuilder setPrice(Number price);
    Nguoibanvip build();
}
