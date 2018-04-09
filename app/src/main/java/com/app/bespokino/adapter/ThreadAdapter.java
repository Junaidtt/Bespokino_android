package com.app.bespokino.adapter;

import android.app.Activity;
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
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by bespokino on 10/10/2017 AD.
 */

public class ThreadAdapter extends BaseAdapter {



    Activity activity;
    List<ThreadModel> itemModels;
    LayoutInflater inflater;

    public ThreadAdapter(Activity activity) {
        this.activity = activity;
    }

    public ThreadAdapter(Activity activity, List<ThreadModel> users) {
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
        ThreadAdapter.ViewHolder holder = null;

        if (view == null){

            view = inflater.inflate(R.layout.mono_color_grid_item, parent, false);

            holder = new ThreadAdapter.ViewHolder();
            holder.threadImg =(ImageView)view.findViewById(R.id.thread);
            holder.linearLayout=(LinearLayout) view.findViewById(R.id.linearThread);
            holder.ticker = (ImageView)view.findViewById(R.id.ticker);

            view.setTag(holder);
        }else

            holder = (ThreadAdapter.ViewHolder)view.getTag();


             ThreadModel model = itemModels.get(position);
            holder.threadImg.setImageResource(model.getImgID());


            //Glide.with(activity).load(itemModels.get(position).getImageLink()).into(holder.threadImg);


        if (model.isSelected()) {

          //  holder.linearLayout.setBackgroundResource(R.drawable.shape);
            holder.ticker.setImageResource(R.drawable.tick);
        }
        else {
         //   holder.linearLayout.setBackgroundResource(0);
            holder.ticker.setImageResource(0);
        }

        return view;
    }
    public void updateRecords(List<ThreadModel> users){
        this.itemModels = users;

        notifyDataSetChanged();
    }
    class ViewHolder{

        ImageView threadImg,ticker;
        LinearLayout linearLayout;



    }
}
