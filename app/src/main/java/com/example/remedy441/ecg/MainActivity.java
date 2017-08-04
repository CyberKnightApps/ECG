package com.example.remedy441.ecg;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.remedy441.ecg.Bluetooth.Select;
import com.example.remedy441.ecg.database.BtpContract;
import com.example.remedy441.ecg.database.BtpDbHelper;
import com.example.remedy441.ecg.database.BtpDbSource;
import com.example.remedy441.ecg.database.BtpRecord;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChartsAdapter adapter;
    private ArrayList<Charts> chartsArrayList;
    private int num_of_values = 2;
    private Queue<Float> values[] = new Queue[num_of_values];
    private Context mContext;
    BtpDbSource bds;

    private int mInterval = 3; // 5 seconds by default, can be changed later
    private Handler mHandler;

    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String check = null;
        Intent i = getIntent();
        check = i.getStringExtra("Check");
        if (check != null)
            flag = true;

        mContext = getApplicationContext();

        bds = new BtpDbSource(mContext);
        bds.open();

        for(int ii=0; ii<num_of_values; ii++)  values[ii] = new LinkedList<>();

        pushContentsToRecyclerView();
        bds.close();

        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:

                if (Build.VERSION.SDK_INT >= 23) {
                    //do your check here
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        saveCsv();
                    }
                }
                else
                    saveCsv();
                return true;
            case R.id.action_connect:
                if (!flag)
                    startActivity(new Intent(MainActivity.this, Select.class));
                else {
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
    public void updateRecords() {


        while(values[0].size()>85){
            values[0].poll();

        }
        while(values[1].size()>85){
            values[1].poll();

        }
        Queue<BtpRecord> q = RecordCollector.getBtpRecords();
        while (!q.isEmpty()) {
            BtpRecord x = q.poll();
            try {
                values[0].add(Float.parseFloat(x.getEcg()));
                values[1].add(Float.parseFloat(x.getPpg()));

                Log.e("LineCharActivity", x.getEcg() + " " + x.getPpg() + " -------------------------------------- ");

                bds.addRecord(x);


            } catch (Exception e) {

            }
        }
        for (int i = 0; i < num_of_values; i++) {
            if (values[i].isEmpty()) {
                values[i].add(500f);
                values[i].add(750f);
                values[i].add(500f);
                values[i].add(750f);
                values[i].add(500f);
            }
        }
    }

    void pushContentsToRecyclerView()
    {

        updateRecords();

        chartsArrayList=new ArrayList<>();

        chartsArrayList.add(new Charts("ECG","Measure ECG",values[0]));
        chartsArrayList.add(new Charts("PPG","Measure PPG",values[1]));
        //chartsArrayList.add(new Charts("ECG/PPG","Measure ECG/PPG",values[2]));

        recyclerView = (RecyclerView) findViewById(R.id.rvCharts);

        adapter = new ChartsAdapter(this,chartsArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

    }

    private void saveCsv() {

        BtpDbHelper dbhelper = new BtpDbHelper(getApplicationContext());
        File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "heart.csv");
        if(file.exists())
            file.delete();
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ BtpContract.BtpEntry.TABLE_NAME,null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2),
                        curCSV.getString(3)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();

            Toast.makeText(MainActivity.this,"Saved Successfully to Downloads folder",Toast.LENGTH_SHORT).show();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {

                updateRecords();
                //pushContentsToRecyclerView(); //this function can change value of mInterval.
                adapter.notifyDataSetChanged();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }



}
