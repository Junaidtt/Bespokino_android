package com.app.bespokino.fragment;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.CheckoutActivity;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.helper.UserStorageHelper;



/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingFragment extends Fragment {

    ViewPager viewPager;
    Button continueButton;
    CheckBox checkBox;
    EditText fullname,lastName,streetAddress,city,state,zipCode,country;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String FULLNAME,ADDRESS,CITY,ZIPCODE,STATE,COUNTRY;

    UserStorageHelper userStorageHelper;

    SQLiteHandler sqLiteHandler;

    public ShippingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_shipping, container, false);
        userStorageHelper = new UserStorageHelper(getActivity());

        sqLiteHandler = new SQLiteHandler(getActivity());

        fullname = (EditText)view.findViewById(R.id.edFullName);
        //lastName = (EditText)view.findViewById(R.id.edLastName);
        streetAddress = (EditText)view.findViewById(R.id.edStreetAddress);
        city = (EditText)view.findViewById(R.id.edCity);
        state = (EditText)view.findViewById(R.id.edState);
        zipCode = (EditText)view.findViewById(R.id.edZipcode);
        country = (EditText)view.findViewById(R.id.edCountry);



        continueButton = (Button)view.findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String FULLNAME = fullname.getText().toString().trim();
             //   String LASTNAME = lastName.getText().toString().trim();
                String ADDRESS = streetAddress.getText().toString().trim();
                String CITY = city.getText().toString().trim();
                String STATE = state.getText().toString().trim();
                String ZIPCODE = zipCode.getText().toString();
                String COUNTRY = country.getText().toString().trim();

                if (FULLNAME.isEmpty() || ADDRESS.isEmpty() || CITY.isEmpty() || STATE.isEmpty()
                        || ZIPCODE.isEmpty() || COUNTRY.isEmpty()) {
                    showMessage();
                }else
                {
                    ((CheckoutActivity)getActivity()).setCurrentItem (2, true);
                }

            }
        });

        setData();

        return  view;
    }


    public void showMessage(){

        //custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        //Custom Android Allert Dialog Title
        dialog.setTitle("BESPOKINO");

        TextView tv = (TextView) dialog.findViewById(R.id.tv);

        tv.setText("All fields are required .");

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();


    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            // Refresh your fragment here
//
//            Cursor res =  userStorageHelper.getAllData();
//            if(res.getCount() == 0){
//
//                Toast.makeText(getActivity(),"Enter Shipping details",Toast.LENGTH_SHORT).show();
//
//            }else {
//
//                while (res.moveToNext()){
//
//                    fullname.setText(res.getString(1));
//                    lastName.setText(res.getString(2));
//                    streetAddress.setText(res.getString(3));
//                    city.setText(res.getString(4));
//                    state.setText(res.getString(5));
//                    zipCode.setText(res.getString(6));
//                    country.setText(res.getString(7));
//
//                }
//            }
//        }
//    }



    public void setData(){

        Cursor res = sqLiteHandler.getAllData();

        while (res.moveToNext()){

            fullname.setText( res.getString(1));

            streetAddress.setText(res.getString(2));

            city.setText(res.getString(7));

            state.setText(res.getString(8));

            zipCode.setText(res.getString(5));
        }



    }
}
