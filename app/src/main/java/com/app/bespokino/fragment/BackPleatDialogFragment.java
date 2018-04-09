package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.AdditionaloptionsActivity;
import com.app.bespokino.activity.MonogramActivity;
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by sunilvg on 20/07/17.
 */

public class BackPleatDialogFragment extends DialogFragment {

    int preSelectedIndex = 1;
    int currentPos=1;
   String backPleatSelected = "";

    ItemModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid_dialog_fragment,null);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            currentPos = savedInstanceState.getInt("position", 0);
        }

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        Button close = (Button)rootView.findViewById(R.id.close);
        Button saveButton = (Button)rootView.findViewById(R.id.saveButton);


        final List<ItemModel> item = new ArrayList<>();

        item.add(new ItemModel(false,"NO PLEAT",R.drawable.nopleats, "PLEAT",156,45));
        item.add(new ItemModel(false,"ONE PLEAT",R.drawable.onepleats, "PLEAT",157,45));
        item.add(new ItemModel(false,"TWO PLEAT",R.drawable.twopleats, "PLEAT",158,45));

        final ItemAdapter adapter = new ItemAdapter(getActivity(),item);

        gridView.setAdapter(adapter);


        model = item.get(currentPos);
        model.setSelected(true);

        item.set(currentPos, model);

        adapter.updateRecords(item);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.get(position);
                currentPos = position;
                ItemModel model = item.get(position); //changed it to model because viewers will confused about it

                model.setSelected(true);

                backPleatSelected = String.valueOf(model.getOptionValude());

                item.set(position, model);

                if (preSelectedIndex > -1){

                    ItemModel preRecord = item.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    item.set(preSelectedIndex, preRecord);

                }
                 preSelectedIndex = position;


                adapter.updateRecords(item);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.backPleats = "";
                getDialog().dismiss();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.backPleats = backPleatSelected;
                getDialog().dismiss();


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
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPos);

    }

}
