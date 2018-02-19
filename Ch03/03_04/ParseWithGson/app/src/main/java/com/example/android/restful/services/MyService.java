package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.utils.HttpHelper;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        String response;

        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Right now, I'm retrieving a string from the web service, and passing that raw
        // string back to the activity. The name of the string is response. My job now is to
        // transform that raw string into an array of strongly typed objects. I'll be using the
        // DataItem class that I created previously.

        // Create an instance of the GSON class
        Gson gson = new Gson();

        // Use the GSON object to do the transformation

        // Declare an array of my DataItem class, and get it's value by calling the GSON
        // object and it's fromJson() method.
        // Pass in the response String that contains the raw data, and then tell GSON
        // which of my Custom class I want to use to represent the data in memory.
        // I'm not passing in an array of DataItems, I'm simply telling GSON, this is the class
        // I want you to use, to instantiate each time you see a new object.
        // That's all you need to do, the data has been transformed.
        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);

        // Send back the dataItems array to the Main Activity
        // I am able to pass this strongly typed object as an extra in an Intent because it
        // implements the parcelable interface
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);

        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
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
