package com.app.bespokino.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by sunilvg on 17/07/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {


    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_ZIPCODE = "zip";
    private static final String KEY_PHONENUMBER = "phone";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FULLNAME + " TEXT," + KEY_ADDRESS + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_ZIPCODE + " TEXT," + KEY_PHONENUMBER + " TEXT," + KEY_CITY + " TEXT,"
                + KEY_STATE + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public boolean addUser(String fname,String address, String email, int uid,int zipcode, String phone,String city,String state) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FULLNAME, fname); // Name
        values.put(KEY_ADDRESS, address); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // userid
        values.put(KEY_ZIPCODE, zipcode); // Email
        values.put(KEY_PHONENUMBER, phone); // phone
        values.put(KEY_CITY, city); // city
        values.put(KEY_STATE, state); //state

        // Inserting Row
        long result = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

       // Log.d(TAG, "New user inserted into sqlite: " + id);

        if(result == -1){
            return false;
        }else
            return true;
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("firstname", cursor.getString(1));
            user.put("lastname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uid", cursor.getString(4));
            user.put("password", cursor.getString(5));
            user.put("phone", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_USER,null);
        return  res;

    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


}
