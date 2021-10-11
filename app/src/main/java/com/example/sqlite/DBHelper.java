package com.example.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final  int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "teaDb";
    public static final String TABLE_TEA = "tea";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_TASTE = "taste";
    public static final String KEY_PRICE = "price";

    public  DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TEA + "(" +
                KEY_ID + " integer primary key," +
                KEY_NAME + " text," +
                KEY_COUNTRY + " text," +
                KEY_TASTE + " text," +
                KEY_PRICE + " text" +")");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_TEA);

        onCreate(db);

    }

}
