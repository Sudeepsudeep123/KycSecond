package com.example.sudeepbajracharya.kycsecond.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sudeepbajracharya.kycsecond.Model.KycModel;
import com.example.sudeepbajracharya.kycsecond.KycInterface.OnDataItemClickListener;
import com.example.sudeepbajracharya.kycsecond.R;

import java.util.ArrayList;

public class KycAdapter extends RecyclerView.Adapter<KycAdapter.MyViewHolder> {
    public ArrayList<KycModel> kycList;
    OnDataItemClickListener onDataItemClickListener;
    Context c;

    public void setOnDataItemClickListener(OnDataItemClickListener onDataItemClickListeners){
        this.onDataItemClickListener = onDataItemClickListeners;
    }

    public KycAdapter(ArrayList<KycModel> kycModels,Context c) {
        kycList = kycModels;
        this.c = c;
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
        holder.imgItems.setImageBitmap(kycModel.getItemImage());
        holder.txtItemName.setText(kycModel.getItemName());
        holder.txtItemDescription.setText(kycModel.getItemDescription());
        holder.imgItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(position + "  clicked here");
                System.out.println(kycModel.getItemName() + "asdasdas");
                onDataItemClickListener.onDataClick(kycModel, position);
            }
        });

//        holder.txtSample.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(c, "clicked here", Toast.LENGTH_SHORT).show();
//                if(position==0){
//                    viewSample();
//                }
//            }
//        });
    }

//    private void viewSample() {
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(c);
//        View mView = LayoutInflater.from(c).inflate(R.layout.popup_image, null);
//        mBuilder.setTitle("Signature");
//        ImageView imgSignature =  mView.findViewById(R.id.imgSignature);
//        imgSignature.setImageBitmap(MainActivity.sampleSignature);
//        mBuilder.setView(mView);
//        final AlertDialog dialog = mBuilder.create();
//        dialog.show();
//    }

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
            txtItemDescription = itemView.findViewById(R.id.txtDesc);
            txtSample = itemView.findViewById(R.id.txtSample);

        }
    }
}
