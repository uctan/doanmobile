package com.example.doanmobile.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.example.doanmobile.profileuser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChatHistoryActivity extends AppCompatActivity {

    private RecyclerView chatHistoryRecyclerView;
    private ChatHistoryAdapter chatHistoryAdapter;
    private List<ChatMessage> chatHistoryList;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    ImageView quaylaitinnhanthongtincanhannguoidung;


private TextView textShopOrUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        quaylaitinnhanthongtincanhannguoidung = findViewById(R.id.quaylaitinnhanthongtincanhannguoidung);
        quaylaitinnhanthongtincanhannguoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatHistoryActivity.this, profileuser.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        chatHistoryRecyclerView = findViewById(R.id.chatHistoryRecyclerView);
        chatHistoryList = new ArrayList<>();
        chatHistoryAdapter = new ChatHistoryAdapter(chatHistoryList);
        chatHistoryRecyclerView.setAdapter(chatHistoryAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatHistoryRecyclerView.setLayoutManager(layoutManager);
        chatHistoryAdapter.notifyDataSetChanged();
        chatHistoryAdapter.setOnItemClickListener(chatMessage -> {
            Intent intent = new Intent(ChatHistoryActivity.this, ChatActivity.class);
            intent.putExtra("shopName", chatMessage.getShopName());
            intent.putExtra("shopID", chatMessage.getShopID());
            intent.putExtra("userID", chatMessage.getUserID());
            startActivity(intent);
        });



        fetchChatHistory();


    }





    private void fetchChatHistory() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userRef = db.collection("KhachHang").document(currentUserID);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long khachHangIDLong = documentSnapshot.getLong("userID");
                int khachHangID = (khachHangIDLong != null) ? khachHangIDLong.intValue() : -1;

                CollectionReference chatCollectionRef = db.collection("chat");
                chatCollectionRef.whereEqualTo("userID", khachHangID).get()

                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            Set<String> uniqueShopNames = new HashSet<>();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                String shopName = document.getString("shopName");

                                if (shopName != null) {
                                    uniqueShopNames.add(shopName);
                                } else {
                                    Log.d("ChatHistoryActivity", "Invalid Data: Shop Name not found!");
                                }
                            }

                            // Add unique shop names to the chatHistoryList
                            for (String shopName : uniqueShopNames) {
                                ChatMessage chatMessage = new ChatMessage();
                                chatMessage.setShopName(shopName);
                                chatHistoryList.add(chatMessage);
                            }

                            // Cập nhật giao diện người dùng
                            chatHistoryAdapter.notifyDataSetChanged();
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

