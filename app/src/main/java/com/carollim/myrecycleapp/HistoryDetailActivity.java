package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class HistoryDetailActivity extends AppCompatActivity {

    TextView txtCategory, txtWeight, txtPrice, txtUnitPrice, txtCollector,
            txtDate, txtTime, txtPay;
    ImageView imageviewHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        txtCategory = findViewById(R.id.txtCategory);
        txtWeight = findViewById(R.id.txtWeight);
        txtPrice = findViewById(R.id.txtPrice);
        txtUnitPrice = findViewById(R.id.txtUnitPrice);
        txtCollector = findViewById(R.id.txtCollector);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtPay = findViewById(R.id.txtPay);
        imageviewHistory = findViewById(R.id.imageviewHistory);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        txtCategory.setText(extras.getString("category"));
        txtWeight.setText(extras.getString("weight")+"kg");
        txtPrice.setText("RM"+extras.getString("price"));
        txtUnitPrice.setText("RM"+extras.getString("unitPrice"));
        txtCollector.setText(extras.getString("collector"));
        txtDate.setText(extras.getString("date"));
        txtPay.setText(extras.getString("pay"));
        txtTime.setText(extras.getString("time"));
        Picasso.get().load(extras.getString("image")).into(imageviewHistory);

    }
}