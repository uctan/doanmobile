package com.example.doanmobile.danhgiasanpham;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.KhachHang;
import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.Products;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class ReViewAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReViewAdapter(Context context,List<Review> reviewList){
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.danhgiaitem, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.binhluandanhgia.setText(review.getTinnhan());
        holder.ratingBaritem.setRating(review.getRating());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int productID = review.getProductID();
        int userID = review.getUserID();
        CollectionReference shopCollectionRef = db.collection("Products");
        Query shopQuery = shopCollectionRef.whereEqualTo("productID", productID);
        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Products product = document.toObject(Products.class);
                    Glide.with(context).load(product.getImageURL()).into(holder.anhdanhgiasanpham);
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });
        CollectionReference shopCollectionRef1 = db.collection("KhachHang");
        Query shopQuery1 = shopCollectionRef1.whereEqualTo("userID", userID);
        shopQuery1.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    KhachHang khachHang = document.toObject(KhachHang.class);
                    holder.tennguoidanhgia.setText(khachHang.getTenDayDu());
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });



    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
class ReviewViewHolder extends RecyclerView.ViewHolder{
    ImageView anhdanhgiasanpham;
    TextView tennguoidanhgia,binhluandanhgia;
    CardView danhgiansanphamcard;
    RatingBar ratingBaritem;

    public ReviewViewHolder(@NonNull View itemView){
        super(itemView);
        anhdanhgiasanpham = itemView.findViewById(R.id.anhdanhgiasanpham);
        tennguoidanhgia = itemView.findViewById(R.id.tennguoidanhgia);
        binhluandanhgia = itemView.findViewById(R.id.binhluandanhgia);
        ratingBaritem = itemView.findViewById(R.id.ratingBaritem);

    }
}
