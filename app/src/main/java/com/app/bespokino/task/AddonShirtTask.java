/*
package com.app.bespokino.task;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.bespokino.activity.CartActivity;
import com.app.bespokino.activity.MainActivity;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.DatabaseHelper;

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

*/
/**
 * Created by bespokino on 9/28/2017 AD.
 *//*



public class AddonShirtTask extends AsyncTask<Void,Void,String>{
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String JsonResponse = null;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "OrderDetails" ;

    CustomerHelperDB customerHelperDB;
    Context context;


    public AddonShirtTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {

        //  {"orderNo":"9120239","customerID":"1949","paperNo":"50460","trackingID":"9120239 - 1949 - 1","phoneNo":"919995261220"}
        JSONObject addNextJSON = new JSONObject();
        try {
            addNextJSON.put("orderNo",sharedpreferences.getInt("orderNo",0));
            addNextJSON.put("customerID",sharedpreferences.getInt("customerID",0));
            addNextJSON.put("paperNo",sharedpreferences.getInt("paperNo",0));
            addNextJSON.put("trackingID",sharedpreferences.getString("trackingID",null));
            addNextJSON.put("phoneNo","919995261220");


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

                */
/*Intent intent = new Intent(,MainActivity.class);
                startActivity(intent);
                finish();*//*


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
*/
