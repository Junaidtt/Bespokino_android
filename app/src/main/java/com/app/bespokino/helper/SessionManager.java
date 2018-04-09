package com.app.bespokino.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by sunilvg on 17/07/17.
 */

public class SessionManager {

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BespokinoLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_SIGNUP = "isSignUp";
    private static final String KEY_IS_MEASURMENT = "isMeasure";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }


    public void setRegIn(boolean isRegin) {

        editor.putBoolean(KEY_IS_SIGNUP, isRegin);

        // commit changes
        editor.commit();

        Log.d(TAG, "User reg session modified!");
    }

    public void isCheckMeasurment(boolean isMeasurment) {

        editor.putBoolean(KEY_IS_MEASURMENT, isMeasurment);

        // commit changes
        editor.commit();

        Log.d(TAG, "User measure session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public boolean isRegIn(){
        return pref.getBoolean(KEY_IS_SIGNUP, false);
    }

    public boolean isCheckMeasurment(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

}
