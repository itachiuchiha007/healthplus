package com.anurag.healthplus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.anurag.healthplus.other.Constants;

/**
 * Created by baji on 15/2/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    // All Static variables
    // Database Version
    SQLiteDatabase db;
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "healthplus.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PATIENT_TABLE = " CREATE TABLE " +
                Constants.TABLE_PATIENT +
                "( " + Constants.COL_ID + " INTEGER PRIMARY KEY, "+
                Constants.COL_PATIENT_ID + " INTEGER, "+
                Constants.COL_PATIENT_NAME + " TEXT )";
        Log.d("database", CREATE_PATIENT_TABLE);

        String CREATE_REPORT_TABLE = " CREATE TABLE " +
                Constants.TABLE_REPORT +
                "( " + Constants.COL_ID + " INTEGER, " +
                Constants.COL_PATIENT_ID + " INTEGER, "+
                Constants.COL_PATIENT_NAME + " TEXT, " +
                Constants.COL_APPT_ID + " INTEGER PRIMARY KEY, " +
                Constants.COL_DOCTOR_NAME + " TEXT, " +
                Constants.COL_TREATMENT + " TEXT)";

        String CREATE_APPT_TABLE = " CREATE TABLE " +
                Constants.TABLE_APPT +
                "( " + Constants.COL_ID + " INTEGER, " +
                Constants.COL_PATIENT_ID + " INTEGER, "+
                Constants.COL_PATIENT_NAME + " TEXT, " +
                Constants.COL_APPT_ID + " INTEGER PRIMARY KEY, " +
                Constants.COL_DOCTOR_NAME + " TEXT, " +
                Constants.COL_DATE + " DATE, " +
                Constants.COL_TIME + " TIME, " +
                Constants.COL_STATUS + " TEXT)";

        db.execSQL(CREATE_PATIENT_TABLE);
        db.execSQL(CREATE_REPORT_TABLE);
        db.execSQL(CREATE_APPT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
