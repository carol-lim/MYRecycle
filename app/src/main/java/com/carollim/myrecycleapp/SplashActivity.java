package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    //private static final int SPLASH_TIME_OUT = 3000;
    //SharedPreferences sharedPreferences;
    Button btnRegister, btnLogin;

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        btnLogin = findViewById(R.id.buttonLogin);
        btnRegister = findViewById(R.id.buttonRegister);
        fAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);

                // store a boolean value
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);

                Intent intent = new Intent(SplashActivity.this, TestActivity.class);

                if (isFirstTime){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();

                    startActivity(intent);
                    finish();
                }
                else{
                    startActivity(intent);
                    finish();
                }

            }
        },SPLASH_TIME_OUT);*/

    }
    protected void onStart() {
        super.onStart();
        if (fAuth.getCurrentUser()!=null){
            Toast.makeText(SplashActivity.this, "Already logged in.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}