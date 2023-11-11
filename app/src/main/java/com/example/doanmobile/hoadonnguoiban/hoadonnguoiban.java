package com.example.doanmobile.hoadonnguoiban;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.example.doanmobile.hoadon.OrderDetail;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class hoadonnguoiban extends AppCompatActivity {
    private static final String TAG = "hoadonnguoiban";
    RecyclerView recyclerViewOrdershop;
    List<OrderDetail> hoadonshopdetailList;
    hoadonnguoibanAdapter hoadonshopdetailadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoadonnguoiban);

        recyclerViewOrdershop = findViewById(R.id.recyclerViewOrdershop);
        hoadonshopdetailList = new ArrayList<>();
        hoadonshopdetailadapter = new hoadonnguoibanAdapter(this, hoadonshopdetailList);
        recyclerViewOrdershop.setAdapter(hoadonshopdetailadapter);
        recyclerViewOrdershop.setLayoutManager(new LinearLayoutManager(this));

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int shopID = bundle.getInt("shopID", 0);

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
                            // Xử lý trường hợp truy vấn không thành công
                        }
                    });
        }

    }

    private void loadOrderDetail(long productID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orderDetailRef = db.collection("order_detail");
        Query orderDetailQuery = orderDetailRef.whereEqualTo("productID", productID);

        orderDetailQuery.get().addOnCompleteListener(detailTask -> {
            if (detailTask.isSuccessful()) {
                for (QueryDocumentSnapshot document : detailTask.getResult()) {
                    OrderDetail orderDetail = document.toObject(OrderDetail.class);
                    hoadonshopdetailList.add(orderDetail);
                }
                hoadonshopdetailadapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            } else {

            }
        });
    }
}
