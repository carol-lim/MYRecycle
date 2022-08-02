package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReserveSelectCategoryActivity extends AppCompatActivity /*implements CatalogFragment.onSomeEventListener*/ {

    Button buttonCategoryContinue;
    TextView txtSelectedAddress;
    String address, selectedCategory;
    Boolean isReserving = true;
    public static final String TAG = "RSelectCategoryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_select_category);

        // assign layout
        buttonCategoryContinue = findViewById(R.id.buttonCategoryContinue);
        txtSelectedAddress = findViewById(R.id.txtSelectedAddress);

        // retrieve selected address
        /*Bundle extras = getIntent().getExtras();
        address = extras.getString("address");*/


        address = savedInstanceState.getString("address");
        txtSelectedAddress.setText(address);

        // retrieve ReserveNowFragment indicator to be passed to catalog fragment
//        reserveNowFragmentIndicator = extras.getString("ReserveNowFragment");

        // display catalog fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.reserveCatalogFCV, CatalogFragment.class, null)
                    .commit();
        }

        // if continue button is clicked (from selecting category to select date)
        buttonCategoryContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveSelectCategoryActivity.this, ReserveSelectDateActivity.class);
                /*Bundle extras = new Bundle();
                extras.putString("address",address);
                extras.putString("category",selectedCategory);
                intent.putExtras(extras);*/

                startActivity(intent);
            }
        });
    }

    // catalog fragment retrieve ReserveNowFragment indicator from this function
    /*public String getReserveNowFragmentIndicator(){
        if (!reserveNowFragmentIndicator.isEmpty())
            Log.d(TAG, "getReserveNowFragmentIndicator: " + reserveNowFragmentIndicator);
        return reserveNowFragmentIndicator;
    }*/

   /* @Override
    public void someEvent(String s) {
        selectedCategory = s;

    }*/
/*
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // put string value
        outState.putBoolean("isReserving",isReserving);
        //outState.putString("address",address);
        //outState.putString("selectedCategory",selectedCategory);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get values from saved state
        if (savedInstanceState != null) {
            isReserving = savedInstanceState.getBoolean("isReserving");
            address = savedInstanceState.getString("address");
            //selectedCategory=savedInstanceState.getString("selectedCategory");
        }

    }*/
}