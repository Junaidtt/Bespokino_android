package com.app.bespokino.activity;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.GridItemView;
import com.app.bespokino.adapter.GridViewAdapter;
import com.app.bespokino.fragment.AllContrast;
import com.app.bespokino.fragment.BackPleatDialogFragment;
import com.app.bespokino.fragment.ContrastDialogFragment;
import com.app.bespokino.fragment.ContrastOptionDialog;
import com.app.bespokino.fragment.InnercontrastDialogFragment;
import com.app.bespokino.fragment.PlacketDialogFragment;
import com.app.bespokino.fragment.ThreadColorDialogFragment;
import com.app.bespokino.fragment.TuxedoDialogFragment;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.model.ItemModel;

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

public class AdditionaloptionsActivity extends AppCompatActivity {

    Button addToCart;
     TextView textCartItemCount;

    Bundle bundle = new Bundle();
    ItemModel model;
    ProgressDialog pDialog;
    CustomerHelperDB customerHelperDB;
    private ArrayList<String> selectedStrings;
    public static List<ItemModel> item;

    int tPositon;

    public static  GridViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additionaloptions);

        addToCart = (Button)findViewById(R.id.addToCart);
        customerHelperDB = new CustomerHelperDB(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        GridView gridView = (GridView)findViewById(R.id.gridview);

        final FragmentManager fm = getFragmentManager();
        final ThreadColorDialogFragment t = new ThreadColorDialogFragment();

        final ContrastOptionDialog o =new ContrastOptionDialog();
        final BackPleatDialogFragment backPleat = new BackPleatDialogFragment();
        final PlacketDialogFragment p = new PlacketDialogFragment();
        final TuxedoDialogFragment tuxedoDialogFragment = new TuxedoDialogFragment();
        final InnercontrastDialogFragment i = new InnercontrastDialogFragment();
        final ContrastDialogFragment contrastDialogFragment = new ContrastDialogFragment();

        final AllContrast allContrast = new AllContrast();



        setData();
// item.add(new ItemModel(false,"INNER CUFF",R.drawable.inner_cuff,"INNER",549,90));
       // item.add(new ItemModel(false,"INNER PLACKET",R.drawable.inner_placket,"PLACKET",150,54));
       // item.add(new ItemModel(false,"SLEEVE VENT",R.drawable.sleeve_vent,"VENT",148,55));


        selectedStrings = new ArrayList<>();

        adapter = new GridViewAdapter(item, this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                model = item.get(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((GridItemView) v).display(false);
                    model.setSelected(false);
                    selectedStrings.remove((ItemModel) parent.getItemAtPosition(position));

                    switch (model.getItemName()) {

                        case "POCKET":
                            AppConfig.pocket = "";
                            break;
                        case "SHORT SLEEVE":
                            AppConfig.shortSleeve = "";
                            break;
                        case "BUTTON HOLE & THREAD":
                            AppConfig.buttonholeColor = "";
                            break;
                        case "INNER COLLAR":
                            AppConfig.collarContrastFabric = "";
                             break;
                        case "INNER CUFF":
                            AppConfig.cuffContrastFabric = "";
                            break;
                        case "INNER PLACKET":
                            AppConfig.placketContrastFabric = "";
                            break;
                        case "SLEEVE VENT":
                            AppConfig.sleeveVentContrastFabric ="";
                            break;
                        case "WHITE COLLAR & CUFF":
                            AppConfig.whiteCuffAndCollar = "";
                            break;
                        case "PLACKET":
                            AppConfig.placket = "";
                            break;
                        case "BACK PLEAT":
                            AppConfig.backPleats = "";
                            break;
                        case  "TUXEDO":
                            AppConfig.tuxedo = "";
                            AppConfig.tuxedoPleat = "";

                    }

                } else {
                    adapter.selectedPositions.add(position);
                    ((GridItemView) v).display(true);
                    model.setSelected(true);
                    selectedStrings.add(String.valueOf((ItemModel) parent.getItemAtPosition(position)));

                    //Toast.makeText(AdditionaloptionsActivity.this, String.valueOf(model.isSelected()), Toast.LENGTH_SHORT).show();

                    switch (model.getItemName()) {


                        case "POCKET":

                            // Toast.makeText(AdditionaloptionsActivity.this,model.getItemName() + AppConfig.pocket,Toast.LENGTH_SHORT).show();

                            if (model.isSelected()) {

                                AppConfig.pocket = String.valueOf(model.getOptionValude());

                            } else {

                                AppConfig.pocket = "";

                            }

                            break;
                        case "SHORT SLEEVE":


                            if (model.isSelected()) {

                                AppConfig.shortSleeve = String.valueOf(model.getOptionValude());

                            } else {

                                AppConfig.shortSleeve = "";
                            }
                            break;
                        case "BUTTON HOLE & THREAD":

                            t.show(fm, "thread");
                            break;
                        case "CONTRAST":
                            bundle.putString("VALUE", model.getItemName());
                          //  contrastDialogFragment.setArguments(bundle);
                             //i.show(fm,"contrast");

                             allContrast.show(fm,"contrast");
                            break;
                        case "INNER CUFF":
                            bundle.putString("VALUE", model.getItemName());
                            contrastDialogFragment.setArguments(bundle);
                            contrastDialogFragment.show(fm,"contrast");
                            break;
                        case "INNER PLACKET":
                            bundle.putString("VALUE", model.getItemName());
                            contrastDialogFragment.setArguments(bundle);
                            contrastDialogFragment.show(fm,"contrast");
                            break;
                        case "SLEEVE VENT":
                            bundle.putString("VALUE", model.getItemName());
                            contrastDialogFragment.setArguments(bundle);
                            contrastDialogFragment.show(fm,"contrast");
                            break;
                        case "WHITE COLLAR & CUFF":

                            AppConfig.whiteCuffAndCollar = String.valueOf(model.getOptionValude());

                            break;
                        case "PLACKET":

                            p.show(fm, "placket");

                            break;
                        case "BACK PLEAT":
                            backPleat.show(fm, "backpleat");
                            break;
                        case "TUXEDO":
                            tuxedoDialogFragment.show(fm, "tuxedo");
                            break;

                    }

                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PostAddOptionsTask().execute();

            }
        });



    }

    public static void setData() {

        item = new ArrayList<>();

        item.add(new ItemModel(false,"POCKET",R.drawable.pocket,"POCKET",187,46));
        item.add(new ItemModel(false,"TUXEDO",R.drawable.yes_tuxedo_pleats,"TUXEDO",528,86));
        item.add(new ItemModel(false,"CONTRAST",R.drawable.contrast_new,"INNER",547,89));
        item.add(new ItemModel(false,"BUTTON HOLE & THREAD",R.drawable.thread,"BUTTON"));
        item.add(new ItemModel(false,"WHITE COLLAR & CUFF",R.drawable.whitec,"WHITE",206,62));
        item.add(new ItemModel(false,"PLACKET",R.drawable.placketb,"PLACKET",159,44));
        item.add(new ItemModel(false,"BACK PLEAT",R.drawable.twopleats,"PLEAT",156,45));
        item.add(new ItemModel(false,"SHORT SLEEVE",R.drawable.short_sleev,"SHORT",208,63));


    }


    private class PostAddOptionsTask extends AsyncTask<Void,Void,String>{

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(AdditionaloptionsActivity.this,R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);
            pDialog.setMessage("Saving your style ...");
            showDialog();
        }


        @Override
        protected String doInBackground(Void... voids) {

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
                jsonObject.put("placket",AppConfig.placket);
                jsonObject.put("backPleats",AppConfig.backPleats);
                jsonObject.put("pocket",AppConfig.pocket);
                jsonObject.put("shortSleeve",AppConfig.shortSleeve);
                jsonObject.put("tuxedo",AppConfig.tuxedo);
                jsonObject.put("tuxedoPleat",AppConfig.tuxedoPleat);
                jsonObject.put("collarContrastFabric",AppConfig.collarContrastFabric);
                jsonObject.put("cuffContrastFabric",AppConfig.cuffContrastFabric);
                jsonObject.put("placketContrastFabric",AppConfig.placketContrastFabric);
                jsonObject.put("sleeveVentContrastFabric",AppConfig.sleeveVentContrastFabric);
                jsonObject.put("whiteCuffAndCollar",AppConfig.whiteCuffAndCollar);
                jsonObject.put("contrastFabricCategory",AppConfig.contrastFabricCategory);
                jsonObject.put("contrastFabricID",AppConfig.contrastFabricID);
                jsonObject.put("buttonholeColor",AppConfig.buttonholeColor);
                jsonObject.put("btnType",AppConfig.btnType);

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=insertAdditionalOptionStylingInfo");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);

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

                    Intent i = new Intent(AdditionaloptionsActivity.this, CartActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

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


    @Override
    protected void onResume() {
        super.onResume();

       // setData();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        newConfig.orientation = getResources().getConfiguration().orientation;
        super.onConfigurationChanged(newConfig);
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
                Intent intent = new Intent(AdditionaloptionsActivity.this,CartActivity.class);
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
