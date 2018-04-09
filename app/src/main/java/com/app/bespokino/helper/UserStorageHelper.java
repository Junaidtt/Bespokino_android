package com.app.bespokino.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bespokino on 10/14/2017 AD.
 */

public class UserStorageHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "user.db";

    public static final String TABLE_NAME = "user_table";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "ADDRESS";
    public static final String COL_5 = "CITY";
    public static final String COL_6 = "STATE";
    public static final String COL_7 = "ZIPCODE";
    public static final String COL_8 = "COUNTRY";
    public static final String COL_9 = "PAIDBYCC";
    public static final String COL_10 = "ORDERNO";
    public static final String COL_11 = "CUSTOMERID";
    public static final String COL_12 = "PAPERNUMBER";

    public UserStorageHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " +TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FIRSTNAME TEXT,LASTNAME TEXT,ADDRESS TEXT,CITY TEXT,STATE TEXT,ZIPCODE TEXT,COUNTRY TEXT,PAIDBYCC TEXT,ORDERNO TEXT,CUSTOMERID TEXT,PAPERNUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String fistname,String lastname,String address,String city,String state,String zipcode,String country,String paidByCC,String orderNo,String customerID,String paperNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,fistname);
        contentValues.put(COL_3,lastname);
        contentValues.put(COL_4,address);
        contentValues.put(COL_5,city);
        contentValues.put(COL_6,state);
        contentValues.put(COL_7,zipcode);
        contentValues.put(COL_8,country);
        contentValues.put(COL_9,paidByCC);
        contentValues.put(COL_10,orderNo);
        contentValues.put(COL_11,customerID);
        contentValues.put(COL_12,paperNo);

        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){
            return false;
        }else
            return true;

    }
    public boolean updateData(String id,String fistname,String lastname,String address,String city,String state,String zipcode,String country,String paidByCC,String orderNo,String customerID,String paperNo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,fistname);
        contentValues.put(COL_3,lastname);
        contentValues.put(COL_4,address);
        contentValues.put(COL_5,city);
        contentValues.put(COL_6,state);
        contentValues.put(COL_7,zipcode);
        contentValues.put(COL_8,country);
        contentValues.put(COL_9,paidByCC);
        contentValues.put(COL_10,orderNo);
        contentValues.put(COL_11,customerID);
        contentValues.put(COL_12,paperNo);
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
}
