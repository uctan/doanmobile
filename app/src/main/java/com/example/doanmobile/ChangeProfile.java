package com.example.doanmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChangeProfile extends AppCompatActivity {
    EditText edtHoten,edtEmail,edtpass,edtsdt;
    Button btnChangeProfile;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestoreSingleton.getInstance(); // Sử dụng singleton

    ImageView btnBackprofile;

    FirebaseUser user = fAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        edtHoten=findViewById(R.id.edtTen);
        edtEmail=findViewById(R.id.edtEmail);
        edtpass=findViewById(R.id.edtPass);
        edtsdt=findViewById(R.id.edtphone);
        btnBackprofile=findViewById(R.id.btnBack);
        btnChangeProfile=findViewById(R.id.btnChangeProfile);


        String userId = user.getUid();

        btnBackprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ChangeProfile.this,Profile.class);
                startActivity(intent);
            }
        });

        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new  OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullName = documentSnapshot.getString("tenDayDu");
                String gmail = documentSnapshot.getString("email");
                String sodienthoai= documentSnapshot.getString("soDienThoai");
                String password= documentSnapshot.getString("matKhau");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        edtHoten.setText(fullName);
                        edtEmail.setText(gmail);
                        edtpass.setText(password);
                        edtsdt.setText(sodienthoai);
                    }
                });
            }
        });
        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String newname=edtHoten.getText().toString().trim();
                String soDienThoai = edtsdt.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String matKhau= edtpass.getText().toString().trim();
                updateProfile(newname,soDienThoai,email,matKhau);
            }
        });

    }
    // sua profile
    private void updateProfile(String newname,String sdt,String email,String pass) {
        Map<String,Object>map=new HashMap<>();
        map.put("tenDayDu",newname);
        map.put("email",email);
        map.put("soDienThoai",sdt);
        map.put("matKhau",pass);
        // khachHang.setEmail(email);
        // khachHang.setMatKhau(pass);
        fStore.collection("KhachHang")
                .document(user.getUid())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Intent intent = new Intent(ChangeProfile.this, profileuser.class);
                        startActivity(intent);
                    }
                });
    }
}