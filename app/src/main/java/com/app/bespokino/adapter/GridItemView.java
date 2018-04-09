package com.app.bespokino.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.bespokino.R;
import com.app.bespokino.model.ItemModel;

/**
 * Created by bespokino on 10/5/2017 AD.
 */

public class GridItemView extends CardView {

    TextView tvUserName;
    ImageView ivCheckBox,ivCollar;
    CardView cardView;


    public GridItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.grid_item, this);
        tvUserName = (TextView) getRootView().findViewById(R.id.tv_collar_name);
        ivCheckBox =(ImageView)getRootView().findViewById(R.id.checked_item);
        ivCollar = (ImageView)getRootView().findViewById(R.id.collar);
        cardView = (CardView)getRootView().findViewById(R.id.itemCard);

    }
    public void display(ItemModel itemModel, boolean isSelected) {

        itemModel.setSelected(isSelected);
        tvUserName.setText(itemModel.getItemName());
        ivCollar.setImageResource(itemModel.getItemImage());
        display(isSelected);
    }

    public void display(boolean isSelected) {

        if (isSelected){
            ivCheckBox.setImageResource(R.drawable.tick);
            cardView.setBackgroundResource(R.drawable.shape);
        }else {
            ivCheckBox.setImageResource(0);
            cardView.setBackgroundResource(0);
        }
    }
}
