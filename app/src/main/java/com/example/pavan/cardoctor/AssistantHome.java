package com.example.pavan.cardoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AssistantHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_home);
    }

 public void assSignOut(View view){

     SharedPreferences preferences = getSharedPreferences("ass", 0);
     SharedPreferences.Editor editor = preferences.edit();
     editor.clear();
     if(editor.commit()) {

         Intent intent = new Intent(this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
         finish();
     }
 }
}
