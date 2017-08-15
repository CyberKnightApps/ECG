package com.example.remedy441.ecg.Firebase;

import android.app.ProgressDialog;
import android.os.Build;
import android.util.Log;

import com.example.remedy441.ecg.database.BtpRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Nirbhay on 15-08-2017.
 */

public class Firebase {

    private static FirebaseDatabase database;
    private static DatabaseReference mRef;

    public static void putAllData(ArrayList<BtpRecord> btpRecord){
        String name = Build.SERIAL.toString();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child(name);
        for(int i = 0;i<btpRecord.size();i++){
            BtpRecord record = btpRecord.get(i);
            StringBuilder str = new StringBuilder();
            String temp[] = record.getTime().split(" ");
            str.append(temp[0]+" "+temp[1]+" "+temp[2]);
            Log.e("Firebase***",str.toString());
            mRef.child(str.toString()).child(record.getTime()).child("ECG").setValue(record.getEcg());
            mRef.child(str.toString()).child(record.getTime()).child("PPG").setValue(record.getPpg());
        }
    }

}
