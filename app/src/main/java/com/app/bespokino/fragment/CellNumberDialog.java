package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.AdditionaloptionsActivity;
import com.app.bespokino.activity.CartActivity;
import com.app.bespokino.activity.MainActivity;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import dmax.dialog.SpotsDialog;


/**
 * Created by sunilvg on 20/07/17.
 */

public class CellNumberDialog extends DialogFragment {

    Button save;
    EditText edPhone;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.cellnumber_layout_fragment,null);

        save =(Button)rootView.findViewById(R.id.saveButton);
        edPhone = (EditText)rootView.findViewById(R.id.edPhoneNumber);
        //progressDialog = new SpotsDialog(getActivity(),R.style.Custom);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = edPhone.getText().toString().trim();
                if (phoneNumber.isEmpty()){

                    Toast.makeText(getActivity(),"Your cell number please!",Toast.LENGTH_SHORT).show();

                }else{

                    //new PostAddOptionsTask().execute();




                }

            }
        });

        return  rootView;
    }




   /* private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

}
