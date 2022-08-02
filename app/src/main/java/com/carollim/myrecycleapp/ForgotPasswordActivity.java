package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailFP;
    private Button buttonFPSubmit;
    private FirebaseAuth fAuth;
    public static final String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailFP = findViewById(R.id.emailFP);
        buttonFPSubmit = findViewById(R.id.buttonFPSubmit);
        fAuth = FirebaseAuth.getInstance();

        buttonFPSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailFP.getEditText().getText().toString();

                if(!validateEmail()){
                    return;
                }else{
                    fAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPasswordActivity.this, "Please check your email inbox or spam for password reset link", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                // prevent go back to register by back button
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e){
                                    emailFP.setError("User does not exists or is no longer valid. Please register again.");
                                    emailFP.requestFocus();
                                } catch (Exception e){
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(ForgotPasswordActivity.this, "Forgot Password Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }

                        }
                    });
                }
            }
        });

    }

    private Boolean validateEmail(){
        String val = emailFP.getEditText().getText().toString();
        //String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()) {
            emailFP.setError("Field cannot be empty");
            emailFP.requestFocus();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            emailFP.setError("Invalid email address");
            emailFP.requestFocus();
            return false;
        }
        else{
            emailFP.setError(null);
            return true;
        }
    }
}