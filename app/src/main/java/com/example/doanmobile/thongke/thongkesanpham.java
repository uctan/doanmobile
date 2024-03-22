package com.example.doanmobile.thongke;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.example.doanmobile.dangsanpham.ProductAdapter;
import com.example.doanmobile.dangsanpham.Products;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class thongkesanpham extends AppCompatActivity {

    List<String> xValues;
    RecyclerView recyclerViewChart;
    List<Products> productsListChart;
    ProductAdapter productAdapter;
    CollectionReference productCollection;
    FirebaseFirestore db;

    private LineChart lineChart;
    private BarChart barChart;
    ImageButton backdonhangshopchart;
    CheckBox chartcot, chartduong;

    private ChartState chartState;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongkesanpham);

        backdonhangshopchart = findViewById(R.id.backdonhangshopchart);
        backdonhangshopchart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thongkesanpham.this, manhinhnguoiban.class);
                startActivity(intent);
            }
        });
        db = FirebaseFirestore.getInstance();
        lineChart = findViewById(R.id.linechartnha);
        chartcot = findViewById(R.id.chartcot);
        chartduong = findViewById(R.id.chartduong);
        barChart = findViewById(R.id.chart);
        recyclerViewChart = findViewById(R.id.recyclerViewChart);
        productsListChart = new ArrayList<>();
        productAdapter = new ProductAdapter(thongkesanpham.this, productsListChart);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(thongkesanpham.this, 2);
        recyclerViewChart.setLayoutManager(gridLayoutManager);
        recyclerViewChart.setAdapter(productAdapter);
        productCollection = FirebaseFirestore.getInstance().collection("Products");

        // Set trạng thái mặc định của checkbox "chartcot" và ẩn LineChart
        chartcot.setChecked(true);
        lineChart.setVisibility(View.GONE);

        chartState = new BarChartState(barChart);
        chartState.showChart();

        chartcot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chartState = new BarChartState(barChart);
                    chartState.showChart();
                } else {
                    chartState.hideChart();
                }
            }
        });

        chartduong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chartState = new LineChartState(lineChart);
                    chartState.showChart();
                } else {
                    chartState.hideChart();
                }
            }
        });

        chartcot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    barChart.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.GONE);
                } else {
                    barChart.setVisibility(View.GONE);
                }
            }
        });

        chartduong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    lineChart.setVisibility(View.VISIBLE);
                    barChart.setVisibility(View.GONE);
                } else {
                    lineChart.setVisibility(View.GONE);
                }
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            db.collection("Shop").document(documentId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
                                List<Entry> entries = new ArrayList<>();
                                List<BarEntry> barEntries = new ArrayList<>();
                                xValues = new ArrayList<>();
                                int index = 0;
                                for (DocumentSnapshot documentSnapshot : value) {
                                    Products products = documentSnapshot.toObject(Products.class);
                                    if (products.getShopID() == shopID && products.getSelled() > 0) {
                                        productsListChart.add(products);
                                        entries.add(new Entry(index, (float) products.getSelled()));
                                        barEntries.add(new BarEntry(index, (float) products.getSelled()));
                                        xValues.add(products.getTitle());
                                        index++;
                                    }
                                }
                                productAdapter.notifyDataSetChanged();

                                LineDataSet lineDataSet = new LineDataSet(entries, "Sản phẩm");
                                lineDataSet.setColors(Color.BLUE);
                                LineData lineData = new LineData(lineDataSet);

                                lineChart.setData(lineData);
                                lineChart.setDescription(null); // Bỏ mô tả
                                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                lineChart.getXAxis().setGranularityEnabled(true);

                                BarDataSet barDataSet = new BarDataSet(barEntries, "Sản phẩm");
                                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                BarData barData = new BarData(barDataSet);

                                barChart.setData(barData);
                                barChart.setDescription(null); // Bỏ mô tả
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