package com.app.bespokino.fragment;

import android.app.DialogFragment;
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
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by sunilvg on 21/07/17.
 */

public class PlacketInnerDialogFragment extends DialogFragment {

    Button close,saveButton;
    boolean exist;

    int preSelectedIndex = -1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.options_dialog_fragment,null);
        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        close =(Button)rootView.findViewById(R.id.close);
        saveButton =(Button)rootView.findViewById(R.id.save);

        final List<ItemModel> item = new ArrayList<>();
        item.add(new ItemModel(false,"INNER COLLAR",R.drawable.inner_c,"INNER COLLAR",547,89));
        item.add(new ItemModel(false,"INNER CUFF",R.drawable.inner_cuff,"INNER CUFF",549,90));
        item.add(new ItemModel(false,"SLEEVE VENTE",R.drawable.sleeve_vent,"SLEEVE VENTE",148,55));

        final ItemAdapter adapter = new ItemAdapter(getActivity(), item);


        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.get(position);

                ItemModel model = item.get(position); //changed it to model because viewers will confused about it

                if (model.getItemName() == "INNER COLLAR"){
                    AppConfig.cuffContrastFabric = String.valueOf(model.getOptionValude());
                }else if (model.getItemName() == "INNER CUFF"){
                    AppConfig.cuffContrastFabric = String.valueOf(model.getOptionValude());
                }else if (model.getItemName() == "SLEEVE VENT"){
                    AppConfig.sleeveVentContrastFabric = String.valueOf(model.getOptionValude());
                }

                item.set(position, model);

                if(model.isSelected()){

                    model.setSelected(false);

                }else {

                    model.setSelected(true);

                }

                adapter.updateRecords(item);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}
