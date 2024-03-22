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

    private AuthenticationStrategy authStrategy1;
    private GoogleSignInClient mGoogleSignInClient;
    private  static final int RC_SIGN_IN = 1;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        authStrategy1 = new GoogleAuthStrategy(mGoogleSignInClient, mAuth, this);

        loginwithgoogle = findViewById(R.id.loginwithgoogle);
        dangkynguoidung = findViewById(R.id.dangkynguoidung);
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
                authStrategy1.authenticate();
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

                // Sử dụng EmailPasswordAuthStrategy để xử lý đăng ký
                AuthenticationStrategy authStrategy = new EmailPasswordAuthStrategy(mAuth, db, dangky.this, email, matKhau, matKhau, tenDayDu, soDienThoai);
                authStrategy.authenticate();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> completedTask = null;
            if (data != null) {
                completedTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            } else {
                // Xử lý trường hợp data là null
                Toast.makeText(this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                return;
            }

            if (completedTask != null) {
                authStrategy1.handleSignInResult(completedTask);
            } else {
                // Xử lý trường hợp task là null
                Toast.makeText(this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }
}