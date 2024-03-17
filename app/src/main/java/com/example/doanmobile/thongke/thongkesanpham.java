package com.example.doanmobile.thongke;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.quanlysanphamthemsanpham;
import com.example.doanmobile.dangsanpham.ProductAdapter;
import com.example.doanmobile.dangsanpham.Products;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class thongkesanpham extends AppCompatActivity {

    List<String> xValues;
    RecyclerView recyclerViewChart;
    List<Products> productsListChart;
    ProductAdapter productAdapter;
    CollectionReference productCollection;
    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongkesanpham);

        db = FirebaseFirestore.getInstance();
        recyclerViewChart = findViewById(R.id.recyclerViewChart);
        productsListChart = new ArrayList<>();
        productAdapter = new ProductAdapter(thongkesanpham.this, productsListChart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thongkesanpham.this, 2);
        recyclerViewChart.setLayoutManager(gridLayoutManager);
        recyclerViewChart.setAdapter(productAdapter);
        productCollection = FirebaseFirestore.getInstance().collection("Products");

        BarChart barChart = findViewById(R.id.chart);
        barChart.getAxisRight().setDrawLabels(false);
        xValues = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        int shopID = documentSnapshot.getLong("shopId").intValue();
                        productCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    // Xử lý lỗi
                                    return;
                                }
                                productsListChart.clear();
                                ArrayList<BarEntry> entries = new ArrayList<>();
                                for (DocumentSnapshot documentSnapshot : value) {
                                    Products products = documentSnapshot.toObject(Products.class);
                                    if (products.getShopID() == shopID && products.getSelled() > 1) {
                                        productsListChart.add(products);
                                        entries.add(new BarEntry(entries.size(), (float) products.getSelled()));
                                        xValues.add(products.getTitle());
                                    }
                                }
                                productAdapter.notifyDataSetChanged();

                                BarDataSet dataSet = new BarDataSet(entries, "Sản phẩm");
                                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                BarData barData = new BarData(dataSet);
                                barChart.setData(barData);

                                barChart.getDescription().setEnabled(false);
                                barChart.invalidate();

                                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                                barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                barChart.getXAxis().setGranularityEnabled(true);
                            }
                        });
                    }
                }
            });
        }
    }
}