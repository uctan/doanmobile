package com.example.doanmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doanmobile.chat.ChatHistoryActivity;
import com.example.doanmobile.dangkynguoiban.dangkylenguoiban;
import com.example.doanmobile.danhgiasanpham.trangxemdanhgianguoidung;
import com.example.doanmobile.hoadon.xemhoadonuser;
import com.example.doanmobile.yeuthichsanpham.yeuthichsanpham;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class profileuser extends AppCompatActivity {

    TextView tendayduprofile,tengmailprofile;
    ImageView backprofile,dangxuatpf;

    View dkyngbanpf,nhantinvoishoppf,thaydoithongtinuser,xemdanhgianguoidungpf,xemhoadonpf,xemyeuthichsanphampf;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileuser);
        xemdanhgianguoidungpf = findViewById(R.id.xemdanhgianguoidungpf);
        xemhoadonpf = findViewById(R.id.xemhoadonpf);
        xemyeuthichsanphampf = findViewById(R.id.xemyeuthichsanphampf);
        xemyeuthichsanphampf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, yeuthichsanpham.class);
                startActivity(intent);
            }
        });
        xemhoadonpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, xemhoadonuser.class);
                startActivity(intent);
            }
        });

        //Cập nhật tên người dùng gmail tenday du len user
        tendayduprofile = findViewById(R.id.tendayduprofile);
        tengmailprofile = findViewById(R.id.tengmailprofile);
        thaydoithongtinuser = findViewById(R.id.thaydoithongtinuser);
        thaydoithongtinuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, Profile.class);

                startActivity(intent);
            }
        });
        xemdanhgianguoidungpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, trangxemdanhgianguoidung.class);
                startActivity(intent);
            }
        });
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullName = documentSnapshot.getString("tenDayDu");
                String gmail = documentSnapshot.getString("email");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tendayduprofile.setText(fullName);
                        tengmailprofile.setText(gmail);
                    }
                });
            }
        });
        //Chuyen sang trang chu nguoidung
        backprofile = findViewById(R.id.backprofile);
        backprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });
        //Đăng xuất tài khoản
        dangxuatpf = findViewById(R.id.dangxuatpf);
        dangxuatpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), dangnhap.class));
                finish();
            }
        });
        //Dang ky len nguoi ban
        dkyngbanpf = findViewById(R.id.dkyngbanpf);
        dkyngbanpf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileuser.this, dangkylenguoiban.class);
                startActivity(intent);
            }
        });
nhantinvoishoppf=findViewById(R.id.nhantinvoishoppf);
nhantinvoishoppf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(profileuser.this, ChatHistoryActivity.class);
        startActivity(intent);
    }
});
    }
}