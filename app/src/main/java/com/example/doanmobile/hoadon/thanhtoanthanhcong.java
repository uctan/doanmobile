package com.example.doanmobile.hoadon;

import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.example.doanmobile.danhgiasanpham.Review;
import com.example.doanmobile.danhgiasanpham.ReviewFacade;
import com.example.doanmobile.trangchunguoidung;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class thanhtoanthanhcong extends AppCompatActivity {

    RatingBar ratingBar;
    EditText delaidanhgia;
    View vetrangchudanhgia, luudanhgianha;
    float myRating = 0;
    int productID;
    double   totalStars,reviewCount;
    private ReviewFacade reviewFacade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanhtoanthanhcong);

        ratingBar = findViewById(R.id.ratingBar);
        delaidanhgia = findViewById(R.id.delaidanhgia);
        vetrangchudanhgia = findViewById(R.id.vetrangchudanhgia);
        luudanhgianha = findViewById(R.id.luudanhgianha);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        reviewFacade = new ReviewFacade(this);
        // Quay trở về trang chủ
        vetrangchudanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thanhtoanthanhcong.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });

        // Đánh giá số sao
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRating = v;
                Toast.makeText(thanhtoanthanhcong.this, "Bạn đã chọn số sao: " + myRating, Toast.LENGTH_SHORT).show();
            }
        });
        luudanhgianha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tinnhan = delaidanhgia.getText().toString();
                float rating = myRating;
                Intent intent = getIntent();
                if (intent != null) {
                    int detailId = intent.getIntExtra("detailId", -1);
                    if (detailId != -1) {
                        fStore.collection("order_detail")
                                .whereEqualTo("detailID", detailId)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            int productID = document.getLong("productID").intValue();
                                            reviewFacade.luuDanhGia(productID, rating, tinnhan, detailId);
                                        }
                                    } else {
                                        Log.d("TAG", "Không tìm thấy dữ liệu cho detailId = " + detailId);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.d("TAG", "Lỗi khi truy vấn order_detail: " + e.getMessage());
                                });
                    } else {
                        Log.d("TAG", "Không có detailId được truyền từ Intent");
                    }
                }
            }
        });
    }
}
        // Lưu vào Firestore
//        luudanhgianha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userId = user.getUid();
//                DocumentReference userRef = fStore.collection("KhachHang").document(userId);
//                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        if (documentSnapshot.exists()) {
//                            if (documentSnapshot.contains("userID")) {
//                                long userID = documentSnapshot.getLong("userID");
//                                int intUserID = (int) userID;
//                                float rating = myRating;
//                                Intent intent = getIntent();
//                                if (intent != null) {
//                                    int detailId = intent.getIntExtra("detailId", -1); // Nhận detailId
//                                    if (detailId != -1) {
//                                        // Truy vấn bảng order_detail với điều kiện detailId và orderID
//                                        fStore.collection("order_detail")
//                                                .whereEqualTo("detailID", detailId)
//                                                .get()
//                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                    @Override
//                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                        if (!queryDocumentSnapshots.isEmpty()) {
//                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                                                String tinnhan = delaidanhgia.getText().toString();
//                                                                int productID = document.getLong("productID").intValue();
//                                                                int orderID = document.getLong("orderID").intValue();
//
//                                                                // Tạo review mới
//                                                                fStore.collection("Reviews")
//                                                                        .orderBy("reviewID", Query.Direction.DESCENDING)
//                                                                        .limit(1)
//                                                                        .get()
//                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                                            @Override
//                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                                                int newreviewID = 1;
//                                                                                if (!queryDocumentSnapshots.isEmpty()) {
//                                                                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                                                                        int highestreviewID = document.getLong("reviewID").intValue();
//                                                                                        newreviewID = highestreviewID + 1;
//                                                                                    }
//                                                                                }
//                                                                                Review review = new Review(newreviewID, productID, intUserID, rating, tinnhan);
//
//                                                                                // Lưu review vào Firestore
//                                                                                fStore.collection("Reviews")
//                                                                                        .add(review)
//                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                                                                            @SuppressLint("RestrictedApi")
//                                                                                            @Override
//                                                                                            public void onSuccess(DocumentReference documentReference) {
//                                                                                                CollectionReference reviewsCollection = fStore.collection("Reviews");
//                                                                                                Query reviewQuery = reviewsCollection.whereEqualTo("productID", productID);
//
//                                                                                                // Reset totalStars và reviewCount trước mỗi lần tính toán
//                                                                                                totalStars = 0.0;
//                                                                                                reviewCount = 0.0;
//
//                                                                                                reviewQuery.get().addOnCompleteListener(task -> {
//                                                                                                    if (task.isSuccessful()) {
//                                                                                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                                                                                            // Lấy số sao từ mỗi đánh giá
//                                                                                                            double stars = document.getDouble("rating");
//                                                                                                            // Tính tổng số sao
//                                                                                                            totalStars += stars;
//                                                                                                            // Tăng số lượng đánh giá
//                                                                                                            reviewCount++;
//                                                                                                        }
//                                                                                                        if (reviewCount > 0) {
//                                                                                                            double averageRating = totalStars / reviewCount;
//                                                                                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                                                                                                            CollectionReference productsCollection = db.collection("Products");
//                                                                                                            Query query = productsCollection.whereEqualTo("productID", productID);
//                                                                                                            query.get().addOnCompleteListener(task1 -> {
//                                                                                                                if (task1.isSuccessful()) {
//                                                                                                                    for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
//                                                                                                                        // Cập nhật trường trungbinhdanhgia trong tài liệu sản phẩm
//                                                                                                                        documentSnapshot.getReference().update("reviewcount", averageRating)
//                                                                                                                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật trungbinhdanhgia thành công"))
//                                                                                                                                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật trungbinhdanhgia", e));
//                                                                                                                        Intent intent1 = new Intent(thanhtoanthanhcong.this, trangchunguoidung.class);
//                                                                                                                        startActivity(intent1);
//                                                                                                                    }
//                                                                                                                } else {
//                                                                                                                    Log.e(TAG, "Error getting product document", task1.getException());
//                                                                                                                }
//                                                                                                            });
//                                                                                                        }
//                                                                                                    }
//                                                                                                });
//
//                                                                                                Toast.makeText(thanhtoanthanhcong.this, "Đánh giá đã được lưu thành công!", Toast.LENGTH_SHORT).show();
//                                                                                                Intent intent1 = new Intent(thanhtoanthanhcong.this, trangchunguoidung.class);
//                                                                                                startActivity(intent1);
//                                                                                            }
//                                                                                        })
//                                                                                        .addOnFailureListener(new OnFailureListener() {
//                                                                                            @Override
//                                                                                            public void onFailure(@NonNull Exception e) {
//                                                                                                Log.w("TAG", "Lỗi khi lưu đánh giá vào Firestore", e);
//                                                                                                Toast.makeText(thanhtoanthanhcong.this, "Có lỗi xảy ra khi lưu đánh giá. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
//                                                                                            }
//                                                                                        });
//                                                                            }
//                                                                        });
//                                                            }
//                                                        } else {
//                                                            Log.d("TAG", "Không tìm thấy dữ liệu cho detailId = " + detailId);
//                                                        }
//                                                    }
//                                                })
//                                                .addOnFailureListener(new OnFailureListener() {
//                                                    @Override
//                                                    public void onFailure(@NonNull Exception e) {
//                                                        Log.d("TAG", "Lỗi khi truy vấn order_detail: " + e.getMessage());
//                                                    }
//                                                });
//                                    } else {
//                                        Log.d("TAG", "Không có detailId được truyền từ Intent");
//                                    }
//                                }
//                            } else {
//                                // Xử lý khi không tìm thấy userID trong tài liệu KhachHang
//                            }
//                        } else {
//                            // Xử lý khi tài liệu không tồn tại
//                        }
//                    }
//                });
//            }
//        });
