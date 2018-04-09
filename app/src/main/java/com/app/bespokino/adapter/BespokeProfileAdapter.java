package com.app.bespokino.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.BespokeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 26/3/2018 AD.
 */

public class BespokeProfileAdapter extends RecyclerView.Adapter<BespokeProfileAdapter.MyViewHolder> {

    Context mContext;
    List<BespokeItem> bespokeItemList = new ArrayList<>();


    public BespokeProfileAdapter(Context mContext, List<BespokeItem> bespokeItemList) {
        this.mContext = mContext;
        this.bespokeItemList = bespokeItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.bespoke_item_profile_layout,parent,false);
        BespokeProfileAdapter.MyViewHolder viewHolder=new BespokeProfileAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BespokeItem bespokeItem = bespokeItemList.get(position);

        holder.itemName.setText(bespokeItem.getItemName());
        int name  = bespokeItem.getImageName();
        holder.itemImage.setImageResource(bespokeItem.getImageName());


    }

    @Override
    public int getItemCount() {
        return bespokeItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;

        TextView itemName;


        public MyViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.bdTvItem);
            itemImage = itemView.findViewById(R.id.bodyItemImage);


            mContext = itemView.getContext();


        }
    }

}
