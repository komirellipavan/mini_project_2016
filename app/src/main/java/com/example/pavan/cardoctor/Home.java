package com.example.pavan.cardoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String email;
    private Bitmap bitmap;
    private ImageView imageView;
    private String profilepic;
    private String name;
    TextView nameP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        imageView = (ImageView)header.findViewById(R.id.circleView);

        SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email","hello");
        TextView emailP = (TextView)header.findViewById(R.id.emailProfile);
        nameP = (TextView)header.findViewById(R.id.nameProfile);
        emailP.setText(email);

        Log.i("h",email);
        new GetUri().execute(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   public void carRepair(View view){
       Intent carRepair = new Intent(this,Search.class);
       carRepair.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

       startActivity(carRepair);
       finish();
   }

    public void carHistory(View view){
        Intent i = new Intent(this,CarHistroyContinue.class);
        startActivity(i);
        finish();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        boolean deleted;
        if (id == R.id.nav_signout) {

            SharedPreferences preferences = getSharedPreferences("log", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            if(editor.commit()) {

                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }


        } else if(id == R.id.nav_car){
            Intent intent = new Intent(Home.this,AddCar.class);
            startActivity(intent);
            Home.this.finish();

        }
        else if(id == R.id.nav_address){
            Intent intent = new Intent(Home.this,AddAddress.class);
            startActivity(intent);
            Home.this.finish();

        }
        else if(id == R.id.nav_change_password){
            Intent intent = new Intent(Home.this,ChangePassword.class);
            startActivity(intent);
            Home.this.finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void editProfile(View view){
        Intent intent = new Intent(Home.this,EditProfile.class);
        startActivity(intent);
        Home.this.finish();

    }

    private class GetUri extends AsyncTask<String, String, String>
    {

        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.10.107/cardoctor/getimage.php");

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
            try {
                JSONArray arr = new JSONObject(result.toString()).getJSONArray("posts");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject post = arr.getJSONObject(i).getJSONObject("post");
                    //ListElementsArrayList.add(GetValue.getText().toString());

                  profilepic =  post.getString("profilepic");
                    name = post.getString("name");
                    nameP.setText(name);
                    SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("name",name);
                    editor.commit();

                }
            }
            catch (Exception e){

            }
            new GetImage().execute(profilepic);

            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Try again after some time", Toast.LENGTH_LONG).show();

            }
        }

    }


    private class GetImage extends AsyncTask<String, String , Bitmap>
    {

        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }
        @Override
        protected Bitmap doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://192.168.10.107/cardoctor/"+ params[0]);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

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
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();

            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);
                    return(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                conn.disconnect();
            }

            return(bitmap);
        }


        @Override
        protected void onPostExecute(Bitmap result) {

            //this method will be running on UI thread
            if(result != null) {
                imageView.setImageBitmap(result);
            }

        }

    }




}
