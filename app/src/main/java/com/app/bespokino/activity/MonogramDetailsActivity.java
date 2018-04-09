package com.app.bespokino.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.model.ItemModel;
import com.app.bespokino.model.ThreadModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MonogramDetailsActivity extends AppCompatActivity {


    int customerID,paperNo,orderNo;
    String trackingNo = null;


    TextView monogramtext,monogramStyle;
    ImageView monoPositinImage,monoThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monogram_details);

        customerID = getIntent().getExtras().getInt("customerID");
        paperNo = getIntent().getExtras().getInt("paperNo");
        orderNo = getIntent().getExtras().getInt("orderNo");
        trackingNo = getIntent().getExtras().getString("trackingID");

        monogramtext = (TextView)findViewById(R.id.monoText);
        monogramStyle = (TextView)findViewById(R.id.monoStyle);
        monoPositinImage = (ImageView)findViewById(R.id.monoPosition);
        monoThread = (ImageView)findViewById(R.id.monoThread);


        new MonogramDetailsTask().execute();

    }

    public class MonogramDetailsTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                //http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getOrderMonogramValue&customerID=51818&orderNo=4380179&paperNo=50541&trackingID=4380179%20-%2051818%20-%201
                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getOrderMonogramValue&customerID="+customerID+"&orderNo="+orderNo+"&paperNo="+paperNo+"&trackingID="+trackingNo+"");
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

            if (s !=null){


                try {
                    JSONObject jsonObject = new JSONObject(s);

                    int monogramColor = jsonObject.getInt("monogramColor");
                    int monogramStyle = jsonObject.getInt("monogramStyle");
                    int monogramPosition =jsonObject.getInt("monogramPosition");
                    String monogramText = jsonObject.getString("monogramText");
                    monoFiner(monogramPosition);
                    monogramThreadFinder(monogramColor);
                    monoStyleFinder(monogramStyle);

                    monogramtext.setText(monogramText);






                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {

                //Toast.makeText(MonogramDetailsTask.this,"Server error:",Toast.LENGTH_SHORT).show();

            }



        }
    }

    private void monoStyleFinder(int m) {

        if (m == 167){
            monogramStyle.setText("Simple");
        }else if (m == 168){
            monogramStyle.setText("Script");
        }else if(m == 169){
            monogramStyle.setText("Fancy");
        }


    }

    private void monogramThreadFinder(int t){

        List<ThreadModel> monoThreadList = new ArrayList();
        ThreadModel threadModel = null;


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


        for (int i = 0;i<monoThreadList.size();i++){


            if (monoThreadList.get(i).getImgCode() == t){
                monoThread.setImageResource(monoThreadList.get(i).getImgID());

            }
        }


    }
    public int monoFiner(int monogram){

        final List<ItemModel> item = new ArrayList<>();
        ItemModel itemModel = null;

        item.add(new ItemModel(false,"POCKET",R.drawable.monogram_pocket,"MONO",172,48));
        item.add(new ItemModel(false,"CUFF",R.drawable.monogram_cuff,"MONO",171,48));
        item.add(new ItemModel(false,"BACK OF COLLAR",R.drawable.monogram_back_collar,"MONO",174,48));
        item.add(new ItemModel(false,"INSIDE COLLAR",R.drawable.monogram_inside_collar,"MONO",173,48));
        item.add(new ItemModel(false,"BODY",R.drawable.monogram_body,"MONO",170,48));

        for (int i=0;i<item.size();i++){


            if (item.get(i).getOptionValude()==monogram){


                monoPositinImage.setImageResource(item.get(i).getItemImage());


            }



        }

        return  174;

    }
}
