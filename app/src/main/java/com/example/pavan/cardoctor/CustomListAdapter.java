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
 * Created by Pavan on 9/8/2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private ArrayList<ServiceProviderList> listData;
    private LayoutInflater layoutInflater;



    public CustomListAdapter(Context aContext, ArrayList<ServiceProviderList> listData) {
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
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();
            holder.shopname = (TextView) convertView.findViewById(R.id.shopname);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.shopname.setText(listData.get(position).getShopName());
        holder.address.setText("Address: " + listData.get(position).getStreet() + ","+listData.get(position).getSubregion() +
                ","+ listData.get(position).getCity() + ","+listData.get(position).getState() + ","+listData.get(position).getCountry());

        return convertView;
    }

    static class ViewHolder {
        TextView shopname;
        TextView address;

    }



}
