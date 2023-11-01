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

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<ChatMessage> messages;
    private FirebaseUser currentUser;

    public ChatAdapter(List<ChatMessage> messages, FirebaseUser currentUser) {
        this.messages = messages;
        this.currentUser = currentUser;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_mess, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
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

