package com.example.doanmobile.dangsanpham;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.example.doanmobile.R;
import com.example.doanmobile.giohang.GioHangActivity;
import com.example.doanmobile.trangchunguoidung;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class tranggiaodienbanhang extends AppCompatActivity {

    ImageView backnguoiban, giaodiengiohang;

    RecyclerView theloaisanphamnha;
    List<Category> categoryList;
    CategoryAdapter adapter;

    List<Products> productsList;
    CollectionReference productCollection;
    ProductAdapter productAdapter;
    RecyclerView dangbansanphamitem;

    CollectionReference categoryCollection;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranggiaodienbanhang);
        theloaisanphamnha = findViewById(R.id.theloaisanphamnha);
        dangbansanphamitem = findViewById(R.id.dangbansanphamitem);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //chuyenvetrangchu
        backnguoiban = findViewById(R.id.backnguoiban);
        backnguoiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tranggiaodienbanhang.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });
        //lay sanpham ra
        productsList = new ArrayList<>();
        productAdapter = new ProductAdapter(tranggiaodienbanhang.this,productsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(tranggiaodienbanhang.this,2);
        dangbansanphamitem.setLayoutManager(gridLayoutManager);
        dangbansanphamitem.setAdapter(productAdapter);
        productCollection = FirebaseFirestore.getInstance().collection("Products");
        AlertDialog.Builder builder = new AlertDialog.Builder(tranggiaodienbanhang.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        productCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý lỗi
                    return;
                }
                productsList.clear();
                for (DocumentSnapshot documentSnapshot : value)
                {
                    Products products = documentSnapshot.toObject(Products.class);
                    productsList.add(products);
                }
                productAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        //the loaisanpham
        categoryCollection = db.collection("Category");
        theloaisanphamnha.setAdapter(adapter);

        categoryList = new ArrayList<>();

        adapter = new CategoryAdapter(tranggiaodienbanhang.this,categoryList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        theloaisanphamnha.addItemDecoration(new ItemSpacingDecoration(spacingInPixels));

        theloaisanphamnha.setLayoutManager(layoutManager);
        theloaisanphamnha.setAdapter(adapter);

        categoryCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    return;
                }
                categoryList.clear();
                for (DocumentSnapshot document : value) {
                    Category dataclass = document.toObject(Category.class);
                    categoryList.add(dataclass);
                }
                adapter.notifyDataSetChanged();
            }
        });
        //giohang
        giaodiengiohang=findViewById(R.id.giaodiengiohang);
        giaodiengiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tranggiaodienbanhang.this, GioHangActivity.class);
                startActivity(intent);
            }
        });
        //dang san pham len
    }
}