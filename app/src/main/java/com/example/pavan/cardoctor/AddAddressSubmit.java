package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class AddAddressSubmit extends AppCompatActivity {

    EditText country;
    EditText state;
    EditText city;
    EditText subregion;
    EditText street;
    private String emailId;

    private String country_;
    private String state_;
    private String city_;
    private String subregion_;
    private String street_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address_submit);
        country = (EditText) findViewById(R.id.countrySubmit);
        state = (EditText) findViewById(R.id.stateSubmit);
        city = (EditText) findViewById(R.id.citySubmit);
        subregion = (EditText) findViewById(R.id.subregionSubmit);
        street = (EditText) findViewById(R.id.streetAddressSubmit);
    }

    public void onBackPressed() {
        Intent i = new Intent(this,AddAddress.class);
        startActivity(i);
        finish();

    }

    public void submit(View view){
        SharedPreferences sharedpreferences = getSharedPreferences("client", Context.MODE_PRIVATE);
        emailId = sharedpreferences.getString("emailId","hello");

        country_ = country.getText().toString();
        state_ = state.getText().toString();
        city_ = city.getText().toString();
        subregion_ = subregion.getText().toString();
        street_ = street.getText().toString();

        new AddCars().execute(emailId,country_,state_,city_,subregion_,street_);

    }
    private class AddCars extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(AddAddressSubmit.this);
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
                url = new URL("http://192.168.10.107/cardoctor/addaddress.php");

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
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("country", params[1])
                        .appendQueryParameter("state", params[2])
                        .appendQueryParameter("city", params[3])
                        .appendQueryParameter("subregion", params[4])
                        .appendQueryParameter("street", params[4]);


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

            if(result.equalsIgnoreCase("done"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                Intent intent = new Intent(AddAddressSubmit.this,AddAddress.class);
                startActivity(intent);
                AddAddressSubmit.this.finish();
                Toast.makeText(getApplicationContext(),"Added successful", Toast.LENGTH_LONG).show();


            } else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Please try after some time.", Toast.LENGTH_LONG).show();

            }
        }

    }



}
