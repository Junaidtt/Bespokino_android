package com.app.bespokino.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.bespokino.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateAppFragment extends Fragment {


    public RateAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rate_app_fragement, container, false);
    }

}
