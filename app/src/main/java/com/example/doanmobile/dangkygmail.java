package com.example.doanmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class dangkygmail extends AppCompatActivity {

    EditText Tendaydugmail,Sodienthoaigmail;
    ImageButton BtnDangKiTaiKhoangmail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkygmail);
        Tendaydugmail = findViewById(R.id.Tendaydugmail);
        Sodienthoaigmail = findViewById(R.id.Sodienthoaigmail);
        BtnDangKiTaiKhoangmail = findViewById(R.id.BtnDangKiTaiKhoangmail);
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        BtnDangKiTaiKhoangmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Tendaydugmail.getText().toString();
                String phone = Sodienthoaigmail.getText().toString();

                if (!TextUtils.isEmpty(name)  && !TextUtils.isEmpty(phone))
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null)
                    {
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("tenDayDu", name);
                        userInfo.put("soDienThoai", phone);
                        fStore.collection("KhachHang").document(currentUser.getUid())
                                .set(userInfo)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(dangkygmail.this, "Thông tin đã được lưu vào Firestore", Toast.LENGTH_SHORT).show();
                                        // (Optional) Nếu cần, thực hiện các bước tiếp theo sau khi lưu thông tin
                                        Intent intent = new Intent(dangkygmail.this,dangnhap.class);
                                        startActivity(intent);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(dangkygmail.this, "Lỗi khi lưu thông tin vào Firestore", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(dangkygmail.this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(dangkygmail.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}