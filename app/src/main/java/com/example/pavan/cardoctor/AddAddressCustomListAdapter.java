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
 * Created by Pavan on 9/11/2016.
 */
public class AddAddressCustomListAdapter extends BaseAdapter {
    private ArrayList<AddAddressList> listData;
    private LayoutInflater layoutInflater;



    public AddAddressCustomListAdapter(Context aContext, ArrayList<AddAddressList> listData) {
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
            convertView = layoutInflater.inflate(R.layout.add_address_list_row, null);
            holder = new ViewHolder();
            holder.address_IDList = (TextView) convertView.findViewById(R.id.address_IDList);
            holder.countryList = (TextView) convertView.findViewById(R.id.countryList);
            holder.stateList = (TextView) convertView.findViewById(R.id.stateList);
            holder.cityList = (TextView) convertView.findViewById(R.id.cityList);
            holder.subregionList = (TextView) convertView.findViewById(R.id.subregionList);
            holder.streetAddressList = (TextView) convertView.findViewById(R.id.streetAddressList);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.address_IDList.setText("Address ID: " + listData.get(position).getId());
        holder.countryList.setText("Country: " + listData.get(position).getCountry());
        holder.stateList.setText("State: " + listData.get(position).getState());
        holder.cityList.setText("City: " + listData.get(position).getCity());
        holder.subregionList.setText("Sub Region: " + listData.get(position).getSubregion());
        holder.streetAddressList.setText("Street Address: " + listData.get(position).getSteetaddress());


        return convertView;
    }

    static class ViewHolder {
        TextView address_IDList ;
        TextView countryList;
        TextView stateList;
        TextView cityList;
        TextView subregionList;
        TextView streetAddressList;

    }

}
