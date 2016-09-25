package com.example.pavan.cardoctor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class Book extends AppCompatActivity {
SharedPreferences sharedpreferences;
    private String shopname;
    private String address;
    private String rating;
    TextView shopNameBook;
    TextView addressBook;
    RatingBar ratingbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        sharedpreferences = getSharedPreferences("book", Context.MODE_PRIVATE);
        shopname= sharedpreferences.getString("shopname","no");
        address= sharedpreferences.getString("address","no");
        rating= sharedpreferences.getString("rating","no");
        shopNameBook = (TextView) findViewById(R.id.shopNameBook);
        addressBook = (TextView) findViewById(R.id.addressBook);
        shopNameBook.setText(shopname);
        addressBook.setText(address);
        ratingbar1=(RatingBar)findViewById(R.id.ratingBar);
        if(rating.isEmpty()){
            rating = "0.0";
        }
        else{
            ratingbar1.setRating(Float.parseFloat(rating));
        }

    }

    public void onBackPressed() {
        Intent i = new Intent(this, Search.class);
        startActivity(i);
        finish();

    }
    public void book(View view){
        Intent i =new Intent(this,BookContinue.class);
        startActivity(i);
        finish();
    }





}
