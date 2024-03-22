package com.example.doanmobile;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EmailPasswordAuthStrategy implements AuthenticationStrategy {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private dangky dangKyActivity;
    private String matKhau, tenDayDu, soDienThoai, emailField, passwordField;

    public EmailPasswordAuthStrategy(FirebaseAuth mAuth, FirebaseFirestore db, dangky dangKyActivity,
                                     String emailField, String passwordField, String matKhau, String tenDayDu, String soDienThoai) {
        this.mAuth = mAuth;
        this.db = db;
        this.dangKyActivity = dangKyActivity;
        this.emailField = emailField;
        this.passwordField = passwordField;
        this.matKhau = matKhau;
        this.tenDayDu = tenDayDu;
        this.soDienThoai = soDienThoai;
    }

    @Override
    public void authenticate() {
        mAuth.createUserWithEmailAndPassword(emailField, passwordField)
                .addOnCompleteListener(dangKyActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            DocumentReference df = db.collection("KhachHang").document(uid);

                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        Toast.makeText(dangKyActivity, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.collection("KhachHang")
                                                .orderBy("userID", Query.Direction.DESCENDING)
                                                .limit(1)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        int newUserID = 1;
                                                        if (!queryDocumentSnapshots.isEmpty()) {
                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                int highestUserID = document.getLong("userID").intValue();
                                                                newUserID = highestUserID + 1;
                                                            }
                                                        }
                                                        KhachHang khachHang = new KhachHang(newUserID, matKhau, emailField, tenDayDu, soDienThoai, true, false, false);
                                                        db.collection("KhachHang")
                                                                .document(uid)
                                                                .set(khachHang)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(dangKyActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                                        if (khachHang.isKhachHang()) {
                                                                            Intent intent = new Intent(dangKyActivity, trangchunguoidung.class);
                                                                            dangKyActivity.startActivity(intent);
                                                                        } else if (khachHang.isNguoiBan()) {
                                                                            Intent intent = new Intent(dangKyActivity, manhinhnguoiban.class);
                                                                            dangKyActivity.startActivity(intent);
                                                                        } else if (khachHang.isNguoiBanVip()) {
                                                                            Intent intent = new Intent(dangKyActivity, manhinhnguoiban.class);
                                                                            dangKyActivity.startActivity(intent); // Chuyển hướng đến giao diện B hoặc giao diện khác cho người bán VIP
                                                                        }
                                                                        dangKyActivity.finish();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(dangKyActivity, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(dangKyActivity, "Lỗi khi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

    }
}