package com.example.doanmobile;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleAuthStrategy implements AuthenticationStrategy {
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 1;
    private dangky dangKyActivity;

    public GoogleAuthStrategy(GoogleSignInClient mGoogleSignInClient, FirebaseAuth mAuth, dangky dangKyActivity) {
        this.mGoogleSignInClient = mGoogleSignInClient;
        this.mAuth = mAuth;
        this.dangKyActivity = dangKyActivity;
    }

    @Override
    public void authenticate() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        dangKyActivity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Handle sign-in result
    @Override
    public void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {

        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(dangKyActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(dangKyActivity, dangkygmail.class);
                            dangKyActivity.startActivity(intent);
                        } else {
                            Toast.makeText(dangKyActivity, "Đăng nhập thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
