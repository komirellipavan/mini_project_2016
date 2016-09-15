package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search extends AppCompatActivity {


    private EditText search;
    private boolean noResult;
    ListView listview;
    Uri.Builder builder;

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
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.listView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listview.getItemAtPosition(position);
                ServiceProviderList newsData = (ServiceProviderList) o;
                //Toast.makeText(Search.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
                ServiceProviderList item = (ServiceProviderList) (listview.getItemAtPosition(position));
                SharedPreferences sharedpreferences = getSharedPreferences("book", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("shopname",item.getShopName());
                editor.putString("rating",item.getRating());
                editor.putString("email",item.getEmail());
                editor.putString("address",item.getStreet()+","+item.getSubregion()+","+item.getCity()+","+item.getState()+","+item.getCountry());
                editor.commit();
                Intent i = new Intent(Search.this,Book.class);
                startActivity(i);
                finish();
            }
        });





        search = (EditText) findViewById(R.id.search);
        //set listeners
        search.addTextChangedListener(textWatcher);




    }

    private class found extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Search.this);
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
                url = new URL("http://192.168.10.107/cardoctor/search.php");

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

                SharedPreferences sharedpreferences = getSharedPreferences("filter", Context.MODE_PRIVATE);
                String status = sharedpreferences.getString("status","no");
                Log.i("hkkk",sharedpreferences.getString("status","no"));
                if(status.equals("true")) {


                     builder = new Uri.Builder()

                            .appendQueryParameter("search", params[0])
                            .appendQueryParameter("status","true")
                            .appendQueryParameter("brand1", sharedpreferences.getString("brand1","no"))
                            .appendQueryParameter("brand2", sharedpreferences.getString("brand2","no"))
                            .appendQueryParameter("brand3", sharedpreferences.getString("brand3","no"))
                            .appendQueryParameter("brand4", sharedpreferences.getString("brand4","no"))
                            .appendQueryParameter("brand5", sharedpreferences.getString("brand5","no"))
                            .appendQueryParameter("brand6", sharedpreferences.getString("brand6","no"));
                }
                else {
                    builder = new Uri.Builder()
                            .appendQueryParameter("status","false")
                            .appendQueryParameter("search", params[0]);
                }

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


                try {

                    ArrayList<ServiceProviderList> results = new ArrayList<>();
                    ServiceProviderList list;

                    Log.i("hi", "onPostExecute: "+result.toString());
                    // Create JSONObject from result JSON string
                    JSONArray arr = new JSONObject(result.toString()).getJSONArray("posts");
                    // get the 'posts' section from the JSON string
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                        //ListElementsArrayList.add(GetValue.getText().toString());
                        list = new ServiceProviderList();
                        Log.i("h", post.getString("shopname"));
                        list.setShopName(post.getString("shopname"));
                        list.setStreet(post.getString("street"));
                        list.setSubregion(post.getString("subregion"));
                        list.setCity(post.getString("city"));
                        list.setState(post.getString("state"));
                        list.setCountry(post.getString("country"));
                        list.setRating(post.getString("rating"));
                        list.setEmail(post.getString("email"));
                        results.add(list);


                    }



                    listview.setAdapter(new CustomListAdapter(Search.this, results));
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

    public void enableSubmitIfReady(){
        if(search.getText().toString().length() >=3){

            new found().execute(search.getText().toString());
        }

    }

    public void onBackPressed() {
        Intent i = new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_loaction_icon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.location) {
           Toast.makeText(getApplicationContext(),"location", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.filter) {
           // Toast.makeText(getApplicationContext(),"location", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, SearchFilter.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
