package com.app.bespokino.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.PlaceArrayAdapter;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.JavaEncryption;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import morxander.editcard.EditCard;

public class Pay20cardActivity extends AppCompatActivity implements View.OnClickListener, EncryptTransactionCallback,GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    //Address Finder Google Api


    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

//Payment
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
    private final int MIN_CVV_LENGTH = 3;
    private final int MIX_CVV_LENGTH = 4;
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
    private String cardType;
    private AcceptSDKApiClient apiClient;
    EditText edfullName,edlastName,edaddress,edcity,edstate,edzipcode,country,edemail,edcellNumber;

    String FULLNAME,EMAIL,ADDRESS,PHONENUMBER,COUNTRY,CITY,STATE,ZIPCODE;
    int uID;
    Button payTwenty;
    public JavaEncryption javaEncryption;
    CheckBox termsAndCondition;
    TextView textCartItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay20_card);

        if (getIntent().getExtras() != null){
            uID = getIntent().getExtras().getInt("uid");
        }else {
            uID = AppConfig.userID;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(Pay20cardActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        edfullName = (EditText)findViewById(R.id.edFirstName);
        //edlastName = (EditText)findViewById(R.id.edLastName);
        //address = (EditText)findViewById(R.id.edAddress);
        edcity = (EditText)findViewById(R.id.edCity);
        edstate = (EditText)findViewById(R.id.edState);
        edzipcode = (EditText)findViewById(R.id.edZipcode);
      //  country = (EditText)findViewById(R.id.edCountry);
        edemail  =(EditText)findViewById(R.id.edEmail);
        edcellNumber =(EditText)findViewById(R.id.edCellNumber);
        termsAndCondition = (CheckBox)findViewById(R.id.acceptCheckBox);


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

        new GetUserInfoTask().execute();


        edcellNumber.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s)
            {
                String text = edcellNumber.getText().toString();
                int  textLength = edcellNumber.getText().length();
                if (text.endsWith("-") || text.endsWith(" ") || text.endsWith(" "))
                    return;
                if (textLength == 1) {
                    if (!text.contains("("))
                    {
                        edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, "(").toString());
                        edcellNumber.setSelection(edcellNumber.getText().length());
                    }
                }
                else if (textLength == 5)
                {
                    if (!text.contains(")"))
                    {
                        edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, ")").toString());
                        edcellNumber.setSelection(edcellNumber.getText().length());
                    }
                }
                else if (textLength == 6)
                {
                    edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, " ").toString());
                    edcellNumber.setSelection(edcellNumber.getText().length());
                }
                else if (textLength == 10)
                {
                    if (!text.contains("-"))
                    {
                        edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                        edcellNumber.setSelection(edcellNumber.getText().length());
                    }
                }
                else if (textLength == 15)
                {
                    if (text.contains("-"))
                    {
                        edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                        edcellNumber.setSelection(edcellNumber.getText().length());
                    }
                }
                else if (textLength == 18)
                {
                    if (text.contains("-"))
                    {
                        edcellNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                        edcellNumber.setSelection(edcellNumber.getText().length());
                    }
                }
            }
        });

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
        //acceptCheckBox.setOnClickListener(this);


    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
    @Override public void onClick(View v) {

        FULLNAME = edfullName.getText().toString();
        //LASTNAME = edlastName.getText().toString();
        EMAIL = edemail.getText().toString();
        ADDRESS = mAutocompleteTextView.getText().toString();
        CITY = edcity.getText().toString();
        PHONENUMBER = edcellNumber.getText().toString();
        STATE = edstate.getText().toString();
        ZIPCODE = edzipcode.getText().toString();
       // COUNTRY = country.getText().toString();

        if (!areFormDetailsValid()) return;
        if (edfullName.getText().toString().length() == 0) {
            edfullName.setError("First name not entered");
            edfullName.requestFocus();
        }
        if (edlastName.getText().toString().length() == 0) {
            edlastName.setError("Last name not entered");
            edlastName.requestFocus();
        }
        if (edemail.getText().toString().length() == 0) {
            edemail.setError("Email  not entered");
            edemail.requestFocus();
        }

        if (edaddress.getText().toString().length() == 0) {
            edaddress.setError(" address not entered");
            edaddress.requestFocus();
        }

        if (edcity.getText().toString().length() == 0) {
            edcity.setError("City not entered");
            edcity.requestFocus();
        }
        if (edcellNumber.getText().toString().length() == 0) {
            edcellNumber.setError("Cell number not entered");
            edcellNumber.requestFocus();
        }
        if (edstate.getText().toString().length() == 0) {
            edstate.setError("State not entered");
            edstate.requestFocus();
        }


        if (edzipcode.getText().toString().length() == 0) {
            edzipcode.setError(" Zipcode not entered");
            edzipcode.requestFocus();

        }else {

            if (!isValidMail(EMAIL)){
                edemail.setError("Invalied email");
                edemail.requestFocus();
            }else {

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
        new SendTwentyPaymentTask().execute(response.getDataDescriptor(),response.getDataValue());
        //new OrderToolPay20Task().execute();
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
        cardType = cardNumberView.getCardType();
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
        if (cvv.length() < MIN_CVV_LENGTH ) {
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

    public class SendTwentyPaymentTask extends AsyncTask<String,Void,String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;
        String ResponseData;
        ProgressDialog pd;


        @Override
        protected String doInBackground(String... strings) {
            try {

                JSONObject opaqueData = new JSONObject();
                opaqueData.put("dataDescriptor", strings[0]);
                opaqueData.put("dataValue", strings[1]);

                JSONObject payment = new JSONObject();
                payment.put("opaqueData", opaqueData);

                JSONObject lineItem = new JSONObject();
                lineItem.put("itemId", "1");
                lineItem.put("name", "Measuring tool");
                lineItem.put("description", "Self measuring tool");
                lineItem.put("quantity", "1");
                lineItem.put("unitPrice", "2.00");

                JSONObject lineItems = new JSONObject();
                lineItems.put("lineItem", lineItem);


                JSONObject billTo = new JSONObject();
                billTo.put("firstName", FULLNAME);
                billTo.put("lastName", " ");
                billTo.put("company", "BESPOKINO");
                billTo.put("address", ADDRESS);
                billTo.put("city", CITY);
                billTo.put("state", STATE);
                billTo.put("zip", ZIPCODE);
                billTo.put("country", COUNTRY);


                JSONObject shipTo = new JSONObject();
                shipTo.put("firstName", FULLNAME);
                shipTo.put("lastName", "");
                shipTo.put("company", "BESPOKINO");
                shipTo.put("address", ADDRESS);
                shipTo.put("city", CITY);
                shipTo.put("state", STATE);
                shipTo.put("zip", ZIPCODE);
                shipTo.put("country", COUNTRY);

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
                transactionRequestJSON.put("amount", "20");
                transactionRequestJSON.put("payment", payment);
                transactionRequestJSON.put("lineItems", lineItems);
                transactionRequestJSON.put("poNumber", PHONENUMBER);
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


                createTransactionRequestJSON.put("refId", "128816");


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

                    Toast.makeText(getApplicationContext(),"Successfully created transaction with Transaction ID: "+transId,Toast.LENGTH_LONG).show();
                    displayDialogBox("Successfully created transaction with Transaction ID: "+transId);

                    new OrderToolPay20Task().execute();


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

        AlertDialog.Builder builder = new AlertDialog.Builder(Pay20cardActivity.this);
        builder.setTitle("Payment");
        builder.setCancelable(true);
        builder.setMessage(s);
        builder.setPositiveButton("OK", null);
        // builder.setNegativeButton("Cancel", null);
        builder.show();

    }

    public class OrderToolPay20Task extends AsyncTask<Void,Void,String>{


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;

        @Override
        protected String doInBackground(Void... voids) {

            try {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fullName",FULLNAME);
                jsonObject.put("email",EMAIL);
                jsonObject.put("phoneNo",PHONENUMBER);
                jsonObject.put("userId",uID);
                jsonObject.put("ccType",cardType);
                jsonObject.put("ccNo",getEncryptedCardNumber());
                jsonObject.put("ccExpMonth",month);
                jsonObject.put("ccExpYear",year);
                jsonObject.put("ccCode",cvv);
                jsonObject.put("address",ADDRESS);
                jsonObject.put("city",CITY);
                jsonObject.put("state",STATE);
                jsonObject.put("zip",ZIPCODE);
                jsonObject.put("ccLastNo",getCCLastFourDigit());

                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=CustomerMeasuringToolDeposit");

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

        }
    }

    public String getEncryptedCardNumber(){

        String cardnumber = "";
        javaEncryption = new JavaEncryption();

        try {
           cardnumber = javaEncryption.encrypt(cardNumber,"!@#123asd");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


        return cardnumber;
    }



    private String getCCLastFourDigit() {

        String lastFour = null;
        if (cardNumber != null && cardNumber.length() >= 4) {
            lastFour = cardNumber.substring(cardNumber.length() - 4);
        }

        return lastFour;
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
                URL url = new URL("http://www.bespokino.com/cfc/app2.cfc?wsdl&method=getSignupInfo&userID="+AppConfig.userID+"");
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
                    String email = jsonObject.getString("Email");
                    String cellnumber = jsonObject.getString("PhoneNo");

                    edfullName.setText(firstName, TextView.BufferType.EDITABLE);

                    edlastName.setText(lastname, TextView.BufferType.EDITABLE);

                    edemail.setText(email,TextView.BufferType.EDITABLE);
                    edcellNumber.setText(cellnumber,TextView.BufferType.EDITABLE);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }





        }
    }
    public void showMessage(){


        // custom dialog
        final Dialog dialog = new Dialog(Pay20cardActivity.this);
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
                Intent intent = new Intent(Pay20cardActivity.this,CartActivity.class);
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


//Address Finder


    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            double latitude= place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;

            getAddressDetails(latitude,longitude);




        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    public void getAddressDetails(double latitude,double longitude){

        Address address=null;
        String addr="";
        String zipcode="";
        String city="";
        String state="";
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {

            addr = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getSubAdminArea();
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();

            for (int i = 0; i < addresses.size(); i++) {
                address = addresses.get(i);
                if (address.getPostalCode() != null) {
                    zipcode = address.getPostalCode();
                    break;
                }

            }
        }


        String realAddress = addr.replace("null","");

        edcity.setText(city);
        edstate.setText(state);
        edzipcode.setText(zipcode);
        mAutocompleteTextView.setText(realAddress);

       // Toast.makeText(Pay20cardActivity.this,zipcode,Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }
}
