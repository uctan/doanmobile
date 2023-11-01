package com.example.doanmobile.chat;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doanmobile.R;
import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryViewHolder> {

    private List<ChatMessage> chatHistoryList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ChatMessage chatMessage);

    }

    public ChatHistoryAdapter(List<ChatMessage> chatHistoryList) {
        this.chatHistoryList = chatHistoryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateChatHistoryList(List<ChatMessage> newChatHistoryList) {
        chatHistoryList = newChatHistoryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        return new ChatHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryViewHolder holder, int position) {
        ChatMessage chatMessage = chatHistoryList.get(position);
        holder.bind(chatMessage);
    }

    @Override
    public int getItemCount() {
        return chatHistoryList.size();
    }

    public class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView textShopOrUserName;
        private CardView nhantinnguoidungvashopnha;
        public ChatHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textShopOrUserName = itemView.findViewById(R.id.textShopOrUserName);
            nhantinnguoidungvashopnha = itemView.findViewById(R.id.nhantinnguoidungvashopnha);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(chatHistoryList.get(position)); // Gửi tin nhắn được chọn đến ChatActivity
                }
            });
        }

        public void bind(ChatMessage chatMessage) {
            textShopOrUserName.setText(chatMessage.getShopName());
        }
    }

}
