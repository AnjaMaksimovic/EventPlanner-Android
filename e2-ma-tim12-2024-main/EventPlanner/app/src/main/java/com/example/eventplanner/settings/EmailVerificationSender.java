package com.example.eventplanner.settings;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class EmailVerificationSender {
    public static void SendVerificationEmails(){
        HashMap<String, String> users = setUsers();
        users.forEach((email, password) -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        if(user.isEmailVerified()) return;
                        user.sendEmailVerification();
                    }
                }
            });
        });
    }


    //If needs to verify new emails, just change existing ones
    private static HashMap<String, String> setUsers(){
        HashMap<String, String> users = new HashMap<>();
        users.put("natalijapopovic.rs@gmail.com", "123456");
        return users;
    }
}
