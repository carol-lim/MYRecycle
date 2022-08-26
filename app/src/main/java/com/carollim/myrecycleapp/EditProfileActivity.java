package com.carollim.myrecycleapp;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;

    private TextInputLayout
            fullNameEP,
            emailEP,
            phoneNumEP,
            addressEP,
            postalCodeEP,
            cityEP,
            stateEP;
    CountryCodePicker  countryCodePicker;
    private Button buttonSubmitEditProfile;
    private String fullName,
            email,
            ccp,
            phoneNum,
            address,
            postalCode,
            city,
            state,
            userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameEP = findViewById(R.id.fullNameEP);
        emailEP = findViewById(R.id.emailEP);
        countryCodePicker = findViewById(R.id.countryCodePicker);
        phoneNumEP = findViewById(R.id.phoneNumEP);
        addressEP = findViewById(R.id.addressEP);
        postalCodeEP = findViewById(R.id.postalCodeEP);
        cityEP = findViewById(R.id.cityEP);
        stateEP = findViewById(R.id.stateEP);
        buttonSubmitEditProfile = findViewById(R.id.buttonSubmitEditProfile);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();

        showProfile(firebaseUser);

        buttonSubmitEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullNameEP.getEditText().getText().toString();
                String email = emailEP.getEditText().getText().toString();
                String countryCode = countryCodePicker.getSelectedCountryCode();
                String phoneNo = phoneNumEP.getEditText().getText().toString();
                String address = addressEP.getEditText().getText().toString();
                String postalCode = postalCodeEP.getEditText().getText().toString();
                String city = cityEP.getEditText().getText().toString();
                String state = stateEP.getEditText().getText().toString();


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
                } else{
                    UserHelperClass helperClass = new UserHelperClass(name, email, countryCode, phoneNo, address, postalCode, city, state, userType);
                    FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
                    DatabaseReference refEditProfile = database.getReference("user");

                    refEditProfile.child(firebaseUser.getUid()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                /*UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                                firebaseUser.updateProfile(profileChangeRequest);*/
                                Toast.makeText(EditProfileActivity.this, "Edited successfully.", Toast.LENGTH_SHORT).show();

                                finish();
                                startActivity(getIntent());

                            } else {
                                try {
                                    throw task.getException();
                                } catch (Exception e) {
                                    Toast.makeText(EditProfileActivity.this, "Edit Profile Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference refProfile = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user");
        refProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                    fullName = userHelperClass.name;
                    email = userHelperClass.email;
                    ccp= userHelperClass.countryCode;
                    phoneNum= userHelperClass.phNum;
                    address= userHelperClass.address;
                    postalCode= userHelperClass.postalCode;
                    city= userHelperClass.city;
                    state= userHelperClass.state;
                    userType= userHelperClass.userType;

                    fullNameEP.getEditText().setText(fullName);
                    emailEP.getEditText().setText(email);
                    emailEP.setEnabled(false);
                    countryCodePicker.setCountryForPhoneCode(Integer.parseInt(ccp));
                    phoneNumEP.getEditText().setText(phoneNum);
                    addressEP.getEditText().setText(address);
                    postalCodeEP.getEditText().setText(postalCode);
                    cityEP.getEditText().setText(city);
                    stateEP.getEditText().setText(state);
                }else {
                    Toast.makeText(EditProfileActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private Boolean validateFullName(){
        String val = fullNameEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            fullNameEP.setError("Field cannot be empty");
            fullNameEP.requestFocus();
            return false;
        }
        else{
            fullNameEP.setError(null);
            return true;
        }
    }
    private Boolean validateEmail(){
        String val = emailEP.getEditText().getText().toString();
        //String emailPattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()) {
            emailEP.setError("Field cannot be empty");
            emailEP.requestFocus();
            return false;
        }/*else if(!val.matches(emailPattern)){
            emailEP.setError("Invalid email address");
            emailEP.requestFocus();
            return false;
        }*/
        else if(!Patterns.EMAIL_ADDRESS.matcher(val).matches()){
            emailEP.setError("Invalid email address");
            emailEP.requestFocus();
            return false;
        }
        else{
            emailEP.setError(null);
            return true;
        }
    }

    private Boolean validatePhNum(){
        String val = phoneNumEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            phoneNumEP.setError("Field cannot be empty");
            phoneNumEP.requestFocus();
            return false;
        }
        else{
            phoneNumEP.setError(null);
            return true;
        }
    }

    private Boolean validateAddress(){
        String val = addressEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            addressEP.setError("Field cannot be empty");
            addressEP.requestFocus();
            return false;
        }
        else{
            addressEP.setError(null);
            return true;
        }
    }

    private Boolean validatePostalCode(){
        String val = postalCodeEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            postalCodeEP.setError("Field cannot be empty");
            postalCodeEP.requestFocus();
            return false;
        }else if (val.length()!=5){
            postalCodeEP.setError("Invalid postal code");
            postalCodeEP.requestFocus();
            return false;
        }
        else{
            postalCodeEP.setError(null);
            return true;
        }
    }

    private Boolean validateCity(){
        String val = cityEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            cityEP.setError("Field cannot be empty");
            cityEP.requestFocus();
            return false;
        }
        else{
            cityEP.setError(null);
            return true;
        }
    }

    private Boolean validateState(){
        String val = stateEP.getEditText().getText().toString();

        if(val.isEmpty()) {
            stateEP.setError("Field cannot be empty");
            stateEP.requestFocus();
            return false;
        }
        else{
            stateEP.setError(null);
            return true;
        }
    }
}