package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class ClientSignUpAddress extends AppCompatActivity {

    private EditText country;
    private EditText state;
    private EditText city;
    private EditText subRegion;
    private EditText streetAddress;
    private Button b;
    private String name;
    private String gender;
    private String emailId;
    private String password;


    private String country_;
    private String state_;
    private String city_;
    private String subRegion_;
    private String streetAddress_;
    SharedPreferences sharedpreferences;
    //TextWatcher
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            enableSubmitIfReady();

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_up_address);
        b = (Button) findViewById(R.id.done);
        b.setEnabled(false);
       
        country = (EditText) findViewById(R.id.country);
        state = (EditText) findViewById(R.id.state);
        city = (EditText) findViewById(R.id.city);
        subRegion = (EditText) findViewById(R.id.subRegion);
        streetAddress = (EditText) findViewById(R.id.streetAddress);

        //set listeners
        country.addTextChangedListener(textWatcher);
        state.addTextChangedListener(textWatcher);
        city.addTextChangedListener(textWatcher);
        subRegion.addTextChangedListener(textWatcher);
        streetAddress.addTextChangedListener(textWatcher);
    }

    public void back(View view){


        Intent i = new Intent(this,ClientSignUp.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    public void onBackPressed() {
        Intent i = new Intent(this, ClientSignUp.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);

    }





    private class CheckEmail extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ClientSignUpAddress.this);
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
                url = new URL("http://192.168.10.107/cardoctor/checkemail.php");

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

            if(result.equalsIgnoreCase("no"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                SharedPreferences sharedpreferences = getSharedPreferences("client", Context.MODE_PRIVATE);
                name = sharedpreferences.getString("name",null);
                password = sharedpreferences.getString("password",null);
                gender = sharedpreferences.getString("gender",null);
                emailId = sharedpreferences.getString("emailId",null);

                country_ = country.getText().toString();
                state_ = state.getText().toString();
                city_= city.getText().toString();
                subRegion_ = subRegion.getText().toString();
                streetAddress_ = streetAddress.getText().toString();

                new InsertDetails().execute(name,password,gender,emailId,country_,state_,city_,subRegion_,streetAddress_);

            } else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("done")){
                Toast.makeText(getApplicationContext(),"emailID already in use,please enter another emailID", Toast.LENGTH_LONG).show();

            }
        }


    }
        private class InsertDetails extends AsyncTask<String, String, String>
        {
            ProgressDialog pdLoading = new ProgressDialog(ClientSignUpAddress.this);
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
                    url = new URL("http://192.168.10.107/cardoctor/signup.php");

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
                            .appendQueryParameter("name", params[0])
                            .appendQueryParameter("password", params[1])
                            .appendQueryParameter("gender", params[2])
                            .appendQueryParameter("email", params[3])
                            .appendQueryParameter("country", params[4])
                            .appendQueryParameter("state", params[5])
                            .appendQueryParameter("city", params[6])
                            .appendQueryParameter("subregion", params[7])
                            .appendQueryParameter("streetaddress", params[8]);

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
                String string = emailId;
                SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("email",emailId);

                    Intent intent = new Intent(ClientSignUpAddress.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    ClientSignUpAddress.this.finish();


            } else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Please try again after some time", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void done(View view){
        SharedPreferences sharedpreferences = getSharedPreferences("client", Context.MODE_PRIVATE);
        emailId = sharedpreferences.getString("emailId","hello");


        new CheckEmail().execute(emailId);



    }
    public void enableSubmitIfReady() {
        b = (Button) findViewById(R.id.done);


        if (country.getText().toString().length() < 5 ||
                 state.getText().length() < 5  ||
                city.getText().toString().length()  < 5 ||
                streetAddress.getText().toString().length() < 5 ||
                subRegion.getText().toString().length() < 5) {
            b.setEnabled(false);


        }
        if (country.getText().toString().length() > 4 &&
                state.getText().length() > 5   &&
                city.getText().toString().length()  > 5 &&
                streetAddress.getText().toString().length() > 5 &&
                subRegion.getText().toString().length() > 5) {
            b.setEnabled(true);


        } else {
            if (country.getText().toString().length() == 0)
                country.setError("Please enter country!");
            if (city.getText().toString().length() == 0)
                city.setError("Please enter city!");
            if (streetAddress.getText().toString().length() == 0)
                streetAddress.setError("please enter street address!");
            if (subRegion.getText().toString().length() == 0)
                subRegion.setError("please enter sub region!");
            if (state.getText().toString().length() == 0)
                state.setError("please enter state!");
        }

    }

}
