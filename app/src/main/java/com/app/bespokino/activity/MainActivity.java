package com.app.bespokino.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.app.Controller;
import com.app.bespokino.fragment.How_it_worksFragment;
import com.app.bespokino.fragment.RateAppFragment;
import com.app.bespokino.fragment.TabFragment;
import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.ConnectivityReceiver;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.SQLiteHandler;
import com.app.bespokino.helper.SessionManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,GoogleApiClient.OnConnectionFailedListener  {

    //properties
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager FM;
    private FragmentTransaction FT;

    static TextView textCartItemCount;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    private SQLiteHandler db;
    private SessionManager session;
    private SQLiteHandler sqLiteHandler;
    private TextView tvUserName;
    CustomerHelperDB customerHelperDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkConnection();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.shitstuff);


        FM = getSupportFragmentManager();
        FT = FM.beginTransaction();
        FT.replace(R.id.containerView, new TabFragment()).commit();

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        customerHelperDB = new CustomerHelperDB(this);

        // session manager
        session = new SessionManager(getApplicationContext());

        sqLiteHandler = new SQLiteHandler(this);

        View header=navigationView.getHeaderView(0);
        tvUserName = (TextView)header.findViewById(R.id.userName);
        ImageView imageView = (ImageView)header.findViewById(R.id.verifiedTick);

        Cursor res =  sqLiteHandler.getAllData();

        while (res.moveToNext()){


            String fullname = res.getString(1);

            tvUserName.setText(fullname);
            imageView.setImageResource(R.drawable.verified);

        }


//        Menu menu = navigationView.getMenu();
//
//        MenuItem tools= menu.findItem(R.id.tools);
//        SpannableString s = new SpannableString(tools.getTitle());
//        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
//        tools.setTitle(s);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                if (item.getItemId() == R.id.nav_home) {
                    FragmentTransaction fragmentTransaction = FM.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, new TabFragment()).commit();
                }

                if (item.getItemId() == R.id.nav_how_it_works) {
                    FragmentTransaction fragmentTransaction1 = FM.beginTransaction();
                    fragmentTransaction1.replace(R.id.containerView, new How_it_worksFragment()).commit();

                }
                if (item.getItemId() == R.id.nav_login) {

                    //FragmentTransaction fragmentTransaction2 = FM.beginTransaction();
                    //fragmentTransaction2.replace(R.id.containerView, new RateAppFragment()).commit();
                    Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                    }


                }
                if (item.getItemId() == R.id.nav_tool) {

                    /* FragmentTransaction fragmentTransaction3 = FM.beginTransaction();
                    fragmentTransaction3.replace(R.id.containerView, new RateAppFragment()).commit();*/

                   Intent intent = new Intent(MainActivity.this,BespokinoProfile.class);
                   startActivity(intent);


                } if (item.getItemId() == R.id.logout_tool) {

                    signOut();
                    AppConfig.mCartItemCount = 0;
                    session.setLogin(false);
                    session.setRegIn(false);
                    customerHelperDB.deleteCustomerInfo();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("measurment");
                    editor.commit();

                    logoutUser();

                }
                return false;
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        } else {
            toggle.getDrawerArrowDrawable().setColor(Color.BLACK);

        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(MainActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

    }
    public void signOut() {
        // Firebase sign out
        mAuth.signOut();


//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
////                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
////                        startActivity(intent);
//                        Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
        this.finish();
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
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void setupBadge() {

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



    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {
            setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.internet_screen);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBadge();
        Controller.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();


        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
