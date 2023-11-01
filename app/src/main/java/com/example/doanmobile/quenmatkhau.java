package com.example.doanmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class quenmatkhau extends AppCompatActivity {
    ImageView backquenmatkhau;
    View btnSendResetLink;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    EditText Emailquenmatkhau;
    String email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhau);


        Emailquenmatkhau = findViewById(R.id.Emailquenmatkhau);
        backquenmatkhau = findViewById(R.id.backquenmatkhau);
        backquenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quenmatkhau.this,dangnhap.class);
                startActivity(intent);
            }
        });
        btnSendResetLink = findViewById(R.id.btnSendResetLink);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        btnSendResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Emailquenmatkhau.getText().toString().trim();
                if (!TextUtils.isEmpty(email)) {
                    ResetPassword();
                } else {
                    Emailquenmatkhau.setError("Email không có trong tài khoản");
                }
            }
        });
    }

    private void ResetPassword(){
        email = Emailquenmatkhau.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(quenmatkhau.this,"Reset mật khẩu",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(quenmatkhau.this,dangnhap.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(quenmatkhau.this,"Bị lỗi",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}