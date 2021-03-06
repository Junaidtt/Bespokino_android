package com.app.bespokino.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.PhoneNumberTextWatcher;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.helper.SessionManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener  {

    private Button registerButton, alreadyRegisterBtn;
    private EditText firstNameField, emailAddressField,passwordTextField;
  //  String paintWaistSize = "";
    Spinner spinnerHeight;
    TextView textCartItemCount;
    ProgressDialog pDialog;
    private int textlength = 0;
    private SessionManager session;
    private SQLiteHandler db;


    //Socail login
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    Button sign_out;
    SignInButton signin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //FB LOGIN
    private CallbackManager mCallbackManager;
    private static final String TAGF = "FACELOG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isRegIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


        registerButton = (Button) findViewById(R.id.buttonSignIn);
        alreadyRegisterBtn = (Button) findViewById(R.id.btnSignIn);
        firstNameField = (EditText) findViewById(R.id.edFirstName);

        emailAddressField = (EditText) findViewById(R.id.edEmailAddress);

        passwordTextField = (EditText) findViewById(R.id.edPassword);

        signin = (SignInButton) findViewById(R.id.sign_in_button);


        findViewById(R.id.sign_in_button).setOnClickListener(RegisterActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(RegisterActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        //register button clicked
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstname = firstNameField.getText().toString().trim();
                String email = emailAddressField.getText().toString().trim();
                String passwprd = passwordTextField.getText().toString().trim();
                //String spinner = paintWaistSize;

                if (firstNameField.getText().toString().length() == 0) {
                    firstNameField.setError("First name not entered");
                    firstNameField.requestFocus();
                }



                if (emailAddressField.getText().toString().length() == 0) {
                    emailAddressField.setError("Email address not entered");
                    emailAddressField.requestFocus();


                } else {

                    String emailAddress = emailAddressField.getText().toString().trim();


                }

                if (passwordTextField.getText().toString().length() == 0) {
                    passwordTextField.setError("Enter Password");
                    passwordTextField.requestFocus();
                } else {

                    if (isValidEmail(email)) {
                        String emailAddress = emailAddressField.getText().toString().trim();
                        new RegisterUserTask().execute(firstname, email, passwprd);

                        Log.d("Email:", "email ok");
                    } else {
                        emailAddressField.setError("Check your email");
                        emailAddressField.requestFocus();
                    }

                }


            }
        });

        alreadyRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        firstNameField.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence cs, int start,
                                               int end, Spanned spanned, int dStart, int dEnd) {
                        // TODO Auto-generated method stub
                        if(cs.equals("")){ // for backspace
                            return cs;
                        }
                        if(cs.toString().matches("[a-zA-Z ]+")){
                            return cs;
                        }
                        return "";
                    }
                }
        });


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAGF, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAGF, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAGF, "facebook:onError", error);
                // ...
            }
        });


    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    public class RegisterUserTask extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);
            pDialog.setMessage("Registering...");
            //showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                // Body -> {"FirstName":"Fred","LastName":"Hakim","Email":"fred.hakim@gemmasuits.com","Password":"fred123","PhoneNo":"1558054560", "pantsWaist":"38"}

                JSONObject regObject = new JSONObject();
                regObject.put("FullName", strings[0]);
                regObject.put("Email", strings[1]);
                regObject.put("Password", strings[2]);
                regObject.put("clientID","");

              //  {"FullName":"Juju","Email":"juju.hakim@gemmasuits.com","Password":"juju123"}

                 URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=CustomerSignup");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(regObject));
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                urlConnection.getResponseCode();
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

            if (pDialog != null) {
                pDialog.dismiss();
            }

            if (s != null) {

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    boolean error = jsonobject.getBoolean("Error");
                    if (!error) {


                        session.setRegIn(true);

                       // int modelNo = jsonobject.getInt("modelNo");
                        int userID = jsonobject.getInt("UserId");
                        AppConfig.userID =userID;
                      //AppConfig.modelNo = modelNo;



                        session.setLogin(true);

                        String fullname = jsonobject.getString("FullName");
                        String message = jsonobject.getString("Message");
                        String email = jsonobject.getString("Email");
                        int userid = jsonobject.getInt("UserId");
                        String address = jsonobject.getString("Address");
                        String city = jsonobject.getString("City");
                        String state = jsonobject.getString("State");
                        String phoneumber = jsonobject.getString("PhoneNo");
                        boolean measurment = jsonobject.getBoolean("Measurements");
                        int zip = jsonobject.getInt("Zip");

                        if (measurment){

                            editor.putBoolean("measurment", true);

                        }else{

                            editor.putBoolean("measurment", false);

                        }
                        editor.commit();

                        db.deleteUsers();

                        boolean isInserted =  db.addUser(fullname,address,email,userid,zip,phoneumber,city,state);

                        if(isInserted){
                         //   Toast.makeText(RegisterActivity.this,"Data inserted",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this,
                                    MainActivity.class);

                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(RegisterActivity.this,"INSERTION FAILED",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Internel Server error", Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(RegisterActivity.this,CartActivity.class);
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
//                INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//        return true;
//    }

    //Social login
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i==R.id.sign_in_button){

            signIn();

        }
    }

    public void signIn(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            }

        }else{

            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Toast.makeText(RegisterActivity.this,""+credential.getProvider(),Toast.LENGTH_LONG).show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        getdata();
                      //  Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();
                        if (task.isSuccessful()){

                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));

                            Toast.makeText(RegisterActivity.this,"Successfully loggined",Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(RegisterActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //  startActivity(intent);
                        Toast.makeText(RegisterActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String getdata(){
        String email = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            email = user.getEmail();
           // String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            String fullname = user.getDisplayName();
            String uniqueid= user.getUid()+"google";

            new SocialRegistrationTask().execute(fullname,email,uniqueid);

            Log.d(uniqueid,"Unique id");
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.

            String uid = user.getUid();
        }

        return email;

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            updateUI();
        }


    }

    private void updateUI() {

        Toast.makeText(RegisterActivity.this,"You are logged in",Toast.LENGTH_LONG).show();

        startActivity(new Intent(RegisterActivity.this,MainActivity.class));

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            String fullname = user.getDisplayName();
                            String uniqueid= user.getUid()+"fb";

                            new SocialRegistrationTask().execute(fullname,"",uniqueid);

                            Log.d(uniqueid,"Unique id");

                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }

    public class  SocialRegistrationTask extends AsyncTask<String,Void,String>{

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String JsonResponse = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getApplicationContext(), R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);
            pDialog.setMessage("Registering...");
            //showDialog();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                // Body ->    {"FirstName":"Fred","LastName":"Hakim","Email":"fred.hakim@gemmasuits.com","Password":"fred123","PhoneNo":"1558054560", "pantsWaist":"38"}

                JSONObject regObject = new JSONObject();
                regObject.put("FullName", strings[0]);
                regObject.put("Email", strings[1]);
                regObject.put("Password", "");
                regObject.put("clientID",strings[2]);

                //  {"FullName":"Juju","Email":"juju.hakim@gemmasuits.com","Password":"juju123"}

                URL url = new URL("http://www.bespokino.com/cfc/app.cfc?wsdl&method=CustomerSignup");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                // is output buffer writter
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(String.valueOf(regObject));
                writer.close();

                InputStream inputStream = urlConnection.getInputStream();
                //input stream
                urlConnection.getResponseCode();
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

            if (pDialog != null) {
                pDialog.dismiss();
            }

            if (s != null) {

                try {
                    JSONObject jsonobject = new JSONObject(s);
                    boolean error = jsonobject.getBoolean("Error");
                    if (!error) {


                        session.setRegIn(true);

                        // int modelNo = jsonobject.getInt("modelNo");
                        int userID = jsonobject.getInt("UserId");
                        AppConfig.userID =userID;
                        //AppConfig.modelNo = modelNo;

                        session.setLogin(true);

                        String fullname = jsonobject.getString("FullName");
                        String message = jsonobject.getString("Message");
                        String email = jsonobject.getString("Email");
                        int userid = jsonobject.getInt("UserId");
                        String address = jsonobject.getString("Address");
                        String city = jsonobject.getString("City");
                        String state = jsonobject.getString("State");
                        String phoneumber = jsonobject.getString("PhoneNo");
                        boolean measurment = jsonobject.getBoolean("Measurements");
                        int zip = jsonobject.getInt("Zip");

                        if (measurment){

                            editor.putBoolean("measurment", true);

                        }else{

                            editor.putBoolean("measurment", false);

                        }
                        editor.apply();


                        db.deleteUsers();

                        boolean isInserted =  db.addUser(fullname,address,email,userid,zip,phoneumber,city,state);

                        if(isInserted){
                          //  Toast.makeText(RegisterActivity.this,"Data inserted",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this,
                                    MainActivity.class);

                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(getApplicationContext(),"INSERTION FAILED",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), " SocailRegistration Failed", Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Internel Server error", Toast.LENGTH_LONG).show();

            }


        }
    }

}
