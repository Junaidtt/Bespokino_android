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
import com.app.bespokino.adapter.GridItemView;
import com.app.bespokino.adapter.GridViewAdapter;
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.ItemModel;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by sunilvg on 20/07/17.
 */

public class AddContrastDialogFragment extends DialogFragment {

    Button close,saveButton;
    private ArrayList<String> selectedStrings;

    boolean collarSelect,cuffSelect,ventSelect,placketSelect;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.options_dialog_fragment,null);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        close = (Button) rootView.findViewById(R.id.close);
        saveButton = (Button) rootView.findViewById(R.id.save);

        final List<ItemModel> item = new ArrayList<>();
        selectedStrings = new ArrayList<>();

        if (savedInstanceState != null) {

            collarSelect = savedInstanceState.getBoolean("col");
            cuffSelect = savedInstanceState.getBoolean("cuf");
            placketSelect = savedInstanceState.getBoolean("plak");
            ventSelect = savedInstanceState.getBoolean("vent");

            item.add(new ItemModel(collarSelect, "INNER COLLAR", R.drawable.inner_c, "INNER COLLAR", 547, 89));
            item.add(new ItemModel(cuffSelect, "INNER CUFF", R.drawable.inner_cuff, "INNER CUFF", 549, 90));
            item.add(new ItemModel(placketSelect, "INNER PLACKET", R.drawable.inner_placket, "INNER PLACKET", 150, 54));
            item.add(new ItemModel(ventSelect, "SLEEVE VENTE", R.drawable.sleeve_vent, "SLEEVE VENTE", 148, 55));


        }else {

            item.add(new ItemModel(false, "INNER COLLAR", R.drawable.inner_c, "INNER COLLAR", 547, 89));
            item.add(new ItemModel(false, "INNER CUFF", R.drawable.inner_cuff, "INNER CUFF", 549, 90));
            item.add(new ItemModel(false, "INNER PLACKET", R.drawable.inner_placket, "INNER PLACKET", 150, 54));
            item.add(new ItemModel(false, "SLEEVE VENTE", R.drawable.sleeve_vent, "SLEEVE VENTE", 148, 55));

        }

        final GridViewAdapter adapter = new GridViewAdapter(item,getActivity());

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.get(position);

                ItemModel model = item.get(position);
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                model = item.get(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) view).display(false);
                    model.setSelected(false);
                    selectedStrings.remove((ItemModel) parent.getItemAtPosition(position));

                    Toast.makeText(getActivity(),"false",Toast.LENGTH_SHORT).show();


                    switch (model.getItemName()){

                        case "INNER COLLAR":

                            AppConfig.collarContrastFabric = "";
                            collarSelect = true;


                            break;
                        case "INNER CUFF":
                            AppConfig.cuffContrastFabric =  "";
                            cuffSelect = true;

                            break;
                        case  "INNER PLACKET":

                            AppConfig.placketContrastFabric =  "";
                            placketSelect = true;

                            break;
                        case  "SLEEVE VENTE":

                            AppConfig.sleeveVentContrastFabric =  "";
                            ventSelect = true;


                            break;


                    }


                }else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) view).display(true);
                    model.setSelected(true);
                    selectedStrings.add(String.valueOf((ItemModel) parent.getItemAtPosition(position)));

                    Toast.makeText(getActivity(),model.getItemName(),Toast.LENGTH_SHORT).show();

                            switch (model.getItemName()){

                                case "INNER COLLAR":

                            AppConfig.contrastFabricCategory = "inner_collar";

                            AppConfig.collarContrastFabric =  String.valueOf(model.getOptionValude());

                            break;
                        case "INNER CUFF":
                            AppConfig.cuffContrastFabric =  String.valueOf(model.getOptionValude());
                            AppConfig.contrastFabricCategory = "inner_cuff";

                            break;
                        case  "INNER PLACKET":

                            AppConfig.placketContrastFabric =  String.valueOf(model.getOptionValude());
                            AppConfig.contrastFabricCategory = "inner_collar";

                            break;
                        case  "SLEEVE VENTE":

                            AppConfig.sleeveVentContrastFabric =  String.valueOf(model.getOptionValude());

                            break;


                    }

                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConfig.collarContrastFabric = "";
                AppConfig.cuffContrastFabric = "";
                AppConfig.placketContrastFabric = "";
                AppConfig.sleeveVentContrastFabric = "";
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("col", collarSelect);
        outState.putBoolean("cuf", cuffSelect);
        outState.putBoolean("vent", ventSelect);
        outState.putBoolean("plak", placketSelect);


    }
}
