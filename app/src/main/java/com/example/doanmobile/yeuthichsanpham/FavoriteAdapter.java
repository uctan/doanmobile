package com.example.doanmobile.yeuthichsanpham;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
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
import com.example.doanmobile.dangsanpham.Products;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {
    private Context context;
    private List<Favorites> favoriteList;

    public FavoriteAdapter(Context context, List<Favorites> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;

    }
    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Favorites favorites = favoriteList.get(position);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int productID = favorites.getProductID();
        CollectionReference shopCollectionRef = db.collection("Products");
        Query shopQuery = shopCollectionRef.whereEqualTo("productID", productID);
        shopQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Products product = document.toObject(Products.class);

                    holder.tensanphamnhafvr.setText(product.getTitle());
                    holder.giasanphamnhafvr.setText(String.valueOf(product.getPrice()));
                    Glide.with(context).load(product.getImageURL()).into(holder.anhsanphamfvr);
                }
            } else {
                // Xử lý trường hợp không thành công
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }
}
class FavoriteViewHolder extends RecyclerView.ViewHolder{
    ImageView anhsanphamfvr;
    TextView  tensanphamnhafvr,giasanphamnhafvr;
    CardView recCardfvr;

    public FavoriteViewHolder(@NonNull View itemView){
        super(itemView);
        anhsanphamfvr = itemView.findViewById(R.id.anhsanphamfvr);
        tensanphamnhafvr = itemView.findViewById(R.id.tensanphamnhafvr);
        giasanphamnhafvr = itemView.findViewById(R.id.giasanphamnhafvr);
    }
}
