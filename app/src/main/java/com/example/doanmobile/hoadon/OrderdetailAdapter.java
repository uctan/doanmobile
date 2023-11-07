package com.example.doanmobile.hoadon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.Products;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class OrderdetailAdapter extends RecyclerView.Adapter<OrderdetailAdapter.ViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetailList;

    public OrderdetailAdapter (Context context,List<OrderDetail> orderDetailList)
    {
        this.context = context;
        this.orderDetailList = orderDetailList;
    }
    @NonNull
    @Override
    public OrderdetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderdetailid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderdetailAdapter.ViewHolder holder, int position) {

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

                    holder. productNamehoadondetail.setText(product.getTitle());
                    holder. productPricehoadondetail.setText(String.valueOf(product.getPrice()));
                    Glide.with(context).load(product.getImageURL()).into(holder. productImagehoadondetail);
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView productImagehoadondetail;
       TextView productNamehoadondetail,productPricehoadondetail,quantitydetailorder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImagehoadondetail = itemView.findViewById(R.id.productImagehoadondetail);
            productNamehoadondetail = itemView.findViewById(R.id.productNamehoadondetail);
            productPricehoadondetail = itemView.findViewById(R.id.productPricehoadondetail);
            quantitydetailorder = itemView.findViewById(R.id.quantitydetailorder);
        }
    }
}
