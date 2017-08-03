package com.example.remedy441.ecg;

import com.example.remedy441.ecg.database.BtpRecord;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Nirbhay on 10-04-2017.
 */

public class RecordCollector {

    private static Queue<BtpRecord> btpRecords = new LinkedList<BtpRecord>();

    public static void addRecord(BtpRecord record){
        btpRecords.add(record);
    }



    public static Queue<BtpRecord> getBtpRecords(){
        return btpRecords;
    }




}
