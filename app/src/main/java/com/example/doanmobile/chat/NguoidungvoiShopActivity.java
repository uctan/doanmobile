package com.example.doanmobile.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NguoidungvoiShopActivity extends AppCompatActivity {
    private RecyclerView nguoidungvoiShopRecyclerView;
    private NguoidungvoiShopAdapter nguoidungvoiShopAdapter;
    private List<ChatMessage> NguoidungvoiShopList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    ImageView quaylaitinnhanthongtincanhannguoiban;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nguoidungvoi_shop);
        quaylaitinnhanthongtincanhannguoiban = findViewById(R.id.quaylaitinnhanthongtincanhannguoiban);
        quaylaitinnhanthongtincanhannguoiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NguoidungvoiShopActivity.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        nguoidungvoiShopRecyclerView= findViewById(R.id.nguoidungvoiShopRecyclerView);
        NguoidungvoiShopList = new ArrayList<>();
        nguoidungvoiShopAdapter = new NguoidungvoiShopAdapter(NguoidungvoiShopList);
        nguoidungvoiShopRecyclerView.setAdapter(nguoidungvoiShopAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        nguoidungvoiShopRecyclerView.setLayoutManager(layoutManager);
        nguoidungvoiShopAdapter.notifyDataSetChanged();


        fetchChatHistory();

        nguoidungvoiShopAdapter.setOnItemClickListener(chatMessage -> {
            Intent intent = new Intent(NguoidungvoiShopActivity.this, ShopChat.class);
            intent.putExtra("tenDayDu", chatMessage.getTenDayDu());
            intent.putExtra("shopID", chatMessage.getShopID()); // Bổ sung ID của shop
            intent.putExtra("userID", chatMessage.getUserID()); // Bổ sung ID của người dùng/khách hàng
            startActivity(intent);
        });

    }
    private void fetchChatHistory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userRef = db.collection("Shop").document(currentUserID);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long ShopIDLong = documentSnapshot.getLong("shopId");
                int ShopID = (ShopIDLong != null) ? ShopIDLong.intValue() : -1;

                CollectionReference chatCollectionRef = db.collection("chat");
                chatCollectionRef.whereEqualTo("shopID", ShopID).get()

                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Set<String> uniquetenDayDu = new HashSet<>();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String tenDayDu = document.getString("tenDayDu");

                                if (tenDayDu != null) {
                                    uniquetenDayDu.add(tenDayDu);
                                } else {
                                    Log.d("ChatHistoryActivity", "Invalid Data: tenDayDu not found!");
                                }
                            }

                            // Add unique shop names to the chatHistoryList
                            for (String tenDayDu : uniquetenDayDu) {
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setTenDayDu(tenDayDu);
                                NguoidungvoiShopList.add(chatMessage);
                            }

                            // Cập nhật giao diện người dùng
                            nguoidungvoiShopAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ChatHistoryActivity", "Error getting chat history: ", e);
                        });
            } else {
                Log.d("ChatHistoryActivity", "User document does not exist!");
            }
        }).addOnFailureListener(e -> {
            Log.e("ChatHistoryActivity", "Error getting user document: ", e);
        });
    }

}