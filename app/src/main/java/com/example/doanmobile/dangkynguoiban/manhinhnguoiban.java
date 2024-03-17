package com.example.doanmobile.dangkynguoiban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.DanhGiaSPcuaShop.XemDanhGiaCuaShop;
import com.example.doanmobile.R;
import com.example.doanmobile.chat.NguoidungvoiShopActivity;
import com.example.doanmobile.dangkynguoibanvip.dangkynguoibanvip;
import com.example.doanmobile.dangnhap;
import com.example.doanmobile.hoadonnguoiban.hoadonnguoiban;
import com.example.doanmobile.livestream.livestream;
import com.example.doanmobile.thongke.thongkesanpham;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class manhinhnguoiban extends AppCompatActivity {

    TextView tencuahangshop;
    FirebaseFirestore db;
    View quanlysanphamcuahang;
    View nhantinvoikhachhang;
    View quanlyhoadoncuahang;
    View dangxuatnguoiban,Xemdanhgia,thongkechart;
    View livestream;
    View dangkynguoibanvip;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhnguoiban);
        tencuahangshop = findViewById(R.id.tencuahangshop);
        quanlysanphamcuahang = findViewById(R.id.quanlysanphamcuahang);
        nhantinvoikhachhang = findViewById(R.id.nhantinvoikhachhang);
        quanlyhoadoncuahang = findViewById(R.id.quanlyhoadoncuahang);
        Xemdanhgia = findViewById(R.id.Xemdanhgia);
        livestream = findViewById(R.id.livestream);
        dangkynguoibanvip = findViewById(R.id.dangkynguoibanvip);
        thongkechart = findViewById(R.id.thongkechart);
        thongkechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this, thongkesanpham.class);
                startActivity(intent);
            }
        });
        livestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this,com.example.doanmobile.livestream.livestream.class);
                startActivity(intent);
            }
        });
        dangkynguoibanvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this,com.example.doanmobile.dangkynguoibanvip.dangkynguoibanvip.class);
                startActivity(intent);
            }
        });

        Xemdanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this, XemDanhGiaCuaShop.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String userId = user1.getUid();
            DocumentReference userRef = db.collection("KhachHang").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        boolean isNguoiBan = documentSnapshot.getBoolean("nguoiBan");

                        // Nếu là người bán, hiển thị View dangkynguoibanvip và ẩn View livestream
                        if (isNguoiBan) {
                            dangkynguoibanvip.setVisibility(View.VISIBLE);
                            livestream.setVisibility(View.GONE);
                        } else {
                            livestream.setVisibility(View.VISIBLE);
                            dangkynguoibanvip.setVisibility(View.GONE);
                        }

                        // Chuyển giá trị isNguoiBan thành chuỗi và in vào log
                        Log.d("nguoiban", String.valueOf(isNguoiBan));
                    }
                }
            });
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String tenDayDu = documentSnapshot.getString("shopName");
                        int shopID = documentSnapshot.getLong("shopId").intValue();
                        tencuahangshop.setText(tenDayDu); // Hiển thị lên TextView
                        quanlyhoadoncuahang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manhinhnguoiban.this, hoadonnguoiban.class);
                                intent.putExtra("shopID", shopID);
                                startActivity(intent);
                            }
                        });
                    }
                }

            });

        }
        nhantinvoikhachhang.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this, NguoidungvoiShopActivity.class);
                startActivity(intent);
            }
        });
        quanlysanphamcuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manhinhnguoiban.this, quanlysanphamthemsanpham.class);
                startActivity(intent);
            }
        });
        //Đăng xuất tài khoản
        dangxuatnguoiban = findViewById(R.id.dangxuatnguoiban);
        dangxuatnguoiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), dangnhap.class));
                finish();
            }
        });
    }
}