package com.app.bespokino.app;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.bespokino.helper.AppConfig;
import com.app.bespokino.helper.ConnectivityReceiver;
import com.app.bespokino.helper.CustomerHelperDB;
import com.app.bespokino.helper.FontsOverride;
import com.app.bespokino.helper.SessionManager;
import com.app.bespokino.helper.UserStorageHelper;

import java.io.File;

/**
 * Created by bespokino on 7/28/2017 AD.
 */

public class Controller extends Application {

    public static Controller mInstance;

    private CustomerHelperDB custDB;
    private UserStorageHelper userStorageHelper;
    private SessionManager session;


    @Override
    public void onCreate() {
        super.onCreate();

        checkInternetConnection();

        mInstance = this;
        custDB = new CustomerHelperDB(this);
        userStorageHelper = new UserStorageHelper(this);

        clearDB();
        session = new SessionManager(getApplicationContext());

//        session.setLogin(false);
//        session.setRegIn(false);
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/univers-next-pro-regular-594ab1cd30a75.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/univers-next-pro-regular-594ab1cd30a75.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/univers-next-pro-regular-594ab1cd30a75.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/univers-next-pro-regular-594ab1cd30a75.ttf");

    }

    private void clearDB() {


        context().deleteDatabase(custDB.DATABASE_NAME);
        context().deleteDatabase(userStorageHelper.DATABASE_NAME);


    }

    private void checkInternetConnection() {


    }

    public static synchronized Controller getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private boolean isNetworkAvailable() {
        // Using ConnectivityManager to check for Network Connection
        ConnectivityManager connectivityManager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public Context context(){
        return mInstance.getApplicationContext();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        deleteCache(getApplicationContext());

    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}