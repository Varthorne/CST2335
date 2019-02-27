package com.example.androidlabs;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "DatabaseFile";
    public static final int VERSION_NUMBER = 1;
    public static final String TABLE_NAME = "Messages";
    public static final String COL_ID = "id";
    public static final String COL_MESSAGE = "message";
    public static final String COL_BOOLEAN = "wasSent";


    public DBOpenHelper(Activity context){
        super(context, DATABASE_NAME, null, VERSION_NUMBER);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME +"( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_MESSAGE + "TEXT " +
                COL_BOOLEAN  + "flag INTEGER DEFAULT 0 )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        Log.i("Database upgrade", "Old version: " + oldVersion + " new Version: " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);

        onCreate(db);
    }
}
