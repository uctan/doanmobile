package com.example.doanmobile.dangkynguoiban;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.coicuahangshopdetail.cuahangshopdetail;
import com.example.doanmobile.dangsanpham.ProductAdapter;
import com.example.doanmobile.dangsanpham.Products;
import com.example.doanmobile.dangsanpham.UploadCategory;
import com.example.doanmobile.hoadonnguoiban.hoadonnguoiban;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class quanlysanphamthemsanpham extends AppCompatActivity {

    Button themsanphamcuahang;
    ImageButton backqlysanphamshop;
    RecyclerView recyclerViewqlysanphamshop;
    List<Products> productsList;
    ProductAdapter productAdapter;
    CollectionReference productCollection;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlysanphamthemsanpham);

        themsanphamcuahang = findViewById(R.id.themsanphamcuahang);
        backqlysanphamshop = findViewById(R.id.backqlysanphamshop);
        recyclerViewqlysanphamshop = findViewById(R.id.recyclerViewqlysanphamshop);
        db = FirebaseFirestore.getInstance();
        backqlysanphamshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quanlysanphamthemsanpham.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });
        themsanphamcuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quanlysanphamthemsanpham.this, UploadCategory.class);
                startActivity(intent);
            }
        });

        //load san pham theo shop
        productsList = new ArrayList<>();
        productAdapter = new ProductAdapter(quanlysanphamthemsanpham.this, productsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(quanlysanphamthemsanpham.this, 2);
        recyclerViewqlysanphamshop.setLayoutManager(gridLayoutManager);
        recyclerViewqlysanphamshop.setAdapter(productAdapter);
        productCollection = FirebaseFirestore.getInstance().collection("Products");
        AlertDialog.Builder builder = new AlertDialog.Builder(quanlysanphamthemsanpham.this);
        builder.setCancelable(false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        int shopID = documentSnapshot.getLong("shopId").intValue();
                        productCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    // Xử lý lỗi
                                    return;
                                }
                                productsList.clear();
                                for (DocumentSnapshot documentSnapshot : value) {
                                    Products products = documentSnapshot.toObject(Products.class);
                                    if (products.getShopID() == shopID) {
                                        productsList.add(products);
                                    }
                                }
                                productAdapter.notifyDataSetChanged();


                            }
                        });
                    }
                }

            });

        }
    }
}