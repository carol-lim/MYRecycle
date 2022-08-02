package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        /* bottom navigation start */
        navController = Navigation.findNavController(findViewById(R.id.fragment));
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, this.navController);

        appBarConfiguration = new AppBarConfiguration
                .Builder(
                    R.id.homeFragment2,
                    R.id.historyFragment2,
                    R.id.catalogFragment2,
                    R.id.recognizeFragment2,
                    R.id.profileFragment2)
                .build();

        NavigationUI.setupActionBarWithNavController(this, this.navController, appBarConfiguration);
        /* bottom navigation end */

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}