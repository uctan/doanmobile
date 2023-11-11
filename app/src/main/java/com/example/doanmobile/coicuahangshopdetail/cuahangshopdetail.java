package com.example.doanmobile.coicuahangshopdetail;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmobile.R;
import com.example.doanmobile.chat.ChatActivity;
import com.example.doanmobile.dangsanpham.ProductAdapter;
import com.example.doanmobile.dangsanpham.Products;
import com.example.doanmobile.dangsanpham.chitietsanpham;
import com.example.doanmobile.dangsanpham.tranggiaodienbanhang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class cuahangshopdetail extends AppCompatActivity {
    List<Products> productsList;
    ProductAdapter productAdapter;
    RecyclerView sanphamtheoshop;
    CollectionReference productCollection;
    TextView tencuahang, motacuahang, diachicuahang;
    ImageView nhantincuahang,backcuahang;
    private List<Integer> favoriteProducts = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuahangshopdetail);
        // Lấy shopId từ Intent
        int shopId = getIntent().getIntExtra("shopId", 0);
        tencuahang = findViewById(R.id.tencuahangsd);
        motacuahang = findViewById(R.id.motacuahangsd);
        diachicuahang = findViewById(R.id.diachicuhangsd);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Products");

        //lấy tên, mô tả, địa chỉ cửa hàng
        CollectionReference shopCollectionRef = db.collection("Shop");
        Query shopQuery = shopCollectionRef.whereEqualTo("shopId", shopId);

        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot documents : task.getResult()) {
                    if (documents.exists()) {
                        String shopName = documents.getString("shopName");
                        String shopDescription = documents.getString("moTa");
                        String shopAddress = documents.getString("diaChi");
                        tencuahang.setText(shopName);
                        motacuahang.setText(shopDescription);
                        diachicuahang.setText(shopAddress);
                    }
                }
            } else {
                Log.d(MotionEffect.TAG, "Failed with: ", task.getException());
            }
        });

        //nhắn tin
        nhantincuahang=findViewById(R.id.nhantincuahangsd);
        nhantincuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy tên cửa hàng từ TextView
                Intent intent = new Intent(cuahangshopdetail.this, ChatActivity.class);
                intent.putExtra("shopId",shopId);
                startActivity(intent);

            }
        });
        //back
        backcuahang=findViewById(R.id.backcuahang);
        backcuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy tên cửa hàng từ TextView
                Intent intent = new Intent(cuahangshopdetail.this, tranggiaodienbanhang.class);
                startActivity(intent);

            }
        });

        sanphamtheoshop = findViewById(R.id.sanphamtheoshop);
        productsList = new ArrayList<>();
        productAdapter = new ProductAdapter(cuahangshopdetail.this, productsList);
        productAdapter.updateProductFavoriteStatus(favoriteProducts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(cuahangshopdetail.this, 2);
        sanphamtheoshop.setLayoutManager(gridLayoutManager);
        sanphamtheoshop.setAdapter(productAdapter);

        productCollection = FirebaseFirestore.getInstance().collection("Products");
        AlertDialog.Builder builder = new AlertDialog.Builder(cuahangshopdetail.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        loadFavoriteProducts();
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
                    if (products.getShopID() == shopId) {
                        productsList.add(products);
                    }
                }
                productAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });

    }

    private void loadFavoriteProducts() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    int userID = documentSnapshot.getLong("userID").intValue();

                    // Tiếp tục xử lý với userID tại đây...

                    // Lấy reference đến collection "favorites" và thực hiện truy vấn
                    fStore.collection("favorites")
                            .whereEqualTo("userID", userID)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    favoriteProducts.clear();
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        // Lấy productID từ document
                                        int productID = document.getLong("productID").intValue();
                                        favoriteProducts.add(productID);
                                    }

                                    // Cập nhật vào adapter
                                    if (productAdapter != null) {
                                        productAdapter.updateProductFavoriteStatus(favoriteProducts);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    // Xảy ra lỗi khi truy vấn Firestore
                                }
                            });
                }
            }
        });
    }
}