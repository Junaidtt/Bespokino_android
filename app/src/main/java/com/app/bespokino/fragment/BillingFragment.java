package com.app.bespokino.fragment;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.CheckoutActivity;
import com.app.bespokino.adapter.PlaceArrayAdapter;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.helper.UserStorageHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillingFragment extends Fragment{




    ViewPager viewPager;
    boolean check;
    CheckBox billingCheckbox;

    EditText fullname,lastName,streetAddress,city,state,zipCode,country;
    String FULLNAME,LASTNAME,ADDRESS,CITY,ZIPCODE,STATE,COUNTRY,PAYBYCC,ORDERNO,CUSTOMERNO,PAPERNO;
    SharedPreferences pref;
    Button continueButton;
    private UserStorageHelper userStorageHelper;
    SQLiteHandler sqLiteHandler;
    public BillingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        userStorageHelper = new UserStorageHelper(getActivity());

        sqLiteHandler = new SQLiteHandler(getActivity());

        fullname = (EditText)view.findViewById(R.id.edFullName);
       // lastName = (EditText)view.findViewById(R.id.edLastName);
        streetAddress = (EditText)view.findViewById(R.id.edStreetAddress);
        city = (EditText)view.findViewById(R.id.edCity);
        state = (EditText)view.findViewById(R.id.edState);
        zipCode = (EditText)view.findViewById(R.id.edZipcode);
        country = (EditText)view.findViewById(R.id.edCountry);

        continueButton = (Button)view.findViewById(R.id.continueButton);
        billingCheckbox =(CheckBox)view.findViewById(R.id.billCheckBox);

        Cursor res =  userStorageHelper.getAllData();
        if(res.getCount() != 0){

            while (res.moveToNext()){


                fullname.setText(res.getString(1));
               // lastName.setText(res.getString(2));
                streetAddress.setText(res.getString(3));
                city.setText(res.getString(4));
                state.setText(res.getString(5));
                zipCode.setText(res.getString(6));
                country.setText(res.getString(7));

               PAYBYCC = res.getString(8);
                ORDERNO = res.getString(9);
                CUSTOMERNO = res.getString(10);
                PAPERNO = res.getString(11);

            }

        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor;
                String FULLNAME = fullname.getText().toString().trim();
              //  String LASTNAME = lastName.getText().toString().trim();
                String ADDRESS = streetAddress.getText().toString().trim();
                String CITY = city.getText().toString().trim();
                String STATE = state.getText().toString().trim();
                String ZIPCODE = zipCode.getText().toString();
                String COUNTRY = country.getText().toString().trim();

                Boolean checkBoxState = billingCheckbox.isChecked();

                if (FULLNAME.isEmpty() || ADDRESS.isEmpty() || CITY.isEmpty() || STATE.isEmpty()
                        || ZIPCODE.isEmpty() || COUNTRY.isEmpty()) {
                    showMessage();
                }else {


                    if (checkBoxState) {
                        Cursor res = userStorageHelper.getAllData();
                        if (res.getCount() == 0) {

                            boolean isInserted = userStorageHelper.insertData(FULLNAME, LASTNAME, ADDRESS, CITY, STATE, ZIPCODE, COUNTRY,PAYBYCC,ORDERNO,CUSTOMERNO,PAPERNO);

                            if (isInserted) {
                               Toast.makeText(getActivity(), "Data inserted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "INSERTION FAILED", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            boolean isUpdate = userStorageHelper.updateData("1", FULLNAME, LASTNAME, ADDRESS, CITY, STATE, ZIPCODE, COUNTRY,PAYBYCC,ORDERNO,CUSTOMERNO,PAPERNO);

                            if (isUpdate == true) {
                                Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getActivity(), "Updation failed", Toast.LENGTH_SHORT).show();

                            }


                            }

                        ((CheckoutActivity) getActivity()).setCurrentItem(1, true);
                    }else {
                        ((CheckoutActivity) getActivity()).setCurrentItem(1, true);

                    }
                }

            }
        });

        fullname.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });

        setData();

        return  view;

    }

    public void showMessage(){


        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        // Custom Android Allert Dialog Title
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here
            UserStorageHelper userStorageHelper = new UserStorageHelper(getActivity());

        }
    }

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
