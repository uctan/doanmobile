package com.example.doanmobile.dangkynguoiban;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseShopRepository implements ShopRepository {
    private final FirebaseFirestore db;

    public FirebaseShopRepository(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void addShop(Shop shop, Callback<Boolean> callback) {
        getHighestShopId(new Callback<Integer>() {
            @Override
            public void onSuccess(Integer highestShopId) {
                int newShopId = highestShopId + 1;
                shop.setShopId(newShopId);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    setShopIdForNewShop(shop, userId, newShopId, callback);
                } else {
                    callback.onError("Loi");
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    private void getHighestShopId(Callback<Integer> callback) {
        db.collection("Shop")
                .orderBy("shopId", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int highestShopId = 0;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        highestShopId = document.getLong("shopId").intValue();
                    }
                    callback.onSuccess(highestShopId);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    private void setShopIdForNewShop(Shop shop, String userId, int newShopId, Callback<Boolean> callback) {
        DocumentReference shopRef = db.collection("Shop").document(userId);
        shop.setShopId(newShopId);
        shopRef.set(shop)
                .addOnSuccessListener(aVoid -> callback.onSuccess(true))
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
}
