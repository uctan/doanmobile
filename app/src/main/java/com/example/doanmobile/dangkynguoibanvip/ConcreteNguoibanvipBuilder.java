package com.example.doanmobile.dangkynguoibanvip;

public class ConcreteNguoibanvipBuilder implements NguoibanvipBuilder{
    private int nguoibanvipID;
    private  int nguoibanviptang = 1;
    private int shopID;
    private Number price;
    @Override
    public NguoibanvipBuilder setNguoibanvipID(int nguoibanvipID) {
        this.nguoibanvipID = nguoibanvipID;
        return this;
    }

    @Override
    public NguoibanvipBuilder setShopID(int shopID) {
        this.shopID = shopID;
        return this;
    }

    @Override
    public NguoibanvipBuilder setPrice(Number price) {
        this.price = price;
        return this;
    }

    @Override
    public Nguoibanvip build() {
        return new Nguoibanvip(nguoibanvipID, shopID, price);
    }
}
