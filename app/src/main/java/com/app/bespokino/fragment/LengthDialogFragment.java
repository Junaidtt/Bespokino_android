package com.app.bespokino.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.GetMeasurmentActivity;
import com.app.bespokino.activity.InvoiceActivity;
import com.app.bespokino.activity.LoginActivity;
import com.app.bespokino.activity.SelfMeasuringToolActivity;
import com.app.bespokino.adapter.LengthRVAdapter;
import com.app.bespokino.adapter.MeasuringAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.model.MeasuringToolTableValue;
import com.app.bespokino.model.ToolCode;

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
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LengthDialogFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    List<ToolCode> toolCodeList;
    Button cancelButton,saveButton;
    String modelValue;
    double codeValue;
    int userid;
    SharedPreferences.Editor editor;

    private SQLiteHandler sqLiteHandler;


    public LengthDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.measurment_pager_fragment, container, false);

        sqLiteHandler = new SQLiteHandler(getActivity());


        Cursor res =  sqLiteHandler.getAllData();

        while (res.moveToNext()){
            userid = res.getInt(4);

        }



        ImageView shirtImage = (ImageView)view.findViewById(R.id.measurmentImage);
        shirtImage.setImageResource(R.drawable.length_guide);
        TextView titleTv = (TextView)view.findViewById(R.id.title);
        titleTv.setText("LENGTH");

        SharedPreferences prefs = getActivity().getSharedPreferences("modelno", 0);
        modelValue = String.valueOf(prefs.getInt("modelNo", 0));


        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LengthRVAdapter(getActivity(),getToolColorCode());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        ToolCode toolCode = toolCodeList.get(position);
                        codeValue = toolCode.getImageValue();
                        double sleeveMaster = setMasterValues(modelValue);
                        AppConfig.customerlengthValue = sleeveMaster + codeValue;
                        AppConfig.lengthValue =  "1";
                       // ((GetMeasurmentActivity) getActivity()).setCurrentItem(9, true);

                                 if (AppConfig.NeckValue=="1" && AppConfig.BiceptsValue == "1"&& AppConfig.CuffValue == "1"
                      &&AppConfig.Sleeve == "1" && AppConfig.lengthValue == "1" && AppConfig.ShoulderValue == "1"
                      &&AppConfig.ChestValue == "1"&&AppConfig.WaistValue == "1"&& AppConfig.HipsValue == "1"){

                         new SelfMeasuringValuesPostTask().execute();

              }else {
                  showMessage();
              }




                    }
                })
        );



        return  view;
    }



    public List getToolColorCode(){
        toolCodeList = new ArrayList<>();

        toolCodeList.add(new ToolCode(R.drawable.green_line,-3));
        toolCodeList.add(new ToolCode(R.drawable.yellow_line,-2));
        toolCodeList.add(new ToolCode(R.drawable.red_line,-1));
        toolCodeList.add(new ToolCode(R.drawable.length_is_good,0));
        toolCodeList.add(new ToolCode(R.drawable.inch_1,2));
        toolCodeList.add(new ToolCode(R.drawable.inch_2,1));




        mRecyclerView.setAdapter(mAdapter);
        return toolCodeList;

    }


    public double setMasterValues(String model){
        double sleeve = 0.0;
        List<MeasuringToolTableValue> measuringToolValueList = new ArrayList<>();
        measuringToolValueList.add(new MeasuringToolTableValue("1",18,24,32,29,38,27,12,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("2",18,24,34,31,39,27,13,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("3",19,25,36,33,40,30,13.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("4",19.5,25.5,38,35,41,30,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("5",19.5,25.5,40,37,42,30,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("6",20,25.5,42,39,43,30,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("7",20,25.5,44,41,44,31,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("8",21,26,46,43,45,31,16,10,45));

        measuringToolValueList.add(new MeasuringToolTableValue("9",21,26,48,46,47,31,16,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("10",22,26,50,47,49,32,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("11",22,26,52,50,51,32,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("12",23,26,54,52,52,33,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("13",23,26,56,54,54,33,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("14",24,26,60,58,58,33,17,11,50));

        measuringToolValueList.add(new MeasuringToolTableValue("1L",18,24,32,29,38,30,12,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("2L",18,24,34,31,39,30,13,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("3L",19,25,36,33,40,33,13.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("4L",19.5,25.5,38,35,41,33,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("5L",19.5,25.5,40,37,42,33,14,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("6L",20,25.5,42,39,43,33,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("7L",20,25.5,44,41,44,34,15.5,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("8L",21,26,46,43,45,34,16,10,45));
        measuringToolValueList.add(new MeasuringToolTableValue("9L",21,26,48,46,47,34,16,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("10L",22,26,50,47,49,35,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("11L",22,26,52,50,51,35,16.5,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("12L",23,26,54,52,52,36,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("13L",23,26,56,54,54,36,17,11,50));
        measuringToolValueList.add(new MeasuringToolTableValue("14L",24,26,60,58,58,36,17,11,50));


        for (int i=0; i<measuringToolValueList.size();i++){

            if (measuringToolValueList.get(i).getModelNo().equals(model)){

                sleeve = measuringToolValueList.get(i).getLengthMaster();

            }

        }

        return sleeve;
    }



    public class SelfMeasuringValuesPostTask extends AsyncTask<Void,Void,String> {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        SharedPreferences prefs = getContext().getSharedPreferences("modelno", 0);
        int modelNo = prefs.getInt("modelNo",0);



        @Override
        protected String doInBackground(Void... voids) {

            try {



                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userID",userid);
                jsonObject.put("modelNo",modelNo);
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

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    boolean error = jsonObject.getBoolean("Error");
                    if (!error){

                        editor = getActivity().getSharedPreferences("MyPref", 0).edit();
                        editor.putBoolean("measurment", true);
                        editor.apply();
                        if (AppConfig.mCartItemCount != 0){
                            Intent intent = new Intent(getActivity(),InvoiceActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(),"No item in cart",Toast.LENGTH_SHORT).show();
                        }
                       // String userID = jsonObject.getString("UserId");

                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }




        }
    }

    public void showMessage(){
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
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

}
