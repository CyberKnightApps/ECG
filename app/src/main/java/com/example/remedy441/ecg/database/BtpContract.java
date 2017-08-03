package com.example.remedy441.ecg.database;

import android.provider.BaseColumns;

/**
 * Created by Parth on 04-03-2017.
 * Project btapp.
 */

public final class BtpContract {

    static final String SQL_CREATE_BTP_TABLE =
            "CREATE TABLE IF NOT EXISTS " + BtpEntry.TABLE_NAME
                    + " ("
                    + BtpEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + BtpEntry.COLUMN_TIME + " VARCHAR (30) ,"
                    + BtpEntry.COLUMN_ECG + " REAL , "
                    + BtpEntry.COLUMN_PPG + " REAL "
                    + ")";

    static final String SQL_DELETE_BTP_TABLE = "DROP TABLE IF EXISTS " + BtpEntry.TABLE_NAME;

    private BtpContract() {}

    public static class BtpEntry implements BaseColumns {
        public static final String TABLE_NAME = "HeartTable";
        public static final String COLUMN_TIME = "Time";
        public static final String COLUMN_ECG = "ECG";
        public static final String COLUMN_PPG = "PPG";


    }
}