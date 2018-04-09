package com.app.bespokino.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.CartActivity;
import com.app.bespokino.activity.CollarActivity;
import com.app.bespokino.activity.MainActivity;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.model.Shirt;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


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

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.app.bespokino.R.id.imageView;
import static com.app.bespokino.R.id.progressBar;

/**
 * Created by bespokino on 7/31/2017 AD.
 */

public class ShirtDisplyDialogFragment extends DialogFragment {
    ImageView shirtImage;
    String shirtURL;
    String fabicType;
    int fabricID,addupPrice;
    TextView fabricType;
    CustomerHelperDB custDB;
    ImageView closeButton;
    public MainActivity activity;

    ProgressBar progressBar;

    public static final String MyPREFERENCES = "OrderDetails" ;

    PhotoViewAttacher photoViewAttacher ;

    SharedPreferences sharedpreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shirt_display_fragment, container, false);

        //initialize customer database
        custDB = new CustomerHelperDB(getActivity());
        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        progressBar = rootView.findViewById(R.id.progressBar);
        TextView priceTxt = rootView.findViewById(R.id.tvPrice);

        shirtURL = getArguments().getString("url");//shirtImg
        fabicType = getArguments().getString("type");
        fabricID = getArguments().getInt("fabId");
        addupPrice = getArguments().getInt("addup");

        if (addupPrice != 0){

            priceTxt.setText("+ $"+addupPrice);

        }else{

            priceTxt.setText(" $"+"79");

        }

        shirtImage =(ImageView) rootView.findViewById(imageView);
        fabricType = (TextView) rootView.findViewById(R.id.tvType);
        closeButton = (ImageView) rootView.findViewById(R.id.closeButton);
        if (fabicType.equals("shirt")){

            fabricType.setText("");

        }else
        {
            fabricType.setText(fabicType);

        }
        addPlus();

        Button save = (Button) rootView.findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new GetCustomerDetailsTask().execute();

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();
            }
        });


        FrameLayout.LayoutParams myImageLayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        myImageLayout.gravity= Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        shirtImage.setLayoutParams(myImageLayout);

        Glide.with(getActivity()).load(shirtURL).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;

            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(shirtImage);

        photoViewAttacher = new PhotoViewAttacher(shirtImage);

        photoViewAttacher.update();
        return rootView;
    }

    private void addPlus() {

        Cursor cursor = custDB.getAllData();
        if (cursor.getCount()!=0){
            int k = cursor.getCount();
        Log.d("CARTCOUNT", String.valueOf(k));
            new AddonShirtTask().execute();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    public class GetCustomerDetailsTask extends AsyncTask<Object, Object, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;
        @Override
        protected String doInBackground(Object... voids) {


            try {

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=insertOrderFabricInfo");


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(getCustomerDataInDB()));
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
                Log.i("Customer Details", JsonResponse);

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

            try {
                JSONObject jsonobject = new JSONObject(s);

                boolean error = jsonobject.getBoolean("Error");
                if (!error){
                    String trackingID = jsonobject.getString("trackingID");
                    int orderNo = jsonobject.getInt("orderNo");
                    int customerID = jsonobject.getInt("customerID");
                    int paperNo = jsonobject.getInt("paperNo");

                     Cursor res = custDB.getAllData();
                    boolean isInserted=false;
                    if(res.getCount() == 0){

                        isInserted =  custDB.insertData(trackingID,orderNo,customerID,paperNo);
                    }
                    if(isInserted){

                        Log.d("user_values_inserted :",s);

                    }else{

                        Log.d("Insertion failed :",s);

                    }

                    try
                    {
                        Intent intent = new Intent(activity, CollarActivity.class);
                        intent.putExtra("selectedFabric",shirtURL);
                        intent.putExtra("fabricType",fabicType);
                        startActivity(intent);
                        getDialog().dismiss();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                Log.d("response :",s);



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    public JSONObject getCustomerDataInDB() {

        JSONObject jsonObject = new JSONObject();
        String trackingNo=null;
        String orderNo= null;
        String customerNo = null;
        String paperNo = null;

        Cursor res =  custDB.getAllData();

        if (res.getCount() == 0){

            try {
                jsonObject.put("orderNo",0);
                jsonObject.put("customerID",0);
                jsonObject.put("paperNo",0);
                jsonObject.put("trackingID",0);
                jsonObject.put("fabricID",fabricID);
            } catch (JSONException e) {

                e.printStackTrace();

            }


        }else{
            while (res.moveToNext()){

                trackingNo = res.getString(1);
                orderNo = res.getString(2);
                customerNo = res.getString(3);
                paperNo = res.getString(4);


            }
            try {
                jsonObject.put("orderNo",orderNo);
                jsonObject.put("customerID",customerNo);
                jsonObject.put("paperNo",paperNo);
                jsonObject.put("trackingID",trackingNo);
                jsonObject.put("fabricID",fabricID);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return jsonObject;
    }

    public class AddonShirtTask extends AsyncTask<Void,Void,String>{
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        @Override
        protected String doInBackground(Void... voids) {
            String trackingNo=null;
            String orderNo= null;
            String customerNo = null;
            String paperNo = null;
            //{"orderNo":"9120239","customerID":"1949","paperNo":"50460","trackingID":"9120239 - 1949 - 1","phoneNo":"919995261220"}
            JSONObject addNextJSON = new JSONObject();
            try {

                Cursor res =  custDB.getAllData();

                if (res.getCount() != 0){
                    while (res.moveToNext()){

                        trackingNo = res.getString(1);
                        orderNo = res.getString(2);
                        customerNo = res.getString(3);
                        paperNo = res.getString(4);


                    }
                }

                addNextJSON.put("orderNo",orderNo);
                addNextJSON.put("customerID",customerNo);
                addNextJSON.put("paperNo",paperNo);
                addNextJSON.put("trackingID",trackingNo);
                addNextJSON.put("phoneNo","");


                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=addNewOrderItem");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(addNextJSON));
                // json data
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {

                    //Nothing to do.
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


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return JsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //{"orderNo":"9120239","customerID":"1949","paperNo":"50460","trackingID":"9120239 - 1949 - 1","phoneNo":"919995261220"}
            try {
                JSONObject jObj = new JSONObject(s);

                boolean error = jObj.getBoolean("Error");
                if (!error) {

                    String trakingID = jObj.getString("trackingID");
                    int orderNo = jObj.getInt("orderNo");
                    int customerID = jObj.getInt("customerID");
                    int paperNo = jObj.getInt("paperNo");

                    updateCustomerID("1", trakingID, orderNo, customerID, paperNo);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    public void updateCustomerID(String s, String trakingID, int orderNo, int customerID, int paperNo){


        boolean isUpdate = custDB.updateData(s,trakingID,orderNo,customerID,paperNo);

        if(isUpdate == true){
            Log.d("Data Updation","success");
         //   Toast.makeText(getActivity(),"Data Updated",Toast.LENGTH_SHORT).show();
        }else
            Log.d("Data Updation","Failed");
           //Toast.makeText(getActivity(),"Updation failed",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (MainActivity) activity;
    }


    /* @Override
    public void onAttach(Activity activity){
        this.activity = activity;
    }*/
}
