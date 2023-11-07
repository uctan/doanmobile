package com.example.doanmobile.hoadon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.CartAdapter;
import com.example.doanmobile.dangsanpham.CartItem;
import com.example.doanmobile.dangsanpham.CartManager;
import com.example.doanmobile.dangsanpham.chitietsanpham;
import com.example.doanmobile.dangsanpham.tranggiaodienbanhang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class trangthanhtoanhoadon extends AppCompatActivity {
    private List<CartItem> cartItems;
    ImageView quaylaidondathang;
    TextView soluongsanphamhoadon,tongtinehoadon,tenhoadon,sodienthoaihoadon,ngaydathanghoadon;
    CheckBox thanhtoantienmathoadon,thanhtoanhoadonmomo;
    EditText diachihoadon;
    ImageView muahangdonhang;
    private int neworderId = 0;
    int currentDetailID = 1;
    private int userID;
    //thanh toanmomo
    private String amount ;
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
        setContentView(R.layout.activity_trangthanhtoanhoadon);
        quaylaidondathang = findViewById(R.id.quaylaidondathang);
        tenhoadon = findViewById(R.id.tenhoadon);
        sodienthoaihoadon = findViewById(R.id.sodienthoaihoadon);
        ngaydathanghoadon = findViewById(R.id.ngaydathanghoadon);
        thanhtoantienmathoadon = findViewById(R.id.thanhtoantienmathoadon);
        thanhtoanhoadonmomo = findViewById(R.id.thanhtoanhoadonmomo);
        diachihoadon = findViewById(R.id.diachihoadon);
        muahangdonhang = findViewById(R.id.muahangdonhang);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        quaylaidondathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trangthanhtoanhoadon.this, tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerViewThanhtoan);

        // Lấy danh sách mặt hàng từ intent
        Intent intent = getIntent();
        cartItems = intent.getParcelableArrayListExtra("cartItems");

        HoadonAdapter hoadonAdapter = new HoadonAdapter(this, cartItems);
        if (recyclerView != null) {
            recyclerView.setAdapter(hoadonAdapter);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //lay tong tien gia san pham
        int totalQuantity = 0;
        int totalPrice = 0 ;

        for (CartItem item : cartItems) {
            totalQuantity += item.getQuantity();
            totalPrice += item.getPrice()   ;
        }

        // Cập nhật vào TextView
        soluongsanphamhoadon = findViewById(R.id.soluongsanphamhoadon);
        tongtinehoadon = findViewById(R.id.tongtinehoadon);

        soluongsanphamhoadon.setText(String.valueOf(totalQuantity));
        tongtinehoadon.setText(String.valueOf(totalPrice));

        //lay ngay dat hang hien tai
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        String formattedDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedDate = currentDate.format(formatter);
        }
        ngaydathanghoadon.setText(formattedDate);
        //layten khach hang
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = fStore.collection("KhachHang").document(userId);

            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String fullName = documentSnapshot.getString("tenDayDu");
                    String sodienthoai = documentSnapshot.getString("soDienThoai");
                    userID = documentSnapshot.getLong("userID").intValue(); // Gán giá trị cho userID
                    tenhoadon.setText(fullName);
                    sodienthoaihoadon.setText(sodienthoai);
                }
            });
        }

        // Xử lý sự kiện khi người dùng nhấn nút mua hàng
        muahangdonhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String diaChi = diachihoadon.getText().toString();

                if (diaChi.isEmpty()) {
                    Toast.makeText(trangthanhtoanhoadon.this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
                    return;
                }

                double tongTien = Double.parseDouble(tongtinehoadon.getText().toString());
                String htThanhToan = "";

                if (thanhtoantienmathoadon.isChecked()) {
                    htThanhToan = "Tiền mặt";
                    Intent intent = new Intent(trangthanhtoanhoadon.this, thanhtoanthanhcong.class);
                    intent.putExtra("orderId", neworderId);
                    intent.putExtra("detailId", currentDetailID);
                    startActivity(intent);
                } else if (thanhtoanhoadonmomo.isChecked()) {
                    htThanhToan = "Momo";
                    amount = String.valueOf(tongTien);
                    requestPayment(merchantName);

                } else {
                    Toast.makeText(trangthanhtoanhoadon.this, "Phải chọn hình thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();

                if (user != null) {
                    String userId = user.getUid();
                    String finalHtThanhToan = htThanhToan;
                    db.collection("orders")
                            .orderBy("orderID", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int neworderId = 1;
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            int highestShopId = document.getLong("orderID").intValue();
                                            neworderId = highestShopId + 1;
                                        }
                                    }

                                    Order order = new Order(neworderId, userID, new Date(), diaChi, tongTien, finalHtThanhToan);

                                    int finalNeworderId = neworderId;
                                    db.collection("orders")
                                            .add(order)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    luuThongTinChiTietDonHang(finalNeworderId, cartItems);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi lưu đơn hàng không thành công
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }

    // luu thong tin orderdetail
    private void luuThongTinChiTietDonHang(int neworderId, List<CartItem> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một biến để lưu trữ newDetailId
        final int[] newDetailId = {1};

        // Thực hiện truy vấn để lấy giá trị lớn nhất của detailID
        db.collection("order_detail")
                .orderBy("detailID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                int highestDetailId = document.getLong("detailID").intValue();
                                newDetailId[0] = highestDetailId + 1;
                            }
                        }

                        // Sau khi lấy được newDetailId, tiến hành tạo và lưu OrderDetail
                        for (int i = 0; i < cartItems.size(); i++) {
                            CartItem item = cartItems.get(i);
                            OrderDetail orderDetail = new OrderDetail(
                                    newDetailId[0], // Sử dụng newDetailId mới lấy được
                                    item.getQuantity(),
                                    neworderId,
                                    item.getProductID(),
                                    item.getPrice()
                            );

                            db.collection("order_detail")
                                    .add(orderDetail)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            // Xử lý khi lưu thông tin chi tiết đơn hàng thành công
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Xử lý khi lưu thông tin chi tiết đơn hàng không thành công
                                        }
                                    });
                        }
                    }
                });
    }

    // Phương thức để tạo mã chi tiết đơn hàng duy nhất

    //thanhtoanmomo
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
                    Intent intent = new Intent(trangthanhtoanhoadon.this, thanhtoanthanhcong.class);
                    intent.putExtra("orderId", neworderId);
                    intent.putExtra("detailId", currentDetailID);
                    startActivity(intent);
                    Log.d("Thanhcong", data.getStringExtra("message"));

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
    //lu vao thong tin chi tiet
}