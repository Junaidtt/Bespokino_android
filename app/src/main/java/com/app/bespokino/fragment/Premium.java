package com.app.bespokino.fragment;


import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.bespokino.R;
import com.app.bespokino.adapter.ThumbAdapter;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.FabricModel;

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
 * A simple {@link Fragment} subclass.
 */
public class Premium extends Fragment {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    Bundle bundle;
    List<FabricModel> iFabric;
    public Premium() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_includ, container, false);
        bundle = new Bundle();

        final FragmentManager fm = getFragmentManager();
        final ShirtDisplyDialogFragment shirtDisplyDialogFragment = new ShirtDisplyDialogFragment();

        iFabric = new ArrayList<>();
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager= new GridLayoutManager(getActivity(),2);
        }
        else{
            layoutManager= new GridLayoutManager(getActivity(),4);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        FabricModel fabricModel = iFabric.get(position);
                        iFabric.size();
                        // Toast.makeText(getActivity(),position+"clicked", Toast.LENGTH_LONG).show();
                        String img = fabricModel.getShirt_img();
                        bundle.putString("url",img);
                        bundle.putInt("fabId",fabricModel.getFabricID());
                        bundle.putString("type","premium");
                        bundle.putInt("addup",fabricModel.getAddup());

                        shirtDisplyDialogFragment.setArguments(bundle);
                        shirtDisplyDialogFragment.show(fm,"shirtdisply");

                    }
                })
        );
        new FetchFabricImageTasker().execute();

        return view;



    }
    public class FetchFabricImageTasker extends AsyncTask<Object, Object, String> {


        @Override
        protected String doInBackground(Object... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=populateFabrics&categoryID=2");
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

            // progressDialog.dismiss();

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
                            String shirtImage = jsonObject.getString("image");
                            int fabID = jsonObject.getInt("fabricID");
                            int addup = jsonObject.getInt("addupPrice");

                            iFabric.add(new FabricModel("http://www.bespokino.com/images/fabric/thumb/" + image, "http://www.bespokino.com/images/fabric/" + image, fabID,addup));

                        }
                    }

                    ThumbAdapter adapter = new ThumbAdapter(iFabric,getActivity());
                    recyclerView.setAdapter(adapter);


                    //  adapter = new FabricAdapter(getActivity(), iFabric);
                    // recyclerView.setAdapter(adapter);

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
}
