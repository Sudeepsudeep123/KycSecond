package com.example.sudeepbajracharya.kycsecond;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KycAdapter extends RecyclerView.Adapter<KycAdapter.MyViewHolder> {
    public ArrayList<KycModel> kycList;
    private OnDataItemClickListener onDataItemClickListener;

    public KycAdapter(ArrayList<KycModel> kycModels) {
        this.kycList = kycModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_items, viewGroup, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final KycModel kycModel = kycList.get(position);
        holder.imgItems.setImageResource(kycModel.getItemImage());
        holder.txtItemName.setText(kycModel.getItemName());
        holder.txtItemDescription.setText(kycModel.getItemDescription());

        holder.imgItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDataItemClickListener.onDataClick(kycModel, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return kycList.size();
    }

    public void setOnDataItemClickListener(OnDataItemClickListener onDataItemClickListener){
        this.onDataItemClickListener = onDataItemClickListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItems;
        public TextView txtItemName;
        public TextView txtItemDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItems = itemView.findViewById(R.id.imgItems);
            txtItemName = itemView.findViewById(R.id.txtItemName);
            txtItemDescription = itemView.findViewById(R.id.txtDesc);
        }
    }
}
