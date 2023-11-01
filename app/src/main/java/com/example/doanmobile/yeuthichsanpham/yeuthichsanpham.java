package com.example.doanmobile.yeuthichsanpham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.tranggiaodienbanhang;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class yeuthichsanpham extends AppCompatActivity {

    ImageButton closefvr;
    private FavoriteAdapter favoriteAdapter;
    private List<Favorites> favoriteList;
    private RecyclerView recyclerViewFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeuthichsanpham);
        closefvr = findViewById(R.id.closefvr);

        recyclerViewFavourite = findViewById(R.id.recyclerViewFavourite);
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(this,favoriteList);

        recyclerViewFavourite.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFavourite.setAdapter(favoriteAdapter);

        loadFavoriteListFromFirestore();

        closefvr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(yeuthichsanpham.this, tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });
    }
    private void loadFavoriteListFromFirestore() {
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long userID = documentSnapshot.getLong("userID");
                fStore.collection("favorites")
                        .whereEqualTo("userID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    favoriteList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Favorites favorite = document.toObject(Favorites.class);
                                        favoriteList.add(favorite);
                                    }
                                    favoriteAdapter.notifyDataSetChanged();
                                } else {
                                    // Handle the error
                                }
                            }
                        });
            }
        });


    }
}