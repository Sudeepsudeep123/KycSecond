package com.example.sudeepbajracharya.kycsecond.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sudeepbajracharya.kycsecond.KycInterface.OnDataItemClickListener;
import com.example.sudeepbajracharya.kycsecond.Model.KycModel;
import com.example.sudeepbajracharya.kycsecond.R;

import java.util.ArrayList;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> {
    public ArrayList<com.example.sudeepbajracharya.kycsecond.Model.KycModel> kycList;
    com.example.sudeepbajracharya.kycsecond.KycInterface.OnDataItemClickListener onDataItemClickListener;
    Context c;
     int counter= 0;
     public  static int count = 0;

    public void setOnDataItemClickListener(OnDataItemClickListener onDataItemClickListeners){
        this.onDataItemClickListener = onDataItemClickListeners;
    }

    public UploadAdapter(ArrayList<com.example.sudeepbajracharya.kycsecond.Model.KycModel> kycModels, Context c,int counter) {
        kycList = kycModels;
        this.c = c;
        this.counter = counter;
    }

    @NonNull
    @Override
    public UploadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_items, viewGroup, false);

        return new UploadAdapter.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(@NonNull UploadAdapter.MyViewHolder holder, final int position) {
        final KycModel kycModel = kycList.get(position);
        count = counter;
        Log.e("Count",count + "");
        holder.imgItems.setImageBitmap(kycModel.getItemImage());
        holder.txtItemName.setText(kycModel.getItemName());
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgItems;
        public TextView txtItemName;
        public TextView txtItemDescription;
        public TextView txtSample;
        Bitmap signature;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItems = itemView.findViewById(R.id.imgItems);
            txtItemName = itemView.findViewById(R.id.txtItemName);

        }
    }
}
