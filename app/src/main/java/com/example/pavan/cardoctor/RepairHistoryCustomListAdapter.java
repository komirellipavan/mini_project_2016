package com.example.pavan.cardoctor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Pavan on 9/25/2016.
 */
public class RepairHistoryCustomListAdapter extends BaseAdapter {
    private ArrayList<RepairHistoryList> listData;
    private LayoutInflater layoutInflater;



    public RepairHistoryCustomListAdapter(Context aContext, ArrayList<RepairHistoryList> listData) {
        this.listData = listData;
        Log.i("",listData.toString() );
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.repair_histroy_list_row, null);
            holder = new ViewHolder();
            holder.carname = (TextView) convertView.findViewById(R.id.repair_list_carname);
            holder.regno = (TextView) convertView.findViewById(R.id.repair_list_regno);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carname.setText("Car : " + listData.get(position).getCarname());
        holder.regno.setText("RegNo : " + listData.get(position).getRegno());

        return convertView;
    }

    static class ViewHolder {
        TextView carname ;
        TextView regno;
    }

}
