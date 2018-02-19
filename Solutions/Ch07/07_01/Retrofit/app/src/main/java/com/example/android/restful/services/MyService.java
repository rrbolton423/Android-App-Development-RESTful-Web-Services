package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Make a service request

//        Return the data to MainActivity
//        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
//        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
//        LocalBroadcastManager manager =
//                LocalBroadcastManager.getInstance(getApplicationContext());
//        manager.sendBroadcast(messageIntent);
    }

}
