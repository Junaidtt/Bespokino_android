package com.app.bespokino.fragment;


import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.GetMeasurmentActivity;
import com.app.bespokino.adapter.LengthRVAdapter;
import com.app.bespokino.adapter.MeasuringAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.MeasuringToolTableValue;
import com.app.bespokino.model.ToolCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleeveDialogFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    List<ToolCode> toolCodeList;
    Button cancelButton,saveButton;
    String modelValue;
    double codeValue;


    public SleeveDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.measurment_pager_fragment, container, false);

      //  modelValue = getArguments().getString("model");
        ImageView shirtImage = (ImageView)view.findViewById(R.id.measurmentImage);
        shirtImage.setImageResource(R.drawable.sleev_guide);
        TextView titleTv = (TextView)view.findViewById(R.id.title);
        titleTv.setText("SLEEVE");

        SharedPreferences prefs = getActivity().getSharedPreferences("modelno", 0);
        modelValue = String.valueOf(prefs.getInt("modelNo", 0));


        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LengthRVAdapter(getActivity(),getToolColorCode());

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {



                        ToolCode toolCode = toolCodeList.get(position);
                        codeValue = toolCode.getImageValue();
                        double lengthMaster = setMasterValues(modelValue);
                        AppConfig.customerSleeve = lengthMaster + codeValue;
                        AppConfig.Sleeve = "1";
                        ((GetMeasurmentActivity) getActivity()).setCurrentItem(3, true);

                       // Toast.makeText(getActivity(),String.valueOf(toolCode.getImageValue()),Toast.LENGTH_SHORT).show();

                    }
                })
        );
      /*  cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double lengthMaster = setMasterValues(modelValue);
                AppConfig.customerSleeve = lengthMaster + codeValue;
                AppConfig.Sleeve = "1";
                getDialog().dismiss();

            }
        });*/




        return  view;
    }

    public List getToolColorCode(){


        toolCodeList = new ArrayList<>();

        toolCodeList.add(new ToolCode(R.drawable.blue_line,-4));
        toolCodeList.add(new ToolCode(R.drawable.green_line,-3));
        toolCodeList.add(new ToolCode(R.drawable.yellow_line,-2));
        toolCodeList.add(new ToolCode(R.drawable.red_line,-1));
        toolCodeList.add(new ToolCode(R.drawable.length_is_good,0));
        toolCodeList.add(new ToolCode(R.drawable.inch_1,2));
        toolCodeList.add(new ToolCode(R.drawable.inch_2,1));

        mRecyclerView.setAdapter(mAdapter);
        return toolCodeList;

    }


    public double setMasterValues(String model){
        double length = 0.0;
        List<MeasuringToolTableValue> measuringToolValueList = new ArrayList<>();
        measuringToolValueList.add(new MeasuringToolTableValue("1",18,24,32,29,38,27,12,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("2",18,24,34,31,39,27,13,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("3",19,25,36,33,40,30,13.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("4",19.5,25.5,38,35,41,30,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("5",19.5,25.5,40,37,42,30,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("6",20,25.5,42,39,43,30,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("7",20,25.5,44,41,44,31,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("8",21,26,46,43,45,31,16,10,45));

        measuringToolValueList.add(new MeasuringToolTableValue("9",21,26,48,46,47,31,16,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("10",22,26,50,47,49,32,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("11",22,26,52,50,51,32,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("12",23,26,54,52,52,33,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("13",23,26,56,54,54,33,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("14",24,26,60,58,58,33,17,11,50));

        measuringToolValueList.add(new MeasuringToolTableValue("1L",18,24,32,29,38,30,12,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("2L",18,24,34,31,39,30,13,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("3L",19,25,36,33,40,33,13.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("4L",19.5,25.5,38,35,41,33,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("5L",19.5,25.5,40,37,42,33,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("6L",20,25.5,42,39,43,33,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("7L",20,25.5,44,41,44,34,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("8L",21,26,46,43,45,34,16,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("9L",21,26,48,46,47,34,16,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("10L",22,26,50,47,49,35,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("11L",22,26,52,50,51,35,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("12L",23,26,54,52,52,36,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("13L",23,26,56,54,54,36,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("14L",24,26,60,58,58,36,17,11,50));


        for (int i=0; i<measuringToolValueList.size();i++){

            if (measuringToolValueList.get(i).getModelNo().equals(model)){

                length = measuringToolValueList.get(i).getLengthMaster();

            }

        }

        return length;
    }

}
