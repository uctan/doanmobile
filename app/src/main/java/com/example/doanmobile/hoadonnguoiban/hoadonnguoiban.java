package com.example.doanmobile.hoadonnguoiban;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.example.doanmobile.hoadon.OrderDetail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class hoadonnguoiban extends AppCompatActivity {

    RecyclerView recyclerViewOrdershop;
    List<OrderDetail> hoadonshopdetailList;
    hoadonnguoibanAdapter hoadonshopdetailadapter;
    ImageButton closeordershopne;
    FirebaseFirestore db;
    TextView tongtienhoadon;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadonnguoiban);
        tongtienhoadon = findViewById(R.id.tongtienhoadon);

        closeordershopne = findViewById(R.id.closeordershopne);
        closeordershopne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(hoadonnguoiban.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });


        recyclerViewOrdershop = findViewById(R.id.recyclerViewOrdershop);
        hoadonshopdetailList = new ArrayList<>();
        hoadonshopdetailadapter = new hoadonnguoibanAdapter(this, hoadonshopdetailList);
        recyclerViewOrdershop.setAdapter(hoadonshopdetailadapter);
        recyclerViewOrdershop.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid();
            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        int shopID = documentSnapshot.getLong("shopId").intValue();
                        loadProducts(shopID);
                    }
                }

            });
        }


    }
    private void loadProducts(int shopID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollectionRef = db.collection("Products");

        productsCollectionRef.whereEqualTo("shopID", shopID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            long productID = document.getLong("productID");
                            loadOrderDetail(productID);
                        }
                    } else {
                        // Handle the case when the query is not successful
                    }
                });
    }
    private void loadOrderDetail(long productID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orderDetailRef = db.collection("order_detail");
        Query orderDetailQuery = orderDetailRef.whereEqualTo("productID", productID);

        orderDetailQuery.get().addOnCompleteListener(detailTask -> {
            if (detailTask.isSuccessful()) {
                double totalAmount = 0;
                for (QueryDocumentSnapshot document : detailTask.getResult()) {
                    OrderDetail orderDetail = document.toObject(OrderDetail.class);
                    hoadonshopdetailList.add(orderDetail);

                    totalAmount += orderDetail.getGiaSanPham();
                }
                tongtienhoadon.setText(String.valueOf(totalAmount));
                hoadonshopdetailadapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            } else {
                Log.e("LoadOrderDetail", "Error getting order details", detailTask.getException());

            }
        });
    }

}
