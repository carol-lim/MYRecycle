package com.carollim.myrecycleapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ReserveSelectTimeListAdapter extends RecyclerView.Adapter<ReserveSelectTimeListAdapter.ViewHolder>{
    private ReserveSelectTimeListData[] listData;

    public ReserveSelectTimeListAdapter(ReserveSelectTimeListData[] listData) {
        this.listData = listData;
    }
    @Override
    public ReserveSelectTimeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item_select_time, parent, false);
        ReserveSelectTimeListAdapter.ViewHolder viewHolder = new ReserveSelectTimeListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReserveSelectTimeListAdapter.ViewHolder holder, int position) {
        final ReserveSelectTimeListData reserveSelectTimeListData = listData[position];
        holder.txtTimeSlot.setText(listData[position].getTimeSlot());

        holder.listItemSelectTimeLayout.setOnClickListener(
                view -> Toast.makeText(
                        view.getContext(),
                        "Clicked on item: " + reserveSelectTimeListData.getTimeSlot(),
                        Toast.LENGTH_SHORT)
                        .show()
        );
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTimeSlot;
        public ConstraintLayout listItemSelectTimeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txtTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
            listItemSelectTimeLayout = itemView.findViewById(R.id.listItemSelectTimeLayout);
        }
    }
}
