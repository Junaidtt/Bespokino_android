package com.app.bespokino.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.activity.AcceptcardPayment;
import com.app.bespokino.activity.CuffActivity;
import com.app.bespokino.activity.Pay20cardActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bespokino on 10/3/2017 AD.
 */

public class TwentyPaymentFragment extends DialogFragment {

    EditText fullName,lastName,address,city,state,zipcode,country,email,cellNumber;
    Button payTwenty;

    int userID;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.twenty_payment_fragment,null);


        userID = getArguments().getInt("uid");

        fullName = (EditText)rootView.findViewById(R.id.edFirstName);
        lastName = (EditText)rootView.findViewById(R.id.edLastName);
        address = (EditText)rootView.findViewById(R.id.edAddress);
        city = (EditText)rootView.findViewById(R.id.edCity);
        state = (EditText)rootView.findViewById(R.id.edState);
        zipcode = (EditText)rootView.findViewById(R.id.edZipcode);
        country = (EditText)rootView.findViewById(R.id.edCountry);
        email  =(EditText)rootView.findViewById(R.id.edEmail);
        cellNumber =(EditText) rootView.findViewById(R.id.edCellNumber);
        payTwenty =(Button)rootView.findViewById(R.id.payTwentyButton);

        new GetUserInfoTask().execute();
        payTwenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getFullname = fullName.getText().toString().trim();
                String getLastname = lastName.getText().toString().trim();
                String getAddress = address.getText().toString().trim();
                String getCity = city.getText().toString().trim();
                String getState = state.getText().toString().trim();
                String getZipcode = zipcode.getText().toString().trim();
                String getCountry = country.getText().toString().trim();
                String getEmail = email.getText().toString().trim();
                String getCellNumber = cellNumber.getText().toString().trim();

                if (!getFullname.isEmpty() && !getLastname.isEmpty() && !getAddress.isEmpty() && !getCity.isEmpty()&&!getState.isEmpty() && !getZipcode.isEmpty() && !getCountry.isEmpty() && !getEmail.isEmpty() && !getCellNumber.isEmpty()){

                    Intent intent = new Intent(getActivity(),Pay20cardActivity.class);


                  //  {"firstName":"Faizal","lastName":"Developer","email":"faizalm@gmail.com",
                           // "phoneNo":"1558054560","userId":"51002", "ccType":"Visa","ccNo":"94234634263493",  "ccExpMonth":"12","ccExpYear":"2019","ccCode":"458", "address":"2019 Chancellor Street", "city":"Philadelphia","state":"PA","zip":"19103","ccLastNo":"3493"}
                    intent.putExtra("firstName",getFullname);
                    intent.putExtra("lastName",getLastname);
                    intent.putExtra("email",getEmail);
                    intent.putExtra("address",getAddress);
                    intent.putExtra("phoneNo",getCellNumber);
                    intent.putExtra("country",getCountry);
                    intent.putExtra("city",getCity);
                    intent.putExtra("state",getState);
                    intent.putExtra("zipcode",getZipcode);
                    intent.putExtra("userID",userID);


                    startActivity(intent);

                }else {
                    Toast.makeText(getActivity(),"All fields are required.",Toast.LENGTH_SHORT).show();


                }




            }
        });


        return  rootView;
    }


   public class GetUserInfoTask extends AsyncTask<Void,Void,String>{

       @Override
       protected String doInBackground(Void... voids) {
           HttpURLConnection urlConnection = null;
           BufferedReader reader = null;

           // Will contain the raw JSON response as a string.
           String result = null;

           try {

               //http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getOrderMonogramValue&customerID=51818&orderNo=4380179&paperNo=50541&trackingID=4380179%20-%2051818%20-%201
               URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getSignupInfo&userID="+userID+"");
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


           if (s!= null){

               try {
                   JSONObject jsonObject = new JSONObject(s);
                    String firstName = jsonObject.getString("firstName");
                    String lastname = jsonObject.getString("lastName");

                   fullName.setText(firstName, TextView.BufferType.EDITABLE);

                   lastName.setText(lastname, TextView.BufferType.EDITABLE);





               } catch (JSONException e) {
                   e.printStackTrace();
               }


           }





       }
   }
    public void showMessage(){


        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog);
        // Custom Android Allert Dialog Title
        dialog.setTitle("BESPOKINO");

        TextView tv = (TextView) dialog.findViewById(R.id.tv);
        tv.setText("All fields are required .");
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
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

}



