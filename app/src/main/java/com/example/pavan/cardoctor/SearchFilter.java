package com.example.pavan.cardoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class SearchFilter extends AppCompatActivity {
    CheckBox ch1;
    CheckBox ch2;
    CheckBox ch3;
    CheckBox ch4;
    CheckBox ch5;
    CheckBox ch6;
    Button b1;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);
        sharedpreferences = getSharedPreferences("filter", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        Log.i("k", "onClick: "+ sharedpreferences.getString("brand1","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("brand2","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("brand3","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("brand4","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("brand5","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("brand6","no"));
        Log.i("k", "onClick: "+ sharedpreferences.getString("status","no"));

        ch1=(CheckBox)findViewById(R.id.checkBox);
        ch2=(CheckBox)findViewById(R.id.checkBox2);
        ch3=(CheckBox)findViewById(R.id.checkBox3);
        ch4=(CheckBox)findViewById(R.id.checkBox4);
        ch5=(CheckBox)findViewById(R.id.checkBox5);
        ch6=(CheckBox)findViewById(R.id.checkBox6);

        if(sharedpreferences.getString("brand1","no")=="Maruti"){
            ch1.setChecked(true);
        }
        if(sharedpreferences.getString("brand2","no")=="Honda"){
            ch2.setChecked(true);
        }
        if(sharedpreferences.getString("brand3","no")=="Tata Motors"){
            ch3.setChecked(true);
        }
        if(sharedpreferences.getString("brand4","no")=="Hyundai"){
            ch4.setChecked(true);
        }
        if(sharedpreferences.getString("brand5","no")=="Toyata"){
            ch5.setChecked(true);
        }
        if(sharedpreferences.getString("brand6","no")=="chevrolet"){
            ch6.setChecked(true);
        }

        b1=(Button)findViewById(R.id.button16);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.putString("brand1","no");
                editor.putString("brand2","no");
                editor.putString("brand3","no");
                editor.putString("brand4","no");
                editor.putString("brand5","no");
                editor.putString("brand6","no");
                editor.putString("status","false");
                if(ch1.isChecked() || ch2.isChecked()||ch3.isChecked()||ch4.isChecked()||ch5.isChecked()||ch6.isChecked()){
                    editor.putString("status","true");

                    if(ch1.isChecked()){

                        editor.putString("brand1","Maruti");
                        //Log.i("k", "onClick: "+ sharedpreferences.getString("brand1","no"));
                    }
                    else
                    {
                        editor.putString("brand1","no");
                    }

                    if(ch2.isChecked()){
                        editor.putString("brand2","Honda");
                    }
                    else
                    {
                        editor.putString("brand2","no");
                    }

                    if(ch3.isChecked()){
                        editor.putString("brand3","Tata Motors");
                    }
                    else
                    {
                        editor.putString("brand3","no");
                    }

                    if(ch4.isChecked()){
                        editor.putString("brand4","Hyundai");
                    }
                    else
                    {
                        editor.putString("brand4","no");
                    }

                    if(ch5.isChecked()){
                        editor.putString("brand5","Toyata");
                    }
                    else
                    {
                        editor.putString("brand5","no");
                    }


                    if(ch6.isChecked()){
                        editor.putString("brand6","chevrolet");
                    }
                    else
                    {
                        editor.putString("brand6","no");
                    }
                }
                editor.commit();


                Intent i = new Intent(SearchFilter.this,Search.class);
                startActivity(i);
                finish();

            }
        });
    }


    public void onBackPressed() {
        Intent i = new Intent(this, Search.class);
        startActivity(i);
        finish();

    }
}
