package com.example.doanmobile.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private EditText messageEditText;
    private Button sendButton;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            currentUserID = currentUser.getUid();

            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            messageEditText = findViewById(R.id.messageEditText);
            sendButton = findViewById(R.id.sendButton);

            chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            chatAdapter = new ChatAdapter(new ArrayList<Message>());
            chatRecyclerView.setAdapter(chatAdapter);

            // Lắng nghe tin nhắn trong Firestore và cập nhật RecyclerView
            listenForMessages();

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String messageText = messageEditText.getText().toString();
                    if (!messageText.isEmpty()) {
                        sendMessage(currentUserID, "KhachHangUserID", messageText);
                    }
                }
            });
        }
    }

    private void sendMessage(String fromUserID, String toUserID, String messageText) {
        // Xác định senderID dựa trên người gửi
        firestore.collection("KhachHang").document(fromUserID)
                .get()
                .addOnSuccessListener(fromUserDocument -> {
                    if (fromUserDocument.exists()) {
                        boolean isFromKhachHang = fromUserDocument.getBoolean("nguoiBan");
                        int senderID = isFromKhachHang ? 4 : 2;

                        // Xác định receiverID dựa trên người nhận
                        firestore.collection("KhachHang").document(toUserID)
                                .get()
                                .addOnSuccessListener(toUserDocument -> {
                                    if (toUserDocument.exists()) {
                                        boolean isToNguoiBan = toUserDocument.getBoolean("nguoiBan");
                                        int receiverID = isToNguoiBan ? 2 : 4;

                                        // Lưu tin nhắn vào Firestore
                                        Message message = new Message();
                                        message.setSenderID(senderID);
                                        message.setReceiverID(receiverID);
                                        message.setMessageText(messageText);

                                        firestore.collection("chats")
                                                .add(message)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Tin nhắn được lưu thành công
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Xử lý lỗi khi lưu tin nhắn
                                                });
                                    }
                                });
                    }
                });
    }



    private void listenForMessages() {
        firestore.collection("chats")
                .whereEqualTo("senderID", Integer.parseInt(currentUserID))
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Xử lý lỗi
                        return;
                    }
                    List<Message> messages = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : value) {
                        Message message = doc.toObject(Message.class);
                        messages.add(message);
                    }
                    chatAdapter.updateMessages(messages);
                });
    }
}

