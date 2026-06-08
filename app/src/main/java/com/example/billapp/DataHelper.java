package com.example.billapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electricitybill.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE bill (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "month TEXT NOT NULL," +
                "unit INTEGER NOT NULL," +
                "totalCharge REAL NOT NULL," +
                "rebate REAL NOT NULL," +
                "finalCost REAL NOT NULL)";

        Log.d("Data", "onCreate: " + sql);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS bill");
        onCreate(db);
    }
}