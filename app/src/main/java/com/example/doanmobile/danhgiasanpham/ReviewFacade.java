package com.example.doanmobile.danhgiasanpham;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.doanmobile.hoadon.thanhtoanthanhcong;
import com.example.doanmobile.trangchunguoidung;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ReviewFacade {
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private Context context;
    double totalStars = 0.0;
    double reviewCount = 0.0;

    public ReviewFacade(Context context) {
        this.context = context;
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    public void luuDanhGia(int productID, float rating, String tinnhan, int detailId) {
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = fStore.collection("KhachHang").document(userId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("userID")) {
                        long userID = documentSnapshot.getLong("userID");
                        int intUserID = (int) userID;

                        // Tạo đánh giá mới
                        createNewReview(productID, intUserID, rating, tinnhan);
                    }
                }
            });
        }
    }

    private void createNewReview(int productID, int intUserID, float rating, String tinnhan) {
        fStore.collection("Reviews")
                .orderBy("reviewID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int newreviewID = 1;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            int highestreviewID = document.getLong("reviewID").intValue();
                            newreviewID = highestreviewID + 1;
                        }
                    }

                    Review review = new Review(newreviewID, productID, intUserID, rating, tinnhan);

                    // Lưu đánh giá vào Firestore
                    fStore.collection("Reviews")
                            .add(review)
                            .addOnSuccessListener(documentReference -> {
                                // Sau khi đánh giá được lưu thành công, cập nhật trung bình đánh giá của sản phẩm
                                updateProductRating(productID);
                            })
                            .addOnFailureListener(e -> {
                                Log.e(TAG, "Lỗi khi lưu đánh giá vào Firestore", e);
                                Toast.makeText(context, "Có lỗi xảy ra khi lưu đánh giá. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Lỗi khi truy vấn reviewID cao nhất", e);
                    Toast.makeText(context, "Có lỗi xảy ra khi lưu đánh giá. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                });
    }


    private void updateProductRating(int productID) {
        // Gọi phương thức cập nhật trung bình đánh giá của sản phẩm ở đây
        // Tính toán tổng số sao, số lượng đánh giá và cập nhật lại trung bình đánh giá của sản phẩm
        CollectionReference reviewsCollection = fStore.collection("Reviews");
        Query reviewQuery = reviewsCollection.whereEqualTo("productID", productID);

        // Reset totalStars và reviewCount trước mỗi lần tính toán
        totalStars = 0.0;
        reviewCount = 0.0;

        // Truy vấn Firestore để lấy danh sách đánh giá
        reviewQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Lấy số sao từ mỗi đánh giá
                    double stars = document.getDouble("rating");
                    // Tính tổng số sao
                    totalStars += stars;
                    // Tăng số lượng đánh giá
                    reviewCount++;
                }

                // Nếu có ít nhất một đánh giá, tính trung bình đánh giá và cập nhật vào TextView
                if (reviewCount > 0) {
                    double averageRating = totalStars / reviewCount;

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference productsCollection = db.collection("Products");

                    // Tìm các tài liệu thỏa mãn điều kiện productID
                    Query query = productsCollection.whereEqualTo("productID", productID);

                    query.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task1.getResult()) {
                                // Cập nhật trường trungbinhdanhgia trong tài liệu sản phẩm
                                documentSnapshot.getReference().update("reviewcount", averageRating)
                                        .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật trungbinhdanhgia thành công"))
                                        .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật trungbinhdanhgia", e));
                            }
                        } else {
                            Log.e(TAG, "Error getting product document", task1.getException());
                        }
                    });


                } else {
                    // Nếu không có đánh giá nào, hiển thị trạng thái không có đánh giá

                }
            } else {
                Log.e(TAG, "Error getting reviews", task.getException());
                // Nếu có lỗi, hiển thị trạng thái không có đánh giá

            }
        });
        Toast.makeText(context, "Đánh giá đã được lưu thành công!", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(context, trangchunguoidung.class);
        context.startActivity(intent1);
    }
}
