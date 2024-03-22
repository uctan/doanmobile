package com.example.doanmobile.taicautrucproduct;

public class ConcreteProductPrototype extends ProductPrototype {
    public ConcreteProductPrototype() {}
    public ConcreteProductPrototype(String title, String mota, double price, int categoryID, int shopID, double soluong, double selled, double reviewcount, double discount) {
        setTitle(title);
        setDescription(mota);
        setPrice(price);
        setCategoryID(categoryID);
        setShopID(shopID);
        setSoluong(soluong);
        setSelled(selled);
        setReviewcount(reviewcount);
        setDiscount(discount);
    }
}
