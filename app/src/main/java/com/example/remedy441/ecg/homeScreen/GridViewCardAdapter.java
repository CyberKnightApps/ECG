package com.example.remedy441.ecg.homeScreen;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remedy441.ecg.R;

import java.util.ArrayList;

/**
 * Created by Parth on 30-03-2017.
 * Project btapp.
 */

public class GridViewCardAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<OverviewValues> params;
    private static LayoutInflater inflater = null;

    public GridViewCardAdapter(Context mContext, ArrayList<OverviewValues> params){
        this.mContext = mContext;
        this.params = params;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return params.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.param_item, null);

            holder.card = (CardView) view.findViewById(R.id.param_item_card);
            holder.type = (TextView) view.findViewById(R.id.param_item_type);
            holder.icon = (ImageView) view.findViewById(R.id.param_item_icon);
            holder.current = (TextView) view.findViewById(R.id.param_item_current_val);
            holder.min = (TextView) view.findViewById(R.id.param_item_min_val);
            holder.max = (TextView) view.findViewById(R.id.param_item_max_val);

            holder.type.setText(params.get(position).getType());
            holder.type.setBackgroundColor(ContextCompat.getColor(mContext, params.get(position).getPrimaryDarkColor()));
            holder.card.setCardBackgroundColor(ContextCompat.getColor(mContext, params.get(position).getPrimaryColor()));
            holder.icon.setImageResource(params.get(position).getImageId());
            holder.current.setText(String.valueOf(params.get(position).getCurrentVal()));
            holder.min.setText(String.valueOf(params.get(position).getMinVal()));
            holder.max.setText(String.valueOf(params.get(position).getMaxVal()));
        }

        return view;
    }

    class ViewHolder{
        CardView card;
        ImageView icon;
        TextView current, max, min, type;
    }
}
