package com.app.bespokino.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.fragment.ThreadColorDialogFragment;
import com.app.bespokino.fragment.TwentyPaymentFragment;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.SQLiteHandler;

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

public class MeasuringToolActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   // Button toolPresentButton,sendMeToolPresent;
    ImageButton slimButton,casualButton;
    TextView casualModeltextView,slimModeltextView,textCartItemCount;

    LinearLayout layoutCasual,layoutSlim;
    String paintWaistSize = null;
    String slim = "";
    String casual = "";

    int uID,modelNo;
    Bundle bundle = new Bundle();
    ImageView ticker,tick;

    private SQLiteHandler sqLiteHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_tool);


        sqLiteHandler = new SQLiteHandler(this);
        //  uID = getIntent().getExtras().getInt("userID");
        tick = (ImageView) findViewById(R.id.tick);
        ticker = (ImageView) findViewById(R.id.ticker);

        tick.setVisibility(View.INVISIBLE);
        ticker.setVisibility(View.INVISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences("modelno", 0);
        paintWaistSize = String.valueOf(prefs.getInt("pantswaistsize", 0));

        Cursor res =  sqLiteHandler.getAllData();

        while (res.moveToNext()){

            uID = res.getInt(4);

        }

        final FragmentManager fm = getSupportFragmentManager();
        final TwentyPaymentFragment t = new TwentyPaymentFragment();

        //toolPresentButton = (Button)findViewById(R.id.btToolPresent);
        //sendMeToolPresent = (Button)findViewById(R.id.btSendMetool);

        casualModeltextView = (TextView)findViewById(R.id.casualModelNumber);
        slimModeltextView = (TextView)findViewById(R.id.slimModelNumber);

        slimButton = (ImageButton)findViewById(R.id.imgSlim);
        casualButton = (ImageButton)findViewById(R.id.imgCasual);

        layoutCasual = (LinearLayout)findViewById(R.id.layoutCasual);

        layoutSlim = (LinearLayout)findViewById(R.id.layoutSlim);

        layoutCasual.setBackgroundResource(R.color.lighterGray);
        layoutSlim.setBackgroundResource(R.color.lighterGray);
        Spinner spinnerHeight = (Spinner) findViewById(R.id.spinner1);

        spinnerHeight.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.pant_waist, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHeight.setAdapter(adapter);
        spinnerHeight.setSelection(setCurrentItem(paintWaistSize));
       // setCurrentItem("31");



        slimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slim = "slim";
                casual = "";
                tick.setVisibility(View.VISIBLE);
                ticker.setVisibility(View.INVISIBLE);
                layoutSlim.setBackgroundResource(R.color.darkerGray);
                layoutCasual.setBackgroundResource(R.color.lighterGray);


                if (modelNo != 0) {

                    SharedPreferences.Editor editor = getSharedPreferences("modelno", MODE_PRIVATE).edit();
                    editor.putInt("modelNo", modelNo-1);
                    editor.apply();

                    Intent in = new Intent(MeasuringToolActivity.this, MeasuringToolStatus.class);
                    startActivity(in);
                }else{
                    Toast.makeText(MeasuringToolActivity.this,"Please.., Select your pant waist size",Toast.LENGTH_SHORT).show();

                }

            }
        });

        casualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                slim = "";
                casual = "casual";
                tick.setVisibility(View.INVISIBLE);
                ticker.setVisibility(View.VISIBLE);
                layoutCasual.setBackgroundResource(R.color.darkerGray);
                layoutSlim.setBackgroundResource(R.color.lighterGray);


                if (modelNo != 0) {

                    SharedPreferences.Editor editor = getSharedPreferences("modelno", MODE_PRIVATE).edit();
                    editor.putInt("modelNo", modelNo);
                    editor.apply();

                    Intent in = new Intent(MeasuringToolActivity.this, MeasuringToolStatus.class);
                    startActivity(in);
                }else {
                    Toast.makeText(MeasuringToolActivity.this,"Please.., Select your pant waist size",Toast.LENGTH_SHORT).show();

                }
            }
        });



    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("UID", uID);
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
                Intent intent = new Intent(MeasuringToolActivity.this,CartActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item = parent.getItemAtPosition(position).toString();
        if (!item.equalsIgnoreCase("SELECT YOUR PAINT WAIST SAIZE")) {

              paintWaistSize = parent.getItemAtPosition(position).toString();
           //  Toast.makeText(getApplicationContext(), paintWaistSize, Toast.LENGTH_SHORT).show();
            new PostBodyPostureTask().execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class PostBodyPostureTask extends AsyncTask<Void,Void,String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        @Override
        protected String doInBackground(Void... voids) {

            try {

                JSONObject bodyPostures = new JSONObject();
                bodyPostures.put("abdomen",AppConfig.abdoman);
                bodyPostures.put("chest",AppConfig.chest);
                bodyPostures.put("pelvis",AppConfig.pelvis);
                bodyPostures.put("shoulders",AppConfig.shoulders);
                bodyPostures.put("posture",AppConfig.postures);
                bodyPostures.put("userID",uID);
                bodyPostures.put("pantsWaist",paintWaistSize);

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

                        AppConfig.modelNo = jsonObject.getInt("modelNo");
                        modelNo = jsonObject.getInt("modelNo");
                        casualModeltextView.setText("BESPOKINO SHIRT SIZE : "+ modelNo);
                        slimModeltextView.setText("BESPOKINO SHIRT SIZE : "+ (modelNo - 1));
                       // Toast.makeText(MeasuringToolActivity.this,AppConfig.modelNo,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MeasuringToolActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {
                Toast.makeText(MeasuringToolActivity.this,"Internal server error",Toast.LENGTH_SHORT).show();

            }




        }
    }

    public int setCurrentItem(String storedPantsSize){

        int position = 0;
        String listItem[] = getResources().getStringArray(R.array.pant_waist);

        for (int i = 0; i<listItem.length;i++){

            if (listItem[i].equals(storedPantsSize)){

               // Toast.makeText(MeasuringToolActivity.this,listItem[i],Toast.LENGTH_SHORT).show();

                position = i;
            }


        }



        return position;
    }

}
