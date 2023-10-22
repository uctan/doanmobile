package com.example.doanmobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class dangky extends AppCompatActivity {

    EditText Tendaydu, Sodienthoai, Email, Matkhau;
    ImageButton btnDangKiTaiKhoan;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView chuyensangdangnhap;
    CheckBox dangkynguoidung;
    ImageButton loginwithgoogle;


    private GoogleSignInClient mGoogleSignInClient;
    private  static final int RC_SIGN_IN = 1;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        KhachHang khachHang = new KhachHang();



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Thêm dòng này để khởi tạo db

        loginwithgoogle = findViewById(R.id.loginwithgoogle);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        dangkynguoidung = findViewById(R.id.dangkynguoidung);
        dangkynguoidung.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(compoundButton.isChecked()) {
                  khachHang.setNguoiBan(false);
                  khachHang.setNguoiBanVip(false);
              }
            }
        });

        Tendaydu = findViewById(R.id.Tendaydu);
        Sodienthoai = findViewById(R.id.Sodienthoai);
        Email = findViewById(R.id.Email);
        Matkhau = findViewById(R.id.Matkhau);
        btnDangKiTaiKhoan = findViewById(R.id.BtnDangKiTaiKhoan);
        chuyensangdangnhap = findViewById(R.id.chuyensangdangnhap);

        chuyensangdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dangky.this, dangnhap.class);
                startActivity(intent);
            }
        });
        //dangnhap bang gg
        loginwithgoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithGmail();
            }
        });

        btnDangKiTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenDayDu = Tendaydu.getText().toString().trim();
                String soDienThoai = Sodienthoai.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String matKhau = Matkhau.getText().toString().trim();



                if (!(dangkynguoidung.isChecked())){
                    Toast.makeText(dangky.this,"Vui lòng nhấn đồng ý",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tenDayDu.isEmpty() || soDienThoai.isEmpty() || email.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(dangky.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, matKhau)
                        .addOnCompleteListener(dangky.this, new OnCompleteListener<AuthResult>() {
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
                                                Toast.makeText(dangky.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
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


                                                                KhachHang khachHang = new KhachHang(newUserID, matKhau, email, tenDayDu, soDienThoai, true, false, false);
                                                                db.collection("KhachHang")
                                                                        .document(uid)
                                                                        .set(khachHang)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(dangky.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                                                                if (khachHang.isKhachHang()) {
                                                                                    Intent intent = new Intent(dangky.this,trangchunguoidung.class);
                                                                                    startActivity(intent);
                                                                                } else if (khachHang.isNguoiBan()) {
                                                                                    Intent intent = new Intent(dangky.this, manhinhnguoiban.class);
                                                                                    startActivity(intent);
                                                                                } else if (khachHang.isNguoiBanVip()) {
                                                                                    Intent intent = new Intent(dangky.this, manhinhnguoiban.class); // Chuyển hướng đến giao diện B hoặc giao diện khác cho người bán VIP
                                                                                    startActivity(intent);
                                                                                }
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(dangky.this, "Lỗi khi đăng ký", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(dangky.this, "Lỗi khi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void registerWithGmail() {
        // Gọi intent để thực hiện đăng ký bằng Gmail
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Tiếp tục xử lý tại đây (nếu cần)
                            Intent intent = new Intent(dangky.this, dangkygmail.class);
                            startActivity(intent);
                        } else {
                            // Đăng nhập thất bại
                            Log.d(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(dangky.this, "Đăng nhập thất bại.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Đăng ký bằng Gmail thành công, thực hiện các bước tiếp theo
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Đăng ký bằng Gmail thất bại
                Log.w(TAG, "Đăng nhập thất bại" + e.getStatusCode());
            }
        }
    }
}