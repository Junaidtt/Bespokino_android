package com.app.bespokino.fragment;

import android.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.AdditionaloptionsActivity;
import com.app.bespokino.activity.CollarActivity;
import com.app.bespokino.activity.CuffActivity;
import com.app.bespokino.activity.MonogramActivity;
import com.app.bespokino.activity.SampleActivity;
import com.app.bespokino.adapter.MonocolorAdapter;
import com.app.bespokino.adapter.ThreadAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.ThreadModel;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sunilvg on 16/07/17.
 */

public class MonoColorDialogFragment extends DialogFragment implements View.OnClickListener {

    GridView gv;
    Button save;
    ImageButton imageButtonOne,imageButtonTwo,imageButtonThree;
    EditText edMonogram;
    int monoTextStyle=168;
    List monoThreadList;
    int mgPosition;
    int mgColor = 0;
    CustomerHelperDB customerHelperDB;
    String MONOGRAMTEXT;
    ProgressDialog pDialog;

    int preSelectedIndex = -1;

    int threadposition=0;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.mono_dialog,null);

        customerHelperDB = new CustomerHelperDB(getActivity());

        mgPosition = getArguments().getInt("mgPosition");
        Log.d("optionValue", String.valueOf(mgPosition));

        gv= (GridView) rootView.findViewById(R.id.gridview);
        monoThreadList = new ArrayList();
        addThread();

        save = (Button)rootView.findViewById(R.id.saveButton);
        edMonogram = (EditText)rootView.findViewById(R.id.edMonogram);
        imageButtonOne = (ImageButton)rootView.findViewById(R.id.imageButton1);
        imageButtonTwo = (ImageButton)rootView.findViewById(R.id.imageButton2);
        imageButtonThree = (ImageButton)rootView.findViewById(R.id.imageButton3);
        imageButtonOne.setOnClickListener(this);
        imageButtonTwo.setOnClickListener(this);
        imageButtonThree.setOnClickListener(this);
        imageButtonTwo.setBackgroundResource(R.drawable.shape);

        final ThreadAdapter adapter = new ThreadAdapter(getActivity(),monoThreadList);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ThreadModel threadModel = (ThreadModel) monoThreadList.get(position);
                threadModel.setSelected(true);

                monoThreadList.set(position, threadModel);

                mgColor = threadModel.getImgCode();
                threadposition = position;

                if (preSelectedIndex > -1){

                    ThreadModel preRecord = (ThreadModel) monoThreadList.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    monoThreadList.set(preSelectedIndex, preRecord);

                }
                preSelectedIndex = position;

                adapter.updateRecords(monoThreadList);





            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 MONOGRAMTEXT = edMonogram.getText().toString().trim();

                if(MONOGRAMTEXT.isEmpty()||monoTextStyle == 0||mgColor == 0){

                    showMessage("MONOGRAM","Check all fields are selected.");
                }
                else {

                    new PostMonogramTask().execute();

                }

            }
        });

        return rootView;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imageButton1:

                monoTextStyle = 167;
                imageButtonOne.setBackgroundResource(R.drawable.shape);
                imageButtonTwo.setBackgroundResource(0);
                imageButtonThree.setBackgroundResource(0);
               // Toast.makeText(getActivity(),"Simple",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton2:
                monoTextStyle = 168;
                imageButtonTwo.setBackgroundResource(R.drawable.shape);
                imageButtonOne.setBackgroundResource(0);
                imageButtonThree.setBackgroundResource(0);
               // Toast.makeText(getActivity(),"Script",Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageButton3:
                monoTextStyle = 169;
                imageButtonThree.setBackgroundResource(R.drawable.shape);
                imageButtonTwo.setBackgroundResource(0);
                imageButtonOne.setBackgroundResource(0);
                //Toast.makeText(getActivity(),"Fancy",Toast.LENGTH_SHORT).show();
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


    public void addThread(){

        monoThreadList.add(new ThreadModel(false,R.drawable.t6811,1));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1796,2));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6010,3));
        monoThreadList.add(new ThreadModel(false,R.drawable.t2480,4));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6614,5));


        monoThreadList.add(new ThreadModel(false,R.drawable.t1861,6));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6711,7));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6768,8));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6463,9));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6040,10));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6046,11));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1502,12));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6536,13));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6537,14));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1629,15));

        monoThreadList.add(new ThreadModel(false,R.drawable.t2331,16));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6603,17));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1614,18));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6800,19));
        monoThreadList.add(new ThreadModel(false,R.drawable.t2424,20));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6403,21));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1419,22));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6211,23));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6224,24));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6213,25));

        monoThreadList.add(new ThreadModel(false,R.drawable.t6211,26));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1193,27));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1700,28));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1532,29));
        monoThreadList.add(new ThreadModel(false,R.drawable.t1987,30));

        monoThreadList.add(new ThreadModel(false,R.drawable.t1853,31));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6543,32));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6132,33));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6237,34));
        monoThreadList.add(new ThreadModel(false,R.drawable.t6141,35));

        monoThreadList.add(new ThreadModel(false,R.drawable.t1717,36));

    }

    public class PostMonogramTask extends AsyncTask<String,Void,String>{


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Progress dialog
            pDialog = new ProgressDialog(getActivity(),R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);
            pDialog.setMessage("Saving your style ...");
            showDialog();
        }
        @Override
        protected String doInBackground(String... voids) {

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
                jsonObject.put("mgLine",MONOGRAMTEXT);
                jsonObject.put("mgColor",mgColor);
                jsonObject.put("mgPosition",mgPosition);
                jsonObject.put("mgStyle",monoTextStyle);

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=insertMonogramStylingInfo");

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

                    setThreadSelectedValue();
                    Intent i = new Intent(getActivity(), AdditionaloptionsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(i);

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

    public void showMessage(String title,String message){

        final AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();

    }

    public void setThreadSelectedValue(){


        final String MY_PREFS_NAME = "threadPref";
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("code", threadposition);
        editor.apply();
        editor.commit();

    }

}
