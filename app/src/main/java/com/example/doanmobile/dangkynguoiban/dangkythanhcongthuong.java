package com.example.doanmobile.dangkynguoiban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dangkythanhcongthuong extends AppCompatActivity {

    TextView tennguoidangkythanhcong;
    ImageView quayvethuong;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkythanhcongthuong);
        quayvethuong = findViewById(R.id.quayvethuong);
        tennguoidangkythanhcong = findViewById(R.id.tennguoidangkythanhcong);
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        quayvethuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangkythanhcongthuong.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });

        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        String tenDayDu = documentSnapshot.getString("shopName");
                        tennguoidangkythanhcong.setText(tenDayDu); // Hiển thị lên TextView
                    }
                }
            });
        }
    }
}