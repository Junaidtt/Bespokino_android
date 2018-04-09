package com.app.bespokino.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bespokino on 9/14/2017 AD.
 */

public class CustomerHelperDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "customer.db";

    public static final String TABLE_NAME = "customer_table";

    private static final int DATABASE_VERSION = 1;

    public static final String COL_1 = "ID";
    public static final String COL_2 = "TRACKINGID";
    public static final String COL_3 = "ORDERID";
    public static final String COL_4 = "CUSTOMERID";
    public static final String COL_5 = "PAPERNO";

    public CustomerHelperDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create table " +TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,TRACKINGID TEXT,ORDERID INTEGER,CUSTOMERID INTEGER,PAPERNO INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {


        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);


    }



    public boolean insertData(String trackingID, int orderNo, int customerID, int paperNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,trackingID);
        contentValues.put(COL_3,orderNo);
        contentValues.put(COL_4,customerID);
        contentValues.put(COL_5,paperNo);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else
            return true;

    }

    public boolean updateData(String id,String trackingID,int orderNo, int customerID, int paperNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,trackingID);
        contentValues.put(COL_3,orderNo);
        contentValues.put(COL_4,customerID);
        contentValues.put(COL_5,paperNo);
        db.update(TABLE_NAME,contentValues,"ID = ?", new String[]{id} );
        return  true;

    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return  res;

    }

    public Integer deleteData(String id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,"ID = ?",new String[]{id});


    }


    public void deleteCustomerInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d("CUSTOMER_DB", "Deleted all user info from sqlite");
    }

}
