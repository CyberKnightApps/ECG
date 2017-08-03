package com.example.remedy441.ecg;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Dhruv on 30-Mar-17.
 */

public class ChartsAdapter extends RecyclerView.Adapter<ChartsAdapter.ChartsViewHolder>  {

    private Context mContext;
    private ArrayList<Charts> dataList;
    //ecg ppg ecg/ppg
    private int upperLimit[]={1500,1500,1500};
    private int lowerLimit[]={0,0,0};



    public ChartsAdapter(Context mContext, ArrayList<Charts> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;

    }

    @Override
    public ChartsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_chart,parent,false);

        ChartsViewHolder holder = new ChartsViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ChartsViewHolder holder, int position) {

        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "OpenSans-Regular.ttf");

        LimitLine ll1 = new LimitLine(upperLimit[position], "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        ll1.setTypeface(tf);
        //Log.d("hhh",pos+" "+this.pos);
        LimitLine ll2 = new LimitLine(lowerLimit[position], "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        ll2.setTypeface(tf);

        //holder.setPosition(position);
        //Log.d("hh",position+" ");
        YAxis leftAxis = holder.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(upperLimit[position]);
        leftAxis.setAxisMinimum(lowerLimit[position]);
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        holder.txtTitle1.setText(dataList.get(position).getTitle1());
        holder.txtTitle2.setText(dataList.get(position).getTitle2());

        ArrayList<Float>arrayList =new ArrayList<>();
        arrayList.addAll(dataList.get(position).getBigDataList());


        setData(holder.mChart,arrayList);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    void setData(LineChart mChart,ArrayList<Float>data1)
    {
        ArrayList<Entry> values = new ArrayList<Entry>();
        int l=data1.size();
        for(int i=0;i<l;i++)
        {

            values.add(new Entry(i, data1.get(i), mContext.getResources().getDrawable(R.drawable.star)));
        }
        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Values");

            //set1.setDrawIcons(false);


            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.GREEN);
            set1.setDrawCircles(false);

            set1.setLineWidth(1f);


            //set1.setValueTextSize(9f);
            set1.setValueTextColor(Color.WHITE);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.WHITE);
            /*if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(mContext, R.color.green_400);
                set1.setFillDrawable(drawable);
            }
            else {
                set1.setFillColor(Color.BLACK);
            }*/

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }

        mChart.animateX(100);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend xl = mChart.getLegend();

        // modify the legend ...
       xl.setForm(Legend.LegendForm.LINE);
    }


    class ChartsViewHolder extends RecyclerView.ViewHolder implements OnChartGestureListener,OnChartValueSelectedListener{


        TextView txtTitle1,txtTitle2;
        LineChart mChart;

        public ChartsViewHolder(View itemView) {
            super(itemView);

            txtTitle1=(TextView)itemView.findViewById(R.id.tvTitle);
            txtTitle2=(TextView)itemView.findViewById(R.id.tvDescription);

            mChart=(LineChart)itemView.findViewById(R.id.chMyChart1);

            mChart.setOnChartGestureListener(this);
            mChart.setOnChartValueSelectedListener(this);
            mChart.setDrawGridBackground(false);
            // no description text
            mChart.getDescription().setEnabled(false);
            // enable touch gestures
            mChart.setTouchEnabled(true);
            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            // mChart.setScaleXEnabled(true);
            // mChart.setScaleYEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(true);
            // set an alternative background color
            // mChart.setBackgroundColor(Color.GRAY);
            LimitLine llXAxis = new LimitLine(10f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);

            XAxis xAxis = mChart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);



            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(false);

            // limit lines are drawn behind data (and not on top)
            leftAxis.setDrawLimitLinesBehindData(true);

            mChart.getAxisRight().setEnabled(false);

        }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }

        @Override
        public void onValueSelected(Entry e, Highlight h) {

        }

        @Override
        public void onNothingSelected() {

        }
    }
}
