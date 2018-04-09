package com.app.bespokino.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.helper.UserStorageHelper;

import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.common.Message;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

import org.json.JSONArray;
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

import morxander.editcard.EditCard;

public class AcceptcardPayment extends AppCompatActivity implements View.OnClickListener, EncryptTransactionCallback {


    public static final String TAG = "PAYMENT ACTIVITY";
    private final String CARD_NUMBER = "4111111111111111";
    private final String EXPIRATION_MONTH = "11";
    private final String EXPIRATION_YEAR = "2017";
    private final String CVV = "256";
    private final String POSTAL_CODE = "98001";
    private final String CLIENT_KEY = "93v555Vx94fahCX2HTe5WrWb4ebT9xqt97DP3CZ5qszVH533xq9AGdn9vuBmyUGL";

    private final String API_LOGIN_ID = "785d4yCQ47";

    private final int MIN_CARD_NUMBER_LENGTH = 13;
    private final int MIN_YEAR_LENGTH = 2;
    private final int MIN_CVV_LENGTH = 4;
    private final String YEAR_PREFIX = "20";

    private Button checkoutButton;
    // private EditText cardNumberView;
    EditCard cardNumberView;
    private EditText monthView;
    private EditText yearView;
    private EditText cvvView;

    private ProgressDialog progressDialog;
    private CheckBox acceptCheckBox;


    private String cardNumber;
    private String month;
    private String year;
    private String cvv;
    private AcceptSDKApiClient apiClient;

    private UserStorageHelper userStorageHelper;

    private  String orderNumber,customerID,paperNumber;
    private String FirstName,LastName,City,Zipcode,Address,State,Country;
    double totalPay = 0.2f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptcard_payment);

        userStorageHelper = new UserStorageHelper(AcceptcardPayment.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        try {
            apiClient = new AcceptSDKApiClient.Builder(this,
                    AcceptSDKApiClient.Environment.PRODUCTION).connectionTimeout(
                    4000) // optional connection time out in milliseconds
                    .build();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        initialize();
        getUserInfoFromDB();
    }

    private void getUserInfoFromDB() {


        Cursor res =  userStorageHelper.getAllData();
        if(res.getCount() != 0){

            while (res.moveToNext()){


                FirstName=res.getString(1);
                LastName=res.getString(2);
                Address=res.getString(3);
                City = res.getString(4);
                State = res.getString(5);
                Zipcode = res.getString(6);
                Country = res.getString(7);

                totalPay = Double.parseDouble(res.getString(8));
                orderNumber = res.getString(9);
                customerID = res.getString(10);
                paperNumber = res.getString(11);

            }

        }




    }

    private void initialize() {

        cardNumberView = (EditCard) findViewById(R.id.card_number_view);
        // cardNumberView = (EditText)findViewById(R.id.card_number_view);
        //setUpCreditCardEditText();
        monthView = (EditText)findViewById(R.id.date_month_view);
        yearView = (EditText)findViewById(R.id.date_year_view);
        cvvView = (EditText)findViewById(R.id.security_code_view);

        checkoutButton = (Button)findViewById(R.id.button_checkout_order);
        checkoutButton.setOnClickListener(this);

        acceptCheckBox =(CheckBox)findViewById(R.id.acceptCheckBox);
       // acceptCheckBox.setOnClickListener(this);

       /* responseLayout = (RelativeLayout)findViewById(R.id.response_layout);
        responseTitle = (TextView)findViewById(R.id.encrypted_data_title);
        responseValue = (TextView)findViewById(R.id.encrypted_data_view);*/
    }

    @Override public void onClick(View v) {

        if (!areFormDetailsValid()) return;

        progressDialog = ProgressDialog.show(this, this.getString(R.string.progress_title),
                this.getString(R.string.progress_message), true);


        try {
            EncryptTransactionObject transactionObject = prepareTransactionObject();

      /*
        Make a call to get Token API
        parameters:
          1) EncryptTransactionObject - The transactionObject for the current transaction
          2) callback - callback of transaction
       */
            apiClient.getTokenWithRequest(transactionObject, this);
        } catch (NullPointerException e) {
            // Handle exception transactionObject or callback is null.
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            if (progressDialog.isShowing()) progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    /**
     * prepares a transaction object with dummy data to be used with the Gateway transactions
     */
    private EncryptTransactionObject prepareTransactionObject() {
        ClientKeyBasedMerchantAuthentication merchantAuthentication =
                ClientKeyBasedMerchantAuthentication.
                        createMerchantAuthentication(API_LOGIN_ID, CLIENT_KEY);

        // create a transaction object by calling the predefined api for creation
        return TransactionObject.
                createTransactionObject(
                        TransactionType.SDK_TRANSACTION_ENCRYPTION) // type of transaction object
                .cardData(prepareCardDataFromFields()) // card data to get Token
                .merchantAuthentication(merchantAuthentication).build();
    }

    private EncryptTransactionObject prepareTestTransactionObject() {
        ClientKeyBasedMerchantAuthentication merchantAuthentication =
                ClientKeyBasedMerchantAuthentication.
                        createMerchantAuthentication(API_LOGIN_ID, CLIENT_KEY);

        // create a transaction object by calling the predefined api for creation
        return EncryptTransactionObject.
                createTransactionObject(
                        TransactionType.SDK_TRANSACTION_ENCRYPTION) // type of transaction object
                .cardData(prepareTestCardData()) // card data to prepare token
                .merchantAuthentication(merchantAuthentication).build();
    }

    private CardData prepareTestCardData() {
        return new CardData.Builder(CARD_NUMBER, EXPIRATION_MONTH, EXPIRATION_YEAR).cvvCode("")
                .zipCode("")
                .cardHolderName("")
                .build();
    }

/* ---------------------- Callback Methods - Start -----------------------*/

    @Override public void onEncryptionFinished(EncryptTransactionResponse response) {
        hideSoftKeyboard();

       // if (responseLayout.getVisibility() != View.VISIBLE) responseLayout.setVisibility(View.VISIBLE);
        if (progressDialog.isShowing()) progressDialog.dismiss();
       /* responseTitle.setText(R.string.token);
        responseValue.setText(
                getString(R.string.data_descriptor) + response.getDataDescriptor() + "\n" + getString(
                        R.string.data_value) + response.getDataValue());*/

        Log.d("RESPONSE DATA: ", response.getDataValue());
        Log.d("RESPONSE DESCRIPTION: ", response.getDataDescriptor());
        Toast.makeText(this,
                response.getDataDescriptor() + " : " + response.getDataValue(),
                Toast.LENGTH_LONG)
                .show();
        new sendPaymentToAuthorizeTask().execute(response.getDataDescriptor(),response.getDataValue());

    }

    @Override public void onErrorReceived(ErrorTransactionResponse errorResponse) {
        hideSoftKeyboard();
        if (progressDialog.isShowing()) progressDialog.dismiss();

        Message error = errorResponse.getFirstErrorMessage();
        String errorString = getString(R.string.code) + error.getMessageCode() + "\n" +
                getString(R.string.message) + error.getMessageText();
        Toast.makeText(this,
                errorString,
                Toast.LENGTH_LONG)
                .show();

    }

/* ---------------------- Callback Methods - End -----------------------*/

    public void hideSoftKeyboard() {
        InputMethodManager keyboard =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this != null && this.getCurrentFocus() != null) {
            keyboard.hideSoftInputFromInputMethod(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private boolean areFormDetailsValid() {
        //cardNumber = cardNumberView.getText().toString().replace(" ", "");
        cardNumber = cardNumberView.getCardNumber();
        month = monthView.getText().toString();
        cvv = cvvView.getText().toString();
        year = yearView.getText().toString();

        if (isEmptyField()) {
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            Toast.makeText(this, "Empty fields", Toast.LENGTH_LONG).show();
            return false;
        }

        year = YEAR_PREFIX + yearView.getText().toString();

        return validateFields();
    }

    private boolean isEmptyField() {
        return (cardNumber != null && cardNumber.isEmpty()) || (month != null && month.isEmpty()) || (
                year != null
                        && year.isEmpty()) || (cvv != null && cvv.isEmpty());
    }

    private boolean validateFields() {
        if (cardNumber.length() < MIN_CARD_NUMBER_LENGTH) {
            cardNumberView.requestFocus();
            cardNumberView.setError(getString(R.string.invalid_card_number));
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        int monthNum = Integer.parseInt(month);
        if (monthNum < 1 || monthNum > 12) {
            monthView.requestFocus();
            monthView.setError(getString(R.string.invalid_month));
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        if (month.length() < MIN_YEAR_LENGTH) {
            monthView.requestFocus();
            monthView.setError(getString(R.string.two_digit_month));
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        if (year.length() < MIN_YEAR_LENGTH) {
            yearView.requestFocus();
            yearView.setError(getString(R.string.invalid_year));
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        if (cvv.length() < MIN_CVV_LENGTH) {
            cvvView.requestFocus();
            cvvView.setError(getString(R.string.invalid_cvv));
            checkoutButton.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return false;
        }
        return true;
    }



    private CardData prepareCardDataFromFields() {
        return new CardData.Builder(cardNumber, month, year).cvvCode(cvv) //CVV Code is optional
                .build();
    }

    public class sendPaymentToAuthorizeTask extends AsyncTask<String,Void,String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;
        String ResponseData;
        ProgressDialog pd;

        @Override
        protected String doInBackground(String... strings) {
            String data= strings[1];
            String desc = strings[0];

            try {

                JSONObject opaqueData = new JSONObject();
                opaqueData.put("dataDescriptor", strings[0]);
                opaqueData.put("dataValue", strings[1]);

                JSONObject payment = new JSONObject();
                payment.put("opaqueData", opaqueData);

                JSONObject lineItem = new JSONObject();
                lineItem.put("itemId", "1");
                lineItem.put("name", "SHIRT");
                lineItem.put("description", "Bespokino");
                lineItem.put("quantity", "18");
                lineItem.put("unitPrice", "2.00");

                JSONObject lineItems = new JSONObject();
                lineItems.put("lineItem", lineItem);


                JSONObject billTo = new JSONObject();
                billTo.put("firstName", FirstName);
                billTo.put("lastName", LastName);
                billTo.put("company", "BESPOKINO");
                billTo.put("address", Address);
                billTo.put("city", City);
                billTo.put("state", State);
                billTo.put("zip", Zipcode);
                billTo.put("country", Country);


                JSONObject shipTo = new JSONObject();
                shipTo.put("firstName", FirstName);
                shipTo.put("lastName", LastName);
                shipTo.put("company", "BESPOKINO");
                shipTo.put("address", Address);
                shipTo.put("city", City);
                shipTo.put("state", State);
                shipTo.put("zip", Zipcode);
                shipTo.put("country",Country);

                JSONObject setting = new JSONObject();
                setting.put("settingName", "testRequest");
                setting.put("settingValue", "false");

                JSONObject transactionSettings = new JSONObject();
                transactionSettings.put("setting", setting);


                JSONArray userField = new JSONArray();

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("name", "MerchantDefinedFieldName1");
                jsonObject1.put("value", "MerchantDefinedFieldValue1");

                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("name", "favorite_color");
                jsonObject2.put("value", "blue");

                userField.put(jsonObject1);
                userField.put(jsonObject2);

                JSONObject userFields = new JSONObject();
                userFields.put("userField", userField);


                JSONObject transactionRequestJSON = new JSONObject();
                transactionRequestJSON.put("transactionType", "authCaptureTransaction");
                transactionRequestJSON.put("amount", String.valueOf(totalPay));
                transactionRequestJSON.put("payment", payment);
                transactionRequestJSON.put("lineItems", lineItems);
                transactionRequestJSON.put("poNumber", "9037000810");
                transactionRequestJSON.put("billTo", billTo);
                transactionRequestJSON.put("shipTo", shipTo);
                transactionRequestJSON.put("customerIP", "255.255.255.255");
                transactionRequestJSON.put("transactionSettings", transactionSettings);
                transactionRequestJSON.put("userFields", userFields);


                JSONObject merchantAuthenticationJSON = new JSONObject();
                merchantAuthenticationJSON.put("name",API_LOGIN_ID);
                merchantAuthenticationJSON.put("transactionKey","8Qm5kGEK2G28Z2n6");


                JSONObject createTransactionRequestJSON = new JSONObject();

                createTransactionRequestJSON.put("merchantAuthentication", merchantAuthenticationJSON);


                createTransactionRequestJSON.put("refId", orderNumber);



                createTransactionRequestJSON.put("transactionRequest",transactionRequestJSON);


                JSONObject postJsonBody = new JSONObject();
                postJsonBody.put("createTransactionRequest", createTransactionRequestJSON);

                URL url = new URL("https://api.authorize.net/xml/v1/request.api");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(postJsonBody));
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
                Log.i(TAG, JsonResponse);


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


            try {
                JSONObject jsonobject = new JSONObject(s);
                String transactionResponse = jsonobject.getString("transactionResponse");
                JSONObject paymentResponse = new JSONObject(transactionResponse);
                String responseCode = paymentResponse.getString("responseCode");
                String transId = paymentResponse.getString("transId");
                String avsResultCode = paymentResponse.getString("avsResultCode");

                if(responseCode.equals("1")){

                    new FinalOrderPlacementTask().execute();

                    Toast.makeText(getApplicationContext(),"Successfully created transaction with Transaction ID: "+transId,Toast.LENGTH_LONG).show();
                    displayDialogBox("Successfully created transaction with Transaction ID: "+transId);


                }else if (responseCode.equals("2")){


                    switch (avsResultCode){

                        case "A":

                            Toast.makeText(getApplicationContext()," Address (Street) matches, ZIP does not.",Toast.LENGTH_LONG).show();

                            break;
                        case "B":
                            //Address information not provided for AVS check.
                            Toast.makeText(getApplicationContext(),"Address information not provided for AVS check.",Toast.LENGTH_LONG).show();

                            break;

                        case "E":
                            Toast.makeText(getApplicationContext(),"AVS error.",Toast.LENGTH_LONG).show();

                            break;
                        case "G":
                            Toast.makeText(getApplicationContext(),"Non-U.S. Card Issuing Bank.",Toast.LENGTH_LONG).show();

                            break;

                        case "N":
                            Toast.makeText(getApplicationContext(),"No Match on Address (Street) or ZIP.",Toast.LENGTH_LONG).show();

                            break;

                        case "P":
                            Toast.makeText(getApplicationContext(),"AVS not applicable for this transaction.",Toast.LENGTH_LONG).show();

                            break;

                        case "R":
                            Toast.makeText(getApplicationContext(),"Retry—System unavailable or timed out.",Toast.LENGTH_LONG).show();

                            break;
                        case "S":
                            Toast.makeText(getApplicationContext(),"Service not supported by issuer.",Toast.LENGTH_LONG).show();

                            break;

                        case "U":
                            Toast.makeText(getApplicationContext(),"Address information is unavailable.",Toast.LENGTH_LONG).show();

                            break;

                        case "W":
                            Toast.makeText(getApplicationContext()," Nine digit ZIP matches, Address (Street) does not.",Toast.LENGTH_LONG).show();

                            break;

                        case "X":
                            Toast.makeText(getApplicationContext(),"Address (Street) and nine digit ZIP match.",Toast.LENGTH_LONG).show();

                            break;

                        case "Y":
                            Toast.makeText(getApplicationContext(),"Address (Street) and five digit ZIP match.",Toast.LENGTH_LONG).show();

                            break;

                        case "Z":
                            Toast.makeText(getApplicationContext(),"  Five digit ZIP matches, Address (Street) does not.",Toast.LENGTH_LONG).show();

                            break;


                    }



                }

                else {
                    Toast.makeText(getApplicationContext(),"This transaction has been declined.",Toast.LENGTH_LONG).show();
                    displayDialogBox("This transaction has been declined.");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



    public void displayDialogBox(String s){

        AlertDialog.Builder builder = new AlertDialog.Builder(AcceptcardPayment.this);
        builder.setTitle("Payment");
        builder.setCancelable(true);
        builder.setMessage(s);
        builder.setPositiveButton("OK", null);
        // builder.setNegativeButton("Cancel", null);
        builder.show();

    }


    public class FinalOrderPlacementTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String result = null;
            try {

                // URL url = new URL(AppConfig.serverConnectionURL+"fabricCategoryAPI/include");
                URL url = new URL("http://www.bespokino.com/cfc/app3.cfc?wsdl&method=placeOrderAndCreatePDF&customerID="+customerID+"&orderNo="+orderNumber+"&paperNo="+paperNumber+"");
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
                    // Stream was empty.No point in parsing.
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


            if (s!=null){

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    boolean error = jsonObject.getBoolean("Error");

                        if (!error){

                            jsonObject.getString("Message");

                        }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }

    public void showMessage(){
        // custom dialog
        final Dialog dialog = new Dialog(AcceptcardPayment.this);
        dialog.setContentView(R.layout.custom_dialog);
        // Custom Android Allert Dialog Title
        dialog.setTitle("BESPOKINO");
        TextView tv = (TextView) dialog.findViewById(R.id.tv);
        tv.setText("Thank You..\nOrder Placed successfully completed.\nCheck your mail for invoice.");
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
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
