package com.app.bespokino.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.bespokino.model.ItemModel;

import java.util.List;

/**
 * Created by bespokino on 10/29/2017 AD.
 */

public class TuxedoAdapter extends BaseAdapter {

    Activity activity;
    List<ItemModel> itemModels;
    LayoutInflater inflater;

    public TuxedoAdapter(Activity activity, List<ItemModel> itemModels) {
        this.activity = activity;
        this.itemModels = itemModels;

        inflater = activity.getLayoutInflater();

    }

    public TuxedoAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemModels.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
