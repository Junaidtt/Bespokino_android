package com.app.bespokino.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.model.ToolCode;

import java.util.ArrayList;
import java.util.List;

public class HLVAdapter extends RecyclerView.Adapter<HLVAdapter.ViewHolder> {

   List<ToolCode>toolCodeList= new ArrayList<>();
    int selectedPosition=-1;
   Context context;
    public HLVAdapter(List<ToolCode> toolCodeList, Context context) {
        this.toolCodeList = toolCodeList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.horizontal_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final ToolCode toolCode = toolCodeList.get(position);
        viewHolder.colorCode.setImageResource(toolCode.getImageColor());


        if (selectedPosition == position) {

            viewHolder.cardView.setBackgroundResource(R.drawable.shape);

        } else {
            viewHolder.cardView.setBackgroundResource(0);
            viewHolder.colorCode.setOnClickListener(new View.OnClickListener() {
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
        return toolCodeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView colorCode;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            colorCode = (ImageView) itemView.findViewById(R.id.colorCode);
            cardView = (CardView)itemView.findViewById(R.id.itemCard);
            colorCode.setOnClickListener(this);
            cardView.setSelected(true);

        }


        @Override
        public void onClick(View view) {

        }
    }

}

