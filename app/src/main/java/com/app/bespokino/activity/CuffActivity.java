package com.app.bespokino.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.ItemAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.model.ItemModel;

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
import java.util.ArrayList;
import java.util.List;

public class CuffActivity extends AppCompatActivity {

    TextView textCartItemCount;
    int preSelectedIndex = -1;


    Bundle bundle;

    boolean exist = false;
    ItemModel model;
    CustomerHelperDB customerHelperDB;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuff);bundle = new Bundle();

        customerHelperDB = new CustomerHelperDB(this);

      //Toast.makeText(CuffActivity.this,PARCEL_COLLAR.getItemName(),Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        GridView gridView = (GridView)findViewById(R.id.gridview);
        //  gridView.setAdapter(new MyAdapter(this));

        final List<ItemModel>item = new ArrayList<>();

        item.add(new ItemModel(false,"1 BUTTON SQUARE",R.drawable.button1square,"CUF",200,43));
        item.add(new ItemModel(false,"1 BUTTON CURVED",R.drawable.button1curved,"CUF",201,43));
        item.add(new ItemModel(false,"1 BUTTON ANGLED",R.drawable.button1angled,"CUF",531,43));
        item.add(new ItemModel(false,"2 BUTTONS SQUARE",R.drawable.button2squared,"CUF",202,43));
        item.add(new ItemModel(false,"2 BUTTONS CURVED",R.drawable.buttons2curved,"CUF",532,43));
        item.add(new ItemModel(false,"2 BUTTONS ANGLE",R.drawable.buttons2angled,"CUF",203,43));
        item.add(new ItemModel(false,"FRENCH SQUARED",R.drawable.frenchsquared,"CUF",533,43));
        item.add(new ItemModel(false,"FRENCH CURVED",R.drawable.frenchcurved,"CUF",359,43));
        item.add(new ItemModel(false,"FRENCH ANGLED",R.drawable.frenchangled,"CUF",358,43));

        final ItemAdapter adapter = new ItemAdapter(this, item);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                model = item.get(position); //changed it to model because viewers will confused about it


                //new CuffInsertionTask().execute();


                model.setSelected(true);

                item.set(position, model);

                if (preSelectedIndex > -1){

                    ItemModel preRecord = item.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    item.set(preSelectedIndex, preRecord);

                }
                    preSelectedIndex = position;

                    adapter.updateRecords(item);

               if (model.getItemName() == "FRENCH SQUARED"){

                   showMessage();
               }else if(model.getItemName() == "FRENCH CURVED"){

                   showMessage();
               }else if (model.getItemName() == "FRENCH ANGLED"){
                  showMessage();

               }else {
                        new CuffInsertionTask().execute();
               }
            }
        });

    }



    public class CuffInsertionTask extends AsyncTask<Void,Void,String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;
        String ResponseData;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Progress dialog
            pDialog = new ProgressDialog(CuffActivity.this,R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);
            pDialog.setMessage("Saving your style ...");
            showDialog();
        }
        @Override
        protected String doInBackground(Void... voids) {

            String trackingNo=null;
            String orderNo= null;
            String customerNo = null;
            String paperNo = null;



            try {

                Cursor res = customerHelperDB.getAllData();
                while (res.moveToNext()){

                    trackingNo = res.getString(1);
                    orderNo = res.getString(2);
                    customerNo = res.getString(3);
                    paperNo = res.getString(4);



                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("orderNo",orderNo);
                jsonObject.put("customerID",customerNo);
                jsonObject.put("paperNo",paperNo);
                jsonObject.put("trackingID",trackingNo);
                jsonObject.put("optionValue",model.getOptionValude());

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=insertCuffStylingInfo");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(jsonObject));
                // json data
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                //response data
                // Log.i(TAG, JsonResponse);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }


                return JsonResponse;
            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (pDialog != null) {
                hideDialog();
            }
            if (s != null) {


            try {
                JSONObject jsonobject = new JSONObject(s);

                boolean error = jsonobject.getBoolean("Error");
                if (!error) {

                    Intent i = new Intent(CuffActivity.this, MonogramActivity.class);
                    startActivity(i);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void showMessage(){


        // custom dialog
        final Dialog dialog = new Dialog(CuffActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("FRENCH CUFF");
        //Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);
        // Your android custom dialog ok action
        // Action for custom dialog ok button click
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CuffInsertionTask().execute();
                dialog.dismiss();
            }
        });

        dialog.show();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_cart:
                Toast.makeText(this, "You have selected cart Menu", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CuffActivity.this,CartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public  void setupBadge() {

        if (textCartItemCount != null) {
            if (AppConfig.mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(AppConfig.mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
