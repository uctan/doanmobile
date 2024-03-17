package com.example.doanmobile.dangkynguoibanvip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.dangkythanhcongthuong;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dangkynguoibanvipthanhcong extends AppCompatActivity {
    TextView tennguoidangkyvipthanhcongnha;
    ImageView quayvevipthanhcongnha;
    FirebaseFirestore db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkynguoibanvipthanhcong);
        tennguoidangkyvipthanhcongnha = findViewById(R.id.tennguoidangkyvipthanhcongnha);
        quayvevipthanhcongnha = findViewById(R.id.quayvevipthanhcongnha);
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
                        tennguoidangkyvipthanhcongnha.setText(tenDayDu); // Hiển thị lên TextView
                    }
                }
            });
        }
        quayvevipthanhcongnha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangkynguoibanvipthanhcong.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });
    }
}