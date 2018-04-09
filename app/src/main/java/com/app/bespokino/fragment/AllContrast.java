package com.app.bespokino.fragment;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.GetMeasurmentActivity;
import com.app.bespokino.adapter.ContrastAdapter;
import com.app.bespokino.adapter.InnerCollarFabricAdapter;
import com.app.bespokino.adapter.InnerCollarFancyAdapter;
import com.app.bespokino.adapter.MeasuringAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.FabricContrast;
import com.app.bespokino.model.ToolCode;

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
 * Created by bespokino on 10/29/2017 AD.
 */

public class AllContrast extends DialogFragment {


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    Button cancelButton;
    List<FabricContrast> fabricContrasts;
    int currentPos = -1;
    private ProgressDialog pDialog;
    Button save,cancel;
    FabricContrast fabricItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_contrast_fragemnt, container, false);

        if (savedInstanceState != null) {

            currentPos = savedInstanceState.getInt("position", 0);
        }

        new FetchContrastFabric().execute(5);
        fabricContrasts = new ArrayList<>();

        cancel = (Button)view.findViewById(R.id.btnCalncel);
       // save = (Button)view.findViewById(R.id.btnSave);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        final FragmentManager fm = getFragmentManager();
        final AddContrastDialogFragment addContrastDialogFragment = new AddContrastDialogFragment();


        mLayoutManager = new GridLayoutManager(getActivity(),4);
       // mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContrastAdapter(currentPos,getActivity(),fabricContrasts);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                fabricItem = fabricContrasts.get(position);
                currentPos = position;
                AppConfig.contrastFabricID = String.valueOf(fabricItem.getFabricId());
                addContrastDialogFragment.show(fm,"contrast");
                getDialog().dismiss();

              //  Toast.makeText(getActivity(),String.valueOf(fabricContrast.getFabricId()),Toast.LENGTH_SHORT).show();
         }
        }));



        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppConfig.contrastFabricID = "";
                getDialog().dismiss();


            }
        });

     /*   save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            addContrastDialogFragment.show(fm,"contrast");
            getDialog().dismiss();

            }
        });*/


        return  view;
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

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=populateFabrics&categoryID=5");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                int c = urlConnection.getResponseCode();

                // Read the input stream into a String
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
            fabricContrasts.clear();
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

                            fabricContrasts.add(new FabricContrast(fabID,"http://www.bespokino.com/images/fabric/contrast/"+image,false));

                            mAdapter = new ContrastAdapter(currentPos,getActivity(),fabricContrasts);
                            mRecyclerView.setAdapter(mAdapter);


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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPos);

    }


}
