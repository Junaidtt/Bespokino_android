package com.app.bespokino.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.app.bespokino.R;
import com.app.bespokino.adapter.CartAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.RecyclerItemClickListener;
import com.app.bespokino.model.CartProduct;
import com.app.bespokino.model.FabricModel;
import com.app.bespokino.model.Product;

import org.json.JSONArray;
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

public class CartActivity extends AppCompatActivity{


   // SwipeRefreshLayout mSwipeRefreshLayout;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static LinearLayout ll;
    private Button continueBUtton,addShirtButton;
    private List<CartProduct>cartProduct = new ArrayList<>();

    static TextView textCartItemCount;

    CustomerHelperDB customerHelperDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
//        editor = pref.edit();

        customerHelperDB = new CustomerHelperDB(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        continueBUtton = (Button)findViewById(R.id.continueButton);
        addShirtButton = (Button)findViewById(R.id.addShirtButton);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CartAdapter(getApplicationContext(),cartProduct);


        new FetchShirtsInCartTask().execute();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        continueBUtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences("MyPref", 0);
                boolean measurment = sp.getBoolean("measurment", false);


                if (cartProduct.size() != 0){

                    if (measurment){

                        Intent intent = new Intent(CartActivity.this,InvoiceActivity.class);
                        startActivity(intent);

                    }else{

                        Intent intent = new Intent(CartActivity.this,BodyPostureActivity.class);
                        startActivity(intent);
                    }

                  //finish();

                }


            }
        });

        addShirtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
 }


    public class FetchShirtsInCartTask extends  AsyncTask<Void,Void,String>
    {


        @Override
        protected String doInBackground(Void... voids) {

            String orderNo= null;
            String customerID = null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Cursor res =  customerHelperDB.getAllData();

            while (res.moveToNext()){
                orderNo = res.getString(2);
                customerID = res.getString(3);

            }


            String result = null;

            try {

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=listOrderItem&orderNo="+orderNo+"&customerID="+customerID+"");
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

            if (s == null){
                Toast.makeText(CartActivity.this,"No item in Cart",Toast.LENGTH_SHORT).show();
                finish();

            }else {

                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject object = new JSONObject();
                    int count = 0;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        count = i + 1;
                        object = jsonArray.getJSONObject(i);
                        String trackingNo = object.getString("trackingID");
                        int orderNumber = object.getInt("orderNo");
                        int customerID = object.getInt("customerID");
                        int paperNo = object.getInt("paperNo");
                        String fabricImage = object.getString("image");
                        double shirtPrice = object.getDouble("shirtPrice");
                        cartProduct.add(new CartProduct(trackingNo, customerID, orderNumber, paperNo, fabricImage, "Shirt " + count, shirtPrice));
                       // Toast.makeText(CartActivity.this,String.valueOf(cartProduct.size()),Toast.LENGTH_SHORT).show();
                    }
                    AppConfig.mCartItemCount = cartProduct.size();
                    setupBadge();
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                    mRecyclerView.addItemDecoration(itemDecoration);
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                Intent intent = new Intent(CartActivity.this,CartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setupBadge() {

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
