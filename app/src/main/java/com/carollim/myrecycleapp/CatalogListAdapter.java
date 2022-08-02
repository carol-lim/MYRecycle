package com.carollim.myrecycleapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CatalogListAdapter extends RecyclerView.Adapter<CatalogListAdapter.ViewHolder>{

    Context context;
    ArrayList<CatalogListData> list;
    private OnCatalogListener mOnCatalogListener;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    public CatalogListAdapter(Context context, ArrayList<CatalogListData> list, OnCatalogListener onCatalogListener){
        this.context = context;
        this.list = list;
        this.mOnCatalogListener = onCatalogListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageviewCategory;
        public TextView txtCatalogCategoryName, txtCatalogUnitPrice;
        public ConstraintLayout listItemCatalogLayout;
        OnCatalogListener onCatalogListener;

        public ViewHolder(@NonNull View itemView, OnCatalogListener onCatalogListener) {
            super(itemView);
            this.imageviewCategory = itemView.findViewById(R.id.imageviewCategory);
            this.txtCatalogCategoryName = itemView.findViewById(R.id.txtCatalogCategoryName);
            this.txtCatalogUnitPrice = itemView.findViewById(R.id.txtCatalogUnitPrice);
            listItemCatalogLayout = itemView.findViewById(R.id.listItemCatalogLayout);

            this.onCatalogListener = onCatalogListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCatalogListener.onCatalogClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_catalog, parent, false);
        return new ViewHolder(v, mOnCatalogListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CatalogListData catalogListData = list.get(position);
        Picasso.get().load(list.get(position).getImageUrl())
                .into(holder.imageviewCategory);
        holder.txtCatalogCategoryName.setText(catalogListData.getCategory());
        holder.txtCatalogUnitPrice.setText("RM"+ df.format(catalogListData.getKgPrice()) +" / kg");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnCatalogListener{
        void onCatalogClick(int position);
    }

}
