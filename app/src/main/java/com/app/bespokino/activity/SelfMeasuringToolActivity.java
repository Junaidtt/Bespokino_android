package com.app.bespokino.activity;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.fragment.BicepsDialogFragment;
import com.app.bespokino.fragment.ChestDialogFragment;
import com.app.bespokino.fragment.CuffDialogFragment;
import com.app.bespokino.fragment.HipsDialogFragment;
import com.app.bespokino.fragment.LengthDialogFragment;
import com.app.bespokino.fragment.NeckDialogFragment;
import com.app.bespokino.fragment.ShoulderDialogFragment;
import com.app.bespokino.fragment.SleeveDialogFragment;
import com.app.bespokino.fragment.WaistDialogFragment;
import com.app.bespokino.helper.AppConfig;

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

public class SelfMeasuringToolActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    Button  continueButton;

    TextView textCartItemCount;

    private Spinner modelSpinner;
    int modelValue = 3;
    int UID;



    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_measuring_tool);

        bundle = new Bundle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        modelValue = AppConfig.modelNo;



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        modelSpinner = (Spinner) findViewById(R.id.spinner1);


        modelSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.model, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        int selectionPosition = adapter.getPosition("SIZE " + String.valueOf(modelValue));
        modelSpinner.setSelection(selectionPosition);


        continueButton = (Button) findViewById(R.id.nextButton);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent intent = new Intent(SelfMeasuringToolActivity.this, GetMeasurmentActivity.class);
                startActivity(intent);


            }
        });


    }
  /*  private void initAppConfig() {
        AppConfig.HipsValue = null;
        AppConfig.WaistValue = null;
        AppConfig.ChestValue = null;
        AppConfig.ShoulderValue = null;

        AppConfig.NeckValue = null;
        AppConfig.BiceptsValue = null;
        AppConfig.CuffValue =null;
        AppConfig.lengthValue = null;
        AppConfig.Sleeve = null;

    }*/


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String item=parent.getItemAtPosition(position).toString();
        if(!item.equalsIgnoreCase("HEIGHT")) {

            Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            String value = parent.getItemAtPosition(position).toString();

            switch (value){

                case "SIZE 1":
                    modelValue = Integer.parseInt("1");
                    break;
                case "SIZE 2":
                    modelValue = Integer.parseInt("2");
                    break;
                case "SIZE 3":
                    modelValue = Integer.parseInt("3");
                    break;
                case "SIZE 4":
                    modelValue = Integer.parseInt("4");
                    break;

                case "SIZE 5":
                    modelValue = Integer.parseInt("5");
                    break;
                case "SIZE 6":
                    modelValue = Integer.parseInt("6");
                    break;
                case "SIZE 7":
                    modelValue = Integer.parseInt("7");
                    break;
                case "SIZE 8":
                    modelValue = Integer.parseInt("8");
                    break;
                case "SIZE 9":
                    modelValue = Integer.parseInt("9");
                    break;
                case "SIZE 10":
                    modelValue = Integer.parseInt("10");
                    break;
                case "SIZE 11":
                    modelValue = Integer.parseInt("11");
                    break;
                case "SIZE 12":
                    modelValue = Integer.parseInt("12");
                    break;
                case "SIZE 13":
                    modelValue = Integer.parseInt("13");
                    break;
                case "SIZE 14":
                    modelValue = Integer.parseInt("14");
                    break;


            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class SelfMeasuringValuesPostTask extends AsyncTask<Void,Void,String>{


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;



        @Override
        protected String doInBackground(Void... voids) {

            try {
               /*{ "userID":"51004", "modelNo":"5", "Neck":"42", "Cuff":"8.5", "Biceps":"13",
                        "Sleeve":"24.5", "Length":"29", "Shoulder":"19", "Chest":"38", "Waist":"36",
                        "Hips":"40", "SleeveAddup":"0.5", "LengthAddup":"1"}*/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userID",UID);
                jsonObject.put("modelNo",modelValue);
                jsonObject.put("Neck",String.valueOf(AppConfig.customerNeckValue));
                jsonObject.put("Cuff",String.valueOf(AppConfig.customerCuffValue));
                jsonObject.put("Biceps",String.valueOf(AppConfig.customerBiceptsValue));
                jsonObject.put("Sleeve",String.valueOf(AppConfig.customerSleeve));
                jsonObject.put("Length",String.valueOf(AppConfig.customerlengthValue));

                jsonObject.put("Shoulder",String.valueOf(AppConfig.customerShoulderValue));
                jsonObject.put("Chest",String.valueOf(AppConfig.customerChestValue));
                jsonObject.put("Waist",String.valueOf(AppConfig.customerWaistValue));
                jsonObject.put("Hips",String.valueOf(AppConfig.CustomerHipsValue));

                jsonObject.put("SleeveAddup","0.5");
                jsonObject.put("LengthAddup","1");

                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=SaveCustomerMeasurements");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(jsonObject));
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
            if (s!=null){

            //{"modelNo":2.0,"Error":false,"UserId":51004.0}

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean error = jsonObject.getBoolean("Error");
                    if (!error){
                        String userID = jsonObject.getString("UserId");
                        Intent intent = new Intent(SelfMeasuringToolActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void showMessage(){
        // custom dialog
        final Dialog dialog = new Dialog(SelfMeasuringToolActivity.this);
        dialog.setContentView(R.layout.cust_dialog);
        // Custom Android Allert Dialog Title
        dialog.setTitle("BESPOKINO");
        TextView tv = (TextView) dialog.findViewById(R.id.tv);
        tv.setText("Make Sure all sections are measured.");
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Intent intent = new Intent(SelfMeasuringToolActivity.this,CartActivity.class);
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
