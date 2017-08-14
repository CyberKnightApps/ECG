package com.example.remedy441.ecg.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Parth on 21-07-2017.
 * Project btapp.
 */

public class BtpDbSource {
    private static final String FILE_NAME = "PrefFile";
    private SQLiteDatabase readDatabase, writeDatabase;
    private BtpDbHelper dbHelper;
    private Context context;
    private String columns[] = {
            BtpContract.BtpEntry._ID,
            BtpContract.BtpEntry.COLUMN_TIME,
            BtpContract.BtpEntry.COLUMN_ECG,
            BtpContract.BtpEntry.COLUMN_PPG,

    };

    public BtpDbSource(Context context){
        dbHelper = new BtpDbHelper(context);
        this.context = context;
        open();
    }

    public void open() throws SQLException {
        writeDatabase = dbHelper.getWritableDatabase();
        readDatabase = dbHelper.getReadableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public BtpRecord addRecord(BtpRecord record){
        ContentValues values = new ContentValues();

        values.put(BtpContract.BtpEntry.COLUMN_TIME, record.getTime());
        values.put(BtpContract.BtpEntry.COLUMN_ECG, record.getEcg());
        values.put(BtpContract.BtpEntry.COLUMN_PPG, record.getPpg());

        record.setId(writeDatabase.insert(BtpContract.BtpEntry.TABLE_NAME, null, values));
        return record;
    }

    public ArrayList<BtpRecord> getAllRecords(){
        ArrayList<BtpRecord> records = new ArrayList<>();

        Cursor cursor = readDatabase.query(BtpContract.BtpEntry.TABLE_NAME, columns, null, null, null, null, null);
        //database.query()
        if(cursor.moveToFirst()){
            do{
                records.add(cursorToRecord(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return records;
    }

    public ArrayList<BtpRecord> getAllRecordsToUpload(){
        ArrayList<BtpRecord> records = new ArrayList<>();

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        long id = pref.getLong("id",0);

        String query = "SELECT * FROM " + BtpContract.BtpEntry.TABLE_NAME +
                " WHERE " + BtpContract.BtpEntry._ID + " > " + id +
                " ORDER BY " + BtpContract.BtpEntry._ID + " ASC";

        Cursor c = readDatabase.rawQuery(query, null);

        if(c.moveToFirst()){
            do{
                records.add(cursorToRecord(c));
            }while(c.moveToNext());
        }

        if(records.size()>0) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong("id", records.get(records.size()-1).getId());
            editor.apply();
        }

        return records;
    }

    public ArrayList<BtpRecord> averageOfLastRecords(){
        String query = "SELECT * FROM " + BtpContract.BtpEntry.TABLE_NAME +
                " ORDER BY " + BtpContract.BtpEntry._ID + " DESC LIMIT " + 10;

        Cursor c = readDatabase.rawQuery(query, null);

        BtpRecord min = BtpRecord.newMinInstance();
        BtpRecord max = BtpRecord.newMaxInstance();
        BtpRecord avg = new BtpRecord();
        int cnt = 0;
        if(c.moveToFirst()){
            do{
                BtpRecord tmp = cursorToRecord(c);
                avg.setEcg(String.valueOf(Double.parseDouble(avg.getEcg())+Double.parseDouble(tmp.getEcg())));
                if(Double.parseDouble(tmp.getEcg())>Double.parseDouble(max.getEcg())){
                    max.setEcg(tmp.getEcg());
                }
                if(Double.parseDouble(tmp.getEcg())<Double.parseDouble(min.getEcg())){
                    min.setEcg(tmp.getEcg());
                }
                avg.setPpg(String.valueOf(Double.parseDouble(avg.getPpg())+Double.parseDouble(tmp.getPpg())));
                if(Double.parseDouble(tmp.getPpg())>Double.parseDouble(max.getPpg())){
                    max.setPpg(tmp.getPpg());
                }
                if(Double.parseDouble(tmp.getPpg())<Double.parseDouble(min.getPpg())){
                    min.setPpg(tmp.getPpg());
                }
                cnt++;
            }while(c.moveToNext());
        }

        if(cnt>0) {
            avg.setEcg(String.valueOf(Double.parseDouble(avg.getEcg()) / cnt));
            avg.setPpg(String.valueOf(Double.parseDouble(avg.getPpg()) / cnt));
        }
        else{
            min = max = new BtpRecord();
        }

        ArrayList<BtpRecord> list = new ArrayList<>();
        list.add(min);
        list.add(avg);
        list.add(max);

        return list;
    }

    private BtpRecord cursorToRecord(Cursor cursor){
        BtpRecord record = new BtpRecord();
        record.setId(cursor.getLong(cursor.getColumnIndex(columns[0])));
        record.setTime(cursor.getString(cursor.getColumnIndex(columns[1])));
        record.setEcg(cursor.getString(cursor.getColumnIndex(columns[2])));
        record.setPpg(cursor.getString(cursor.getColumnIndex(columns[3])));

        return record;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
