package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.InnerCollarFabricAdapter;
import com.app.bespokino.adapter.InnerCollarFancyAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.model.FabricContrast;
import com.app.bespokino.model.ItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunilvg on 19/07/17.
 */

public class InnercontrastDialogFragment extends DialogFragment implements View.OnClickListener {

    GridView gv,gv2;
    Bundle bundle = new Bundle();
    List<FabricContrast>fancyFabricList;
    List<FabricContrast>solidFabricList;


    FabricContrast fabricContrast;

    Button cancel,solid,fancy;
    private ProgressDialog pDialog;
    int preSelectedIndex = -1;
    int preSelectedIndex1 = -1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.inner_collar_dialogfragment,null);

        fancyFabricList = new ArrayList<>();
        solidFabricList = new ArrayList<>();

        new FetchContrastFabric().execute(6);

        solid =  (Button)rootView.findViewById(R.id.btSolid);
        fancy  = (Button)rootView.findViewById(R.id.btFancy);
        cancel = (Button)rootView.findViewById(R.id.btCancel);

        solid.setTextColor(Color.BLACK);
        fancy.setTextColor(Color.GRAY);

        gv= (GridView) rootView.findViewById(R.id.gridview);
        gv2= (GridView) rootView.findViewById(R.id.gridview2);

        final FragmentManager fm = getFragmentManager();

        final AddContrastDialogFragment addContrastDialogFragment = new AddContrastDialogFragment();


        final InnerCollarFabricAdapter innerCollarFabricAdapter =new InnerCollarFabricAdapter(getActivity(),solidFabricList);
        gv.setAdapter(innerCollarFabricAdapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FabricContrast solidContrast = solidFabricList.get(position);

                fabricContrast.setSelected(true);

                solidFabricList.set(position, fabricContrast);

                if (preSelectedIndex > -1){

                    FabricContrast preRecord = solidFabricList.get(preSelectedIndex);
                    preRecord.setSelected(false);
                    solidFabricList.set(preSelectedIndex, preRecord);

                }

                preSelectedIndex = position;

                innerCollarFabricAdapter.updateRecords(solidFabricList);

                // int contrastFabricID = fabricContrast.getFabricId();

                // AppConfig.contrastFabricID = String.valueOf(contrastFabricID);

                //addContrastDialogFragment.show(fm,"collarin");

            }
        });

        final InnerCollarFancyAdapter innerCollarFancyAdapter = new InnerCollarFancyAdapter(getActivity(),fancyFabricList);
        gv2.setVisibility(View.INVISIBLE);
        gv2.setAdapter(innerCollarFancyAdapter);
        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FabricContrast fabricContrast =  fancyFabricList.get(position);

                fabricContrast.setSelected(true);
                AppConfig.contrastFabricID = String.valueOf(fabricContrast);
               // addContrastDialogFragment.show(fm,"collarin");

                fancyFabricList.set(position, fabricContrast);

                if (preSelectedIndex > -1){

                    FabricContrast preRecord = (FabricContrast) fancyFabricList.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    fancyFabricList.set(preSelectedIndex, preRecord);

                }
                preSelectedIndex = position;

                innerCollarFancyAdapter.updateRecords(fancyFabricList);

            }
        });
        solid.setOnClickListener(this);
        fancy.setOnClickListener(this);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btFancy:
                new FetchContrastFabric().execute(5);
                gv.setVisibility(View.GONE);
                gv2.setVisibility(View.VISIBLE);
                fancy.setTextColor(Color.BLACK);
                solid.setTextColor(Color.GRAY);
                break;
            case R.id.btSolid:
                new FetchContrastFabric().execute(6);
                gv.setVisibility(View.VISIBLE);
                gv2.setVisibility(View.INVISIBLE);
                fancy.setTextColor(Color.GRAY);
                solid.setTextColor(Color.BLACK);
                break;

        }

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

    public class FetchContrastFabric extends AsyncTask<Integer, Object, String> {

        int id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading ...");
            showDialog();
        }

        @Override
        protected String doInBackground(Integer... integer) {
            id = integer[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String result = null;

            try {

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=populateFabrics&categoryID="+integer[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                result = buffer.toString();
                return result;
            } catch (IOException e) {
                Log.e("Include Fragment", "Error ", e);

                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Include", "Error closing stream", e);
                    }
                }
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            hideDialog();

            if(s==null){

                showMessage("Alert","Check your internet connection !");

            }else {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = null;
                    boolean error = false;

                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = (JSONObject) jsonArray.get(i);

                        error = jsonObject.getBoolean("Error");

                        if (!error) {

                            String image = jsonObject.getString("image");
                            int fabID = jsonObject.getInt("fabricID");
                            if (id == 5){

                                fancyFabricList.add(new FabricContrast(fabID,"http://www.bespokino.com/images/fabric/contrast/"+image,false));
                                InnerCollarFancyAdapter innerCollarFancyAdapter = new InnerCollarFancyAdapter(getActivity(),fancyFabricList);
                                //gv2.setVisibility(View.INVISIBLE);
                                gv2.setAdapter(innerCollarFancyAdapter);

                            }else{
                                solidFabricList.add(new FabricContrast(fabID,"http://www.bespokino.com/images/fabric/contrast/"+image,false));
                                InnerCollarFabricAdapter innerCollarFabricAdapter =new InnerCollarFabricAdapter(getActivity(),solidFabricList);
                                gv.setAdapter(innerCollarFabricAdapter);
                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    public void showMessage(String title,String message){

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // new FetchFabricImageTask().execute();
                getActivity().finish();
            }
        });
        builder.show();

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
