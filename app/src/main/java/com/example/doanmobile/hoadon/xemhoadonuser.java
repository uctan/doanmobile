package com.example.doanmobile.hoadon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.ItemSpacingDecoration;
import com.example.doanmobile.danhgiasanpham.Review;
import com.example.doanmobile.trangchunguoidung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class xemhoadonuser extends AppCompatActivity {

    ImageButton closeorderuser;
    RecyclerView recyclerViewOrderuser;
    List<Order> orderList;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xemhoadonuser);
        recyclerViewOrderuser = findViewById(R.id.recyclerViewOrderuser);
        closeorderuser = findViewById(R.id.closeorderuser);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this,orderList);
        recyclerViewOrderuser.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrderuser.addItemDecoration(new ItemSpacingDecoration(16));
        recyclerViewOrderuser.setAdapter(orderAdapter);
        loadOrdernguoidung();
        closeorderuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(xemhoadonuser.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });
    }
    private  void loadOrdernguoidung()
    {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long userID = documentSnapshot.getLong("userID");
                fStore.collection("orders")
                        .whereEqualTo("userID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    orderList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                       Order order = document.toObject(Order.class);
                                        orderList.add(order);
                                    }
                                    orderAdapter.notifyDataSetChanged();
                                }
                                else {

                                }
                            }
                        });
            }
        });
    }
}