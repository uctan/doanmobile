package com.example.doanmobile.dangkynguoiban;

public interface Callback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}