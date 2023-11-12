package com.example.doanmobile.DanhGiaSPcuaShop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.Shop;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.example.doanmobile.dangsanpham.ItemSpacingDecoration;
import com.example.doanmobile.dangsanpham.Products;
import com.example.doanmobile.danhgiasanpham.ReViewAdapter;
import com.example.doanmobile.danhgiasanpham.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class XemDanhGiaCuaShop extends AppCompatActivity {
    RecyclerView RVReviewShop;
    ReViewAdapter reViewAdapter;
    List<Review> reviewList;
    ImageButton closeReviewShop;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_danh_gia_cua_shop);
        RVReviewShop=findViewById(R.id.RVreviewshop);
        closeReviewShop=findViewById(R.id.closereviewshop);
        closeReviewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(XemDanhGiaCuaShop.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });
        reviewList = new ArrayList<>();
        reViewAdapter = new ReViewAdapter(this, reviewList);
        RVReviewShop.setLayoutManager(new LinearLayoutManager(this));
        RVReviewShop.addItemDecoration(new ItemSpacingDecoration(16));
        RVReviewShop.setAdapter(reViewAdapter);
        loadReviewShop();
    }

    private void loadReviewShop() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        DocumentReference userRef = fStore.collection("Shop").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long shopID = documentSnapshot.getLong("shopId");
                    fStore.collection("Products")
                            .whereEqualTo("shopID", shopID)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        reviewList.clear();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            int productId = document.getLong("productID").intValue();
                                            loadReviewsForProduct(productId);
                                        }
                                        reViewAdapter.notifyDataSetChanged();
                                    } else {
                                        // Xử lý trường hợp không thành công khi lấy danh sách sản phẩm
                                    }
                                }
                            });
                } else {
                    // Xử lý trường hợp không tồn tại dữ liệu cho người bán này
                }
            }
        });
    }

    private void loadReviewsForProduct(int productId) {
        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
        fstore.collection("Reviews")
                .whereEqualTo("productID", productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Review review = document.toObject(Review.class);
                                reviewList.add(review);
                            }
                            reViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}