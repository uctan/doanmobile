package com.example.doanmobile.dangkynguoiban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanmobile.KhachHang;
import com.example.doanmobile.R;
import com.example.doanmobile.profileuser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

public class dangkylenguoiban extends AppCompatActivity {
    ImageView dknguoiban, dongdangkyshop;
    EditText nhaptencuahang, nhaptendiachi, nhapmotacuahang;
    CheckBox nguoibanthuong, nguoibanvip;
    KhachHang khachHang;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkylenguoiban);
        khachHang = new KhachHang(); // Khởi tạo đối tượng KhachHang
        nguoibanthuong = findViewById(R.id.nguoibanthuong);
        nguoibanvip = findViewById(R.id.nguoibanvip);
        nguoibanthuong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    nguoibanvip.setChecked(false);
                    khachHang.setKhachHang(false);
                    khachHang.setNguoiBan(true);
                    khachHang.setNguoiBanVip(false);
                }
            }
        });
        nguoibanvip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                nguoibanthuong.setChecked(false);
                khachHang.setKhachHang(false);
                khachHang.setNguoiBan(false);
                khachHang.setNguoiBanVip(true);
            }
        });

        //đóng đăng ký shop
        dongdangkyshop = findViewById(R.id.dongdangkyshop);
        dongdangkyshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangkylenguoiban.this, profileuser.class);
                startActivity(intent);
            }
        });
        //đăng ký lên người bán
        dknguoiban = findViewById(R.id.dknguoiban);
        nhapmotacuahang = findViewById(R.id.nhapmotacuahang);
        nhaptencuahang = findViewById(R.id.nhaptencuahang);
        nhaptendiachi = findViewById(R.id.nhaptendiachi);
        nguoibanthuong = findViewById(R.id.nguoibanthuong);
        nguoibanvip = findViewById(R.id.nguoibanvip);

        //phần nhập dữ liệu
        dknguoiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tencuahang = nhaptencuahang.getText().toString().trim();
                String diachicuahang = nhaptendiachi.getText().toString().trim();
                String motacuahang = nhapmotacuahang.getText().toString().trim();

                boolean isNguoiBan = nguoibanthuong.isChecked();
                boolean isNguoiBanVip = nguoibanvip.isChecked();

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();



                if (user != null) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("KhachHang")
                            .document(user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {

                                        KhachHang khachHang = documentSnapshot.toObject(KhachHang.class);



                                        int userId = documentSnapshot.getLong("userID").intValue();
                                        Shop newShop = new Shop();
                                        newShop.setShopName(tencuahang);
                                        newShop.setDiaChi(diachicuahang);
                                        newShop.setMoTa(motacuahang);
                                        newShop.setUserId(userId);

                                        db.collection("Shop")
                                                .orderBy("shopId", Query.Direction.DESCENDING)
                                                .limit(1)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        int newshopId = 1;
                                                        if (!queryDocumentSnapshots.isEmpty()) {
                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                int highestShopId = document.getLong("shopId").intValue();
                                                                newshopId = highestShopId + 1;
                                                            }
                                                        }


                                                        newShop.setShopId(newshopId);
                                                        String documentId = user.getUid();

                                                        DocumentReference shopRef = db.collection("Shop").document(documentId);

                                                        shopRef.set(newShop);

                                                        db.collection("Shop")
                                                                .add(newShop)

                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        khachHang.setNguoiBan(true);
                                                                        khachHang.setKhachHang(false);

                                                                        db.collection("KhachHang")
                                                                                .document(user.getUid())
                                                                                .set(khachHang);

                                                                        Toast.makeText(dangkylenguoiban.this, "Đăng ký người bán thành công", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(dangkylenguoiban.this, dangkythanhcongthuong.class);
                                                                        startActivity(intent);
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
}