package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class AssistantLogin extends AppCompatActivity {

    private EditText name;
    private EditText password;
    private EditText token;
    private Button b;
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
        setContentView(R.layout.activity_assistant_login);

        b = (Button) findViewById(R.id.login);
        b.setEnabled(false);

        name = (EditText) findViewById(R.id.emailId);
        password = (EditText) findViewById(R.id.password);
        token = (EditText) findViewById(R.id.token);

        name.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        token.addTextChangedListener(textWatcher);
    }
    public void cancel(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();// destroys current activity
    }
    public void onBackPressed(){
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }
    public void login(View view){

        new ValidateUser().execute(token.getText().toString(),name.getText().toString(),password.getText().toString());

    }
    private class ValidateUser extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(AssistantLogin.this);
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
                url = new URL("http://192.168.10.107/cardoctor/assistlogin.php");

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
                Log.i("ppp",params[0] + params[1] +params[2]);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("token", params[0])
                        .appendQueryParameter("name", params[1])
                        .appendQueryParameter("password", params[2]);


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
            Log.i("hhh",result);
            if(result.equalsIgnoreCase("accept"))
            {
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                SharedPreferences sharedpreferences = getSharedPreferences("ass", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("email",name.getText().toString());
                if(editor.commit()) {
                    Intent intent = new Intent(AssistantLogin.this, AssistantHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    AssistantLogin.this.finish();
                }

            } else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("reject")){
                Toast.makeText(getApplicationContext(),"emailID or password is incorrect", Toast.LENGTH_LONG).show();

            }
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void enableSubmitIfReady() {
        b = (Button) findViewById(R.id.login);


        if ( name.getText().toString().length()  < 4||
                password.getText().toString().length()  < 4 ||
                token.getText().toString().length()  < 9){

            b.setEnabled(false);
        }
        if (name.getText().toString().length()  >=4 &&
                password.getText().toString().length()  >=4 &&
                token.getText().toString().length()  >= 9 ) {
            b.setEnabled(true);
        } else {
            if (password.getText().toString().length() == 0)
                password.setError("Password is required!(at least 4 char)");

            if (name.getText().toString().length() == 0)
                name.setError("name is required!");

            if (token.getText().toString().length() == 0)
                token.setError("token is required!");
        }

    }

}
