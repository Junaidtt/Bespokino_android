package com.app.bespokino.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.bespokino.R;
import com.app.bespokino.model.FabricContrast;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.ThreadModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunilvg on 19/07/17.
 */

public class InnerCollarFabricAdapter extends BaseAdapter {

    Activity activity;
    List<FabricContrast> itemModels;
    LayoutInflater inflater;

    public InnerCollarFabricAdapter(Activity activity, List<FabricContrast> contrastFabricList) {
        this.activity = activity;
        this.itemModels = contrastFabricList;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return itemModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        InnerCollarFabricAdapter.ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.contrast_fabric_grid_item, parent, false);

            holder = new InnerCollarFabricAdapter.ViewHolder();
            holder.threadImg =(ImageView)view.findViewById(R.id.thread);
            holder.linearLayout=(LinearLayout) view.findViewById(R.id.linearThread);
            holder.ticker = (ImageView)view.findViewById(R.id.ticker);

            view.setTag(holder);
        }
        else

            holder = (InnerCollarFabricAdapter.ViewHolder)view.getTag();

            Glide.with(activity).load(itemModels.get(position).getImage()).into(holder.threadImg);

            FabricContrast fabricContrast = itemModels.get(position);

            if (fabricContrast.isSelected()){

            holder.ticker.setImageResource(R.drawable.tick);

            }else {

            holder.ticker.setImageResource(0);

            }

        //Glide.with(c).load(contrastFabricList.get(position).getImage()).into(imageView);


        return view;
    }
    public void updateRecords(List<FabricContrast> users){
        this.itemModels = users;

        notifyDataSetChanged();
    }
    class ViewHolder{

        ImageView threadImg,ticker;
        LinearLayout linearLayout;



    }
}
