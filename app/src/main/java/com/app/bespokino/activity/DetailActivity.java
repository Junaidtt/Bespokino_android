package com.app.bespokino.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.DetailItemAdapter;
import com.app.bespokino.model.ItemModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

public class DetailActivity extends AppCompatActivity {


    List<ItemModel>DetailList;
    GridView gridView;
    DetailItemAdapter adapter;
    ImageView fabImage;
    int customerID,paperNo,orderNo;
    String trackingNo = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        customerID = getIntent().getExtras().getInt("customerID");
        paperNo = getIntent().getExtras().getInt("paperNo");
        orderNo = getIntent().getExtras().getInt("orderNo");
        trackingNo = getIntent().getExtras().getString("trackingID").replace(" ","%20");
        //String trackNO = getIntent().getExtras().getString("trackingID").replace()



        DetailList = new ArrayList<>();

        new ShirtItemFetcherTask().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
         gridView = (GridView)findViewById(R.id.gridview);
        fabImage = (ImageView)findViewById(R.id.shirtItemImage);

        adapter = new DetailItemAdapter(this, DetailList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ItemModel itemModel = DetailList.get(i);
                if (itemModel.getChoiceID() == 48){

                    Intent intent = new Intent(DetailActivity.this,MonogramDetailsActivity.class);

                    intent.putExtra("customerID",customerID);
                    intent.putExtra("orderNo",orderNo);
                    intent.putExtra("paperNo",paperNo);
                    intent.putExtra("trackingID",trackingNo);

                    startActivity(intent);

                }


            }
        });


    }

    private class ShirtItemFetcherTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {
               // URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getCartListItem&customerID=51818&orderNo=4380179&paperNo=50541&trackingID=4380179%20-%2051818%20-%201");
                //
                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getCartListItem&customerID="+customerID+"&orderNo="+orderNo+"&paperNo="+paperNo+"&trackingID="+trackingNo+"");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "*/*");
                urlConnection.connect();


                // Read the input stream into a String

                int c = urlConnection.getResponseCode();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                int c1 = urlConnection.getResponseCode();

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
                Log.e("DetailActivity", "Error ", e);

                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("DetailActivity", "Error closing stream", e);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s !=null){


                try {
                    JSONObject jsonObject = new JSONObject(s);

                    //addition

                    int cuff = jsonObject.getInt("cuff");

                    DetailList.add(cuffFiner(cuff));

                    int collar = jsonObject.getInt("collar");

                    DetailList.add(collarFinder(collar));

                    boolean monogramStatus = jsonObject.getBoolean("monogramStatus");
                    if (monogramStatus){
                        int monogram =jsonObject.getInt("monogram");
                        DetailList.add(monoFiner(monogram));
                    }



//                    if (jsonObject.getString("cuff").equals("")){
//                        String cuff = jsonObject.getString("cuff");
//                    }else{
//                        int cuff = jsonObject.getInt("cuff");
//
//                        DetailList.add(cuffFiner(cuff));
//
//                    }

//                    if (jsonObject.getString("collar").equals("")){
//                        String collar = jsonObject.getString("collar");
//
//                    }else{
//                        int collar = jsonObject.getInt("collar");
//
//                        DetailList.add(collarFinder(collar));
//
//                    }


//
//                    if (jsonObject.getString("monogramStatus").equals("")){
//                        String monogram =jsonObject.getString("monogram");
//
//                    }
//                    if(jsonObject.getString("monogram").equals("false")){
//                        Log.d("DETAIL","NO MONOGRAM");
//                    }
//                    else {
//                        int monogram =jsonObject.getInt("monogram");
//                        DetailList.add(monoFiner(monogram));
//
//
//                    }


                    String image = jsonObject.getString("image");
                    boolean additionalOptions = jsonObject.getBoolean("additionalOptions");
                    if (additionalOptions){
                        new AddOptionTask().execute();
                    }



                    fabricLoader(image);
                    adapter.updateRecords(DetailList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    private void fabricLoader(String image) {

        Glide.with(DetailActivity.this).load("http://www.bespokino.com/images/fabric/"+image)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(fabImage);


    }

    public ItemModel collarFinder(int collar){

        final List<ItemModel> item = new ArrayList<>();
        ItemModel itemModel = null;
        item.add(new ItemModel(false,"NORMAL",R.drawable.collar_a,"COL",191,42));
        item.add(new ItemModel(false,"WIDE SPREAD",R.drawable.collar_b,"COL",192,42));
        item.add(new ItemModel(false,"ROUND EDGE",R.drawable.collar_c,"COL",193,42));
        item.add(new ItemModel(false,"BUTTON DOWN",R.drawable.collar_d,"COL",194,42));
        item.add(new ItemModel(false,"MANDARIN",R.drawable.collar_e,"COL",195,42));

        for (int i=0;i<item.size();i++){


            if (item.get(i).getOptionValude()==collar){

            //DetailList.add(item.get(i));
                itemModel = item.get(i);

            }
        }
        //DetailList.size();
        return itemModel;
    }

    public ItemModel cuffFiner(int cuff){

        final List<ItemModel> item = new ArrayList<>();
        ItemModel itemModel = null;

        item.add(new ItemModel(false,"1 BUTTON SQUARE",R.drawable.button1square,"CUF",200,43));
        item.add(new ItemModel(false,"1 BUTTON CURVED",R.drawable.button1curved,"CUF",201,43));
        item.add(new ItemModel(false,"1 BUTTON ANGLED",R.drawable.button1angled,"CUF",531,43));
        item.add(new ItemModel(false,"2 BUTTONS SQUARE",R.drawable.button2squared,"CUF",202,43));
        item.add(new ItemModel(false,"2 BUTTONS CURVED",R.drawable.buttons2curved,"CUF",532,43));
        item.add(new ItemModel(false,"2 BUTTONS ANGLE",R.drawable.buttons2angled,"CUF",203,43));
        item.add(new ItemModel(false,"FRENCH SQUARED",R.drawable.frenchsquared,"CUF",533,43));
        item.add(new ItemModel(false,"FRENCH CURVED",R.drawable.frenchcurved,"CUF",359,43));
        item.add(new ItemModel(false,"FRENCH ANGLED",R.drawable.frenchangled,"CUF",358,43));

        for (int i=0;i<item.size();i++){


            if (item.get(i).getOptionValude()==cuff){

                //DetailList.add(item.get(i));
                itemModel = item.get(i);

            }



        }


        return itemModel;

    }

    public ItemModel monoFiner(int monogram){

        final List<ItemModel> item = new ArrayList<>();
        ItemModel itemModel = null;

        item.add(new ItemModel(false,"POCKET",R.drawable.monogram_pocket,"MONO",172,48));
        item.add(new ItemModel(false,"CUFF",R.drawable.monogram_cuff,"MONO",171,48));
        item.add(new ItemModel(false,"BACK OF COLLAR",R.drawable.monogram_back_collar,"MONO",174,48));
        item.add(new ItemModel(false,"INSIDE COLLAR",R.drawable.monogram_inside_collar,"MONO",173,48));
        item.add(new ItemModel(false,"BODY",R.drawable.monogram_body,"MONO",170,48));

        for (int i=0;i<item.size();i++){


            if (item.get(i).getOptionValude()==monogram){

                itemModel = item.get(i);


            }



        }


        return itemModel;

    }

    private class AddOptionTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;
            try {

                //http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getOrderMonogramValue&customerID=51818&orderNo=4380179&paperNo=50541&trackingID=4380179%20-%2051818%20-%201
                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getAdditionalOptionsInfo&customerID="+customerID+"&orderNo="+orderNo+"&paperNo="+paperNo+"&trackingID="+trackingNo+"");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
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


            if (s !=null) {


                try {
                    JSONObject jsonObject = new JSONObject(s);


                    jsonObject.getString("pocket").equals("");
                    jsonObject.getString("whiteCuffAndCollar").equals("");


                    if (jsonObject.getInt("pocket") == 187){
                        DetailList.add(new ItemModel(false,"ADD POCKET",R.drawable.pocket,"POCKET",187,46));
                        adapter.updateRecords(DetailList);
                    }



                    if (jsonObject.getInt("whiteCuffAndCollar")== 206){

                        DetailList.add(new ItemModel(false,"WHITE COLLAR & CUFF",R.drawable.whitec,"WHITE",206,62));
                        adapter.updateRecords(DetailList);

                    }

                    if (jsonObject.getInt("shortSleeve") == 208){
                        DetailList.add(new ItemModel(false,"SHORT SLEAVE",R.drawable.short_sleev,"SHORT",208,63));
                        adapter.updateRecords(DetailList);
                    }

                    if (jsonObject.getInt("tuxedo") == 527){

                        DetailList.add(new ItemModel(false, "YES TUXEDO", R.drawable.yes_tuxedo_pleats, "TUXEDO", 527, 86));

                    }


                    if (jsonObject.getInt("frontTuxedoPleat") == 527){
                        //DetailList.add(new ItemModel(false, "YES TUXEDO", R.drawable.yes_tuxedo_pleats, "TUXEDO", 527, 86));

                    }else if(jsonObject.getInt("frontTuxedoPleat") == 544) {
                        DetailList.add(new ItemModel(false, "YES TUXEDO", R.drawable.yes_tuxedo_pleats, "TUXEDO", 527, 86));
                        DetailList.add(new ItemModel(false,"TUXEDO WITH PLEATS",R.drawable.tuxedo_pleats,"TUXEDO",544,86));
                    }


                    if (jsonObject.getInt("sleeveVentFabricContrast") == 148){
                     //   String sleeveVentFabricContrast = jsonObject.getString("sleeveVentFabricContrast");
                        DetailList.add(new ItemModel(false,"SLEEVE VENT",R.drawable.sleeve_vent,"VENT",148,55));

                    }

                    if (jsonObject.getInt("backPleats") == 156){
                        DetailList.add(new ItemModel(false,"NO PLEAT",R.drawable.nopleats, "PLEAT",156,45));

                    }else if (jsonObject.getInt("backPleats") == 157){
                        DetailList.add(new ItemModel(false,"ONE PLEAT",R.drawable.onepleats, "PLEAT",157,45));

                    }else if (jsonObject.getInt("backPleats") == 158){
                        DetailList.add(new ItemModel(false,"TWO PLEAT",R.drawable.twopleats, "PLEAT",158,45));

                    }



                    if (jsonObject.getInt("placket") ==159){
                        DetailList.add(new ItemModel(false,"NO PLACKET",R.drawable.noplacket, "PLACKET",159,44));

                    }else if (jsonObject.getInt("placket") ==160){
                        DetailList.add(new ItemModel(false,"PLACKET",R.drawable.placketb, "PLACKET",160,44));

                    }else if (jsonObject.getInt("placket") ==161){
                        DetailList.add(new ItemModel(false,"HIDDEN PLACKET",R.drawable.placketc,"PLACKET",161,44));


                    }

                    if (jsonObject.getInt("collarFabricContrast") == 547){
                      //  String collarFabricContrast = jsonObject.getString("collarFabricContrast");
                        DetailList.add(new ItemModel(false,"INNER COLLAR",R.drawable.inner_c,"INNER",547,89));

                    }

                    if (jsonObject.getInt("cuffFabricContrast") == 549){
                        //  String collarFabricContrast = jsonObject.getString("collarFabricContrast");
                        DetailList.add(new ItemModel(false,"INNER CUFF",R.drawable.inner_cuff,"INNER",549,89));

                    }


                    if (jsonObject.getInt("placketFabricContrast") == 150){
                        //  String collarFabricContrast = jsonObject.getString("collarFabricContrast");
                        DetailList.add(new ItemModel(false,"INNER PLACKET",R.drawable.inner_placket,"INNER",549,89));

                    }
                    adapter.updateRecords(DetailList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }


}
