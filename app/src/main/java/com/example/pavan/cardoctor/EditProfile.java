package com.example.pavan.cardoctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;


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
import java.util.UUID;

public class EditProfile extends AppCompatActivity implements SingleUploadBroadcastReceiver.Delegate {
    //Declaring views
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editText;
    private String path;
    private String email;
    private String profilepic;
    private String name;
    EditText name_,email_;
    private  boolean delete;

    Button b;

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




    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.selectImage);
        buttonUpload = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.imageView2);
        //editText = (EditText) findViewById(R.id.editTextName);
        SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        email = sharedpreferences.getString("email","hello");
        name = sharedpreferences.getString("name","hello");




        name_ = (EditText) findViewById(R.id.editText);
        email_ = (EditText) findViewById(R.id.editText2);

        name_.addTextChangedListener(textWatcher);
        email_.addTextChangedListener(textWatcher);

        name_.setText(name,TextView.BufferType.EDITABLE);
        email_.setText(email,TextView.BufferType.EDITABLE);
        delete = false;
        new GetUri().execute(email);

    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }
    @Override
    public void onProgress(int progress) {
        //your implementation
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        //your implementation
    }

    @Override
    public void onError(Exception exception) {
        //your implementation
    }

    @Override
    public void onCancelled() {
        //your implementation
    }

    /*
   * This is the method responsible for image upload
   * We need the full image path and the name for the image in this method
   * */
    public void uploadMultipart() {


        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {

            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);


            //Creating a multi part request
            Log.i("h", "uploadMultipart: " + path);
            new MultipartUploadRequest(this, uploadId, "http://192.168.10.107/cardoctor/upload.php")
                    .addParameter("email",email)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", "hello") //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        try {
            String response = new String(serverResponseBody, "UTF-8");
            Log.i("", "onCompleted: " + response.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    //method to show file chooser
    public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        path =  cursor.getString(column_index);

        return this.path;
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
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



                }
            }
            catch (Exception e){

            }
            if(delete){
                new ProfileDelete().execute(email,profilepic);
            }
            else {
                new GetImage().execute(profilepic);
            }

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



            if (result != null){
                imageView.setImageBitmap(result);

            }





        }

    }


    public void onBackPressed() {
        Intent i = new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }


    private class ProfileDelete extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.10.107/cardoctor/profiledelete.php");

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
                        .appendQueryParameter("profilepic", params[1]);



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

            String uri = "@mipmap/ic_profile_picture";  // where myresource (without the extension) is the file

            int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            Drawable res = getResources().getDrawable(imageResource);
            imageView.setImageDrawable(res);
            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Try again after some time", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void remove(View view){
        delete = true;
        new GetUri().execute(email);


    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public void enableSubmitIfReady() {
        b = (Button) findViewById(R.id.saveProfile);


        if (isValidEmail( email_.getText().toString())== false||
                name_.getText().toString().length()  < 4){

            b.setEnabled(false);
        }
        if (isValidEmail( email_.getText().toString()) &&
                name_.getText().toString().length()  >=4) {
            b.setEnabled(true);
        } else {
            if (name_.getText().toString().length() == 0)
                name_.setError("name is required!(at least 4 char)");

            if (email_.getText().toString().length() == 0)
                email_.setError("emailId is required!");
        }

    }


    private class SaveProfile extends AsyncTask<String, String, String>
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
                url = new URL("http://192.168.10.107/cardoctor/saveprofile.php");

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
                        .appendQueryParameter("updateemail", params[1])
                        .appendQueryParameter("name", params[2]);




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

            if(result.trim().equals("done")){
                Toast.makeText(getApplicationContext(),"Profile saved", Toast.LENGTH_LONG).show();
                SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("email",email_.getText().toString());
                editor.putString("name",name_.getText().toString());

                editor.commit();
            }

            if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(getApplicationContext(),"OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

            } else if(result.equalsIgnoreCase("no")){
                Toast.makeText(getApplicationContext(),"Try again after some time", Toast.LENGTH_LONG).show();

            }
        }

    }
public void saveProfile(View view){

new SaveProfile().execute(email,email_.getText().toString(),name_.getText().toString());
}


}




