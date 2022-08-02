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

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder>{
//    private HistoryListData[] listData;
    Context context;
    ArrayList<History> list;
    private OnMyReserveListener mOnMyReserveListener;

//    public HistoryListAdapter(HistoryListData[] listData) {
//        this.listData = listData;
//    }
    public HistoryListAdapter(Context context, ArrayList<History> list, OnMyReserveListener onMyReserveListener) {
        this.context = context;
        this.list = list;
        this.mOnMyReserveListener = onMyReserveListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;*/
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_history, parent, false);
        return new ViewHolder(v, mOnMyReserveListener);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*final HistoryListData HistoryListData = listData[position];
        holder.txtWeightCategory.setText(listData[position].getWeightCategory());
        holder.txtCompanyCollector.setText(listData[position].getCompanyCollector());
        holder.txtEarnPrice.setText(listData[position].getEarnPrice());
        holder.txtCollectDateTime.setText(listData[position].getCollectDateTime());*/
        final History history = list.get(position);
        holder.txtCollectDateTime.setText(history.getDate()+" "+history.getTime());
        holder.txtEarnPrice.setText("RM"+history.getPrice());
        holder.txtCompanyCollector.setText(history.getCollector());
        holder.txtWeightCategory.setText(history.getWeight()+"kg "+ history.getCategory());

        /*holder.listItemHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoryDetailActivity.class);
                v.getContext().startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtWeightCategory, txtCompanyCollector, txtEarnPrice, txtCollectDateTime;
        public ConstraintLayout listItemHistoryLayout;
        OnMyReserveListener onMyReserveListener;

        public ViewHolder(@NonNull View itemView, OnMyReserveListener onMyReserveListener) {
            super(itemView);
            this.txtWeightCategory = itemView.findViewById(R.id.txtWeightCategory);
            this.txtCompanyCollector = itemView.findViewById(R.id.txtCompanyCollector);
            this.txtEarnPrice = itemView.findViewById(R.id.txtEarnPrice);
            this.txtCollectDateTime = itemView.findViewById(R.id.txtCollectDateTime);
            listItemHistoryLayout = itemView.findViewById(R.id.listItemHistoryLayout);
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
