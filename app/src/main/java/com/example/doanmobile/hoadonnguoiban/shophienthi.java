package com.example.doanmobile.hoadonnguoiban;

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
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.example.doanmobile.dangsanpham.ItemSpacingDecoration;
import com.example.doanmobile.hoadon.Order;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class shophienthi extends AppCompatActivity {

    RecyclerView recyclerViewOrderuser;
    List<Order> hoadonkhdetailList;
    shophienthiAdapter hoadonkhdetailAdapter;
    ImageView back;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shophienthihoadon);

        recyclerViewOrderuser = findViewById(R.id.recyclerViewOrdershopKH);
        hoadonkhdetailList = new ArrayList<>();
        hoadonkhdetailAdapter = new shophienthiAdapter(this, hoadonkhdetailList);
        recyclerViewOrderuser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrderuser.addItemDecoration(new ItemSpacingDecoration(16));
        recyclerViewOrderuser.setAdapter(hoadonkhdetailAdapter);
        back=findViewById(R.id.backdonhangshop);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(shophienthi.this,hoadonnguoiban.class);
                startActivity(intent);
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int orderID = bundle.getInt("orderID", 0);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference productsCollectionRef = db.collection("orders");

            productsCollectionRef.whereEqualTo("orderID", orderID).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                hoadonkhdetailList.add(order);
                            }
                            hoadonkhdetailAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        } else {

                        }
                    });
        }
    }

    private void loadOrdernguoidung(long orderId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference khachHangRef = db.collection("orders");
        Query khachhangQuery = khachHangRef.whereEqualTo("orderID", orderId);

        khachhangQuery.get().addOnCompleteListener(detailTask -> {

        });


    }
}