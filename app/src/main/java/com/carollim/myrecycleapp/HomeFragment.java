package com.carollim.myrecycleapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentStateAdapter pagerAdapter;

    FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = fAuth.getCurrentUser();
        String userID = firebaseUser.getUid();

        /* tab layout page sliding start */
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager2);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getResources().getString(R.string.db));
        firebaseDatabase.getReference().child("user").child(userID).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userType = String.valueOf(snapshot.getValue());
                String[] titles = new String[]{};

                //citizen
                if (userType.equals("0")){
                    titles = new String[] {"Reserve Now","My Reserve"};
                }
                //collector
                else if (userType.equals("1")){
                    titles = new String[]{"Reservation","To Collect"};
                }

                pagerAdapter = new PagerAdapter(HomeFragment.this, titles, userType);
                viewPager2.setAdapter(pagerAdapter);

                String[] finalTitles = titles;
                new TabLayoutMediator(tabLayout, viewPager2,((tab, position) -> tab.setText(finalTitles[position]))).attach();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
            }
        });



        /* tab layout page sliding end */
        return view;
    }


}