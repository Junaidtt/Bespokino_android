package com.app.bespokino.activity;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.BodyPostureAdapter;
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

public class BodyPostureActivity extends AppCompatActivity {

    int preSelectedIndex = -1;
    int preSelectedShoulderIndex = -1;
    int preSelectedChestIndex = -1;
    TextView textCartItemCount;
    int preSelectedAbdomenIndex = -1;
    int preSelectedPelvisIndex = -1;

    int uID;
    int modelNumber;

    String abdoman=null,chest = null,pelvis = null,shoulders = null,postures = null;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_posture);

         uID = AppConfig.userID;
           //  modelNumber = AppConfig.modelNo;

         //uID = getIntent().getExtras().getInt("userID");

         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setHomeButtonEnabled(true);
         getSupportActionBar().setDisplayShowTitleEnabled(false);
         Button nextButton = (Button)findViewById(R.id.nextButton);

        GridView gridView = (GridView)findViewById(R.id.postureGrid);
        GridView gridViewShoulder = (GridView)findViewById(R.id.shoulderGrid);
        GridView gridviewChest = (GridView)findViewById(R.id.gridChest);
        GridView gridviewAdbomen = (GridView)findViewById(R.id.gridAbdoman);
        GridView gridviewPelvis = (GridView)findViewById(R.id.gridPelvis);



        final List<ItemModel> item = new ArrayList<>();
        item.add(new ItemModel(false,"NORMAL",R.drawable.posture_normal,"posture-NORMAL"));
        item.add(new ItemModel(false,"LEANING",R.drawable.posture_leaning,"posture-LEANING"));
        item.add(new ItemModel(false,"ERACT",R.drawable.posture_erect,"posture-ERACT"));

        final List<ItemModel> itemShoulder = new ArrayList<>();
        itemShoulder.add(new ItemModel(false,"STEEP",R.drawable.shoulders_steap,"shoulders-STEAP"));
        itemShoulder.add(new ItemModel(false,"NORMAL",R.drawable.shoulders_normal,"shoulders-NORMAL"));
        itemShoulder.add(new ItemModel(false,"FLAT",R.drawable.shoulders_flat,"shoulders-FLAT"));

        final List<ItemModel> itemChest = new ArrayList<>();
        itemChest.add(new ItemModel(false,"THIN",R.drawable.chest_thin,"chest-THIN"));
        itemChest.add(new ItemModel(false,"FIT",R.drawable.chest_fit,"chest-FIT"));
        itemChest.add(new ItemModel(false,"NORMAL",R.drawable.chest_normal,"chest-NORMAL"));
        itemChest.add(new ItemModel(false,"MUSCULAR",R.drawable.chest_muscular,"chest-MUSCULAR"));
        itemChest.add(new ItemModel(false,"LARGE",R.drawable.chest_large,"chest-LARGE"));


        final List<ItemModel> itemAbdomen = new ArrayList<>();
        itemAbdomen.add(new ItemModel(false,"THIN",R.drawable.abdomen_thin,"abdomen-THIN"));
        itemAbdomen.add(new ItemModel(false,"NORMAL",R.drawable.abdomen_normal,"abdomen-NORMAL"));
        itemAbdomen.add(new ItemModel(false,"MEDIUM",R.drawable.abdomen_medium,"abdomen-MEDIUM"));
        itemAbdomen.add(new ItemModel(false,"LARGE",R.drawable.abdomen_large,"abdomen-LARGE"));

        final List<ItemModel> itemPelvis= new ArrayList<>();
        itemPelvis.add(new ItemModel(false,"THIN",R.drawable.pelvis_thin,"pelvis-THIN"));
        itemPelvis.add(new ItemModel(false,"NORMAL",R.drawable.pelvis_normal,"pelvis-NORMAL"));
        itemPelvis.add(new ItemModel(false,"CURVED",R.drawable.pelvis_curved,"pelvis-CURVED"));
        itemPelvis.add(new ItemModel(false,"LARGE",R.drawable.pelvis_large,"pelvis-LARGE"));

        final BodyPostureAdapter adapter = new BodyPostureAdapter(this, item);
        gridView.setAdapter(adapter);

        final BodyPostureAdapter adapterShoulder = new BodyPostureAdapter(this, itemShoulder);
        gridViewShoulder.setAdapter(adapterShoulder);

        final BodyPostureAdapter adapterChest = new BodyPostureAdapter(this, itemChest);
        gridviewChest.setAdapter(adapterChest);

        final BodyPostureAdapter adapterAbdomen = new BodyPostureAdapter(this, itemAbdomen);
        gridviewAdbomen.setAdapter(adapterAbdomen);

        final BodyPostureAdapter adapterPelvis = new BodyPostureAdapter(this, itemPelvis);
        gridviewPelvis.setAdapter(adapterPelvis);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if (abdoman == null||chest == null||pelvis == null||shoulders == null||postures == null){

                   Toast.makeText(BodyPostureActivity.this,"Make sure all options are selected:",Toast.LENGTH_LONG).show();

               }else {
                   Toast.makeText(BodyPostureActivity.this,"Select your style",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(BodyPostureActivity.this,MeasuringToolActivity.class);
//                   intent.putExtra("userID",uID);
//                   intent.putExtra("modelNo",modelNumber);
                   startActivity(intent);
               }

            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  ItemModel  model = item.get(position); //changed it to model because viewers will confused about it
                  model.setSelected(true);

                    postures = model.getItemCode();
                AppConfig.postures = model.getItemCode();
                  item.set(position, model);
                  if (preSelectedIndex > -1){
                      ItemModel preRecord = item.get(preSelectedIndex);
                      preRecord.setSelected(false);
                      item.set(preSelectedIndex, preRecord);
                  }
                preSelectedIndex = position;
                adapter.updateRecords(item);


            }
        });

        gridViewShoulder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel  model = itemShoulder.get(position); //changed it to model because viewers will confused about it
                model.setSelected(true);

                shoulders = model.getItemCode();

                AppConfig.shoulders = model.getItemCode();
                itemShoulder.set(position, model);
                if (preSelectedShoulderIndex > -1){
                    ItemModel preRecord = itemShoulder.get(preSelectedShoulderIndex);
                    preRecord.setSelected(false);
                    itemShoulder.set(preSelectedShoulderIndex, preRecord);
                }
                preSelectedShoulderIndex = position;
                adapterShoulder.updateRecords(itemShoulder);


            }
        });

        gridviewChest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel  model = itemChest.get(position); //changed it to model because viewers will confused about it
                model.setSelected(true);

                chest = model.getItemCode();
                AppConfig.chest = model.getItemCode();

                itemChest.set(position, model);
                if (preSelectedChestIndex > -1){
                    ItemModel preRecord = itemChest.get(preSelectedChestIndex);
                    preRecord.setSelected(false);
                    itemChest.set(preSelectedChestIndex, preRecord);
                }
                preSelectedChestIndex = position;
                adapterChest.updateRecords(itemChest);


            }
        });


        gridviewAdbomen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel  model = itemAbdomen.get(position); //changed it to model because viewers will confused about it
                model.setSelected(true);

                abdoman = model.getItemCode();
                AppConfig.abdoman = model.getItemCode();

                itemAbdomen.set(position, model);
                if (preSelectedAbdomenIndex > -1){
                    ItemModel preRecord = itemAbdomen.get(preSelectedAbdomenIndex);
                    preRecord.setSelected(false);
                    itemAbdomen.set(preSelectedAbdomenIndex, preRecord);
                }
                preSelectedAbdomenIndex = position;
                adapterAbdomen.updateRecords(itemAbdomen);


            }
        });

        gridviewPelvis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemModel  model = itemPelvis.get(position); //changed it to model because viewers will confused about it
                model.setSelected(true);

                pelvis = model.getItemCode();
                AppConfig.pelvis = model.getItemCode();

                itemPelvis.set(position, model);
                if (preSelectedPelvisIndex > -1){
                    ItemModel preRecord = itemPelvis.get(preSelectedPelvisIndex);
                    preRecord.setSelected(false);
                    itemPelvis.set(preSelectedPelvisIndex, preRecord);
                }
                preSelectedPelvisIndex = position;
                adapterPelvis.updateRecords(itemPelvis);


            }
        });



    }

    public class PostBodyPostureTask extends AsyncTask<Void,Void,String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        @Override
        protected String doInBackground(Void... voids) {

            try {

                JSONObject bodyPostures = new JSONObject();
                bodyPostures.put("abdomen",abdoman);
                bodyPostures.put("chest",chest);
                bodyPostures.put("pelvis",pelvis);
                bodyPostures.put("shoulders",shoulders);
                bodyPostures.put("posture",postures);
                bodyPostures.put("userID",uID);
                bodyPostures.put("pantsWaist","34");



                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=updateBodyPostures");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(bodyPostures));
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return JsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null){


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean error = jsonObject.getBoolean("Error");
                    if (!error){
                        Toast.makeText(BodyPostureActivity.this,"Select your style",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BodyPostureActivity.this,MeasuringToolActivity.class);
                        intent.putExtra("userID",uID);
                        intent.putExtra("modelNo",modelNumber);
                        startActivity(intent);
                    }else {
                        Toast.makeText(BodyPostureActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {
                Toast.makeText(BodyPostureActivity.this,"Internal server error",Toast.LENGTH_SHORT).show();

            }




        }
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
                Intent intent = new Intent(BodyPostureActivity.this,CartActivity.class);
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
