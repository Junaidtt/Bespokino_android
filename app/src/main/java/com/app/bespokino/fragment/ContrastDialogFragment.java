package com.app.bespokino.fragment;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.ContrastAdapter;
import com.app.bespokino.adapter.FabricAdapter;
import com.app.bespokino.adapter.InnerCollarFabricAdapter;
import com.app.bespokino.adapter.InnerCollarFancyAdapter;
import com.app.bespokino.adapter.MeasuringAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.FabricContrast;
import com.app.bespokino.model.ToolCode;
import com.bumptech.glide.Glide;

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
 * Created by bespokino on 10/20/2017 AD.
 */

public class ContrastDialogFragment extends DialogFragment {

    RecyclerView mRecyclerViewSolid;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    ImageView selectedContrast;
    List<FabricContrast>fabricContrastList = new ArrayList<>();

    Button cancel,solid,fancy,save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.rv_contrast_dialog,null);

        final String strtext = getArguments().getString("VALUE");
        solid = (Button)view.findViewById(R.id.btSolid);
        fancy  = (Button)view.findViewById(R.id.btFancy);
        cancel = (Button)view.findViewById(R.id.btCancel);
        save = (Button)view.findViewById(R.id.btSave);
       // selectedContrast = (ImageView)view.findViewById(R.id.selectedContast);

        solid.setTextColor(Color.BLACK);
        fancy.setTextColor(Color.GRAY);

        new FetchContrastFabricTask().execute(6);
        mRecyclerViewSolid = (RecyclerView)view.findViewById(R.id.recyclerview);
        mRecyclerViewSolid.setHasFixedSize(true);


        mLayoutManager = new GridLayoutManager(getActivity(),4);
        mRecyclerViewSolid.setLayoutManager(mLayoutManager);

        mRecyclerViewSolid.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                FabricContrast fabricContrast = fabricContrastList.get(position);
                int contrastFabricID = fabricContrast.getFabricId();
                AppConfig.contrastFabricID = String.valueOf(contrastFabricID);




            }
        }));


       solid.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               fancy.setTextColor(Color.GRAY);
               solid.setTextColor(Color.BLACK);
               fabricContrastList.clear();
               new FetchContrastFabricTask().execute(6);

           }
       });

        fancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fancy.setTextColor(Color.BLACK);
                solid.setTextColor(Color.GRAY);
                fabricContrastList.clear();
                new FetchContrastFabricTask().execute(5);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getDialog().dismiss();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (strtext){

                    case "INNER COLLAR":
                        AppConfig.collarContrastFabric = "547";
                        getDialog().dismiss();
                        break;
                    case "INNER CUFF":
                        AppConfig.cuffContrastFabric = "549";
                        getDialog().dismiss();
                        break;
                    case "INNER PLACKET":
                        AppConfig.placketContrastFabric = "150";
                        getDialog().dismiss();
                        break;
                    case "SLEEVE VENT":
                        AppConfig.sleeveVentContrastFabric = "148";
                        getDialog().dismiss();
                        break;

                }
            }
        });
       setSelectedContrast();


        return view;


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



    public class FetchContrastFabricTask extends AsyncTask<Integer, Object, String> {

        int id;


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


            if(s==null){

               // showMessage("Alert","Check your internet connection !");

            }else {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = null;
                    boolean error = false;
                    fabricContrastList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = (JSONObject) jsonArray.get(i);

                        error = jsonObject.getBoolean("Error");

                        if (!error) {

                            String image = jsonObject.getString("image");
                            int fabID = jsonObject.getInt("fabricID");

                                fabricContrastList.add(new FabricContrast(fabID, "http://www.bespokino.com/images/fabric/contrast/" + image, false));

                        }
                    }
                    setSelectedContrast();
                    //mAdapter = new ContrastAdapter(fabricContrastList,getActivity());
                    mRecyclerViewSolid.setAdapter(mAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void setSelectedContrast(){




            for (int i =0; i<fabricContrastList.size();i++){

                if (AppConfig.contrastFabricID.equals(String.valueOf(fabricContrastList.get(i).getFabricId()))){
                  // String urlContrast = fabricContrastList.get(i).getImage();
                    Glide.with(getActivity()).load(fabricContrastList.get(i).getImage()).into(selectedContrast);

                }


            }



    }
}




