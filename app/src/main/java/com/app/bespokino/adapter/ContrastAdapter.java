package com.app.bespokino.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.bespokino.R;
import com.app.bespokino.model.FabricContrast;
import com.app.bespokino.model.FabricModel;
import com.app.bespokino.model.ToolCode;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 10/20/2017 AD.
 */

public class ContrastAdapter extends RecyclerView.Adapter<ContrastAdapter.MyViewHolder> {


    int selectedPosition=-1;
    private Context mContext;
    private List<FabricContrast> iFabricContrast = new ArrayList<>();


    public ContrastAdapter(Context mContext, List<FabricContrast> iFabricContrast) {
        this.mContext = mContext;
        this.iFabricContrast = iFabricContrast;
    }

    public ContrastAdapter(int selectedPosition, Context mContext, List<FabricContrast> iFabricContrast) {
        this.selectedPosition = selectedPosition;
        this.mContext = mContext;
        this.iFabricContrast = iFabricContrast;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumb, parent, false);

        return new ContrastAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        FabricContrast fabricContrast = iFabricContrast.get(position);

        Glide.with(mContext)
                .load(fabricContrast.getImage())
                .centerCrop()
                .fitCenter()
                .into(holder.thumbnail);
        if (selectedPosition == position){
            holder.ticker.setImageResource(R.drawable.tick);
        }else {
            // viewHolder.cardView.setBackgroundResource(0);
            holder.ticker.setImageResource(0);
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = position;
                    notifyDataSetChanged();
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return iFabricContrast.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView thumbnail,ticker;

        public MyViewHolder(View view) {
            super(view);

            thumbnail = (ImageView) view.findViewById(R.id.iv_photo);
            ticker = (ImageView)view.findViewById(R.id.ticker);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
