package com.example.doanmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.dangsanpham.tranggiaodienbanhang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class trangchunguoidung extends AppCompatActivity {

    TextView tennguoidungtrangchu;
    ImageView profile,bansanphamtrangchu;
    //congkhanh
//thanhsy
    //thong

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchunguoidung);
        //chuyensangtrangbanhang
        bansanphamtrangchu = findViewById(R.id.bansanphamtrangchu);
        bansanphamtrangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trangchunguoidung.this, tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });

        // lấy tên khách hàng vào trang chủ để hiện tên
        tennguoidungtrangchu = findViewById(R.id.tennguoidungtrangchu);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullName = documentSnapshot.getString("tenDayDu");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tennguoidungtrangchu.setText(fullName);
                    }
                });
            }
        });

        //chuyen sang trang profile
        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trangchunguoidung.this, profileuser.class);
                startActivity(intent);
            }
        });

    }
}