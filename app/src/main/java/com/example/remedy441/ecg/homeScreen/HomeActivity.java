package com.example.remedy441.ecg.homeScreen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.remedy441.ecg.MainActivity;
import com.example.remedy441.ecg.R;
import com.example.remedy441.ecg.database.BtpDbSource;
import com.example.remedy441.ecg.database.BtpRecord;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    GridView gridView;
    BtpDbSource database;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String check = null;
        Intent i = getIntent();
        check = i.getStringExtra("Check");
        if (check != null)
            flag = true;

        database = new BtpDbSource(getApplicationContext());
        ArrayList<OverviewValues> list = getOverviewsFromRecords(database.averageOfLastRecords());

        gridView = (GridView) findViewById(R.id.activity_home_grid_view);
        GridViewCardAdapter adapter = new GridViewCardAdapter(this, list);
        gridView.setAdapter(adapter);
    }

    public ArrayList<OverviewValues> getOverviewsFromRecords(ArrayList<BtpRecord> records){
        ArrayList<OverviewValues> list = new ArrayList<>();
        list.add(new OverviewValues("SBP",Double.parseDouble(records.get(1).getEcg()),
                Double.parseDouble(records.get(0).getEcg()),Double.parseDouble(records.get(2).getEcg())));
        list.add(new OverviewValues("DBP",Double.parseDouble(records.get(1).getPpg()),
                Double.parseDouble(records.get(0).getPpg()),Double.parseDouble(records.get(2).getPpg())));
        list.add(new OverviewValues("HR",Double.parseDouble(records.get(1).getPpg()),
                Double.parseDouble(records.get(0).getPpg()),Double.parseDouble(records.get(2).getPpg())));
        list.add(new OverviewValues("Temperature",Double.parseDouble(records.get(1).getPpg()),
                Double.parseDouble(records.get(0).getPpg()),Double.parseDouble(records.get(2).getPpg())));
        return list;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next:
                if (!flag){
                    startActivity(new Intent(HomeActivity.this,MainActivity.class));
                }
                else{
                    super.onBackPressed();
                }
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
