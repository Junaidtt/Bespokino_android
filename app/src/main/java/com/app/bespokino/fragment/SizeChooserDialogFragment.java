package com.app.bespokino.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.GetMeasurmentActivity;
import com.app.bespokino.activity.SelfMeasuringToolActivity;
import com.app.bespokino.helper.AppConfig;

/**
 * Created by bespokino on 11/2/2017 AD.
 */

public class SizeChooserDialogFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener {

    Button continueButton;


    private Spinner modelSpinner;
    int modelValue;
    int UID;
    String model;
    TextView sizer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.size_chooser, container, false);


        UID = AppConfig.userID;
       // model = String.valueOf(AppConfig.modelNo);
        SharedPreferences prefs = getActivity().getSharedPreferences("modelno", 0);
        model = String.valueOf(prefs.getInt("modelNo", 0));


        modelSpinner = (Spinner)view.findViewById(R.id.spinner1);
        sizer = (TextView)view.findViewById(R.id.size);

        modelSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.model, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        int selectionPosition = adapter.getPosition("SIZE " + String.valueOf(model));
        modelSpinner.setSelection(selectionPosition);

        continueButton = (Button) view.findViewById(R.id.nextButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if (modelValue != 0){

                    SharedPreferences.Editor editor = getContext().getSharedPreferences("modelno", 0).edit();
                    editor.putInt("modelNo", modelValue);
                    editor.apply();
                    ((GetMeasurmentActivity) getActivity()).setCurrentItem(1, true);

                }else{

                    Toast.makeText(getContext(),"Choose your bespoke shirt model",Toast.LENGTH_SHORT).show();

                }


            }
        });
        sizer.setText("BESPOKINO SHIRT SIZE "+model );
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item=parent.getItemAtPosition(position).toString();
        if(!item.equalsIgnoreCase("HEIGHT")) {

            Toast.makeText(getActivity(), item, Toast.LENGTH_SHORT).show();
            String value = parent.getItemAtPosition(position).toString();

            switch (value){

                case "SIZE 1":
                    modelValue = Integer.parseInt("1");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 2":
                    modelValue = Integer.parseInt("2");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 3":
                    modelValue = Integer.parseInt("3");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 4":
                    modelValue = Integer.parseInt("4");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 5":
                    modelValue = Integer.parseInt("5");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 6":
                    modelValue = Integer.parseInt("6");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 7":
                    modelValue = Integer.parseInt("7");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 8":
                    modelValue = Integer.parseInt("8");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 9":
                    modelValue = Integer.parseInt("9");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 10":
                    modelValue = Integer.parseInt("10");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 11":
                    modelValue = Integer.parseInt("11");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 12":
                    modelValue = Integer.parseInt("12");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 13":
                    modelValue = Integer.parseInt("13");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;
                case "SIZE 14":
                    modelValue = Integer.parseInt("14");
                    sizer.setText("BESPOKINO SHIRT SIZE "+modelValue );
                    break;


            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getActivity().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getActivity().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }
}
