package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.app.bespokino.R;
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunilvg on 19/07/17.
 */

public class ContrastOptionDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.options_dialog_fragment,null);

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);

        final List<ItemModel> users = new ArrayList<>();

        users.add(new ItemModel(false,"NO PLACKET",R.drawable.noplacket,"PLACKET"));
        users.add(new ItemModel(false,"PLACKET",R.drawable.placketb,"PLACKET"));
        users.add(new ItemModel(false,"HIDDEN PLACKET",R.drawable.hiddenplacket,"PLACKET"));

        final ItemAdapter adapter = new ItemAdapter(getActivity(), users);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                users.get(position);

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
