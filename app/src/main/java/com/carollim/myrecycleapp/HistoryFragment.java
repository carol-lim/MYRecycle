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

public class HistoryFragment extends Fragment  implements HistoryListAdapter.OnMyReserveListener {
    private DatabaseReference ref1;
    private ArrayList<History> list;
    private HistoryListAdapter historyListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        /* spinner start
        Spinner spinnerDate = (Spinner) view.findViewById(R.id.spinnerHistoryDate);
        ArrayAdapter<CharSequence> adapterDate = ArrayAdapter.createFromResource(getContext(),
                R.array.history_date_array, android.R.layout.simple_spinner_item);
        adapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(adapterDate);

        Spinner spinnerFilter = (Spinner) view.findViewById(R.id.spinnerHistoryFilter);
        ArrayAdapter<CharSequence> adapterfilter = ArrayAdapter.createFromResource(getContext(),
                R.array.history_filter_array, android.R.layout.simple_spinner_item);
        adapterfilter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapterfilter);

        spinnerDate.setOnItemSelectedListener(this);
        spinnerFilter.setOnItemSelectedListener(this);

        spinner end */

        /* data list for recycler view start */
        /*HistoryListData[] myListData = new HistoryListData[] {
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
                new HistoryListData("3kg Paper", "Recycle Company, Hamid","RM2.00", "22 May, 15:00"),
        };*/

        /*RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);
        HistoryListAdapter adapter = new HistoryListAdapter(myListData);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);*/
        /* data list for recycler view end */

        RecyclerView recyclerView = view.findViewById(R.id.historyRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        ref1 = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("history");
        list = new ArrayList<>();
        ClearAll();
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    History history = new History();
                    history.setAddr(dataSnapshot.child("addr").getValue().toString());
                    history.setCollector(dataSnapshot.child("collector").getValue().toString());
                    history.setDate(dataSnapshot.child("date").getValue().toString());
                    history.setCategory(dataSnapshot.child("category").getValue().toString());
                    history.setTime(dataSnapshot.child("time").getValue().toString());
                    history.setPay(dataSnapshot.child("pay").getValue().toString());
                    history.setUnitPrice(dataSnapshot.child("unitPrice").getValue().toString());
                    history.setWeight(dataSnapshot.child("weight").getValue().toString());
                    history.setUser(dataSnapshot.child("user").getValue().toString());
                    history.setrId(dataSnapshot.child("rId").getValue().toString());
                    history.setPrice(dataSnapshot.child("price").getValue().toString());
                    history.setImage(dataSnapshot.child("image").getValue().toString());
                    list.add(history);

                }
                historyListAdapter = new HistoryListAdapter(getContext(), list, HistoryFragment.this::onMyReserveClick);
                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(historyListAdapter);
                historyListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
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
            if (historyListAdapter != null) {
                historyListAdapter.notifyDataSetChanged();
            }
        }
        list = new ArrayList<>();
    }

    @Override
    public void onMyReserveClick(int position) {
        Intent intent = new Intent(getContext(), HistoryDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("rId", list.get(position).getrId());
        extras.putString("category", list.get(position).getCategory());
        extras.putString("date", list.get(position).getDate());
        extras.putString("time", list.get(position).getTime());
        extras.putString("address", list.get(position).getAddr());
        extras.putString("collector", list.get(position).getCollector());
        extras.putString("price", list.get(position).getPrice());
        extras.putString("unitPrice", list.get(position).getUnitPrice());
        extras.putString("weight", list.get(position).getWeight());
        extras.putString("image", list.get(position).getImage());
        extras.putString("pay", list.get(position).getPay());
        intent.putExtras(extras);
        getContext().startActivity(intent);
    }
}

