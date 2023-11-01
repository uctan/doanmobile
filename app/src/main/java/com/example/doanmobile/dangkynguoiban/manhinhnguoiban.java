package com.example.doanmobile.dangkynguoiban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.doanmobile.R;
import com.example.doanmobile.chat.NguoidungvoiShopActivity;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manhinhnguoiban);
        tencuahangshop = findViewById(R.id.tencuahangshop);
        quanlysanphamcuahang = findViewById(R.id.quanlysanphamcuahang);
       nhantinvoikhachhang=findViewById(R.id.nhantinvoikhachhang);

       nhantinvoikhachhang.setOnClickListener(new View.OnClickListener(){

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

        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String tenDayDu = documentSnapshot.getString("shopName");
                        tencuahangshop.setText(tenDayDu); // Hiển thị lên TextView
                    }
                }
            });
        }
    }
}