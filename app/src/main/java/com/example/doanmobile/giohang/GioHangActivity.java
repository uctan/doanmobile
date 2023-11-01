package com.example.doanmobile.giohang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.CartAdapter;
import com.example.doanmobile.dangsanpham.CartItem;
import com.example.doanmobile.dangsanpham.CartManager;
import com.example.doanmobile.dangsanpham.ProductAdapter;
import com.example.doanmobile.dangsanpham.Products;
import com.example.doanmobile.dangsanpham.chitietsanpham;
import com.example.doanmobile.dangsanpham.tranggiaodienbanhang;
import com.example.doanmobile.trangchunguoidung;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    private ListView listView;
    private TextView totalTextView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    ImageView cong,tru,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        listView = findViewById(R.id.listView);
        totalTextView = findViewById(R.id.totalTextView);
        cartItems = CartManager.getInstance().getCartItems();
        cartAdapter = new CartAdapter(this, cartItems);
        listView.setAdapter(cartAdapter);
        updateTotalPrice();

        CartManager.getInstance().setOnCartChangeListener(new CartManager.OnCartChangeListener() {
            @Override
            public void onCartChanged() {
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });
        back=findViewById(R.id.backgiohang1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GioHangActivity.this,tranggiaodienbanhang.class);
                startActivity(intent);
            }
        });
    }
    private void updateTotalPrice() {
        double total = CartManager.getInstance().getTotalPrice();
        totalTextView.setText("Tổng cộng: " + total + " VNĐ");
    }
}