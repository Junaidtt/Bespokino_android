package com.app.bespokino.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.BespokeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 27/3/2018 AD.
 */

public class BodyPostureProfileAdapter extends RecyclerView.Adapter<BodyPostureProfileAdapter.MyViewHolder> {


    Context mContext;
    List<BespokeItem> bodyPostureList = new ArrayList<>();

    public BodyPostureProfileAdapter(Context mContext, List<BespokeItem> bodyPostureList) {
        this.mContext = mContext;
        this.bodyPostureList = bodyPostureList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.bespoke_item_profile_layout,parent,false);
        BodyPostureProfileAdapter.MyViewHolder viewHolder=new BodyPostureProfileAdapter.MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        BespokeItem bodyPostureListitem = bodyPostureList.get(position);

        holder.itemName.setText(bodyPostureListitem.getItemName());
        int name  = bodyPostureListitem.getImageName();
        holder.itemImage.setImageResource(bodyPostureListitem.getImageName());
    }

    @Override
    public int getItemCount() {
        return bodyPostureList.size();
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
