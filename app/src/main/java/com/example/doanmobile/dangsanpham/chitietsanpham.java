package com.example.doanmobile.dangsanpham;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.chat.ChatActivity;
import com.example.doanmobile.coicuahangshopdetail.cuahangshopdetail;
import com.example.doanmobile.dangkynguoiban.Shop;
import com.example.doanmobile.danhgiasanpham.ReViewAdapter;
import com.example.doanmobile.danhgiasanpham.Review;
import com.example.doanmobile.giohang.GioHangActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class chitietsanpham extends AppCompatActivity {
    TextView detailtensp,detailgia,detailmotasp,detailsoluong,tinhtiengiohangdetail,detailtencuahang,soluongdetail,giamgiadetail,soluongbandetail,trungbinhdanhgiadetail;
    View detailtru,detailcong,themgiohang;
    ImageView detailanh,backnguoibanchitiet,detailgiohang,nhantinvoishop;
    //Thêm hoặc giảm số lượng sản phẩm

    int soLuong = 1;
    FirebaseFirestore db;

    //xuathien danh gia san pham
    RecyclerView xuathiendanhgiatungsanpham;
    ReViewAdapter reViewAdapter;
    List<Review> reviewList;
    NotificationBadge soluonggiohang;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitietsanpham);
        detailtensp = findViewById(R.id.detailtensp);
        detailgia = findViewById(R.id.detailgia);
        detailmotasp = findViewById(R.id.detailmotasp);
        detailsoluong = findViewById(R.id.detailsoluong);
        detailtru = findViewById(R.id.detailtru);
        detailcong = findViewById(R.id.detailcong);
        detailanh = findViewById(R.id.detailanh);
        tinhtiengiohangdetail = findViewById(R.id.tinhtiengiohangdetail);
        detailtencuahang = findViewById(R.id.detailtencuahang);
        detailgiohang = findViewById(R.id.detailgiohang);
        themgiohang = findViewById(R.id.themgiohang);
        xuathiendanhgiatungsanpham = findViewById(R.id.xuathiendanhgiatungsanpham);
        soluongdetail = findViewById(R.id.soluongdetail);
        giamgiadetail = findViewById(R.id.giamgiadetail);
        soluongbandetail = findViewById(R.id.soluongbandetail);
        trungbinhdanhgiadetail = findViewById(R.id.trungbinhdanhgiadetail);
        soluonggiohang = findViewById(R.id.soluonggiohang);
        db = FirebaseFirestore.getInstance();

        //trang hien so luong gio hang

        nhantinvoishop = findViewById(R.id.nhantinvoishop);
        //hieện chi tiết sản phẩm
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                int shopId = bundle.getInt("shopId", 0);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference shopRef = db.collection("Shop").document(String.valueOf(shopId));

                shopRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String shopName = document.getString("shopName");
                            detailtencuahang.setText(shopName);
                        }
                    }
                });


                detailtensp.setText(bundle.getString("Title"));
                detailmotasp.setText(bundle.getString("mota"));
                double soluong = bundle.getDouble("soluong");
                double discount = bundle.getDouble("discount");
                double selled = bundle.getDouble("selled");
                double reviewcount = bundle.getDouble("reviewcount");
                soluongdetail.setText(String.valueOf(soluong));
                giamgiadetail.setText(String.valueOf(discount));
                soluongbandetail.setText(String.valueOf(selled));
                trungbinhdanhgiadetail.setText(String.valueOf(reviewcount));

                double giaca = bundle.getDouble("Giaca");
                detailgia.setText(String.valueOf(giaca)); // Hiển thị giá sau khi giảm giá







                Glide.with(this).load(bundle.getString("Image")).into(detailanh);

            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        }
        //xuat hien phan danh gia
        reviewList = new ArrayList<>();
        reViewAdapter = new ReViewAdapter(chitietsanpham.this,reviewList);

        xuathiendanhgiatungsanpham.setLayoutManager(new LinearLayoutManager(this));
        xuathiendanhgiatungsanpham.addItemDecoration(new ItemSpacingDecoration(16));
        xuathiendanhgiatungsanpham.setAdapter(reViewAdapter);
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        CollectionReference reviewCollectionRef = db1.collection("Reviews");
        Query reviewQuery = reviewCollectionRef.whereEqualTo("productID", bundle.getInt("productID"));

        reviewQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Review review = document.toObject(Review.class);
                    reviewList.add(review);
                }
                // Thông báo cho adapter biết dữ liệu đã thay đổi
                reViewAdapter.notifyDataSetChanged();
            } else {
                // Xử lý trường hợp không thành công
            }
        });
        //quavetrangnguoiban
        backnguoibanchitiet = findViewById(R.id.backnguoibanchitiet);
        backnguoibanchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chitietsanpham.this,tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });



        //nhantin voi shop
        nhantinvoishop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy tên cửa hàng từ TextView
                String shopName = detailtencuahang.getText().toString();
                int shopId = bundle.getInt("shopId", 0);
                Intent intent = new Intent(chitietsanpham.this, ChatActivity.class);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopId",shopId);
                startActivity(intent);

            }
        });
        //giảm số lượng sản phẩm
        detailtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong--;
                if (soLuong < 1) {
                    soLuong = 1;
                }
                detailsoluong.setText(String.valueOf(soLuong));
                // Tính lại tổng tiền sau khi thay đổi số lượng
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    double giaca = bundle.getDouble("Giaca");
                    double discount = bundle.getDouble("discount");
                    double discountAmount = (discount / 100) * giaca; // Tính số tiền giảm giá
                    double giacade = giaca - discountAmount; // Tính giá cuối cùng sau khi giảm giá
                    double totalGia = soLuong * giacade; // Tính tổng giá sản phẩm sau khi giảm giá và nhân với số lượng
                    tinhtiengiohangdetail.setText(String.valueOf(totalGia));
                }
            }
        });

// Cộng số lượng sản phẩm
        detailcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soLuong++;
                detailsoluong.setText(String.valueOf(soLuong));

                // Lấy giá từ Bundle
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    double giaca = bundle.getDouble("Giaca");
                    double discount = bundle.getDouble("discount");
                    double discountAmount = (discount / 100) * giaca; // Tính số tiền giảm giá
                    double giacade = giaca - discountAmount; // Tính giá cuối cùng sau khi giảm giá
                    double totalGia = soLuong * giacade; // Tính tổng giá sản phẩm sau khi giảm giá và nhân với số lượng
                    tinhtiengiohangdetail.setText(String.valueOf(totalGia));
                }
            }
        });

        //laytenshop
        int shopId = bundle.getInt("shopId", 0);

        CollectionReference shopCollectionRef = db.collection("Shop");
        Query shopQuery = shopCollectionRef.whereEqualTo("shopId", shopId);

        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    if (doc.exists()) {
                        String shopName = doc.getString("shopName");
                        // Lấy dữ liệu từ doc
                        detailtencuahang.setText(shopName);
                    }
                }
            } else {
                Log.d(TAG, "Failed with: ", task.getException());
            }
        });
        detailtencuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chitietsanpham.this, cuahangshopdetail.class);
                intent.putExtra("shopId", shopId);
                startActivity(intent);
            }
        });

        //nut gio hang
        detailgiohang=findViewById(R.id.detailgiohang);
        detailgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chitietsanpham.this, GioHangActivity.class);
                startActivity(intent);
            }
        });
        //button themgiohang
        themgiohang=findViewById(R.id.themgiohang);
        themgiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double giaca = bundle.getDouble("Giaca");
                double discount = bundle.getDouble("discount");
                double discountAmount = (discount / 100) * giaca; // Tính số tiền giảm giá
                double giacade = giaca - discountAmount; // Tính giá cuối cùng sau khi giảm giá
                double totalGia = soLuong * giacade;
                double soluongMax = bundle.getDouble("soluong");

                // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng hay chưa
                CartItem existingItem = CartManager.getInstance().getCarrtItemByProductID(bundle.getInt("productID"));
                if (existingItem != null) {
                    // Tính tổng số lượng hiện tại của sản phẩm trong giỏ hàng và số lượng mới muốn thêm vào
                    int totalQuantity = existingItem.getQuantity() + soLuong;

                    // Kiểm tra nếu tổng số lượng vượt quá số lượng tối đa
                    if (totalQuantity > soluongMax) {
                        Toast.makeText(chitietsanpham.this, "Số lượng sản phẩm vượt quá giới hạn", Toast.LENGTH_SHORT).show();
                        return; // Kết thúc sự kiện onClick
                    }

                    // Cập nhật số lượng của sản phẩm trong giỏ hàng và hiển thị thông báo thành công
                    existingItem.setQuantity(totalQuantity);

                    Toast.makeText(chitietsanpham.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                } else {

                    // Thêm sản phẩm vào giỏ hàng nếu chưa tồn tại và tổng số lượng không vượt quá số lượng tối đa
                    if (soLuong <= soluongMax) {
                        CartItem cartItem = new CartItem(bundle.getInt("productID"), bundle.getString("Title"), totalGia, bundle.getString("Image"), soLuong,bundle.getDouble("discount"));
                        CartManager.getInstance().addToCart(cartItem);
                        Toast.makeText(chitietsanpham.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        // Hiển thị thông báo cho người dùng nếu số lượng vượt quá giới hạn
                        Toast.makeText(chitietsanpham.this, "Số lượng sản phẩm vượt quá giới hạn", Toast.LENGTH_SHORT).show();
                    }
                }
                updateCartBadge();
            }

        });
    }
    private void updateCartBadge() {
        // Lấy danh sách CartItem từ CartManager
        int uniqueProductCount = CartManager.getInstance().getUniqueProductCount();

        // Set số lượng sản phẩm lên NotificationBadge
        if (uniqueProductCount > 0) {
            soluonggiohang.setVisibility(View.VISIBLE);
            soluonggiohang.setNumber(uniqueProductCount);
        } else {
            soluonggiohang.setVisibility(View.GONE);
        }
    }

}