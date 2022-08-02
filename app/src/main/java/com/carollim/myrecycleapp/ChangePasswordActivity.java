package com.carollim.myrecycleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;
    TextInputLayout currentPassword, passwordChange, confirmPasswordChange;
    Button buttonSubmitChangePassword, buttonVerifyCurrentPassword;
    String currentP, pChange, confirmPChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.currentPassword);
        passwordChange = findViewById(R.id.passwordChange);
        confirmPasswordChange = findViewById(R.id.confirmPasswordChange);
        buttonSubmitChangePassword = findViewById(R.id.buttonSubmitChangePassword);
        buttonVerifyCurrentPassword = findViewById(R.id.buttonVerifyCurrentPassword);

        passwordChange.setEnabled(false);
        confirmPasswordChange.setEnabled(false);
        buttonSubmitChangePassword.setEnabled(false);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();


        if (firebaseUser.equals("")){
            Toast.makeText(ChangePasswordActivity.this, "Something went wrong. User's detail not available.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            reAuthenticateUser(firebaseUser);
        }

    }

    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonVerifyCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentP = currentPassword.getEditText().getText().toString();

                if (TextUtils.isEmpty(currentP)){
                    Toast.makeText(ChangePasswordActivity.this, "Password is needed.", Toast.LENGTH_SHORT).show();
                    currentPassword.setError("Please enter current password");
                    currentPassword.requestFocus();
                }else{
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), currentP);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                passwordChange.setEnabled(true);
                                confirmPasswordChange.setEnabled(true);
                                buttonSubmitChangePassword.setEnabled(true);

                                buttonVerifyCurrentPassword.setEnabled(false);
                                currentPassword.setEnabled(false);

                                Toast.makeText(ChangePasswordActivity.this, "Password is verified.", Toast.LENGTH_SHORT).show();

                                buttonSubmitChangePassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changePassword(firebaseUser);
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(ChangePasswordActivity.this, "Change Password Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private void changePassword(FirebaseUser firebaseUser) {
        pChange = passwordChange.getEditText().getText().toString();
        confirmPChange = confirmPasswordChange.getEditText().getText().toString();

        if (!validatePassword()){
            return;
        }else if (!validatePasswordConfirm()){
            return;
        }else{
            firebaseUser.updatePassword(pChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChangePasswordActivity.this, "Changed password successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                        // prevent go back to register by back button
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "Change Password Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

        
    }

    private Boolean validatePassword(){
        String val = passwordChange.getEditText().getText().toString();
        String passwordPattern = "^" +
                "(?=.*[0-9])" + // at least 1 digit
                "(?=.*[a-z])" + // at least 1 lower case letter
                "(?=.*[A-Z])" + // at least 1 upper case letter
                "(?=.*[a-zA-Z])" + // any letter
                "(?=.*[@#$%^&+=.\\-*])" + // at least 1 special character
                "(?=\\S+$)" + // no white space
                ".{4,}" + // at least 4 characters
                "$";

        if(val.isEmpty()) {
            passwordChange.setError("Field cannot be empty");
            passwordChange.requestFocus();
            return false;
        }else if(!val.matches(passwordPattern)){
            passwordChange.setError("Password is too weak");
            passwordChange.requestFocus();
            return false;
        }
        else{
            passwordChange.setError(null);
            return true;
        }
    }

    private Boolean validatePasswordConfirm(){
        String val = confirmPasswordChange.getEditText().getText().toString();
        String val2 = passwordChange.getEditText().getText().toString();

        if(val.isEmpty()) {
            confirmPasswordChange.setError("Field cannot be empty");
            confirmPasswordChange.requestFocus();
            return false;
        }else if(!val.matches(val2)){
            confirmPasswordChange.setError("Password must be the same");
            confirmPasswordChange.requestFocus();
            return false;
        }
        else{
            confirmPasswordChange.setError(null);
            return true;
        }
    }
}