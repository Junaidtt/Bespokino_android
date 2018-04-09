package com.app.bespokino.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.BespokeProfileAdapter;
import com.app.bespokino.adapter.BodyPostureProfileAdapter;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.model.BespokeItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class BespokinoProfile extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static RecyclerView.Adapter adapterBody;

    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static RecyclerView recyclerViewBody;
    private static ArrayList<BespokeItem> bespokeItems;
    private static  ArrayList<BespokeItem> bodyPostureList;
    //static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    TextView tvPantsWaist,tvModelNumber;
    int userid;

    SQLiteHandler sqLiteHandler;

    String[] value = new String[]{
            "MODEL NUMBER",
            "PANTS WAIST SIZE",
            "BODY POSTURE",
            "BESPOKE MEASURMENT"
    };

    ListView listView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bespokino_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
        // myOnClickListener = new MyOnClickListener(this);
        bespokeItems = new ArrayList<>();
        bodyPostureList = new ArrayList<>();
        bespokeItems.clear();
        bodyPostureList.clear();

        //bespokeItems.add(new BespokeItem("PANTS WAIST SIZE",R.drawable.pants));
        // bespokeItems.add(new BespokeItem("MODEL NUMBER",R.drawable.chest_guide));
        //getData;

        tvPantsWaist = (TextView)findViewById(R.id.tvPantsWaist);
        tvModelNumber = (TextView)findViewById(R.id.tvModelNumber);

        sqLiteHandler = new SQLiteHandler(this);

        Cursor res =  sqLiteHandler.getAllData();
        while (res.moveToNext()){

            userid = res.getInt(4);


        }
        new GetMeasurmentTask().execute();

        recyclerViewBody = (RecyclerView) findViewById(R.id.bespoke_rv_item);
        recyclerViewBody.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewBody.setLayoutManager(layoutManager);
        recyclerViewBody.setItemAnimator(new DefaultItemAnimator());
        recyclerViewBody.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BodyPostureProfileAdapter(this, bodyPostureList);


        recyclerView = (RecyclerView) findViewById(R.id.body_rv_item);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //new FetchBodyPostureTask().execute();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new BespokeProfileAdapter(this, bespokeItems);
        recyclerView.setAdapter(adapter);



    }



    public class FetchBodyPostureTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            //Will contain the raw JSON response as a string.
            String result = null;


            try {

                // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=getBodyPostureInfo&userID="+userid);
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


            if (s != null){

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    String posture = jsonObject.getString("posture");

                    String shoulders= jsonObject.getString("shoulders");

                    String pelvis = jsonObject.getString("pelvis");

                    String chest = jsonObject.getString("chest");

                    String abdomen = jsonObject.getString("abdomen");

                    if (posture.equals("posture-NORMAL.jpg")){

                        bodyPostureList.add(new BespokeItem("POSTURE", R.drawable.posture_normal));


                    }else if (posture.equals("posture-LEANING.jpg")){

                        bodyPostureList.add(new BespokeItem("POSTURE", R.drawable.posture_leaning));


                    }else if (posture.equals("posture-ERACT.jpg")){

                        bodyPostureList.add(new BespokeItem("POSTURE", R.drawable.posture_erect));

                    }

                    if (shoulders.equals("shoulders-STEAP.jpg")){

                        bodyPostureList.add(new BespokeItem("SHOULDER", R.drawable.shoulders_steap));

                    }else if (shoulders.equals("shoulders-NORMAL.jpg")){

                        bodyPostureList.add(new BespokeItem("SHOULDER", R.drawable.shoulders_normal));

                    }else if (shoulders.equals("shoulders-FLAT.jpg")){

                        bodyPostureList.add(new BespokeItem("SHOULDER", R.drawable.shoulders_flat));

                    }


                    if (pelvis.equals("pelvis-THIN.jpg")){

                        bodyPostureList.add(new BespokeItem("PELVIS", R.drawable.pelvis_thin));

                    }else if (pelvis.equals("pelvis-NORMAL.jpg")){

                        bodyPostureList.add(new BespokeItem("PELVIS", R.drawable.pelvis_normal));

                    }else if (pelvis.equals("pelvis-CURVED.jpg")){

                        bodyPostureList.add(new BespokeItem("PELVIS", R.drawable.pelvis_curved));

                    }else if (pelvis.equals("pelvis-LARGE.jpg")){

                        bodyPostureList.add(new BespokeItem("PELVIS", R.drawable.pelvis_large));

                    }



                    if (abdomen.equals("abdomen-THIN.jpg")){

                        bodyPostureList.add(new BespokeItem("ABDOMEN", R.drawable.abdomen_thin));

                    }else if (abdomen.equals("abdomen-NORMAL.jpg")){

                        bodyPostureList.add(new BespokeItem("ABDOMEN", R.drawable.abdomen_normal));

                    }else if (abdomen.equals("abdomen-MEDIUM.jpg")){

                        bodyPostureList.add(new BespokeItem("ABDOMEN", R.drawable.abdomen_medium));

                    }else if (abdomen.equals("abdomen-LARGE.jpg")){

                        bodyPostureList.add(new BespokeItem("ABDOMEN", R.drawable.abdomen_large));

                    }


                    if (chest.equals("chest-THIN.jpg")){

                        bodyPostureList.add(new BespokeItem("CHEST", R.drawable.chest_thin));

                    }else if (chest.equals("chest-FIT.jpg")){

                        bodyPostureList.add(new BespokeItem("CHEST", R.drawable.chest_fit));

                    }else if (chest.equals("chest-NORMAL.jpg")){

                        bodyPostureList.add(new BespokeItem("CHEST", R.drawable.chest_normal));

                    }else if (chest.equals("chest-MUSCULAR.jpg")){

                        bodyPostureList.add(new BespokeItem("CHEST", R.drawable.chest_muscular));

                    }else if (chest.equals("chest-LARGE.jpg")){

                        bodyPostureList.add(new BespokeItem("CHEST", R.drawable.chest_large));

                    }


                    adapterBody = new BodyPostureProfileAdapter(getApplicationContext(), bodyPostureList);
                    recyclerViewBody.setAdapter(adapterBody);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }





        }
    }




    public class GetMeasurmentTask extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=getMeasurementAdjustmentInfo&userID="+userid);
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
            } finally {
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

              if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                  //  boolean error = jsonObject.getBoolean("Error");

                    Double Sleeve_Adjustment = jsonObject.getDouble("Sleeve_Adjustment");
                    Double Length_Adjustment = jsonObject.getDouble("Length_Adjustment");
                    Double Shoulder_Adjustment = jsonObject.getDouble("Shoulder_Adjustment");
                    Double Neck_Adjustment = jsonObject.getDouble("Neck_Adjustment");
                    Double Chest_Adjustment = jsonObject.getDouble("Chest_Adjustment");
                    Double Hips_Adjustment = jsonObject.getDouble("Hips_Adjustment");

                    Double Cuff_Adjustment = jsonObject.getDouble("Cuff_Adjustment");
                    Double Waist_Adjustment = jsonObject.getDouble("Waist_Adjustment");
                    Double Biceps_Adjustment = jsonObject.getDouble("Biceps_Adjustment");
                    int pantsWaist = jsonObject.getInt("pantsWaist");

                    int modelNumber = jsonObject.getInt("modelNo");
                    int userid = jsonObject.getInt("UserId");

                    SharedPreferences.Editor editor = getSharedPreferences("modelno", MODE_PRIVATE).edit();
                    editor.putInt("modelNo", modelNumber);
                    editor.putInt("pantswaistsize",pantsWaist);
                    editor.apply();

                    tvModelNumber.setText("MODEL NUMBER : "+modelNumber);
                    tvPantsWaist.setText("PANTS WAIST SIZE : "+pantsWaist);
                    if (Shoulder_Adjustment == 0.0) {

                        bespokeItems.add(new BespokeItem("SHOULDER", R.drawable.hook));


                    } else if (Shoulder_Adjustment == -1.0) {

                        bespokeItems.add(new BespokeItem("SHOULDER", R.drawable.red));

                    } else if (Shoulder_Adjustment == -2.0) {

                    bespokeItems.add(new BespokeItem("SHOULDER", R.drawable.yellow
                        ));

                    }


                    if (Sleeve_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.length_is_good));

                    }else if (Sleeve_Adjustment == 1.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.inch1));

                    }else if (Sleeve_Adjustment == 2.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.inch2));

                    }else if (Sleeve_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.red_line));

                    }else if (Sleeve_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.yellow_line));

                    }else if (Sleeve_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.green_line));

                    }else if (Sleeve_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("SLEEVE", R.drawable.blue_line));

                    }



                    if (Length_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.length_is_good));

                    }else if (Length_Adjustment == 1.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.inch1));

                    }else if (Length_Adjustment == 2.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.inch2));

                    }else if (Length_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.red_line));

                    }else if (Length_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.yellow_line));

                    }else if (Length_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.green_line));

                    }else if (Length_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("LENGTH", R.drawable.blue_line));

                    }


                    if (Neck_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck1));


                    }else if (Neck_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck2));


                    }else if (Neck_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck3));


                    }else if (Neck_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck4));


                    }else if (Neck_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck5));


                    }else if (Neck_Adjustment == -5.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck6));


                    }else if (Neck_Adjustment == -6.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck7));


                    }else if (Neck_Adjustment == -7.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck8));


                    }else if (Neck_Adjustment == -8.0){

                        bespokeItems.add(new BespokeItem("NECK", R.drawable.neck9));


                    }


                    if (Chest_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("CHEST", R.drawable.hook));

                    }else if (Chest_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("CHEST", R.drawable.red));

                    }else if (Chest_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("CHEST", R.drawable.yellow));

                    }else if (Chest_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("CHEST", R.drawable.green));

                    }else if (Chest_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("CHEST", R.drawable.blue));

                    }


                    if(Hips_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("HIPS", R.drawable.hook));

                    }else if (Chest_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("HIPS", R.drawable.red));

                    }else if (Chest_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("HIPS", R.drawable.yellow));

                    }else if (Chest_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("HIPS", R.drawable.green));

                    }else if (Chest_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("HIPS", R.drawable.blue));

                    }

                    if (Cuff_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff1));

                    }else if (Cuff_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff2));

                    }else if (Cuff_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff3));

                    }else if (Cuff_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff4));

                    }else if (Cuff_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff5));

                    }else if (Cuff_Adjustment == -5.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff6));

                    }else if (Cuff_Adjustment == -6.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff7));

                    }else if (Cuff_Adjustment == -7.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff8));

                    }else if (Cuff_Adjustment == -8.0){

                        bespokeItems.add(new BespokeItem("CUFF", R.drawable.cuff9));

                    }


                    if (Waist_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("WAIST", R.drawable.hook));

                    }else if (Waist_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("WAIST", R.drawable.red));

                    }else if (Waist_Adjustment == -2.0){

                        bespokeItems.add(new BespokeItem("WAIST", R.drawable.yellow));

                    }else if (Waist_Adjustment == -3.0){

                        bespokeItems.add(new BespokeItem("WAIST", R.drawable.green));

                    }else if (Waist_Adjustment == -4.0){

                        bespokeItems.add(new BespokeItem("WAIST", R.drawable.blue));

                    }



                    if (Biceps_Adjustment == 0.0){

                        bespokeItems.add(new BespokeItem("BICEPS", R.drawable.hook));

                    }else if (Biceps_Adjustment == -0.5){

                        bespokeItems.add(new BespokeItem("BICEPS", R.drawable.red));

                    }else if (Biceps_Adjustment == -1.0){

                        bespokeItems.add(new BespokeItem("BICEPS", R.drawable.yellow));

                    }else if (Biceps_Adjustment == -1.5){

                        bespokeItems.add(new BespokeItem("BICEPS", R.drawable.green));

                    }



//Biceps_Adjustment



                    adapter = new BespokeProfileAdapter(getApplicationContext(), bespokeItems);
                    recyclerView.setAdapter(adapter);


                    new FetchBodyPostureTask().execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(BespokinoProfile.this, "Internal server error", Toast.LENGTH_SHORT).show();

            }

        }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bespoke_profile, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_profile);

        //View actionView = MenuItemCompat.getActionView(menuItem);
      //  textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

       // setupBadge();

//        actionView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onOptionsItemSelected(menuItem);
//            }
//        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_profile:
                Toast.makeText(this, "Update bespoke profile", Toast.LENGTH_SHORT).show();
               // Intent intent = new Intent(BespokinoProfile.this,CartActivity.class);
               // startActivity(intent);
                showDailogList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showDailogList(){

        List<String> mAnimals = new ArrayList<String>();
        mAnimals.add("Body posture");
        mAnimals.add("Bespoke measurment");

        //Create sequence of items
        final CharSequence[] Animals = mAnimals.toArray(new String[mAnimals.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("UPDATE YOUR BESPOKE PROFILE");
        dialogBuilder.setItems(Animals, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = Animals[item].toString();  //Selected item in listview

                if (selectedText == "Body posture"){
                    startActivity(new Intent(BespokinoProfile.this,BodyPostureActivity.class));
                }else if (selectedText == "Bespoke measurment"){
                    startActivity(new Intent(BespokinoProfile.this,GetMeasurmentActivity.class));
                }
//                else if(selectedText == "Model number"){
//                    startActivity(new Intent(BespokinoProfile.this,MeasuringToolActivity.class));
//                }else if (selectedText == "Pants waist size"){
//                    startActivity(new Intent(BespokinoProfile.this,MeasuringToolActivity.class));
//                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel",null);
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();

    }




}

