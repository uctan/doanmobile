package com.example.doanmobile.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.Shop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShopChat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShopChatAdapter shopChatAdapter;
    private List<ChatMessage> chatMessageList;
    private EditText messageEditText;
    private ImageView sendMessageImageView;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private int userID;
    private int shopID;
    private String tenDayDu;
    ImageView quaylaitinnhannguoibannha;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_chat);
        quaylaitinnhannguoibannha = findViewById(R.id.quaylaitinnhannguoibannha);
        quaylaitinnhannguoibannha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopChat.this, NguoidungvoiShopActivity.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


        //layten shop




        Intent intent = getIntent();
        if (intent != null) {
            tenDayDu = intent.getStringExtra("tenDayDu");
            userID = intent.getIntExtra("userID", 0);
            if (tenDayDu != null) {
                TextView tenDayduTextView = findViewById(R.id.tenDayduTextView);
                tenDayduTextView.setText(tenDayDu);

                // Lấy userID từ bảng khách hàng dựa trên tenDayDu
                db.collection("KhachHang")
                        .whereEqualTo("tenDayDu", tenDayDu)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                userID = documentSnapshot.getLong("userID").intValue();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý khi truy vấn không thành công
                        });
            }
        }


        recyclerView = findViewById(R.id.recycle_shopchat);
        messageEditText = findViewById(R.id.edtinputtext);
        sendMessageImageView = findViewById(R.id.imagechat);

        chatMessageList = new ArrayList<>();
        shopChatAdapter = new ShopChatAdapter(chatMessageList, currentUser);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(shopChatAdapter);
        listenForMessages();
        sendMessageImageView.setOnClickListener(v -> {
            // Gọi phương thức để gửi tin nhắn ở đây
            sendMessageToUser();
        });
        loadChatHistory();


    }

    private void sendMessageToUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid();
            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    shopID = documentSnapshot.getLong("shopId").intValue();
                    String userIDD = currentUser.getUid();
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef = db.collection("Shop").document(userIDD);

                    userRef.get().addOnSuccessListener(documentSnapshot1 -> {
                        if (documentSnapshot1.exists()) {
                            String ShopName = documentSnapshot1.getString("shopName");
                            String messageText = messageEditText.getText().toString().trim();
                            if (!TextUtils.isEmpty(messageText)) {
                                if (userID != 0) {
                                    ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.setUserID(userID);
                                    chatMessage.setShopID(shopID);
                                    chatMessage.setMess(messageText);
                                    chatMessage.setDateObj(new Date());
                                    chatMessage.setDatetime(getCurrentTimestamp());
                                    chatMessage.setTenDayDu(tenDayDu);
                                    chatMessage.setShopName(ShopName);

                                    db.collection("chat")
                                            .add(chatMessage)
                                            .addOnSuccessListener(documentReference -> {
                                                chatMessageList.add(chatMessage);
                                                shopChatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                                                recyclerView.scrollToPosition(chatMessageList.size() - 1);
                                                messageEditText.setText("");
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(ShopChat.this, "Gửi tin nhắn không thành công", Toast.LENGTH_SHORT).show();
                                            });
                                } else {
                                    Toast.makeText(this, "Vui lòng chọn cửa hàng", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Vui lòng nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }



    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void listenForMessages() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Long shopIDLong = documentSnapshot.getLong("shopId");
                    if (shopIDLong != null) {
                        int shopID = shopIDLong.intValue();

                        db.collection("chat")
                                .whereEqualTo("shopID", shopID)
                                .addSnapshotListener((value, error) -> {
                                    if (error != null) {
                                        Log.e("ChatActivity", "Listen failed", error);
                                        return;
                                    }

                                    for (DocumentChange dc : value.getDocumentChanges()) {
                                        if (dc.getType() == DocumentChange.Type.ADDED) {
                                            ChatMessage message = dc.getDocument().toObject(ChatMessage.class);
                                            chatMessageList.add(message);
                                            shopChatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                                            recyclerView.scrollToPosition(chatMessageList.size() - 1);
                                        }
                                    }
                                });
                    }
                }
            });
        }
    }
    private void loadChatHistory() {
        // Retrieve and display chat history using shopID and userID
        db.collection("chat")
                .whereEqualTo("shopID", shopID)
                .whereEqualTo("userID", userID)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("ShopChat", "Listen failed", error);
                        return;
                    }

                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            ChatMessage message = dc.getDocument().toObject(ChatMessage.class);
                            chatMessageList.add(message);
                            shopChatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                            recyclerView.scrollToPosition(chatMessageList.size() - 1);
                        }
                    }
                });
    }

}