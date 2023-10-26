package com.example.doanmobile.chat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanmobile.R;

import java.util.ArrayList;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryViewHolder> {

    private ArrayList<ChatHistoryModel> chatHistoryList;

    public ChatHistoryAdapter() {
        chatHistoryList = new ArrayList<>();
    }

    public void addChatHistory(ChatHistoryModel model) {
        chatHistoryList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public ChatHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        return new ChatHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatHistoryViewHolder holder, int position) {
        ChatHistoryModel model = chatHistoryList.get(position);
        holder.shopNameTextView.setText(model.getShopName());
    }

    @Override
    public int getItemCount() {
        return chatHistoryList.size();
    }

    public static class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView shopNameTextView;

        public ChatHistoryViewHolder(View itemView) {
            super(itemView);
            shopNameTextView = itemView.findViewById(R.id.shopNameTextView);
        }
    }
}
