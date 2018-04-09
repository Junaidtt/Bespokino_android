package com.app.bespokino.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.bespokino.R;
import com.app.bespokino.model.FabricModel;
import com.bumptech.glide.Glide;

import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by bespokino on 7/31/2017 AD.
 */

public class FabricAdapter extends RecyclerView.Adapter<FabricAdapter.MyViewHolder> {


    private Context mContext;
    private List<FabricModel> iFabric;

    public FabricAdapter(Context mContext, List<FabricModel> iFabric) {
        this.mContext = mContext;
        this.iFabric = iFabric;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fabric, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FabricModel fabricModel = iFabric.get(position);

        Glide.with(mContext)
                .load(fabricModel.getFabric_img())
                .centerCrop()
                .fitCenter()
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return iFabric.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);

            thumbnail = (ImageView) view.findViewById(R.id.iv_photo);

        }
    }
}
