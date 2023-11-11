package com.example.doanmobile.hoadonnguoiban;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.Products;
import com.example.doanmobile.hoadon.OrderDetail;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class hoadonnguoibanAdapter extends RecyclerView.Adapter<hoadonnguoibanAdapter.ViewHolder> {

    private Context context;
    private List<OrderDetail> orderDetailList;

    public hoadonnguoibanAdapter(Context context, List<OrderDetail> hoadonshopdetailList) {
        this.context = context;
        this.orderDetailList = hoadonshopdetailList;
    }

    @NonNull
    @Override
    public hoadonnguoibanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull hoadonnguoibanAdapter.ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        holder.quantitydetailorder.setText(String.valueOf(orderDetail.getSoLuong()));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int productID = orderDetail.getProductID();
        CollectionReference shopCollectionRef = db.collection("Products");
        Query shopQuery = shopCollectionRef.whereEqualTo("productID", productID);
        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Products product = document.toObject(Products.class);
                    holder.productNamehoadondetail.setText(product.getTitle());
                    holder.productPricehoadondetail.setText(String.valueOf(product.getPrice()));
                    Glide.with(context).load(product.getImageURL()).into(holder.productImagehoadondetail);
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });
        holder.ordercardnhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, shophienthi.class);
                intent.putExtra("orderID",orderDetail.getOrderID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImagehoadondetail;
        TextView productNamehoadondetail, productPricehoadondetail, quantitydetailorder;
        RelativeLayout ordercardnhe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImagehoadondetail = itemView.findViewById(R.id.productImagehoadondetail);
            productNamehoadondetail = itemView.findViewById(R.id.productNamehoadondetail);
            productPricehoadondetail = itemView.findViewById(R.id.productPricehoadondetail);
            quantitydetailorder = itemView.findViewById(R.id.quantitydetailorder);
            ordercardnhe = itemView.findViewById(R.id.ordercarddetailid);
        }
    }
}
