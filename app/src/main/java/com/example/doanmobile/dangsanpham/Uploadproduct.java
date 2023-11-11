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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.doanmobile.R;
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
import java.util.List;
import java.util.Objects;

public class Uploadproduct extends AppCompatActivity {

    ImageView uploadImage;
    EditText uploadtensanpham,uploadmotasanpham,uploadgiacasanpham;
    Spinner categorysanphamnha;
    Button luusanpham;
    FirebaseFirestore db;

    //phần danh mục sản phẩm
    List<String> danhSachDanhMuc;
    ArrayAdapter<String> adapter;
    String imageURL;

    private int shopID;
    private int categoryID;
    private static int currentProductID = 0;

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
        luusanpham = findViewById(R.id.luusanpham);
        db = FirebaseFirestore.getInstance();
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




        // Thêm đoạn code sau vào phương thức onCreate
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
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(task -> {
                    Uri urlImage = task.getResult();
                    imageURL = urlImage.toString();
                    currentProductID++; // Tăng giá trị currentProductID
                    uploadData(title, mota, price, currentProductID, categoryID, shopID); // Truyền currentProductID vào hàm uploadData
                    if (!isFinishing()) {
                        dialog.show();
                    }
                });
            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(Uploadproduct.this, "Lỗi khi tải lên hình ảnh", Toast.LENGTH_SHORT).show();
            });
        }
    }
    public  void uploadData (String title,String mota, double price,int productID, int categoryID,int shopID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsCollection = db.collection("Products");
        currentProductID++;
        Products product = new Products(currentProductID, shopID, categoryID, title, mota, price, imageURL,0);
        String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
        Query query = productsCollection.whereEqualTo("title", currentDate);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty())
                {
                    DocumentReference docRef = querySnapshot.getDocuments().get(0).getReference();
                    docRef.update("title",title);
                    docRef.update("mota",mota);
                    docRef.update("price",price);
                }else {
                    // Sản phẩm chưa tồn tại, thêm mới
                    productsCollection.add( product);
                }
                Toast.makeText(Uploadproduct.this, "Lưu thông tin", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(Uploadproduct.this, "Lỗi khi lưu thông tin", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(Uploadproduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}