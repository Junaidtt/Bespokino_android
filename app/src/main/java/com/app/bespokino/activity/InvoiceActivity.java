package com.app.bespokino.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.InvoiceAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.UserStorageHelper;
import com.app.bespokino.model.Invoice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Button payButton;
    TextView textCartItemCount;
    int updatedCustId;
    int orderNumber;
    int paperNumber;


    private UserStorageHelper userStorageHelper;
    List<Invoice>invoice = new ArrayList<>();

    String firstName,lastName,country,zip,city,state,address;
    double customeShirtPrice,fabricUpgrade,stylingAddup;
    int customerID;

    //TextView invoiceName,invoiceCustID,inviceOrderNo,DateText,invoiceAddress,invoiceCountry;
    TextView tvSubTotal,tvSalesTax,tvSaleAmt,tvShipping,tvPaidByCC;
    double subTotalAmount,salesTaxAmount,totalSalesAmount,shippingCost,paidByCCAmount;
    CustomerHelperDB customerHelperDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

       // updatedCustId = AppConfig.updatedCustomerId;
//        orderNumber = AppConfig.updatedOrderNo;
//        paperNumber = AppConfig.updatedPaperNo;

        userStorageHelper = new UserStorageHelper(InvoiceActivity.this);
        customerHelperDB = new CustomerHelperDB(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//
//        invoiceName = (TextView)findViewById(R.id.invoiceName);
//        invoiceCustID = (TextView)findViewById(R.id.invoiceCustID);
//        inviceOrderNo = (TextView)findViewById(R.id.invoiceOrderNo);
//        invoiceAddress = (TextView)findViewById(R.id.invoiceAddress);
//        invoiceCountry = (TextView)findViewById(R.id.ivoicePlace);
//        DateText = (TextView)findViewById(R.id.invoiceDate);
        payButton = (Button)findViewById(R.id.payButton);
        //DateText.setText("Date :"+new SimpleDateFormat("dd-MM-yyy").format(new Date()));

        tvSubTotal = (TextView)findViewById(R.id.tvSubtotal);
        tvSalesTax = (TextView)findViewById(R.id.tvSalesTax);
        tvSaleAmt = (TextView)findViewById(R.id.tvSaleAmt);
        tvShipping = (TextView)findViewById(R.id.tvContinentalShipping);
        tvPaidByCC = (TextView)findViewById(R.id.tvPaidbyCard);

        Cursor res =  customerHelperDB.getAllData();

        while (res.moveToNext()){
            orderNumber = res.getInt(2);
            customerID = res.getInt(3);

        }


        new InvoiceFetcherTask().execute();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(InvoiceActivity.this,CheckoutActivity.class));

            }
        });

    }


    public class InvoiceFetcherTask extends AsyncTask<Void,Void,String> {


        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;

            try {

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=getInvoiceInfo&orderNo="+orderNumber+"&customerID="+customerID+"");
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

            if (s != null){

                try {
                    JSONArray response = new JSONArray(s);

                    for (int i = 0; i <response.length();i++){

                        JSONObject object = response.getJSONObject(i);


                         firstName = object.getString("firstName");
                         lastName = object.getString("lastName");
                         country = object.getString("country");
                         zip  = object.getString("zip");
                         city = object.getString("city");
                         state = object.getString("state");
                         address = object.getString("address");

                         customeShirtPrice = object.getDouble("TotalPrice");
                         fabricUpgrade = object.getDouble("FabricUpgrade");
                         stylingAddup = object.getDouble("StylingAddup");
                         invoice.add(new Invoice(customeShirtPrice,fabricUpgrade,stylingAddup));

                         customerID = object.getInt("customerID") ;
                         orderNumber = object.getInt("orderNo");

                         subTotalAmount = object.getDouble("subTotalAmount");
                         salesTaxAmount = object.getDouble("salesTaxAmount");
                         totalSalesAmount = object.getDouble("totalSalesAmount");
                         shippingCost = object.getDouble("shippingCost");
                         paidByCCAmount = object.getDouble("paidByCCAmount");



                 }
                    Cursor res = userStorageHelper.getAllData();
                    if (res.getCount() == 0) {

                        boolean isInserted = userStorageHelper.insertData(firstName, lastName, address, city, state, zip, country,String.valueOf(paidByCCAmount),String.valueOf(orderNumber),String.valueOf(customerID),String.valueOf(paperNumber));

                        if (isInserted) {
                           // Toast.makeText(InvoiceActivity.this, "Data inserted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InvoiceActivity.this, "INSERTION FAILED", Toast.LENGTH_SHORT).show();
                        }

                    } else {

                        boolean isUpdate = userStorageHelper.updateData("1",firstName, lastName, address, city, state, zip, country,String.valueOf(paidByCCAmount),String.valueOf(orderNumber),String.valueOf(customerID),String.valueOf(paperNumber));

                        if (isUpdate == true) {
                            //Toast.makeText(InvoiceActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(InvoiceActivity.this, "Updation failed", Toast.LENGTH_SHORT).show();

                    }

                    mAdapter = new InvoiceAdapter(InvoiceActivity.this,invoice);
                    mRecyclerView.setAdapter(mAdapter);

                    tvSubTotal.setText("$ "+String.format("%.2f",subTotalAmount));
                    tvSalesTax.setText("$ "+String.format("%.2f",salesTaxAmount));
                    tvSaleAmt.setText("$ "+String.format("%.2f",totalSalesAmount));
                    tvShipping.setText("$ "+String.format("%.2f",shippingCost));
                    //tvShipping.setText("Free");
                    tvPaidByCC.setText("$ "+String.format("%.2f",paidByCCAmount));

//                    invoiceName.setText(firstName+" "+lastName);
//                    invoiceCustID.setText("CustomerID :"+String.valueOf(customerID));
//                    inviceOrderNo.setText("OrderNo :"+String.valueOf(orderNumber));
//                    invoiceAddress.setText(country);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else {

            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.e("INVOICE", "Saving");
        savedInstanceState.putInt("UPDATED_CUST_ID", updatedCustId);
        savedInstanceState.putInt("ORDER_NO", orderNumber);
        savedInstanceState.putInt("PAPER_NO",paperNumber);
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
                Intent intent = new Intent(InvoiceActivity.this,CartActivity.class);
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
