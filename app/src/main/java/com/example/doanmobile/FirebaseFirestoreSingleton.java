package com.example.doanmobile;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFirestoreSingleton {
    private static FirebaseFirestore instance;

    private FirebaseFirestoreSingleton() {} // Không cho phép khởi tạo từ bên ngoài

    public static synchronized FirebaseFirestore getInstance() {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }
}