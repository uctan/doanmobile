package com.example.doanmobile.dangsanpham;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doanmobile.R;
import com.example.doanmobile.dangnhap;
import com.example.doanmobile.yeuthichsanpham.Favorites;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<sanphamHolder> {
    private Context context;
    private List<Products> productsList;
    private List<Integer> favoriteProducts;
    private sanphamHolder holder;
    private FirebaseFirestore db;


    public ProductAdapter (Context  context,List<Products> productsList){
        this.context = context;
        this.productsList = productsList;
        this.favoriteProducts = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

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
        this.holder = holder;
        Glide.with(context).load(data.getImageURL()).into(holder.anhsanpham);
        holder.tensanphamnha.setText(data.getTitle());
        holder.giasanphamnha.setText(String.valueOf(data.getPrice()));
        //lam chuc nang yeu thich san pham
        final boolean[] isFavorite = {false};
        if (favoriteProducts.contains(data.getProductID())) {
            holder.traitimbth.setImageResource(R.drawable.traitimdo);
            isFavorite[0] = true;

        } else {
            holder.traitimbth.setImageResource(R.drawable.traitimbth);
            isFavorite[0] = false;

        }

        holder.soluotthich.setText(String.valueOf(data.getLikeCount()));
        holder.traitimbth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();
                if(user != null){
                    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                    String userId = user.getUid();
                    DocumentReference userRef = fStore.collection("KhachHang").document(userId);
                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            int productID = data.getProductID();
                            int userID = documentSnapshot.getLong("userID").intValue();

                            if (isFavorite[0]) {
                                removeFromFavorites(userID, productID);
                            } else {
                                addToFavorites(userID, productID);
                            }

                            // Cập nhật hình ảnh của nút
                            isFavorite[0] = !isFavorite[0]; // Đảo ngược trạng thái yêu thích
                            holder.traitimbth.setImageResource(isFavorite[0] ? R.drawable.traitimdo : R.drawable.traitimbth);

                        }
                    });
                }
                else{
                    Toast.makeText(context, "Yêu cầu đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, dangnhap.class);
                    context.startActivity(intent);
                }
            }


        });

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                FirebaseUser user = fAuth.getCurrentUser();
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                if (user != null) {
                    Intent intent = new Intent(context, chitietsanpham.class);
                    intent.putExtra("Image", data.getImageURL());
                    intent.putExtra("Title", data.getTitle());
                    intent.putExtra("mota", data.getDescription());
                    double giaCa = data.getPrice();
                    int shopId = data.getShopID();
                    intent.putExtra("shopId", shopId);
                    intent.putExtra("Giaca", giaCa);
                    int productID = data.getProductID();
                    intent.putExtra("productID",productID);
                    context.startActivity(intent);}
                else{
                    Toast.makeText(context, "Yêu cầu đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, dangnhap.class);
                    context.startActivity(intent);
                }
            }
        });

        //yeu thich san pham

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
    public void searchDataList(List<Products> searchList) {
        productsList = searchList;
        notifyDataSetChanged();
    }

    //them san pham yeu thich
    public void updateProductFavoriteStatus(List<Integer> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
        notifyDataSetChanged();
    }


    private void addToFavorites(int userID, int productID) {
        // Tạo một đối tượng Favorites mới


        // Lấy favoriteID lớn nhất
        db.collection("favorites")
                .orderBy("favoriteID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int maxFavoriteID = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Favorites favorite = documentSnapshot.toObject(Favorites.class);
                            maxFavoriteID = favorite.getFavoriteID();
                        }

                        // Tạo một đối tượng Favorites mới
                        Favorites favorite = new Favorites(maxFavoriteID + 1, userID, productID);

                        // Thêm favorite vào Firestore
                        db.collection("favorites")
                                .add(favorite)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // Thêm thành công
                                        Log.d(TAG, "Thêm vào yêu thích thành công");
                                        favoriteProducts.add(productID);
                                        updateProductFavoriteStatus(favoriteProducts);
                                        updateLikeCount(productID, +1);

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xảy ra lỗi
                                        Log.e(TAG, "Lỗi khi thêm vào yêu thích", e);
                                    }
                                });
                    }
                });
    }

    //xoa san pham yeu thich
    private void removeFromFavorites(int userID, int productID) {
        db.collection("favorites")
                .whereEqualTo("userID", userID)
                .whereEqualTo("productID", productID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Xóa thành công
                                            Log.d(TAG, "Xóa yêu thích thành công");
                                            favoriteProducts.add(productID);
                                            updateProductFavoriteStatus(favoriteProducts);
                                            updateLikeCount(productID, -1);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Xảy ra lỗi
                                            Log.e(TAG, "Lỗi khi xóa yêu thích", e);
                                        }
                                    });
                        }
                    }
                });
    }
    private void updateLikeCount(int productID, int change) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Products");

        // Tìm các tài liệu thỏa mãn điều kiện productID
        Query query = productsCollection.whereEqualTo("productID", productID);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Long currentLikeCountLong = document.getLong("likeCount");

                    if (currentLikeCountLong != null) {
                        int currentLikeCount = currentLikeCountLong.intValue();
                        int newLikeCount = currentLikeCount + change;

                        // Cập nhật likeCount
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("likeCount", newLikeCount);

                        // Thực hiện cập nhật
                        document.getReference().update(updates)
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "Cập nhật likeCount thành công"))
                                .addOnFailureListener(e -> Log.e(TAG, "Lỗi khi cập nhật likeCount", e));
                    } else {
                        Log.e(TAG, "likeCount có giá trị null");
                    }
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }
}

class sanphamHolder extends  RecyclerView.ViewHolder{
    ImageView  anhsanpham;
    TextView tensanphamnha,giasanphamnha,soluotthich;
    CardView recCard;
    ImageButton traitimbth;

    public sanphamHolder (@NonNull View itemView){
        super(itemView);
        anhsanpham = itemView.findViewById(R.id.anhsanpham);
        tensanphamnha = itemView.findViewById(R.id.tensanphamnha);
        giasanphamnha = itemView.findViewById(R.id.giasanphamnha);
        recCard = itemView.findViewById(R.id.recCard);
        traitimbth = itemView.findViewById(R.id.traitimbth);
        soluotthich = itemView.findViewById(R.id.soluotthich);


    }
}