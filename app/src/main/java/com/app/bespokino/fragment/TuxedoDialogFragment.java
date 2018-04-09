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
 * Created by sunilvg on 20/07/17.
 */

public class TuxedoDialogFragment extends DialogFragment {

    Button close,save;

    int preSelectedIndex = 0;
    int currentPos=0;


    ItemModel model;
    List<ItemModel> item;

    String tuxedoSelected= "";
    String tuxedWithPleats = "";
    boolean yesTuxedo;
    boolean pleatTuxed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tuxed_dialog_fragment,null);
        if (savedInstanceState != null) {

            currentPos = savedInstanceState.getInt("cPos", 0);

        }
        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);

        close =(Button)rootView.findViewById(R.id.close);
        save =(Button)rootView.findViewById(R.id.saveButton);

        item = new ArrayList<>();

        item.add(new ItemModel(false,"YES TUXEDO",R.drawable.yes_tuxedo_pleats,"TUXEDO",527,86));
        item.add(new ItemModel(false,"TUXEDO WITH PLEATS",R.drawable.tuxedo_pleats,"TUXEDO",544,86));

        final ItemAdapter adapter = new ItemAdapter(getActivity(), item);



       if (currentPos == 0){
           item.get(0).setSelected(true);
       }else  if (currentPos == 1){

           item.get(0).setSelected(true);
           item.get(1).setSelected(true);

       }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item.get(position);

                 model = item.get(position);
                currentPos = position;
                if (model.getItemName() == "YES TUXEDO"){
                    tuxedoSelected = String.valueOf(model.getOptionValude());
                    item.get(1).setSelected(false);
                }
                else if (model.getItemName() == "TUXEDO WITH PLEATS"){

                    tuxedoSelected ="527";
                    tuxedWithPleats = String.valueOf(model.getOptionValude());
                    item.get(0).setSelected(true);
                }
                item.set(position, model);

                if(model.isSelected()){

                    model.setSelected(false);
                    currentPos = 0;

                }else {
                    model.setSelected(true);

                }
                adapter.updateRecords(item);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppConfig.tuxedo = "";
                AppConfig.tuxedoPleat = "";
                getDialog().dismiss();


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.tuxedo = tuxedoSelected;
                AppConfig.tuxedoPleat = tuxedWithPleats;
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

        outState.putInt("cPos", currentPos);
        /*if (item.get(1).isSelected() == true){

            outState.putInt("cPos", 2);

        }
        else  if (item.get(0).isSelected() == false){

            outState.putInt("cPos", 0);

        }
        else {
            outState.putInt("cPos", currentPos);
        }
*/
    }

}
