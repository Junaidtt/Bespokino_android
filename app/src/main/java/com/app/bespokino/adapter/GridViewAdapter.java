package com.app.bespokino.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bespokino on 10/5/2017 AD.
 */

public class GridViewAdapter extends BaseAdapter {

    private Activity activity;
    //private String[] strings;
    public List selectedPositions;
    List<ItemModel>list = new ArrayList<>();

    public GridViewAdapter(List<ItemModel> modelList, Activity activity) {
        this.list = modelList;
        this.activity = activity;
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {

        return list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        GridItemView customView = (convertView == null) ? new GridItemView(activity) : (GridItemView) convertView;

        customView.display(list.get(position), selectedPositions.contains(position));

        return customView;
    }
}
