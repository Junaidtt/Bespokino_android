package com.app.bespokino.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.app.bespokino.R;
import com.app.bespokino.activity.AcceptcardPayment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {


    Button paymentButton;
    CheckBox cardCheckBox;

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_payment, container, false);

        paymentButton = view.findViewById(R.id.continueButton);
        cardCheckBox = (CheckBox)view.findViewById(R.id.checkBox);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean cardCheckBoxState = cardCheckBox.isChecked();

                if (cardCheckBoxState){
                    startActivity(new Intent(getActivity(), AcceptcardPayment.class));
                }

            }
        });

        return  view;

    }

}
