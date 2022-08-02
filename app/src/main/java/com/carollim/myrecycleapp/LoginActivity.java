package com.carollim.myrecycleapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextInputLayout email, password;
    TextView forgotPassword;
    FirebaseAuth fAuth;
    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.buttonLoginAcc);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.textViewForgotPassword);

        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);*/
                loginUser(v);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser(View view){
        String loginEmail = email.getEditText().getText().toString();
        String loginPassword = password.getEditText().getText().toString();
        if (!validateEmail()){
            return;
        } else if (!validatePassword()){
            return;
        }else{
            //isUser();

            fAuth.signInWithEmailAndPassword(loginEmail,loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser firebaseUser = fAuth.getCurrentUser();
                        if (firebaseUser.isEmailVerified()){
                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            // prevent go back to register by back button
                            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            firebaseUser.sendEmailVerification();
                            fAuth.signOut();
                            showAlertDialog();
                        }
                    }else{
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e){
                            email.setError("User does not exists or is no longer valid. Please register again.");
                            email.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e){
                            password.setError("Invalid credentials. Please check and re-enter");
                            password.requestFocus();
                        } catch (Exception e){
                            Log.e(TAG, e.getMessage());
                            Toast.makeText(LoginActivity.this, "Login Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You cannot login without email verification.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // goto email app
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (fAuth.getCurrentUser()!=null){
            //Toast.makeText(LoginActivity.this, "Already logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(LoginActivity.this, "You can login now.", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean validateEmail(){
        String val = email.getEditText().getText().toString();

        if(val.isEmpty()) {
            email.setError("Field cannot be empty");
            email.requestFocus();
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            email.setError("Invalid email address");
            email.requestFocus();
            return false;
        }
        else{
            email.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if(val.isEmpty()) {
            password.setError("Field cannot be empty");
            password.requestFocus();
            return false;
        }else{
            password.setError(null);
            //email.setErrorEnabled(false);
            return true;
        }
    }



/*    private void isUser() {
        String inputEmail = email.getEditText().getText().toString().trim();
        String inputPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance("https://myrecycleapp-f1c49-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("user");
        Query checkUser = reference.orderByChild("email").equalTo(inputEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    email.setError(null);
                    email.setErrorEnabled(false);

                    String retrievedPassword = snapshot.child(inputEmail).child("password").getValue(String.class);

                    if (retrievedPassword.equals(inputPassword)){
                        email.setError(null);
                        email.setErrorEnabled(false);

                        String retrievedName = snapshot.child(inputEmail).child("name").getValue(String.class);
                        String retrievedUsername = snapshot.child(inputEmail).child("username").getValue(String.class);
                        String retrievedPhNum = snapshot.child(inputEmail).child("phNum").getValue(String.class);
                        String retrievedEmail = snapshot.child(inputEmail).child("email").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), TestActivity.class);

                        intent.putExtra("name", retrievedName);
                        intent.putExtra("username", retrievedUsername);
                        intent.putExtra("phNum", retrievedPhNum);
                        intent.putExtra("password", retrievedPassword);
                        intent.putExtra("email", retrievedEmail);

                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                    }else{
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }else{
                    email.setError("Username doesn't exists");
                    email.requestFocus();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/
}