package com.example.doanmobile.dangsanpham;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<sanphamHolder> {
    private Context context;
    private List<Products> productsList;

    public ProductAdapter (Context  context,List<Products> productsList){
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public sanphamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sanphamitem,parent,false);
        return new  sanphamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sanphamHolder holder, int position) {
        Products data = productsList.get(position);
        Glide.with(context).load(data.getImageURL()).into(holder.anhsanpham);
        holder.tensanphamnha.setText(data.getTitle());
        holder.giasanphamnha.setText(String.valueOf(data.getPrice()));

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chitietsanpham.class);
                intent.putExtra("Image", data.getImageURL());
                intent.putExtra("Title", data.getTitle());
                intent.putExtra("mota", data.getDescription());
                double giaCa = data.getPrice();
                int shopId = data.getShopID();
                intent.putExtra("shopId", shopId);
                intent.putExtra("Giaca", giaCa);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}

class sanphamHolder extends  RecyclerView.ViewHolder{
    ImageView  anhsanpham;
    TextView tensanphamnha,giasanphamnha;
    CardView recCard;

    public sanphamHolder (@NonNull View itemView){
        super(itemView);
        anhsanpham = itemView.findViewById(R.id.anhsanpham);
        tensanphamnha = itemView.findViewById(R.id.tensanphamnha);
        giasanphamnha = itemView.findViewById(R.id.giasanphamnha);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
