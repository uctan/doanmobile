package com.example.doanmobile.livestream;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.doanmobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;
import java.util.UUID;

public class livestream extends AppCompatActivity {

    Button btnStartNewLive;
    TextInputEditText edtLiveId,edtName;

    String liveId,name,userID;
    FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestream);

        btnStartNewLive = findViewById(R.id.btnStartLive);
        edtLiveId = findViewById(R.id.edtLiveId);
        edtName = findViewById(R.id.edtName);
        db = FirebaseFirestore.getInstance();
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String userId = user1.getUid();
            DocumentReference userRef = db.collection("KhachHang").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        boolean isNguoiBan = documentSnapshot.getBoolean("nguoiBanVip");

                        // Nếu là người bán, hiển thị View dangkynguoibanvip và ẩn View livestream
                        if (isNguoiBan) {
                            edtName.setVisibility(View.VISIBLE);
                            edtLiveId.setVisibility(View.GONE);

                        } else {
                           
                        }

                    }
                }
            });
        }
        edtLiveId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                liveId = edtLiveId.getText().toString();
                if(liveId.length()==0){
                    btnStartNewLive.setText("Join Live ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnStartNewLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                liveId = edtLiveId.getText().toString();

                if(name.isEmpty()) {
                    edtName.setError("Name is Required");
                    edtName.requestFocus();
                    return;
                }

                if(liveId.length()>0 && liveId.length()!=5 ){
                    edtLiveId.setError("Invalid Live id");
                    edtLiveId.requestFocus();
                    return;
                }
                startMeeting();
            }
        });

    }
    void  startMeeting(){
       boolean isHost = true;
       if((liveId.length()==5)){
           isHost = false;
       }else {
           liveId = generateLiveID();
       }
        userID = UUID.randomUUID().toString();

       Intent intent = new Intent(livestream.this, LiveActivity.class);
        intent.putExtra("user_id",userID);
        intent.putExtra("name",name);
        intent.putExtra("live_id",liveId);
        intent.putExtra("host",isHost);
        startActivity(intent);
    }
    String generateLiveID() {
        StringBuilder id = new StringBuilder();
        while (id.length()!=5){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return  id.toString();
    }
}