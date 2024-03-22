package com.example.doanmobile.dangsanpham;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UploadCategory extends AppCompatActivity {

    EditText uploadtheloai;
    Button luuButton, boquaButton;
    private CategoryManager categoryManager;
    private CategoryObserver categoryObserver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_category);
        uploadtheloai = findViewById(R.id.uploadtheloai);
        luuButton = findViewById(R.id.luuButton);
        boquaButton = findViewById(R.id.boquaButton);

        categoryManager = new CategoryManager();
        categoryObserver = new CategoryObserver();


        boquaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadCategory.this,Uploadproduct.class);
                startActivity(intent);
            }
        });

        luuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = uploadtheloai.getText().toString();
                if (!categoryName.isEmpty()) {
                    Category category = new Category(0, categoryName); // categoryID tạm thời là 0
                    categoryManager.addCategory(category,view.getContext());

                }
            }
        });
    }
}