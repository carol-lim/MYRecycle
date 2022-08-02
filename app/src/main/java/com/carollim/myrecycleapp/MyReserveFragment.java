package com.carollim.myrecycleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyReserveFragment extends Fragment implements MyReserveListAdapter.OnMyReserveListener {
    private DatabaseReference ref1;
    private ArrayList<MyReserveListData> list;
    private MyReserveListAdapter myReserveListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_reserve, container, false);

        /* spinner start
        Spinner spinnerDate = (Spinner) view.findViewById(R.id.spinnerMyReserveDate);
        ArrayAdapter<CharSequence> adapterDate = ArrayAdapter.createFromResource(getContext(),
                R.array.my_reserve_date_array, android.R.layout.simple_spinner_item);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapterDate);

        Spinner spinnerFilter = (Spinner) view.findViewById(R.id.spinnerMyReserveFilter);
        ArrayAdapter<CharSequence> adapterfilter = ArrayAdapter.createFromResource(getContext(),
                R.array.my_reserve_filter_array, android.R.layout.simple_spinner_item);
        adapterfilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterfilter);

        spinnerDate.setOnItemSelectedListener(this);
        spinnerFilter.setOnItemSelectedListener(this);

         spinner end */

        /* data list for recycler view start */
        // pass only user's reservation
       /* MyReserveListData[] myListData = new MyReserveListData[] {
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Paper", "22 May, 15:00", "Reserved"),
                new MyReserveListData("Metal", "22 May, 15:00", "Reserved"),

        };*/

        RecyclerView recyclerView = view.findViewById(R.id.myReserveRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref1 = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("reservation");
        list = new ArrayList<>();
        ClearAll();
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MyReserveListData myReserveListData = new MyReserveListData();

                    myReserveListData.setWasteCategoryName(dataSnapshot.child("category").getValue().toString());
                    myReserveListData.setReserveStatus(dataSnapshot.child("status").getValue().toString());
                    myReserveListData.setReserveDateTime(dataSnapshot.child("date").getValue().toString() + " " +
                            dataSnapshot.child("time").getValue().toString());
                    myReserveListData.setDate(dataSnapshot.child("date").getValue().toString());
                    myReserveListData.setTime(dataSnapshot.child("time").getValue().toString());
                    myReserveListData.setAddress(dataSnapshot.child("address").getValue().toString());
                    myReserveListData.setCollector(dataSnapshot.child("collectorId").getValue().toString());
                    myReserveListData.setUser(dataSnapshot.child("uid").getValue().toString());
                    list.add(myReserveListData);
                }
                myReserveListAdapter = new MyReserveListAdapter(getContext(), list, MyReserveFragment.this::onMyReserveClick);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(myReserveListAdapter);
                myReserveListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

        //MyReserveListAdapter adapter = new MyReserveListAdapter(myListData);
        //recyclerView.setAdapter(adapter);
        /* data list for recycler view end */



//        return view;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
    private void ClearAll(){
        if (list != null){
            list.clear();
            if (myReserveListAdapter != null) {
                myReserveListAdapter.notifyDataSetChanged();
            }
        }
        list = new ArrayList<>();
    }

    @Override
    public void onMyReserveClick(int position) {
        Intent intent = new Intent(getContext(), MyReserveDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("category", list.get(position).getWasteCategoryName());
        extras.putString("status", list.get(position).getReserveStatus());
        extras.putString("date", list.get(position).getDate());
        extras.putString("time", list.get(position).getTime());
        extras.putString("address", list.get(position).getAddress());
        extras.putString("collector", list.get(position).getCollector());
        extras.putString("user", list.get(position).getUser());
        intent.putExtras(extras);
        getContext().startActivity(intent);
    }
}