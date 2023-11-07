package com.example.doanmobile.hoadon;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.doanmobile.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class xemchitiethoadon extends AppCompatActivity {

    RecyclerView recyclerViewOrdechitietruser;
    ImageView backnguoiorderchitiet;
    List<OrderDetail> orderDetailList;
    OrderdetailAdapter orderdetailAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemchitiethoadon);
        backnguoiorderchitiet = findViewById(R.id.backnguoiorderchitiet);
        recyclerViewOrdechitietruser = findViewById(R.id.recyclerViewOrdechitietruser);

        backnguoiorderchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(xemchitiethoadon.this, xemhoadonuser.class);
                startActivity(intent);
            }
        });

        orderDetailList = new ArrayList<>();
        orderdetailAdapter = new OrderdetailAdapter(this, orderDetailList);
        recyclerViewOrdechitietruser.setAdapter(orderdetailAdapter);
        recyclerViewOrdechitietruser.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                int orderID = bundle.getInt("orderID", 0);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference orderDetailRef = db.collection("order_detail");
                Query orderDetailQuery = orderDetailRef.whereEqualTo("orderID", orderID);

                orderDetailQuery.get().addOnCompleteListener(detailTask -> {
                    if (detailTask.isSuccessful()) {
                        orderDetailList.clear(); // Xóa các mục cũ trước khi thêm các mục mới

                        for (QueryDocumentSnapshot document : detailTask.getResult()) {
                            OrderDetail orderDetail = document.toObject(OrderDetail.class);
                            orderDetailList.add(orderDetail);
                        }

                        orderdetailAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    } else {
                        // Xử lý trường hợp không thành công khi lấy thông tin order_detail
                        Log.e(TAG, "Error getting order details: ", detailTask.getException());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

