package com.example.pavan.cardoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validateUser();
        setContentView(R.layout.activity_main);


    }


    public boolean fileExistance(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }


    private void validateUser(){

        boolean valid;

        SharedPreferences sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        String email =sharedpreferences.getString("email","no");


        if(email.equals("no"))
            valid = false;
        else
            valid = true;

            if(valid){
                //goto Home Page

                Intent home = new Intent(this,Home.class);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                finish();
            }


    }

    public void clientLogin(View view){
        Intent intent = new Intent(this,ClientLogin.class);
        startActivity(intent);
        finish();// destroys current activity
    }
    public void onBackPressed(){
        finish();

    }
    public void assistantLogin(View view){
        Intent intent = new Intent(this,AssistantLogin.class);
        startActivity(intent);
        finish();// destroys current activity
    }

}
