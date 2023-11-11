package com.example.doanmobile.dangsanpham;

import androidx.annotation.NonNull;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.doanmobile.R;
import com.example.doanmobile.dangnhap;
import com.example.doanmobile.giohang.GioHangActivity;
import com.example.doanmobile.trangchunguoidung;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    SearchView searchView;
    private List<Integer> favoriteProducts = new ArrayList<>();

    CheckBox cbthapcao, cbcaothap, cbsoluotyeuthich;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranggiaodienbanhang);
        theloaisanphamnha = findViewById(R.id.theloaisanphamnha);
        dangbansanphamitem = findViewById(R.id.dangbansanphamitem);
        searchView = findViewById(R.id.search);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        cbthapcao = findViewById(R.id.cbthapcao);
        cbcaothap = findViewById(R.id.cbcaothap);
        cbsoluotyeuthich = findViewById(R.id.cbsoluotyeuthich);

        //lấy user
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        FirebaseUser user = fAuth.getCurrentUser();

        giaodiengiohang = findViewById(R.id.giaodiengiohang);
        giaodiengiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user == null) {
                    Toast.makeText(tranggiaodienbanhang.this, "Yêu cầu đăng nhập", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tranggiaodienbanhang.this, dangnhap.class);
                    startActivity(intent);
                } else {
                Intent intent = new Intent(tranggiaodienbanhang.this, GioHangActivity.class);
                startActivity(intent);}
            }
        });


        //chuyenvetrangchu
        backnguoiban = findViewById(R.id.backnguoiban);
        backnguoiban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(tranggiaodienbanhang.this, trangchunguoidung.class);
                startActivity(intent);
            }
        });
        //lay sanphamra
        productsList = new ArrayList<>();
        productAdapter = new ProductAdapter(tranggiaodienbanhang.this, productsList);
        productAdapter.updateProductFavoriteStatus(favoriteProducts);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(tranggiaodienbanhang.this, 2);
        dangbansanphamitem.setLayoutManager(gridLayoutManager);
        dangbansanphamitem.setAdapter(productAdapter);
        productCollection = FirebaseFirestore.getInstance().collection("Products");
        AlertDialog.Builder builder = new AlertDialog.Builder(tranggiaodienbanhang.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        loadFavoriteProducts();
        productCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý lỗi
                    return;
                }
                productsList.clear();
                for (DocumentSnapshot documentSnapshot : value) {
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

        adapter = new CategoryAdapter(tranggiaodienbanhang.this, categoryList);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        //phần thêm để lựa chon thể loại sản phẩm
        adapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                int categoryIDToLoad = category.getCategoryID();
                loadProductsByCategory(categoryIDToLoad);

            }
        });

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
        //Tìm kiếm theo tên san pham
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        //Tim kiem theo gia thap cao
        cbthapcao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Khi checkbox "Thấp đến Cao" được chọn
                    // Gọi hàm sắp xếp sản phẩm từ thấp đến cao
                    sortByPriceLowToHigh();
                }
            }
        });
        //Tim kiem theo gia tu cao den thap
        cbcaothap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    sortByPriceHighToLow();
                }
            }
        });

        //tim kiem san pham yeu thich
        cbsoluotyeuthich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    sortByLikeCount();
                }
            }
        });
    }

    //tim kiem theo san pham yeu thich
    private void sortByLikeCount() {
        // Sắp xếp danh sách sản phẩm theo số lượt yêu thích (likeCount)
        Collections.sort(productsList, new Comparator<Products>() {
            @Override
            public int compare(Products product1, Products product2) {
                return Integer.compare(product2.getLikeCount(), product1.getLikeCount());
            }
        });

        // Cập nhật RecyclerView
        productAdapter.notifyDataSetChanged();
    }

    //tim kiem san pham theo the loan
    private void loadProductsByCategory(int categoryID) {
        productCollection.whereEqualTo("categoryID", categoryID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Xử lý lỗi
                            return;
                        }
                        productsList.clear();
                        for (DocumentSnapshot documentSnapshot : value) {
                            Products products = documentSnapshot.toObject(Products.class);
                            productsList.add(products);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }

    //tim kiem theo ten
    public void searchList(String text) {
        ArrayList<Products> searchList = new ArrayList<>();
        for (Products dataClass : productsList) {
            if (dataClass.getTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        productAdapter.searchDataList(searchList);
    }

    //Tim kiem san pham tu thap den cao
    private void sortByPriceLowToHigh() {
        // Sắp xếp danh sách sản phẩm từ thấp đến cao
        Collections.sort(productsList, new Comparator<Products>() {
            @Override
            public int compare(Products product1, Products product2) {
                return Double.compare(product1.getPrice(), product2.getPrice());
            }
        });

        // Cập nhật RecyclerView
        productAdapter.notifyDataSetChanged();
    }

    //Tim kiem san pham tu cao den thap
    private void sortByPriceHighToLow() {
        // Sắp xếp danh sách sản phẩm từ cao đến thấp
        Collections.sort(productsList, new Comparator<Products>() {
            @Override
            public int compare(Products product1, Products product2) {
                return Double.compare(product2.getPrice(), product1.getPrice());
            }
        });

        // Cập nhật RecyclerView
        productAdapter.notifyDataSetChanged();
    }

    private void loadFavoriteProducts() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseUser user = fAuth.getCurrentUser();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = fStore.collection("KhachHang").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        int userID = documentSnapshot.getLong("userID").intValue();

                        // Tiếp tục xử lý với userID tại đây...

                        // Lấy reference đến collection "favorites" và thực hiện truy vấn
                        fStore.collection("favorites")
                                .whereEqualTo("userID", userID)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        favoriteProducts.clear();
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            // Lấy productID từ document
                                            int productID = document.getLong("productID").intValue();
                                            favoriteProducts.add(productID);
                                        }

                                        // Cập nhật vào adapter
                                        if (productAdapter != null) {
                                            productAdapter.updateProductFavoriteStatus(favoriteProducts);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Xảy ra lỗi khi truy vấn Firestore
                                    }
                                });
                    }
                }
            });
        } else {

        }
    }
}