package com.example.pavan.cardoctor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Notification extends Service {

    private Socket socket;
    private  String data;
    public Notification() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        try {
            socket = IO.socket("http://192.168.10.107:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Log.i("hhh","connected");
                    socket.emit("adduser","pavan@gmail.com");

                }

            }).on("message", new Emitter.Listener() {

                @Override
                public void call(Object... args) {

                    Log.i("hhh","received");
                    data = (String)args[0];
                    notification();

                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {


                @Override
                public void call(Object... args) {
                    socket.disconnect();
                }

            });
            socket.connect();

        }
        catch(URISyntaxException e){
            e.printStackTrace();
        }
        return START_STICKY;
    }

    private void notification(){
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
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
