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


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_category);
        uploadtheloai = findViewById(R.id.uploadtheloai);
        luuButton = findViewById(R.id.luuButton);
        boquaButton = findViewById(R.id.boquaButton);
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
                    // Lưu Category lên Firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("Category")
                            .orderBy("categoryID", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    int newcategoryID = 1;
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            int highestCategoryID = document.getLong("categoryID").intValue();
                                            newcategoryID = highestCategoryID + 1;
                                        }
                                    }

                                    // Tạo một đối tượng Category với categoryID mới
                                    Category category = new Category(newcategoryID, categoryName);

                                    // Thêm category vào Firestore
                                    db.collection("Category")
                                            .add(category)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(UploadCategory.this,"Thêm thể loại sản phẩm thành công",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UploadCategory.this,Uploadproduct.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Xử lý khi có lỗi xảy ra
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi có lỗi xảy ra
                                }
                            });
                }
            }
        });
    }
}