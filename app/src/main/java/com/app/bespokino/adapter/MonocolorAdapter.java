package com.app.bespokino.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.ThreadModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunilvg on 16/07/17.
 */

public class MonocolorAdapter extends BaseAdapter {

    Activity activity;
    List<ThreadModel> threadModelList = new ArrayList<>();
    LayoutInflater inflater;

    public MonocolorAdapter(Activity activity, List threadModelList) {
        this.activity = activity;
        this.threadModelList = threadModelList;

        inflater = activity.getLayoutInflater();

    }

    @Override
    public int getCount() {
        return threadModelList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        MonocolorAdapter.ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.mono_color_grid_item, parent, false);

            holder = new MonocolorAdapter.ViewHolder();


            holder.threadImg = (ImageView)convertView.findViewById(R.id.thread);//    android:id="@+id/thread"
            holder.linearLayout = (LinearLayout)convertView.findViewById(R.id.linearThread);

        }else {

            holder = (MonocolorAdapter.ViewHolder)convertView.getTag();
            ThreadModel threadModel = threadModelList.get(position);

            holder.threadImg.setImageResource(threadModel.getImgID());
            if (threadModel.isSelected()){

              holder.linearLayout.setBackgroundResource(R.drawable.shape);
            }else{
                holder.linearLayout.setBackgroundResource(0);
            }


        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.thread);
        imageView.setImageResource(threadModelList.get(position).getImgID());

        return convertView;
    }


    class ViewHolder{

        ImageView threadImg;
        LinearLayout linearLayout;




    }




}
