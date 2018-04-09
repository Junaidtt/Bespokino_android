package com.app.bespokino.task;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bespokino on 9/28/2017 AD.
 */

public class DeleteCartItemTask extends AsyncTask<String,Void,String> {

    String customerID = "";
    String orderNo = "";
    String paperNo = "";
    String trackingID ="";

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        customerID = strings[0];
        orderNo = strings[1];
        paperNo = strings [2];
       // trackingID =strings[3];
        trackingID = strings[3].replace(" ","%20");

        String result = null;

        try {
            //http://www.bespokino.com/cfc/app.cfc?wsdl&method=deleteOrderItem&customerID="+customerID+"&orderNo="+orderNo+"&paperNo="+paperNo+"&trackingID="+trackingID+"%20-%202221%20-%202
            // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
            URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=deleteOrderItem&customerID="+customerID+"&orderNo="+orderNo+"&paperNo="+paperNo+"&trackingID="+trackingID+"");
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
    }
}
