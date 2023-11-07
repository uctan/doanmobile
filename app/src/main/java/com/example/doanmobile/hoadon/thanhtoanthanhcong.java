package com.example.doanmobile.hoadon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.doanmobile.trangchunguoidung;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class thanhtoanthanhcong extends AppCompatActivity {

    RatingBar  ratingBar;
    EditText  delaidanhgia;
    View  vetrangchudanhgia,luudanhgianha;
    float myRating = 0;
    int productID;

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

        //quay tre ve trang chu
        vetrangchudanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thanhtoanthanhcong.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });
        //danh gia so sao
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                myRating = v;
                Toast.makeText(thanhtoanthanhcong.this, "lựa chọn số sao " + myRating, Toast.LENGTH_SHORT).show();
            }
        });

        //luuvao firestore
        Intent intent = getIntent();
        if (intent != null) {
            int orderId = intent.getIntExtra("orderId", -1); // Nhận orderId
            int detailId = intent.getIntExtra("detailId", -1); // Nhận detailId
            if (detailId != -1) {
                // Truy vấn bảng order_detail với điều kiện detailId và orderID
                fStore.collection("order_detail")
                        .whereEqualTo("detailID", detailId)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                if (!queryDocumentSnapshots.isEmpty()) {

                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        int productID = document.getLong("productID").intValue();
                                        int orderID = document.getLong("orderID").intValue();

                                    }
                                } else {

                                    Log.d("TAG", "Không tìm thấy dữ liệu cho detailId = " + detailId);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d("TAG", "Lỗi khi truy vấn order_detail: " + e.getMessage());
                            }
                        });
            } else {
                Log.d("TAG", "Không có detailId được truyền từ Intent");
            }
        }



        luudanhgianha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = user.getUid();
                DocumentReference userRef = fStore.collection("KhachHang").document(userId);
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.contains("userID")) {
                                long userID = documentSnapshot.getLong("userID");
                                int intUserID = (int) userID;
                                float rating = myRating;
                                Intent intent = getIntent();
                                if (intent != null) {
                                    int orderId = intent.getIntExtra("orderId", -1); // Nhận orderId

                                    int detailId = intent.getIntExtra("detailId", -1); // Nhận detailId
                                    if (detailId != -1) {
                                        // Truy vấn bảng order_detail với điều kiện detailId và orderID
                                        fStore.collection("order_detail")
                                                .whereEqualTo("detailID", detailId)

                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        if (!queryDocumentSnapshots.isEmpty()) {

                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                String tinnhan = delaidanhgia.getText().toString();
                                                                int productID = document.getLong("productID").intValue();
                                                                int orderID = document.getLong("orderID").intValue();
                                                                Review review = new Review(0, productID, intUserID, rating, tinnhan);

                                                                fStore.collection("Reviews")
                                                                        .add(review)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                Toast.makeText(thanhtoanthanhcong.this, "Đánh giá đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                                                                                Intent intent1 = new Intent(thanhtoanthanhcong.this,trangchunguoidung.class);
                                                                                startActivity(intent1);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w("TAG", "Lỗi khi lưu đánh giá vào Firestore", e);
                                                                                Toast.makeText(thanhtoanthanhcong.this, "Có lỗi xảy ra khi lưu đánh giá. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        } else {

                                                            Log.d("TAG", "Không tìm thấy dữ liệu cho detailId = " + detailId);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        Log.d("TAG", "Lỗi khi truy vấn order_detail: " + e.getMessage());
                                                    }
                                                });
                                    } else {
                                        Log.d("TAG", "Không có detailId được truyền từ Intent");
                                    }
                                }


                                // Sử dụng biến productID đã được lấy từ truy vấn Firestore

                            } else {
                            }
                        } else {
                        }
                    }
                });
            }
        });
    }
}