package com.example.doanmobile.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.doanmobile.dangsanpham.chitietsanpham;
public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;
    private EditText messageEditText;
    private ImageView sendMessageImageView;
    private FirebaseFirestore db;
    private int userID;
    private int shopID;
    private String shopName;
    ImageView quaylaitinnhanchitiet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        quaylaitinnhanchitiet = findViewById(R.id.quaylaitinnhanchitietnha);


        db = FirebaseFirestore.getInstance();




        Intent intent = getIntent();
        if (intent != null) {
            shopName = intent.getStringExtra("shopName");
            shopID = intent.getIntExtra("shopId", 0);
            Log.d("ChatActivity", "Shop ID: " + shopID);
            userID = intent.getIntExtra("userID", 0);
            if (shopName != null) {
                TextView shopNameTextView = findViewById(R.id.shopNameTextView);
                shopNameTextView.setText(shopName);
            }
        }


        recyclerView = findViewById(R.id.recycle_chat);
        messageEditText = findViewById(R.id.edtinputtext);
        sendMessageImageView = findViewById(R.id.imagechat);

        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessageList,userID);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(chatAdapter);
        listenForMessages();
        sendMessageImageView.setOnClickListener(v -> {
            // Gọi phương thức để gửi tin nhắn ở đây
            sendMessageToShop();
        });
        loadChatHistory();

        quaylaitinnhanchitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, chitietsanpham.class);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopId", shopID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    private void sendMessageToShop() {
        String messageText = messageEditText.getText().toString().trim();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            DocumentReference userRef = fStore.collection("KhachHang").document(user.getUid());
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    int userId = documentSnapshot.getLong("userID").intValue();

                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRefFirestore = db.collection("KhachHang").document(user.getUid());
                    userRefFirestore.get().addOnSuccessListener(documentSnapshotFirestore -> {
                        if (documentSnapshotFirestore.exists()) {
                            String tenDayDu = documentSnapshotFirestore.getString("tenDayDu");

                            if (!TextUtils.isEmpty(messageText)) {
                                if (shopID != 0) {
                                    ChatMessage chatMessage = new ChatMessage();
                                    // Thiết lập userID cho tin nhắn

                                    chatMessage.setShopID(shopID);
                                    chatMessage.setMess(messageText);
                                    chatMessage.setDateObj(new Date());
                                    chatMessage.setDatetime(getCurrentTimestamp());
                                    chatMessage.setTenDayDu(tenDayDu);
                                    chatMessage.setShopName(shopName);

                                    db.collection("chat")
                                            .add(chatMessage)
                                            .addOnSuccessListener(documentReference -> {
                                                chatMessageList.add(chatMessage);
//                                                chatMessage.setUserID(userId);
                                                chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                                                recyclerView.scrollToPosition(chatMessageList.size() - 1);
                                                messageEditText.setText("");
                                            })
                                            .addOnFailureListener(e -> {
                                                // Xử lý khi gửi tin nhắn thất bại
                                                Toast.makeText(ChatActivity.this, "Gửi tin nhắn không thành công", Toast.LENGTH_SHORT).show();
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
        } else {
            // Xử lý trường hợp không có người dùng đăng nhập
        }
    }



    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }
    private void listenForMessages() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();

        if (currentUser != null) {
            String userIDD = currentUser.getUid();
            db.collection("KhachHang").document(userIDD)
                    .addSnapshotListener((documentSnapshot, error) -> {
                        if (error != null) {
                            Log.e("ChatActivity", "Listen failed", error);
                            return;
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            int userID = documentSnapshot.getLong("userID").intValue();

                            db.collection("chat")
                                    .whereEqualTo("userID", userID)
                                    .addSnapshotListener((value, chatError) -> {
                                        if (chatError != null) {
                                            Log.e("ChatActivity", "Listen failed", chatError);
                                            return;
                                        }

                                        for (DocumentChange dc : value.getDocumentChanges()) {
                                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                                ChatMessage message = dc.getDocument().toObject(ChatMessage.class);
                                                chatMessageList.add(message);
                                                Collections.reverse(chatMessageList); // Đảo ngược danh sách để hiển thị theo thứ tự đúng
                                                chatAdapter.notifyItemInserted(chatMessageList.size() - 1);
                                                recyclerView.scrollToPosition(chatMessageList.size() - 1);
                                            }
                                        }



                                    });
                        }
                    });
        } else {
            Log.e("ChatActivity", "FirebaseUser is null");
            // Xử lý trường hợp currentUser là null
        }
    }
    private void loadChatHistory() {
        // Retrieve and display chat history using shopID and userID
        db.collection("chat")
                .whereEqualTo("shopID", shopID)
                .whereEqualTo("userID", userID)
                .orderBy("datetime",Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("ChatActivity", "Listen failed", error);
                        return;
                    }
                    chatMessageList.clear();
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            ChatMessage message = dc.getDocument().toObject(ChatMessage.class);
                            chatMessageList.add(message);
                            Collections.reverse(chatMessageList); // Đảo ngược danh sách để hiển thị theo thứ tự đúng

                            chatAdapter.notifyDataSetChanged(); // Thông báo cho adapter rằng dữ liệu đã thay đổi
                            recyclerView.scrollToPosition(chatMessageList.size() - 1);
                        }
                    }
                });
    }

}
