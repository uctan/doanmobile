package com.example.doanmobile.danhgiasanpham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.ItemSpacingDecoration;
import com.example.doanmobile.profileuser;
import com.example.doanmobile.trangchunguoidung;
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

public class trangxemdanhgianguoidung extends AppCompatActivity {
    ImageButton closereviewnguoidung;
    RecyclerView recyclerViewReviewnguoidung;
    ReViewAdapter reViewAdapter;
    List<Review> reviewList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangxemdanhgianguoidung);
        closereviewnguoidung = findViewById(R.id.closereviewshop);
        recyclerViewReviewnguoidung = findViewById(R.id.recyclerViewReviewnguoidung);
        closereviewnguoidung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(trangxemdanhgianguoidung.this, profileuser.class);
                startActivity(intent);
            }
        });
        //xem danh gia nguoidung
        reviewList = new ArrayList<>();
        reViewAdapter = new ReViewAdapter(this,reviewList);
        recyclerViewReviewnguoidung.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviewnguoidung.addItemDecoration(new ItemSpacingDecoration(16));
        recyclerViewReviewnguoidung.setAdapter(reViewAdapter);
        loadReviewnguoidung();

    }
    private  void loadReviewnguoidung(){
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference userRef = fStore.collection("KhachHang").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long userID = documentSnapshot.getLong("userID");
                fStore.collection("Reviews")
                        .whereEqualTo("userID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    reviewList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Review review = document.toObject(Review.class);
                                        reviewList.add(review);
                                    }
                                    reViewAdapter.notifyDataSetChanged();
                                }
                                else {

                                }
                            }
                        });
            }
        });
    }
}