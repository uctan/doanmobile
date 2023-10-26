package com.example.doanmobile.dangkynguoiban;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.doanmobile.R;
import com.example.doanmobile.dangsanpham.UploadCategory;

public class quanlysanphamthemsanpham extends AppCompatActivity {

    Button themsanphamcuahang;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanlysanphamthemsanpham);

        themsanphamcuahang = findViewById(R.id.themsanphamcuahang);
        themsanphamcuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quanlysanphamthemsanpham.this, UploadCategory.class);
                startActivity(intent);
            }
        });
    }
}