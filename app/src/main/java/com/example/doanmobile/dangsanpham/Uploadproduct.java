package com.example.doanmobile.dangsanpham;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doanmobile.R;
import com.example.doanmobile.dangkynguoiban.quanlysanphamthemsanpham;
import com.example.doanmobile.taicautrucproduct.ConcreteProductPrototype;
import com.example.doanmobile.taicautrucproduct.ProductPrototype;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Uploadproduct extends AppCompatActivity {

    ImageView uploadImage;
    EditText uploadtensanpham,uploadmotasanpham,uploadgiacasanpham;
    EditText uploaddiscountsanpham,uploadsoluongsanpham;
    Spinner categorysanphamnha;
    Button luusanpham;
    FirebaseFirestore db;

    //phần danh mục sản phẩm
    List<String> danhSachDanhMuc;
    ArrayAdapter<String> adapter;
    String imageURL;

    private int shopID;
    private int categoryID;

    Uri uri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadproduct);

        uploadImage = findViewById(R.id.uploadImage);
        uploadtensanpham = findViewById(R.id.uploadtensanpham);
        uploadmotasanpham = findViewById(R.id.uploadmotasanpham);
        uploadgiacasanpham = findViewById(R.id.uploadgiacasanpham);
        categorysanphamnha = findViewById(R.id.categorysanphamnha);
        uploaddiscountsanpham = findViewById(R.id.uploaddiscountsanpham);
        uploadsoluongsanpham = findViewById(R.id.uploadsoluongsanpham);
        luusanpham = findViewById(R.id.luusanpham);
        db = FirebaseFirestore.getInstance();

        //neu vip se xuat hien disscoutn
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        if (user1 != null) {
            String userId = user1.getUid();
            DocumentReference userRef = db.collection("KhachHang").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        boolean isNguoiBan = documentSnapshot.getBoolean("nguoiBan");

                        // Nếu là người bán, hiển thị View dangkynguoibanvip và ẩn View livestream
                        if (isNguoiBan) {
                            uploaddiscountsanpham.setVisibility(View.GONE);
                        } else {
                            
                        }

                        // Chuyển giá trị isNguoiBan thành chuỗi và in vào log
                        Log.d("nguoiban", String.valueOf(isNguoiBan));
                    }
                }
            });
        }
        //danh mục sản phâẩm
        danhSachDanhMuc = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, danhSachDanhMuc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner categorysanphamnha = findViewById(R.id.categorysanphamnha);
        categorysanphamnha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Lấy category name
                String selectedCategory = parent.getSelectedItem().toString();

                // Query lấy category ID
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference categoryRef = db.collection("Category");
                categoryRef.whereEqualTo("categoryName", selectedCategory)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (!querySnapshot.isEmpty()) {
                                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                        categoryID = document.getLong("categoryID").intValue();
                                    }
                                }
                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có item nào được chọn
            }
        });
        categorysanphamnha.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                danhSachDanhMuc.add(category.getCategoryName());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Uploadproduct.this, "Không thể lấy danh mục từ Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //lựa chọn hình ảnh
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(Uploadproduct.this, "Lựa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        luusanpham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    //luu san pham
    public  void saveData()
    {
        String title = uploadtensanpham.getText().toString();
        String mota = uploadmotasanpham.getText().toString();
        double price = Double.parseDouble(uploadgiacasanpham.getText().toString());
        double soluong = Double.parseDouble(uploadsoluongsanpham.getText().toString());

        double selled = 0;
        double reviewcount = 0;
        double discount;
        String discountInput = uploaddiscountsanpham.getText().toString();
        if (!discountInput.isEmpty()) {
            discount = Double.parseDouble(discountInput);
        } else {
            // Nếu không nhập, gán giá trị mặc định cho discount là 0.0
            discount = 0.0;
        }

        db = FirebaseFirestore.getInstance();

        String selectedCategory = categorysanphamnha.getSelectedItem().toString(); // Lấy tên danh mục
        CollectionReference categoryRef = db.collection("Category");
        categoryRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            Category category = querySnapshot.getDocuments().get(0).toObject(Category.class);
                            int categoryId = category.getCategoryID();
                        }
                    }
                });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String documentId = user.getUid(); // Đây là ID của tài khoản người dùng

            DocumentReference docRef = db.collection("Shop").document(documentId);

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        Long shopIdLong = documentSnapshot.getLong("shopId");
                        if (shopIdLong != null) {
                            shopID = shopIdLong.intValue();
                        }
                    }
                }
            });
        }
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("SanPham Images")
                    .child(Objects.requireNonNull(uri.getLastPathSegment()));

            AlertDialog.Builder builder = new AlertDialog.Builder(Uploadproduct.this);
            builder.setCancelable(false);
//


            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(task -> {
                    Uri urlImage = task.getResult();
                    imageURL = urlImage.toString();
                    CollectionReference productsCollection = db.collection("Products");
                    productsCollection.orderBy("productID", Query.Direction.DESCENDING)
                            .limit(1)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                int newproductID = 1;
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        int highestProductID = document.getLong("productID").intValue();
                                        newproductID = highestProductID + 1;
                                    }
                                }
                                uploadData(title, mota, price, 0, categoryID, shopID, soluong, selled, reviewcount, discount);
                            });

                });
            }).addOnFailureListener(e -> {

                Toast.makeText(Uploadproduct.this, "Lỗi khi tải lên hình ảnh", Toast.LENGTH_SHORT).show();
            });
        }
    }
    public void uploadData(String title, String mota, double price, int productID, int categoryID, int shopID,double soluong,double selled, double reviewcount,double discount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Products");
        ConcreteProductPrototype prototypeProduct = new ConcreteProductPrototype(title, mota, price, categoryID, shopID, soluong, selled, reviewcount, discount);

        // Thêm bộ lọc và sắp xếp cho collection
        productsCollection.orderBy("productID", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int newproductID = 1;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            int highestProductID = document.getLong("productID").intValue();
                            newproductID = highestProductID + 1;
                        }
                    }


                    ConcreteProductPrototype product = new ConcreteProductPrototype();
                    product.setProductID(newproductID);
                    product.setShopID(prototypeProduct.getShopID());
                    product.setCategoryID(prototypeProduct.getCategoryID());
                    product.setTitle(prototypeProduct.getTitle());
                    product.setDescription(prototypeProduct.getDescription());
                    product.setPrice(prototypeProduct.getPrice());
                    product.setSoluong(prototypeProduct.getSoluong());
                    product.setSelled(prototypeProduct.getSelled());
                    product.setReviewcount(prototypeProduct.getReviewcount());
                    product.setDiscount(prototypeProduct.getDiscount());
                    product.setImageURL(imageURL);
                    // Tạo một bản sao của đối tượng prototypeProduct
                    ProductPrototype clonedProduct = null;
                    try {
                        clonedProduct = prototypeProduct.clone();
                        clonedProduct.setProductID(newproductID);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                    String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());

                    // Query tại đây với điều kiện phù hợp
                    Query query = productsCollection.whereEqualTo("title", currentDate);
                    query.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                DocumentSnapshot docSnapshot = querySnapshot.getDocuments().get(0);
                                DocumentReference docRef = docSnapshot.getReference();
                                docRef.update("title", title);
                                docRef.update("mota", mota);
                                docRef.update("price", price);
                            } else {
                                // Sản phẩm chưa tồn tại, thêm mới
                                productsCollection.add(product);
                            }
                            Toast.makeText(Uploadproduct.this, "Lưu thông tin", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Uploadproduct.this, quanlysanphamthemsanpham.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Uploadproduct.this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Uploadproduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Uploadproduct.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                });
    }
}