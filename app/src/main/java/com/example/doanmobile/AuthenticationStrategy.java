package com.example.doanmobile;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface AuthenticationStrategy {
    void authenticate();

    // dangky google
    void handleSignInResult(Task<GoogleSignInAccount> completedTask);
}
