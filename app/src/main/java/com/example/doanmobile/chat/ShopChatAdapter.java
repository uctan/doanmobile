package com.example.doanmobile.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShopChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<ChatMessage> messages;
    private FirebaseUser currentUser;

    public ShopChatAdapter(List<ChatMessage> messages, FirebaseUser currentUser) {
        this.messages = messages;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_mess, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_mess, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_RECEIVED) {
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
        // Implement the logic to return the view type (Sent or Received) based on your data
        // For example, you can check if the message is sent by the current user or received.
        // Return VIEW_TYPE_SENT or VIEW_TYPE_RECEIVED accordingly.
        return super.getItemViewType(position);
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

    public static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView txt_messsend, txttimesend;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            txt_messsend = itemView.findViewById(R.id.txt_messsend);
            txttimesend = itemView.findViewById(R.id.txttimesend); // Ánh xạ TextView thời gian
        }

        public void bind(ChatMessage message) {
            txt_messsend.setText(message.getMess());
            txttimesend.setText(formatDate(message.getDateObj())); // Hiển thị thời gian
        }
    }

    // Method to format Date to string
    private static String formatDate(Date dateObj) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(dateObj);
    }
}
