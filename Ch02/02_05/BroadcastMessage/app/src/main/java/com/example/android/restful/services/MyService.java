package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MyService extends IntentService {

    public static final String TAG = "MyService";

    // Constant to identify the message
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";

    // Constant to identify the message payload
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // To pass data back through a message, once again you'll use an
        // Intent object.
        // Use the constructor that receives a String known as the ACTION
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        // Next pass the data back by adding it to the intent as an extra
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, "Service all done!");

        // Now, send the message
        // Create a LocalBroadcastManager object and get it's instance
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());

        // Send the message
        manager.sendBroadcast(messageIntent);

        // That's it. You've packaged up the information you want to share with the
        // rest of the application, you've gotten a reference to the LocalBroadcastManager,
        // and you've got the Broadcast message. The Service has no idea who might be
        // listening, it simply throws the message out to the rest of the application, and
        // then any activity or any service can listen for and handle the message.
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

}
