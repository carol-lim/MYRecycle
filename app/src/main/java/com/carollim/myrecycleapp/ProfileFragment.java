package com.carollim.myrecycleapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageButton btnSetting;
    private TextView txtUserName;
   // private String name;
    private FirebaseAuth fAuth;
    BarChart barChart;
    PieChart pieChart;
    String ut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUserName = view.findViewById(R.id.txtUserName);
        btnSetting = view.findViewById(R.id.btnSetting);

        barChart = view.findViewById(R.id.barChart);
        pieChart = view.findViewById(R.id.pieChart);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();

        if (firebaseUser == null){
            Toast.makeText(getContext(), "Something went wrong. User's details are not available at the moment.", Toast.LENGTH_LONG).show();
        }else {
            showProfile(firebaseUser);
            btnSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(intent);
                }
            });
        }

        showBarChart();
        showPieChart();

        return view;
    }

    private void showBarChart() {

        ArrayList<BarEntry> earn = new ArrayList<>();
        earn.add(new BarEntry(1, 2.0f));
        earn.add(new BarEntry(2, 5.3f));
        earn.add(new BarEntry(3, 6.2f));
        earn.add(new BarEntry(4, 10.0f));
        earn.add(new BarEntry(5, 9.6f));
        earn.add(new BarEntry(6, 4.2f));


        BarDataSet barDataSet = new BarDataSet(earn, "Total Amount Transferred");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Monthly Transferred");
        barChart.animateY(1000);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

    }

    private void showPieChart() {
        ArrayList<PieEntry> waste = new ArrayList<>();
//        waste.add(new PieEntry(1, "Alumnimium"));
//        waste.add(new PieEntry(1, "Cardboard"));
        waste.add(new PieEntry(5.3f, "Egg Carton"));
        waste.add(new PieEntry(7, "Electronic"));
//        waste.add(new PieEntry(1, "Glass"));
//        waste.add(new PieEntry(1, "HDPE Plastic"));
        waste.add(new PieEntry(4.5f, "Mix Paper"));
//        waste.add(new PieEntry(1, "Newspaper"));
        waste.add(new PieEntry(4, "PET Plastic"));
//        waste.add(new PieEntry(1, "Tin Can"));
        PieDataSet pieDataSet = new PieDataSet(waste, "Waste Recycled");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animate();
        pieChart.setCenterText("Weights of Waste Recycled");
        pieChart.setTouchEnabled(true);
    }


    private void showProfile(FirebaseUser firebaseUser){

        DatabaseReference refProfile = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("user");
        refProfile.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                if (userHelperClass!= null){
                    String name = userHelperClass.name;
                    txtUserName.setText(name);
                    refProfile.child(firebaseUser.getUid()).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ut = userHelperClass.userType;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });
    }
}