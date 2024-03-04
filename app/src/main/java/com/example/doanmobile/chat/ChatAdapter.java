package com.example.doanmobile.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.KhachHang;
import com.example.doanmobile.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private int currentUserId;
    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages, int currentUserId) {
        this.messages = messages;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext()); // Thêm dòng này để có LayoutInflater

        if (viewType == VIEW_TYPE_SENT) {
            view = inflater.inflate(R.layout.item_send_mess, parent, false);
            Log.d("ChatAdapter", "onCreateViewHolder: viewType = " + viewType);
            return new SentMessageHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_received, parent, false);
            Log.d("ChatAdapter", "reciew: viewType = " + viewType);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        // Đảm bảo rằng currentUserId đã được cập nhật trước khi hiển thị tin nhắn
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).bind(message);
        } else {
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = messages.get(position);
        // Nếu là tin nhắn của userID hiện tại thì trả về VIEW_TYPE_SENT, ngược lại trả về VIEW_TYPE_RECEIVED
        return (chatMessage.getUserID() == currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    public static class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView sentMessageText, sentMessageTime;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageText = itemView.findViewById(R.id.txt_messsend);
            sentMessageTime = itemView.findViewById(R.id.txttimesend); // Ánh xạ TextView thời gian
        }

        public void bind(ChatMessage message) {
            sentMessageText.setText(message.getMess());
            sentMessageTime.setText(formatDate(message.getDateObj())); // Hiển thị thời gian
        }
    }

    // Inner static class for ReceivedMessageHolder
    public static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView receivedMessageText, receivedMessageTime;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessageText = itemView.findViewById(R.id.txt_mess_received);
            receivedMessageTime = itemView.findViewById(R.id.txt_time_received); // Ánh xạ TextView thời gian
        }

        public void bind(ChatMessage message) {
            receivedMessageText.setText(message.getMess());
            receivedMessageTime.setText(formatDate(message.getDateObj())); // Hiển thị thời gian
        }
    }

    // Method to format Date to string
    private static String formatDate(Date dateObj) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(dateObj);
    }
}

