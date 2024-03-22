package com.example.doanmobile.dangsanpham;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;



public class CategoryObserver implements Observer {
    @SuppressLint("RestrictedApi")
    @Override
    public void update(Category category) {
        Log.d(TAG, "New category added: " + category.getCategoryName());
    }
}
