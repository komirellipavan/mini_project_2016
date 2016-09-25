package com.example.pavan.cardoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class temp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

    }

    public void onBackPressed(){
        Intent i = new Intent(this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "temp closed", Toast.LENGTH_LONG).show();


    }

    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), Notification.class));

    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), Notification.class));
    }

    @Override
    protected void onPause() {
        // Unregister since the activity is paused.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceiver);
        super.onPause();
        Toast.makeText(this, "temp paused", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("custom-event-name"));
        super.onResume();
        Toast.makeText(this, "temp resume", Toast.LENGTH_LONG).show();

    }


    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };


    public void sendMessage(View view) {
        Log.d("service", "Broadcasting message");
        Intent sve = new Intent("service");
        // You can also include some extra data.
        sve.putExtra("message", "This is my service!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(sve);
    }

}
