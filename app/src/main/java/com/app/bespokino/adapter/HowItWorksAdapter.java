package com.app.bespokino.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.bespokino.R;

/**
 * Created by sunilvg on 06/07/17.
 */

public class HowItWorksAdapter extends RecyclerView.Adapter<HowItWorksAdapter.ViewHolder> {

    private String[] dataSource;

    public HowItWorksAdapter(String[] dataArgs){
        dataSource = dataArgs;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create selection new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_list_howitworks_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       holder.textView.setText(dataSource[position]);

    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.row_item);
        }
    }
}
