package com.example.doanmobile.dangsanpham;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private List<CartItem> cartItems;
    private static CartManager instance;
    private OnCartChangeListener listener;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        if (listener != null) {
            listener.onCartChanged();
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }
    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.listener = listener;
    }
    public interface OnCartChangeListener {
        void onCartChanged();
    }
    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
    public CartItem getCarrtItemByProductID (int productID)
    {
        for (CartItem item : cartItems)
        {
            if (item.getProductID() == productID){
                return item;
            }
        }
        return null;
    }

}

