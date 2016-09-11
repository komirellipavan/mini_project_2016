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
 * Created by Pavan on 9/10/2016.
 */
public class AddCarCustomListAdapter extends BaseAdapter{

    private ArrayList<AddCarList> listData;
    private LayoutInflater layoutInflater;



    public AddCarCustomListAdapter(Context aContext, ArrayList<AddCarList> listData) {
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
            convertView = layoutInflater.inflate(R.layout.add_car_list_row, null);
            holder = new ViewHolder();
            holder.carNameList = (TextView) convertView.findViewById(R.id.carNameList);
            holder.brandList = (TextView) convertView.findViewById(R.id.brandList);
            holder.madeYearList = (TextView) convertView.findViewById(R.id.madeYearList);
            holder.regNoList = (TextView) convertView.findViewById(R.id.regNoList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.carNameList.setText("Car Name: " + listData.get(position).getName());
        holder.brandList.setText("Brand Name: " + listData.get(position).getBrand());
        holder.madeYearList.setText("Made Year: " + listData.get(position).getMadeyear());
        holder.regNoList.setText("Reg No: " + listData.get(position).getRegno());


        return convertView;
    }

    static class ViewHolder {
        TextView carNameList;
        TextView brandList;
        TextView madeYearList;
        TextView regNoList;

    }

}
