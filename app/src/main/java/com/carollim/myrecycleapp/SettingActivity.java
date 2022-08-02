package com.carollim.myrecycleapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    TextView editProfile, changePswd, logout;
    private FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        editProfile = findViewById(R.id.btnEditProfile);
        changePswd = findViewById(R.id.btnChangePassword);
        logout = findViewById(R.id.btnlogout);
        fAuth = FirebaseAuth.getInstance();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        changePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Toast.makeText(SettingActivity.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

    }
}