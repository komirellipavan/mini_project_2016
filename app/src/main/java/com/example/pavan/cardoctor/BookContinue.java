package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.List;

public class BookContinue extends AppCompatActivity {
    String item;
    List<String> cars,address;
    ArrayAdapter<String> dataAdapter,dataAdapter2;
    Spinner spinner,spinner2;
    String providerEmail,clientEmail;
    String regno,addressid;
    private boolean stop = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_continue);
        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner2 = (Spinner) findViewById(R.id.spinner3);

        // Spinner click listener
        startService();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                regno = (String) (spinner.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here


            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                addressid = (String) (spinner2.getItemAtPosition(position));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        // Spinner Drop down elements
        //categories.add("Maruti ");

        cars = new ArrayList<>();
        address = new ArrayList<>();
        // Creating adapter for spinner
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cars);
        dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, address);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SharedPreferences sharedpreferences = getSharedPreferences("book", Context.MODE_PRIVATE);
        providerEmail= sharedpreferences.getString("email","no");
        sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        clientEmail= sharedpreferences.getString("email","no");

        new cars().execute(clientEmail,providerEmail);
        // attaching data adapter to spinner
        //spinner.setAdapter(dataAdapter);

    }
    @Override
    protected void onPause() {
        if(stop){
        stopService();}
        super.onPause();


    }
    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), Notification.class));
    }

    public void onBackPressed() {
        Intent i = new Intent(this, Book.class);
        startActivity(i);
        finish();

    }
    public void sendMessage() {
        Log.d("book", "Broadcasting message");
        Intent sve = new Intent("book");
        // You can also include some extra data.
        sve.putExtra("message", "book");
        LocalBroadcastManager.getInstance(this).sendBroadcast(sve);
    }
    // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), Notification.class));

    }
    public void confirm(View view){
        stop = false;
        new confirmm().execute(clientEmail,providerEmail,addressid,regno);

    }
    private class confirmm extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(BookContinue.this);
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
                url = new URL("http://192.168.10.107/cardoctor/booktable.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cemail", params[0])
                        .appendQueryParameter("semail", params[1])
                        .appendQueryParameter("addressid", params[2])
                        .appendQueryParameter("status", "book")
                        .appendQueryParameter("regno", params[3]);


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

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
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

            sendMessage();
            Intent i = new Intent(BookContinue.this,BookSucces.class);
            startActivity(i);
            finish();

            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Something is wrong try after some time!", Toast.LENGTH_LONG).show();

            }
        }

    }

    private class cars extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(BookContinue.this);
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
                url = new URL("http://192.168.10.107/cardoctor/pickcar.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("clientemail", params[0])
                        .appendQueryParameter("provideremail", params[1]);


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

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
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



                Log.i("hi", "onPostExecute: "+result.toString());
                // Create JSONObject from result JSON string
                JSONArray arr = new JSONObject(result.toString()).getJSONArray("posts");
                // get the 'posts' section from the JSON string
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                    //Log.i("hi", "onPostExecute: "+post.getString("name"));
                   cars.add(post.getString("regno"));

                }


                spinner.setAdapter(dataAdapter);

                new address().execute(clientEmail,providerEmail);

            }
            catch (Exception e){
                e.printStackTrace();
            }







            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("reject")){
                Toast.makeText(getApplicationContext(),"Something is wrong try after some time!", Toast.LENGTH_LONG).show();

            }
        }

    }


    private class address extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(BookContinue.this);
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
                url = new URL("http://192.168.10.107/cardoctor/pickaddress.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("clientemail", params[0])
                        .appendQueryParameter("provideremail", params[1]);


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

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
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



                Log.i("hi", "onPostExecute: "+result.toString());
                // Create JSONObject from result JSON string
                JSONArray arr = new JSONObject(result.toString()).getJSONArray("posts");
                // get the 'posts' section from the JSON string
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                    //Log.i("hi", "onPostExecute: "+post.getString("name"));
                    address.add(post.getString("id"));

                }


                spinner2.setAdapter(dataAdapter2);

            }
            catch (Exception e){
                e.printStackTrace();
            }







            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("reject")){
                Toast.makeText(getApplicationContext(),"Something is wrong try after some time!", Toast.LENGTH_LONG).show();

            }
        }

    }



}
