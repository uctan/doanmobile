package com.example.doanmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Profile extends AppCompatActivity {
   TextView Hoten,email,pass,sdt;
    Button ChangeProfile;
    ImageView backProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         backProfile=findViewById(R.id.backProfile);
        Hoten = findViewById(R.id.Hoten);
         email= findViewById(R.id.gmail);
          pass = findViewById(R.id.Password);
           sdt = findViewById(R.id.Phonenum);
           ChangeProfile=findViewById(R.id.Suathongtin);

        FirebaseAuth fAuth=FirebaseAuth.getInstance();

       FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullName = documentSnapshot.getString("tenDayDu");
                String gmail = documentSnapshot.getString("email");
                String sodienthoai= documentSnapshot.getString("soDienThoai");
                String password= documentSnapshot.getString("matKhau");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Hoten.setText(fullName);
                        email.setText(gmail);
                        pass.setText(password);
                        sdt.setText(sodienthoai);
                    }
                });
            }
        }) ;
        ChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(Profile.this, ChangeProfile.class);
                startActivity(intent);
            }
        });

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Profile.this,profileuser.class);
                startActivity(intent);
            }
        });
    }
}