package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReserveSelectTimeActivity extends AppCompatActivity {

    Button buttonTimeContinue;
    TextView txtSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_select_time);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("selectedDate");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("selectedDate");
        }

        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedDate.setText(newString);

        /* data list for recycler view start */
        ReserveSelectTimeListData[] myListData = new ReserveSelectTimeListData[] {
                new ReserveSelectTimeListData("10:00 am"),
                new ReserveSelectTimeListData("11:00 am"),
                new ReserveSelectTimeListData("12:00 am"),
                new ReserveSelectTimeListData("01:00 pm"),
                new ReserveSelectTimeListData("02:00 pm"),
                new ReserveSelectTimeListData("03:00 pm"),
                new ReserveSelectTimeListData("04:00 pm"),
                new ReserveSelectTimeListData("05:00 pm"),
                new ReserveSelectTimeListData("06:00 pm"),

        };

        RecyclerView recyclerView = findViewById(R.id.selectTimeRecyclerView);
        ReserveSelectTimeListAdapter adapter = new ReserveSelectTimeListAdapter(myListData);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
        /* data list for recycler view end */

        buttonTimeContinue = findViewById(R.id.buttonTimeContinue);
        buttonTimeContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveSelectTimeActivity.this, ReserveSubmitActivity.class);
                /*selectedDate = getSelectedDate();
                intent.putExtra("selectedDate",selectedDate);*/
                startActivity(intent);
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}