package com.app.bespokino.fragment;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.bespokino.R;
import com.app.bespokino.activity.MonogramActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FrenchCuffDialogFragment extends DialogFragment {

    Button saveButton;

    public FrenchCuffDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_french_cuff_dialog, container, false);
        saveButton = (view.findViewById(R.id.saveButton));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MonogramActivity.class));


            }
        });
        return  view;

    }

}
