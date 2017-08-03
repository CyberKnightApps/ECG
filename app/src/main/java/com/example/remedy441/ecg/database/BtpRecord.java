package com.example.remedy441.ecg.database;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Parth on 04-03-2017.
 * Project btapp.
 */

public class BtpRecord {
    private long id;
    private String time;
    private String ecg;
    private String ppg;
    private Date tmpDate;

    public BtpRecord(String to_store){
        to_store = to_store.trim();
        String s[] = to_store.split(";");
        tmpDate = Calendar.getInstance().getTime();
        time = tmpDate.toString();
        ecg = s[0];
        ppg = s[1];

    }

    public BtpRecord(){}


    public String getDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(tmpDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getPpg() {
        return ppg;
    }

    public void setPpg(String ppg) {
        this.ppg = ppg;
    }
}
