package com.example.doanmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanmobile.dangkynguoiban.manhinhnguoiban;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class dangnhap extends AppCompatActivity {

    EditText Emaildangnhap,Matkhaudangnhap;
    View btnDangNhap;
    TextView chuyensangdangkytaikhoan,quenmatkhau;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    ImageButton dangnhapgmail;
    GoogleSignInClient mGoogleSignInClient;
    private  static final String TAG = "GOOGLEAUTH";
    private  static final int RC_SIGN_IN = 1;
    Dialog dialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        dialog = new Dialog(dangnhap.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCanceledOnTouchOutside(false);

        Emaildangnhap = findViewById(R.id. Emaildangnhap);
        Matkhaudangnhap = findViewById(R.id. Matkhaudangnhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        chuyensangdangkytaikhoan = findViewById(R.id.chuyensangdangkytaikhoan);
        quenmatkhau = findViewById(R.id.quenmatkhau);
        dangnhapgmail = findViewById(R.id.dangnhapgmail);
        dangnhapgmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        firestore = FirebaseFirestore.getInstance();
        quenmatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangnhap.this,com.example.doanmobile.quenmatkhau.class);
                startActivity(intent);
            }
        });

        chuyensangdangkytaikhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangnhap.this, dangky.class);
                startActivity(intent);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Emaildangnhap.getText().toString().trim();
                String matKhau =  Matkhaudangnhap.getText().toString().trim();

                // Kiểm tra tính hợp lệ của dữ liệu
                if (email.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(dangnhap.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, matKhau)
                        .addOnCompleteListener(dangnhap.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String uid = user.getUid();

                                    DocumentReference df = firestore.collection("KhachHang").document(uid);


                                    df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                KhachHang khachHang = documentSnapshot.toObject(KhachHang.class);

                                                if (khachHang != null) {
                                                    if (khachHang.isKhachHang()) {
                                                        Intent intent = new Intent(dangnhap.this, trangchunguoidung.class);
                                                        startActivity(intent);
                                                    } else if (khachHang.isNguoiBan()) {
                                                        Intent intent = new Intent(dangnhap.this, manhinhnguoiban.class);
                                                        startActivity(intent);
                                                    } else if (khachHang.isNguoiBanVip()) {
                                                        Intent intent = new Intent(dangnhap.this, manhinhnguoiban.class);
                                                        startActivity(intent);
                                                    }

                                                    finish();
                                                }
                                            } else {
                                                Toast.makeText(dangnhap.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(dangnhap.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            dialog.show();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                dialog.dismiss();
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Đăng nhap thanh công");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i = new Intent(dangnhap.this,trangchunguoidung.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
                            //  updateUI(user);
                        } else {

                            dialog.dismiss();
                            Toast.makeText(dangnhap.this,"Đăng nhặp thất bại",Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}