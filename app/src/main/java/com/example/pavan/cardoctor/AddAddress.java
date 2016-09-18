package com.example.pavan.cardoctor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AddAddress extends AppCompatActivity {

    private boolean noResult;
    ListView listview;
    String emailId;
    ArrayList<AddAddressList> results;
    AddAddressCustomListAdapter  addCLA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        emailId = sharedpreferences.getString("email","hello");

        listview = (ListView) findViewById(R.id.listView3);

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddAddress.this);
                alertDialogBuilder.setMessage("Are you sure,You wanted to delete?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                        //delete record

                        AddAddressList item = (AddAddressList) (listview.getItemAtPosition(pos));

                        // Toast.makeText(AddCar.this,item.getId().toString(),Toast.LENGTH_LONG).show();

                        new AddAddressListDelete().execute(emailId,item.getId().toString());

                        results.remove(pos);
                        addCLA.notifyDataSetChanged();



                    }
                });

                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;
            }
        });




        new AddCarListShow().execute(emailId);

    }

    public void AddAddressSubmit(View view){
        Intent i = new Intent(this,AddAddressSubmit.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed() {
        Intent i = new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }


    private class AddAddressListDelete extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddAddress.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.10.107/cardoctor/deleteaddress.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("id", params[1]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    noResult = false;
                    while ((line = reader.readLine()) != null) {

                        if(line.equals("no")) {

                            noResult = true;

                        }
                        else {
                            result.append(line);
                        }

                    }

                    // Pass data to onPostExecute method
                    return (result.toString());


                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();


            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(noResult){
                Toast.makeText(getApplicationContext(),"No search results found.", Toast.LENGTH_LONG).show();

            }
        }


    }





    private class AddCarListShow extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(AddAddress.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.10.107/cardoctor/showaddresslist.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0]);

                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if succe
                // ssful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    noResult = false;
                    while ((line = reader.readLine()) != null) {

                        if(line.equals("no")) {

                            noResult = true;

                        }
                        else {
                            result.append(line);
                        }

                    }

                    // Pass data to onPostExecute method
                    return (result.toString());


                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            pdLoading.dismiss();


            try {
                results = new ArrayList<>();
                AddAddressList list;
                Log.i("h", result.toString());
                // Create JSONObject from result JSON string
                JSONArray arr = new JSONObject(result.toString()).getJSONArray("posts");
                // get the 'posts' section from the JSON string
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                    //ListElementsArrayList.add(GetValue.getText().toString());
                    list = new AddAddressList();
                    list.setId(post.getString("id"));
                    list.setCountry(post.getString("country"));
                    list.setState(post.getString("state"));
                    list.setCity(post.getString("city"));
                    list.setSubregion(post.getString("subregion"));
                    list.setSteetaddress(post.getString("streetaddress"));
                    results.add(list);


                }

                addCLA = new AddAddressCustomListAdapter(AddAddress.this, results);


                listview.setAdapter(addCLA);
            }
            catch (Exception e){
                e.printStackTrace();
            }



            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(noResult){
                Toast.makeText(getApplicationContext(),"No search results found.", Toast.LENGTH_LONG).show();

            }
        }


    }





}
