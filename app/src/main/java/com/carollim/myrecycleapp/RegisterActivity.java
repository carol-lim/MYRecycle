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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    Button submit;
    TextInputLayout regFullName, regPassword, regEmail, regPhNum, regAddress, regPostalCode, regCity, regState, regPasswordConfirm;
    CountryCodePicker ccp;
    FirebaseAuth fAuth;
    public static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        regFullName = findViewById(R.id.fullNameReg);
        regEmail = findViewById(R.id.emailReg);
        ccp = findViewById(R.id.countryCodePicker);
        regPhNum = findViewById(R.id.phoneNumReg);
        regAddress = findViewById(R.id.addressReg);
        regPostalCode = findViewById(R.id.postalCodeReg);
        regCity = findViewById(R.id.cityReg);
        regState = findViewById(R.id.stateReg);
        regPassword = findViewById(R.id.passwordReg);
        regPasswordConfirm = findViewById(R.id.confirmPasswordReg);
        submit = findViewById(R.id.buttonRegisterAcc);

        fAuth = FirebaseAuth.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get all values from text views
                String name = regFullName.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String countryCode = ccp.getSelectedCountryCode();
                String phoneNo = regPhNum.getEditText().getText().toString();
                String address = regAddress.getEditText().getText().toString();
                String postalCode = regPostalCode.getEditText().getText().toString();
                String city = regCity.getEditText().getText().toString();
                String state = regState.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String userType = "0";

                if (!validateFullName()){
                    return;
                }else if(!validateEmail()){
                    return;
                }else if(!validatePhNum()){
                    return;
                }else if (!validateAddress()){
                    return;
                }else if (!validatePostalCode()){
                    return;
                }else if (!validateCity()){
                    return;
                }else if (!validateState()){
                    return;
                }else if (!validatePassword()){
                    return;
                }else if (!validatePasswordConfirm()){
                    return;
                } else{
                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser firebaseUser = fAuth.getCurrentUser();

                                // update display name of user
                                /*UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                firebaseUser.updateProfile(profileChangeRequest);*/

                                // store user data into firebase realtime database
                                UserHelperClass helperClass = new UserHelperClass(name, email, countryCode, phoneNo, address, postalCode, city, state, userType);

                                DatabaseReference refRegister = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user");
                                refRegister.child(firebaseUser.getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            // send verification email
                                            firebaseUser.sendEmailVerification();

                                            Toast.makeText(RegisterActivity.this, "Registered successfully. Please verify your email", Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            // prevent go back to register by back button
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }else{
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e){
                                    regEmail.setError("Email is invalid or already in use. Please enter another email.");
                                    regEmail.requestFocus();
                                } catch (FirebaseAuthUserCollisionException e){
                                    regEmail.setError("Email is already registered. Please enter another email.");
                                    regEmail.requestFocus();
                                } catch (Exception e){
                                    Log.e(TAG, e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private Boolean validateFullName(){
        String val = regFullName.getEditText().getText().toString();

        if(val.isEmpty()) {
            regFullName.setError("Field cannot be empty");
            regFullName.requestFocus();
            return false;
        }
        else{
            regFullName.setError(null);
            return true;
        }
    }

   /* private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "(?=\\S+$)"; // or "\\A\\w{4,20}\\z"

        if(val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=15){
            regUsername.setError("Username is too long");
            return false;
        }else if(val.matches(noWhiteSpace)){
            regUsername.setError("White spaces are not allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }*/

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        //String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            regEmail.requestFocus();
            return false;
        }/*else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            regEmail.requestFocus();
            return false;
        }*/
        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            regEmail.setError("Invalid email address");
            regEmail.requestFocus();
            return false;
        }
        else{
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhNum(){
        String val = regPhNum.getEditText().getText().toString();

        if(val.isEmpty()) {
            regPhNum.setError("Field cannot be empty");
            regPhNum.requestFocus();
            return false;
        }
        else{
            regPhNum.setError(null);
            return true;
        }
    }

    private Boolean validateAddress(){
        String val = regAddress.getEditText().getText().toString();

        if(val.isEmpty()) {
            regAddress.setError("Field cannot be empty");
            regAddress.requestFocus();
            return false;
        }
        else{
            regAddress.setError(null);
            return true;
        }
    }

    private Boolean validatePostalCode(){
        String val = regPostalCode.getEditText().getText().toString();

        if(val.isEmpty()) {
            regPostalCode.setError("Field cannot be empty");
            regPostalCode.requestFocus();
            return false;
        }else if (val.length()!=5){
            regPostalCode.setError("Invalid postal code");
            regPostalCode.requestFocus();
            return false;
        }
        else{
            regPostalCode.setError(null);
            return true;
        }
    }

    private Boolean validateCity(){
        String val = regCity.getEditText().getText().toString();

        if(val.isEmpty()) {
            regCity.setError("Field cannot be empty");
            regCity.requestFocus();
            return false;
        }
        else{
            regCity.setError(null);
            return true;
        }
    }

    private Boolean validateState(){
        String val = regState.getEditText().getText().toString();

        if(val.isEmpty()) {
            regState.setError("Field cannot be empty");
            regState.requestFocus();
            return false;
        }
        else{
            regState.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
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
            regPassword.setError("Field cannot be empty");
            regPassword.requestFocus();
            return false;
        }else if(!val.matches(passwordPattern)){
            regPassword.setError("Password is too weak");
            regPassword.requestFocus();
            return false;
        }
        else{
            regPassword.setError(null);
            return true;
        }
    }

    private Boolean validatePasswordConfirm(){
        String val = regPasswordConfirm.getEditText().getText().toString();
        String val2 = regPassword.getEditText().getText().toString();

        if(val.isEmpty()) {
            regPasswordConfirm.setError("Field cannot be empty");
            regPasswordConfirm.requestFocus();
            return false;
        }else if(!val.matches(val2)){
            regPasswordConfirm.setError("Password must be the same");
            regPasswordConfirm.requestFocus();
            return false;
        }
        else{
            regPasswordConfirm.setError(null);
            return true;
        }
    }
}