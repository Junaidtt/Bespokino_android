package com.app.bespokino.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class InnerCollarFancyAdapter extends BaseAdapter {



    Activity activity;
    List<FabricContrast>contrastFabricList = new ArrayList<>();
    LayoutInflater inflater;


    public InnerCollarFancyAdapter(Activity activity, List<FabricContrast> contrastFabricList) {
        this.activity = activity;
        this.contrastFabricList = contrastFabricList;

        inflater = activity.getLayoutInflater();

    }



    @Override
    public int getCount() {
        return contrastFabricList.size();
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

        InnerCollarFancyAdapter.ViewHolder holder = null;

        if(convertView == null){

           // LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // convertView=inflater.inflate(R.layout.contrast_fabric_grid_item,null);
            convertView = inflater.inflate(R.layout.contrast_fabric_grid_item, parent, false);
            holder = new InnerCollarFancyAdapter.ViewHolder();
            holder.threadImg =(ImageView)convertView.findViewById(R.id.thread);
            holder.linearLayout=(LinearLayout) convertView.findViewById(R.id.linearThread);
            holder.ticker = (ImageView)convertView.findViewById(R.id.ticker);

            convertView.setTag(holder);

        }else

            holder = (InnerCollarFancyAdapter.ViewHolder)convertView.getTag();

            FabricContrast fabricContrast = contrastFabricList.get(position);

            Glide.with(activity).load(contrastFabricList.get(position).getImage()).into(holder.threadImg);

          // Toast.makeText(activity,String.valueOf(fabricContrast.isSelected()),Toast.LENGTH_SHORT).show();

            if (fabricContrast.isSelected()) {

                holder.linearLayout.setBackgroundResource(R.drawable.shape);
                holder.ticker.setImageResource(R.drawable.tick);
            }
            else {

                holder.linearLayout.setBackgroundResource(0);
                holder.ticker.setImageResource(0);

            }




      //  ImageView imageView = (ImageView) convertView.findViewById(R.id.thread);
      //  Glide.with(activity).load(contrastFabricList.get(position).getImage()).into(holder.threadImg);

        return convertView;
    }
    public void updateRecords(List<FabricContrast> users){
        this.contrastFabricList = users;

        notifyDataSetChanged();
    }

    class ViewHolder{

        ImageView threadImg,ticker;
        LinearLayout linearLayout;



    }
}
