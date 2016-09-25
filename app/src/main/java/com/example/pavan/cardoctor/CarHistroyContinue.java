package com.example.pavan.cardoctor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CarHistroyContinue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_histroy_continue);
    }

    public void onBackPressed() {
        Intent i = new Intent(this, Home.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();

    }

    public void repair(View view) {
        Intent i = new Intent(this,RepairHistory.class);
        startActivity(i);
        finish();
    }
}
