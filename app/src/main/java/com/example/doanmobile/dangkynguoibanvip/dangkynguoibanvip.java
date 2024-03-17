package com.example.doanmobile.dangkynguoibanvip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.KhachHang;
import com.example.doanmobile.R;
import com.example.doanmobile.dangky;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.example.doanmobile.hoadon.thanhtoanthanhcong;
import com.example.doanmobile.hoadon.trangthanhtoanhoadon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class dangkynguoibanvip extends AppCompatActivity {

    TextView tennguoidangkyvip, diachinguoidangkyvip;
    CheckBox nguoibanvipvip;
    ImageButton dknguoibanvip;
    FirebaseFirestore db;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "HoangNgoc";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "HoangNgoc";
    private String description = "mua hàng online";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangkynguoibanvip);
        tennguoidangkyvip = findViewById(R.id.tennguoidangkyvip);
        diachinguoidangkyvip = findViewById(R.id.diachinguoidangkyvip);
        nguoibanvipvip = findViewById(R.id.nguoibanvipvip);
        dknguoibanvip = findViewById(R.id.dknguoibanvipnha);


        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        String tenDayDu = documentSnapshot.getString("shopName");
                        String diachi = documentSnapshot.getString("diaChi");
                        tennguoidangkyvip.setText(tenDayDu); // Hiển thị lên TextView
                        diachinguoidangkyvip.setText(diachi);

                    }
                }

            });

        }



        dknguoibanvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nguoibanvipvip.isChecked()) {
                    amount = "150000"; // Thay đổi thành giá trị mới

                    // Gọi hàm thanh toán MoMo
                    requestPayment(merchantName);
                } else {
                    Toast.makeText(dangkynguoibanvip.this, "Vui lòng nhấn đồng ý với mọi điều khoản", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void requestPayment(String idDonHang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);



        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, fee);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);

        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"-"+ UUID.randomUUID().toString());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, "MOMOC2IC20220510");

        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
            objExtraData.put("ticket", "{\"ticket\":{\"01\":{\"type\":\"std\",\"price\":110000,\"qty\":3}}}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {

                if(data.getIntExtra("status", -1) == 0) {

                    Log.d("Thanhcong", data.getStringExtra("message"));

                    saveDataToFirestore();
                    if(data.getStringExtra("data") != null && !data.getStringExtra("data").equals("")) {
                        // TODO:

                    } else {
                        Log.d("Thanhcong", data.getStringExtra("Không nhận được thông tin"));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.d("Thanhcong", data.getStringExtra("Không thành công"));
                } else if(data.getIntExtra("status", -1) == 2) {
                    Log.d("Thanhcong", data.getStringExtra("Không thành công"));
                } else {
                    Log.d("Thanhcong", data.getStringExtra("Không thành công"));
                }
            } else {
                Log.d("Thanhcong", data.getStringExtra("Không thành công"));
            }
        } else {
            Log.d("Thanhcong", data.getStringExtra("Không thành công"));
        }
    }
    private void saveDataToFirestore() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (user != null) {
            String documentId = user.getUid();

            db.collection("KhachHang")
                    .document(documentId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                KhachHang khachHang = documentSnapshot.toObject(KhachHang.class);
                                DocumentReference docRef = db.collection("Shop").document(documentId);

                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            int shopID = documentSnapshot.getLong("shopId").intValue();
                                            Nguoibanvip nguoibanvip1 = new Nguoibanvip(0, shopID, 150000);

                                            FirebaseFirestore.getInstance().collection("NguoiBanVip")
                                                    .add(nguoibanvip1)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            khachHang.setNguoiBan(false);
                                                            khachHang.setKhachHang(false);
                                                            khachHang.setNguoiBanVip(true);

                                                            db.collection("KhachHang")
                                                                    .document(documentId)
                                                                    .set(khachHang)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(dangkynguoibanvip.this, "Đăng ký người bán thành công", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(dangkynguoibanvip.this, dangkynguoibanvipthanhcong.class);
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
}