package com.example.doanmobile.taicautrucproduct;

public abstract class ProductPrototype implements Cloneable {
    // Xác định các thuộc tính và phương thức chung của sản phẩm
    private int productID;
    private int shopID;
    private int categoryID;
    private String title;
    private String description;
    private double price;
    private String imageURL;
    private int likeCount;
    private double selled;
    private double soluong;
    private double reviewcount;
    private double discount;

    // Cài đặt phương thức clone()
    @Override
    public ProductPrototype clone() throws CloneNotSupportedException {
        return (ProductPrototype) super.clone();
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public double getSelled() {
        return selled;
    }

    public void setSelled(double selled) {
        this.selled = selled;
    }

    public double getSoluong() {
        return soluong;
    }

    public void setSoluong(double soluong) {
        this.soluong = soluong;
    }

    public double getReviewcount() {
        return reviewcount;
    }

    public void setReviewcount(double reviewcount) {
        this.reviewcount = reviewcount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
