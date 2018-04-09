package com.app.bespokino.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.ItemModel;

import java.util.List;

/**
 * Created by bespokino on 8/1/2017 AD.
 */

public class SizeToolAdapter extends BaseAdapter {


    Activity activity;
    List<ItemModel>itemModels;
    LayoutInflater inflater;

    public SizeToolAdapter(Activity activity) {
        this.activity = activity;
    }

    public SizeToolAdapter(Activity activity, List<ItemModel> users) {
        this.activity = activity;
        this.itemModels = users;

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
        ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.size_tool_neck_item_layout, parent, false);

            holder = new ViewHolder();


            holder.ivCollar =(ImageView)view.findViewById(R.id.collar);
           // holder.cardView=(CardView)view.findViewById(R.id.itemCard);

            view.setTag(holder);
        }else
            holder = (ViewHolder)view.getTag();

        ItemModel model = itemModels.get(position);

       // holder.tvUserName.setText(model.getItemName());
        holder.ivCollar.setImageResource(model.getItemImage());


        if (model.isSelected()) {
           // holder.ivCheckBox.setBackgroundResource(R.drawable.tick);
           // holder.cardView.setBackgroundResource(R.drawable.shape);
        }
        else {
            // holder.ivCheckBox.setBackgroundResource(0);
          //  holder.ivCheckBox.setBackgroundResource(R.drawable.tick1);
           // holder.cardView.setBackgroundResource(R.drawable.non);
        }

        return view;
    }
    public void updateRecords(List<ItemModel> users){
        this.itemModels = users;

        notifyDataSetChanged();
    }
    class ViewHolder{

        //TextView tvUserName;
        ImageView ivCollar;
        //CardView cardView;


    }
}
