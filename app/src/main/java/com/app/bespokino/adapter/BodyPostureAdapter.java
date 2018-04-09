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
 * Created by bespokino on 10/3/2017 AD.
 */

public class BodyPostureAdapter extends BaseAdapter {

 Activity activity;
 List<ItemModel> itemModels;
 LayoutInflater inflater;

    public BodyPostureAdapter(Activity activity) {
        this.activity = activity;
    }

    public BodyPostureAdapter(Activity activity, List<ItemModel> itemModels) {
        this.activity = activity;
        this.itemModels = itemModels;
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

        view = inflater.inflate(R.layout.posture_grid_item, parent, false);

        holder = new ViewHolder();

        holder.tvUserName = (TextView)view.findViewById(R.id.tv_collar_name);
        holder.ivCheckBox = (ImageView) view.findViewById(R.id.checked_item);
        holder.ivCollar =(ImageView)view.findViewById(R.id.collar);
        holder.cardView=(CardView)view.findViewById(R.id.itemCard);
            holder.checkmarkImg = (ImageView)view.findViewById(R.id.checked_item);

        view.setTag(holder);
        }else
        holder = (ViewHolder)view.getTag();

        ItemModel model = itemModels.get(position);

        holder.tvUserName.setText(model.getItemName());
        holder.ivCollar.setImageResource(model.getItemImage());


        if (model.isSelected()) {
        //holder.ivCheckBox.setBackgroundResource(R.drawable.tick);
        holder.cardView.setBackgroundResource(R.drawable.shape);
            holder.checkmarkImg.setImageResource(R.drawable.tick);
        }
        else {

        holder.cardView.setBackgroundResource(0);
            holder.checkmarkImg.setImageResource(0);

        }

        return view;
        }
        public void updateRecords(List<ItemModel> users){
        this.itemModels = users;

        notifyDataSetChanged();
        }
       class ViewHolder{

        TextView tvUserName;
        ImageView ivCheckBox,ivCollar,checkmarkImg;
        CardView cardView;


}
}
