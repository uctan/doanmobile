package com.example.doanmobile.dangsanpham;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.Shop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class chitietsanpham extends AppCompatActivity {
    TextView detailtensp,detailgia,detailmotasp,detailsoluong,tinhtiengiohangdetail,detailtencuahang;
    View detailtru,detailcong;
    ImageView detailanh,backnguoibanchitiet;
    //Thêm hoặc giảm số lượng sản phẩm
    double giacade ;
    int soLuong = 1;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        detailtensp = findViewById(R.id.detailtensp);
        detailgia = findViewById(R.id.detailgia);
        detailmotasp = findViewById(R.id.detailmotasp);
        detailsoluong = findViewById(R.id.detailsoluong);
        detailtru = findViewById(R.id.detailtru);
        detailcong = findViewById(R.id.detailcong);
        detailanh = findViewById(R.id.detailanh);
        tinhtiengiohangdetail = findViewById(R.id.tinhtiengiohangdetail);
        detailtencuahang = findViewById(R.id.detailtencuahang);

        db = FirebaseFirestore.getInstance();

        //quavetrangnguoiban
        backnguoibanchitiet = findViewById(R.id.backnguoibanchitiet);
        backnguoibanchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chitietsanpham.this,tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });

        //hieện chi tiết sản phẩm
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                int shopId = bundle.getInt("shopId", 0);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference shopRef = db.collection("Shop").document(String.valueOf(shopId));

               shopRef.get().addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document.exists()) {
                           String shopName = document.getString("shopName");
                           detailtencuahang.setText(shopName);
                       }
                   }
               });


                detailtensp.setText(bundle.getString("Title"));
                detailmotasp.setText(bundle.getString("mota"));

                Object giacaObject = bundle.get("Giaca");
                if (giacaObject != null) {
                    if (giacaObject instanceof Double) {
                        double giaca = (Double) giacaObject;
                        detailgia.setText(String.valueOf(giaca));
                    } else if (giacaObject instanceof String) {
                        try {
                            double giaca = Double.parseDouble((String) giacaObject);
                            detailgia.setText(String.valueOf(giaca));
                        } catch (NumberFormatException e) {
                            // Handle the case where parsing to double fails
                        }
                    }
                }

                Glide.with(this).load(bundle.getString("Image")).into(detailanh);

            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }

        //giảm số lượng sản phẩm
        detailtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong--;
                if (soLuong < 1) {
                    soLuong = 1;
                }
                detailsoluong.setText(String.valueOf(soLuong));

                // Lấy giá từ Bundle
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Object giacaObject = bundle.get("Giaca");

                    if (giacaObject instanceof String) {
                        String giacaString = (String) giacaObject;
                        double giaca = Double.parseDouble(giacaString.trim());
                        giacade = soLuong * giaca;
                        tinhtiengiohangdetail.setText(String.valueOf(giacade));
                    } else if (giacaObject instanceof Double) {
                        Double giacaDouble = (Double) giacaObject;
                        giacade = soLuong * giacaDouble;
                        tinhtiengiohangdetail.setText(String.valueOf(giacade));
                    }
                }
            }
        });

// Cộng số lượng sản phẩm
        detailcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong++;
                detailsoluong.setText(String.valueOf(soLuong));

                // Lấy giá từ Bundle
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    Object giacaObject = bundle.get("Giaca");

                    if (giacaObject instanceof String) {
                        String giacaString = (String) giacaObject;
                        double giaca = Double.parseDouble(giacaString.trim());
                        giacade = soLuong * giaca;
                        tinhtiengiohangdetail.setText(String.valueOf(giacade));
                    } else if (giacaObject instanceof Double) {
                        Double giacaDouble = (Double) giacaObject;
                        giacade = soLuong * giacaDouble;
                        tinhtiengiohangdetail.setText(String.valueOf(giacade));
                    }
                }
            }
        });

        //laytenshop
        int shopId = bundle.getInt("shopId", 0);

        CollectionReference shopCollectionRef = db.collection("Shop");
        Query shopQuery = shopCollectionRef.whereEqualTo("shopId", shopId);

        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        String shopName = doc.getString("shopName");
                        // Lấy dữ liệu từ doc
                        detailtencuahang.setText(shopName);
                    }
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });


    }

}