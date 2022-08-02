package com.carollim.myrecycleapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CatalogFragment extends Fragment implements CatalogListAdapter.OnCatalogListener {

    private DatabaseReference refCatalog;
    private ArrayList<CatalogListData> list;
    private CatalogListAdapter catalogListAdapter;
    private RecyclerView recyclerView;
    public static final String TAG = "CatalogFragment";
//    String from;
//    Boolean isReserving;
//    String address, selectedCategory;

/*    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isReserving = savedInstanceState.getBoolean("isReserving");
            //address = savedInstanceState.getString("address");
            selectedCategory = savedInstanceState.getString("selectedCategory");
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        // retrieve ReserveNowFragment indicator
         /*ReserveSelectCategoryActivity reserveSelectCategoryActivity = (ReserveSelectCategoryActivity) getActivity();
         if (!reserveSelectCategoryActivity.getReserveNowFragmentIndicator().isEmpty()){
             from = reserveSelectCategoryActivity.getReserveNowFragmentIndicator(); // "ReserveNowFragment"
         } else {
             from = "catalog";
         }*/

        // assign layout variables
        recyclerView = view.findViewById(R.id.catalogRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        // catalog db ref
        refCatalog = FirebaseDatabase.getInstance(getResources().getString(R.string.db)).getReference("catalog");

        // retrieve catalog data from db and bind using adapter
        list = new ArrayList<>();
        ClearAll();
         refCatalog.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    CatalogListData catalogListData = new CatalogListData();

                    catalogListData.setImageUrl(dataSnapshot.child("image").getValue().toString());
                    catalogListData.setCategory(dataSnapshot.child("category").getValue().toString());
                    catalogListData.setKgPrice(Double.valueOf(dataSnapshot.child("kgPrice").getValue().toString()));
                    catalogListData.setDesc(dataSnapshot.child("desc").getValue().toString());

                    list.add(catalogListData);
                }
                catalogListAdapter = new CatalogListAdapter(getContext(), list, CatalogFragment.this::onCatalogClick);
                recyclerView.setAdapter(catalogListAdapter);
                catalogListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void ClearAll(){
         if (list != null){
             list.clear();
             if (catalogListAdapter != null) {
                 catalogListAdapter.notifyDataSetChanged();
             }
         }
         list = new ArrayList<>();
    }

    // one of the recyclerView data is clicked
    @Override
    public void onCatalogClick(int position) {
        Log.d(TAG, "onCatalogClick: clicked.");

        // if from reserve now, get value of selected position
        // return the value back to reserveSelectCategoryActivity
//        if (isReserving){

//            someEventListener.someEvent(list.get(position).getCategory());

//            selectedCategory = list.get(position).getCategory();

            /*Bundle extras = new Bundle();
            extras.putString("category", list.get(position).getCategory());*/

//        } else if (!isReserving){

            // if from Catalog, goto catalog detail
            Intent intent = new Intent(getContext(), CatalogDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putString("image", list.get(position).getImageUrl());
            extras.putString("category", list.get(position).getCategory());
            extras.putDouble("kgPrice", list.get(position).getKgPrice());
            extras.putString("desc", list.get(position).getDesc());
            intent.putExtras(extras);
            getContext().startActivity(intent);
//        }
    }

/*    public interface onSomeEventListener {
        public void someEvent(String s);
    }

    onSomeEventListener someEventListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }*/

/*    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // put string value
        //outState.putBoolean("isReserving",isReserving);
        //outState.putString("address",address);
        outState.putString("selectedCategory",selectedCategory);

        super.onSaveInstanceState(outState);
    }*/

/*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        // get values from saved state
        isReserving=savedInstanceState.getBoolean("isReserving");
        address=savedInstanceState.getString("address");
        selectedCategory=savedInstanceState.getString("selectedCategory");

        super.onRestoreInstanceState(savedInstanceState);
    }
*/

/*    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            isReserving = savedInstanceState.getBoolean("isReserving");
            //address = savedInstanceState.getString("address");
            selectedCategory = savedInstanceState.getString("selectedCategory");
        }
    }*/
}