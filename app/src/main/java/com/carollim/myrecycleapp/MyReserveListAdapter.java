package com.carollim.myrecycleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyReserveListAdapter extends RecyclerView.Adapter<MyReserveListAdapter.ViewHolder>{
    Context context;
    ArrayList<MyReserveListData> list;
    private OnMyReserveListener mOnMyReserveListener;

    // RecyclerView recyclerView;
    public MyReserveListAdapter(Context context, ArrayList<MyReserveListData> list, OnMyReserveListener onMyReserveListener) {
        this.context = context;
        this.list = list;
        this.mOnMyReserveListener = onMyReserveListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_my_reserve, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;*/
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_my_reserve, parent, false);
        return new ViewHolder(v, mOnMyReserveListener);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyReserveListData myReserveListData = list.get(position);
        holder.txtWasteCategoryName.setText(myReserveListData.getWasteCategoryName());
        holder.txtReserveDateTime.setText(myReserveListData.getReserveDateTime());
        holder.txtReserveStatus.setText(myReserveListData.getReserveStatus());

        /*holder.listItemMyReserveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // citizen
                Intent intent = new Intent(v.getContext(), MyReserveDetailActivity.class);
                // collector
                //Intent intent = new Intent(v.getContext(), ReservationActivity.class);
                //Intent intent = new Intent(v.getContext(), ToCollectActivity.class);

                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtWasteCategoryName, txtReserveDateTime, txtReserveStatus;
        public ConstraintLayout listItemMyReserveLayout;
        OnMyReserveListener onMyReserveListener;

        public ViewHolder(@NonNull View itemView, OnMyReserveListener onMyReserveListener) {
            super(itemView);
            this.txtWasteCategoryName = itemView.findViewById(R.id.txtWasteCategoryName);
            this.txtReserveDateTime = itemView.findViewById(R.id.txtReserveDateTime);
            this.txtReserveStatus = itemView.findViewById(R.id.txtReserveStatus);
            listItemMyReserveLayout = itemView.findViewById(R.id.listItemMyReserveLayout);

            this.onMyReserveListener = onMyReserveListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMyReserveListener.onMyReserveClick(getAdapterPosition());
        }
    }

    public interface OnMyReserveListener{
        void onMyReserveClick(int position);
    }
}
