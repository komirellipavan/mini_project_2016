package com.example.pavan.cardoctor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Notification extends Service {

    private  Socket socket;
    private  String data;
    private  String email;
    private  String serviceprovider;
    SharedPreferences sharedpreferences;
    public Notification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedpreferences = getSharedPreferences("log", Context.MODE_PRIVATE);
        email =sharedpreferences.getString("email","no");

        sharedpreferences = getSharedPreferences("book", Context.MODE_PRIVATE);
        serviceprovider =sharedpreferences.getString("email","no");
        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceive, new IntentFilter("book"));

        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        try {
            socket = IO.socket("http://192.168.10.107:3000");
        }
        catch(Exception e){}
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
                public void call(Object... args) {
                    Log.i("hhh","connected");
                    socket.emit("adduser",email);

                }

            }).on("message", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("hhh","received");
                    data = (String)args[0];
                    notification();

                }

            }).on("req", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("hhh","received");
                    data = (String)args[0];
                    sendMessag();

                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {


                @Override
                public void call(Object... args) {

                }


            });





        return START_STICKY;
    }
    // Our handler for received Intents. This will be called whenever an Intent
    // with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("book", "Got message: " + message);
            if(socket.connected()) {
                socket.emit("book", serviceprovider);
            }

        }
    };
    private void sendMessag() {
        Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("custom-event-name");
        // You can also include some extra data.
        intent.putExtra("message", "This is my message!");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void notification(){
        sendMessag();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setContentTitle("My notification")
                        .setContentText(data);

        Intent resultIntent = new Intent(this, temp.class);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
// Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hhh","closing");
        socket.disconnect();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(
                mMessageReceive);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
