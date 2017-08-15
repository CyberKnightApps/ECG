package com.example.remedy441.ecg.homeScreen;

import com.example.remedy441.ecg.R;

/**
 * Created by Parth on 30-03-2017.
 * Project btapp.
 */

public class OverviewValues {
    private int imageId;
    private int primaryColor;
    private int primaryDarkColor;
    private double currentVal;
    private double minVal;
    private double maxVal;
    private String type;

    public OverviewValues(){
        type = "";
    }

    public OverviewValues(String type, double currentVal, double minVal, double maxVal) {
        this.currentVal = currentVal;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.type = type;
        applyColorAndIcon();
    }

    private void applyColorAndIcon(){
        switch(type){
            case "SBP":
                double avgTemp = (maxVal+minVal)/2;
                imageId = DrawableIds.COLD;
                primaryColor = R.color.veryColdPrimary;
                primaryDarkColor = R.color.veryColdPrimaryDark;
                break;
            case "DBP":
                double avgPress = (maxVal+minVal)/2;
                imageId = DrawableIds.PRESSURE;
                primaryColor = R.color.green_300;
                primaryDarkColor = R.color.green_400;
                break;
            case "HR":
                double avgHumidity = (maxVal+minVal)/2;
                imageId = DrawableIds.HUMIDITY;
                primaryColor = R.color.light_blue_400;
                primaryDarkColor = R.color.light_blue_500;
                break;
            case "Temperature":
                double avgSpeed = (maxVal+minVal)/2;
                imageId = DrawableIds.WIND_SPEED;
                primaryColor = R.color.teal_400;
                primaryDarkColor = R.color.teal_500;
                break;
            default:
                imageId = DrawableIds.NOT_APPLICABLE;
                primaryColor = R.color.grey_100;
                primaryDarkColor = R.color.grey_200;
        }
    }

    public int getImageId() {
        return imageId;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public double getCurrentVal() {
        return currentVal;
    }

    public double getMinVal() {
        return minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public String getType() {
        return type;
    }
}
