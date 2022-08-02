package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReserveSelectDateActivity extends AppCompatActivity {
    DatePicker datePicker;
    Button buttonDateContinue;
    String selectedDate, address, category;
    TextView addr, cat;
    public static final String TAG = "RSelectDateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_select_date);

        addr = findViewById(R.id.txtSelectedAddress);
        cat = findViewById(R.id.txtSelectedCategory);

        /*Bundle extras = getIntent().getExtras();
        address = extras.getString("address");
        category = extras.getString("category");*/
        address = savedInstanceState.getString("address");
        category = savedInstanceState.getString("selectedCategory");
        addr.setText(address);
        cat.setText(category);

        datePicker = findViewById(R.id.datePicker);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);

        buttonDateContinue = findViewById(R.id.buttonDateContinue);
        buttonDateContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveSelectDateActivity.this, ReserveSelectTimeActivity.class);
                selectedDate = getSelectedDate();
                intent.putExtra("selectedDate",selectedDate);
                startActivity(intent);
            }
        });

    }

    public String getSelectedDate(){
        StringBuilder builder = new StringBuilder();
        // date: dd/mm/yyyy
        builder.append(datePicker.getDayOfMonth() + "/");
        builder.append((datePicker.getMonth() + 1) + "/");//month is 0 based
        builder.append(datePicker.getYear());
        return builder.toString();
    }


}