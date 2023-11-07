package com.example.doanmobile.hoadon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.CartItem;

import java.util.List;

public class HoadonAdapter extends RecyclerView.Adapter<HoadonAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> cartItems;

    public HoadonAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hoadonitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.productName.setText(cartItem.getTitle());
        holder.productPrice.setText(String.valueOf(cartItem.getPrice()));
        holder.quantity.setText(String.valueOf(cartItem.getQuantity()));

        Glide.with(context)
                .load(cartItem.getImageURL())
                .placeholder(R.drawable.anhdemo)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        TextView quantity;
        ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNamehoadon);
            productPrice = itemView.findViewById(R.id.productPricehoadon);
            quantity = itemView.findViewById(R.id.quantity);
            productImage = itemView.findViewById(R.id.productImagehoadon);
        }
    }
}