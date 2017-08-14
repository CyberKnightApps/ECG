package com.example.remedy441.ecg.homeScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.remedy441.ecg.R;
import com.example.remedy441.ecg.database.BtpDbSource;
import com.example.remedy441.ecg.database.BtpRecord;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    GridView gridView;
    BtpDbSource database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = new BtpDbSource(getApplicationContext());
        ArrayList<OverviewValues> list = getOverviewsFromRecords(database.averageOfLastRecords());

        gridView = (GridView) findViewById(R.id.activity_home_grid_view);
        GridViewCardAdapter adapter = new GridViewCardAdapter(this, list);
        gridView.setAdapter(adapter);
    }

    public ArrayList<OverviewValues> getOverviewsFromRecords(ArrayList<BtpRecord> records){
        ArrayList<OverviewValues> list = new ArrayList<>();
        list.add(new OverviewValues("Temperature",Double.parseDouble(records.get(1).getEcg()),
                Double.parseDouble(records.get(0).getEcg()),Double.parseDouble(records.get(2).getEcg())));
        list.add(new OverviewValues("Pressure",Double.parseDouble(records.get(1).getPpg()),
                Double.parseDouble(records.get(0).getPpg()),Double.parseDouble(records.get(2).getPpg())));
        return list;
    }
}
