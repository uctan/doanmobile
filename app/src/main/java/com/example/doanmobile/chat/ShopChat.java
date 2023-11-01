package com.example.doanmobile.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_chat);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();



        Intent intent = getIntent();
        if (intent != null) {
            tenDayDu = intent.getStringExtra("tenDayDu");
            shopID = intent.getIntExtra("shopId", 0);
            Log.d("ChatActivity", "User ID: " + userID);
            userID = intent.getIntExtra("userID", 0);
            if (tenDayDu != null) {
                TextView tenDayduTextView = findViewById(R.id.tenDayduTextView);
                tenDayduTextView.setText(tenDayDu);
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
        String messageText = messageEditText.getText().toString().trim();

        String userIDD= currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("Shop").document(userIDD);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                int ShopID = documentSnapshot.getLong("shopId").intValue();
                String ShopName = documentSnapshot.getString("shopName");
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
                                    // Xử lý khi gửi tin nhắn thất bại
                                    Toast.makeText(ShopChat.this, "Gửi tin nhắn không thành công", Toast.LENGTH_SHORT).show();
                                });


                        // Xóa nội dung trong EditText sau khi gửi tin nhắn
                    } else {
                        Toast.makeText(this, "Vui lòng chọn cửa hàng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Vui lòng nhập nội dung tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void listenForMessages() {
        db.collection("chat")
                .whereEqualTo("userID", userID)
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
