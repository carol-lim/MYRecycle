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

public class ReservationListAdapter extends RecyclerView.Adapter<ReservationListAdapter.ViewHolder>{
    Context context;
    ArrayList<MyReserveListData> list;
    private OnReservationListener mOnReservationListener;

    // RecyclerView recyclerView;
    public ReservationListAdapter(Context context, ArrayList<MyReserveListData> list, OnReservationListener onReservationListener) {
        this.context = context;
        this.list = list;
        this.mOnReservationListener = onReservationListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_my_reserve, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;*/
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_my_reserve, parent, false);
        return new ViewHolder(v, mOnReservationListener);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyReserveListData MyReserveListData = list.get(position);
        holder.txtWasteCategoryName.setText(MyReserveListData.getWasteCategoryName());
        holder.txtReserveDateTime.setText(MyReserveListData.getReserveDateTime());
        holder.txtReserveStatus.setText(MyReserveListData.getReserveStatus());

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
        OnReservationListener onReservationListener;

        public ViewHolder(@NonNull View itemView, OnReservationListener onReservationListener) {
            super(itemView);
            this.txtWasteCategoryName = itemView.findViewById(R.id.txtWasteCategoryName);
            this.txtReserveDateTime = itemView.findViewById(R.id.txtReserveDateTime);
            this.txtReserveStatus = itemView.findViewById(R.id.txtReserveStatus);
            listItemMyReserveLayout = itemView.findViewById(R.id.listItemMyReserveLayout);

            this.onReservationListener = onReservationListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onReservationListener.onMyReserveClick(getAdapterPosition());
        }
    }

    public interface OnReservationListener{
        void onMyReserveClick(int position);
    }



}
