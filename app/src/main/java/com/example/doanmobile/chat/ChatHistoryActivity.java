package com.example.doanmobile.chat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatHistoryActivity extends AppCompatActivity {

    private RecyclerView chatHistoryRecyclerView;
    private ChatHistoryAdapter chatHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        chatHistoryRecyclerView = findViewById(R.id.chatHistoryRecyclerView);
        chatHistoryAdapter = new ChatHistoryAdapter();

        // Kết nối RecyclerView với Adapter
        chatHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatHistoryRecyclerView.setAdapter(chatHistoryAdapter);

        // Lấy danh sách người dùng đã chat với bạn và thông tin cửa hàng
        getChatHistoryData();
    }

    private void getChatHistoryData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn collection 'KhachHang' để lấy danh sách người dùng có nguoiBan là true
        db.collection("KhachHang")
                .whereEqualTo("nguoiBan", true)
                .get()
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                    // Lấy danh sách người dùng có nguoiBan là true và hiển thị trên giao diện
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String userID = document.getId();
                        // Thực hiện truy vấn tiếp để lấy dữ liệu từ collection 'Shop' sử dụng userID
                        // Sau đó hiển thị dữ liệu trên giao diện
                        getShopData(userID);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }

    private void getShopData(String userID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn collection 'Shop' sử dụng userID
        db.collection("Shop")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener((QuerySnapshot queryDocumentSnapshots) -> {
                    // Lấy dữ liệu từ collection 'Shop' và hiển thị lên giao diện
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String shopName = document.getString("shopName");
                        // Tạo một mô hình dữ liệu (model) và thêm vào Adapter
                        ChatHistoryModel model = new ChatHistoryModel(userID, shopName);
                        chatHistoryAdapter.addChatHistory(model);
                    }
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi nếu cần
                });
    }
}
