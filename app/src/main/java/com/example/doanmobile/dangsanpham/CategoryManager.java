package com.example.doanmobile.dangsanpham;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryManager implements Subject {
    private List<Observer> observers = new ArrayList<>();


    @Override
    public void notifyObservers(Category category) {
        for (Observer observer : observers) {
            observer.update(category);
        }
    }

    public void addCategory(Category category, Context context) {
        // Thực hiện logic thêm category vào Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Category")
                .orderBy("categoryID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int newcategoryID = 1;
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                int highestCategoryID = document.getLong("categoryID").intValue();
                                newcategoryID = highestCategoryID + 1;
                            }
                        }

                        // Tạo một đối tượng Category với categoryID mới
                        Category newCategory = new Category(newcategoryID, category.getCategoryName());

                        // Thêm category vào Firestore
                        db.collection("Category")
                                .add(newCategory)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Thông báo cho các Observer về category mới
                                        notifyObservers(newCategory);
                                        Intent intent = new Intent(context, Uploadproduct.class);
                                        context.startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xử lý khi có lỗi xảy ra
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
    }
}