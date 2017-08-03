package com.example.remedy441.ecg;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by Dhruv on 30-Mar-17.
 */

public class Charts {
    private String title1;
    private String title2;
    private Queue<Float> bigDataList;

    public Charts(String title1, String title2, Queue<Float> bigDataList) {
        this.title1 = title1;
        this.title2 = title2;
        this.bigDataList = bigDataList;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public Queue<Float> getBigDataList() {
        return bigDataList;
    }

    public void setBigDataList(Queue<Float> bigDataList) {
        this.bigDataList = bigDataList;
    }
}
