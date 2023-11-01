package com.example.doanmobile.chat;

import android.content.Context;
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

public class NguoidungvoiShopAdapter extends RecyclerView.Adapter<NguoidungvoiShopAdapter.NguoidungvoiShopViewHolder> {
    private List<ChatMessage>NguoidungvoiShopList;
    private ChatHistoryAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ChatMessage chatMessage);
    }

    public NguoidungvoiShopAdapter(List<ChatMessage> NguoidungvoiShopList) {
        this.NguoidungvoiShopList = NguoidungvoiShopList;
    }

    public void setOnItemClickListener(ChatHistoryAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateNguoidungvoiShopList(List<ChatMessage> newnguoidungvoiShopList) {
        NguoidungvoiShopList = newnguoidungvoiShopList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NguoidungvoiShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngdungvashop, parent, false);
        return new NguoidungvoiShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoidungvoiShopViewHolder holder, int position) {
        ChatMessage chatMessage = NguoidungvoiShopList.get(position);
        holder.bind(chatMessage);


    }

    @Override
    public int getItemCount() {
        return NguoidungvoiShopList.size();
    }

    public class NguoidungvoiShopViewHolder extends RecyclerView.ViewHolder {
        private TextView textUserName;
        CardView nguoibanchuyensangtinnhan;

        public NguoidungvoiShopViewHolder(@NonNull View itemView) {
            super(itemView);
            textUserName = itemView.findViewById(R.id.textUserName);

            nguoibanchuyensangtinnhan = itemView.findViewById(R.id.nguoibanchuyensangtinnhan);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(NguoidungvoiShopList.get(position));
                }
            });
        }

        public void bind(ChatMessage chatMessage) {
            textUserName.setText(chatMessage.getTenDayDu());
        }
    }

}


