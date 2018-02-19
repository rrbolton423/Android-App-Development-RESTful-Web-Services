package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by romellbolton on 2/6/18.
 */

// IntentService is named that way because it receives instructions
// packaged in an Intent object.
// Register the Service in the application manifest.
public class MyService extends IntentService {

    private static final String TAG = "MyService";

    // Create mandatory constructor method
    public MyService() {
        super("MyService");
    }

    // The onHandleIntent() method will be called
    // automatically when the service starts up, and it receives an intent
    // object as an argument.
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // This method is called automatically. The first step will be to get the
        // intent object, and extract any data I need for the Service.
        // The getData() method returns the URI that was passed onto the intent object
        Uri uri = intent.getData();

        // Log the URI that the Service received
        Log.i(TAG, "onHandleIntent: " + uri.toString());

        // Delay the Thread for visualization
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /*
    Intent Services also have Lifecycle methods
     */

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    // When the onHandleIntent() method is finished executing,
    // onDestroy() is called and will destroy the service automatically
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
