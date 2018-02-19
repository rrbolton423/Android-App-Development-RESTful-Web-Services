package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.restful.model.DataItem;

import java.io.IOException;

import retrofit2.Call;

public class MyService extends IntentService {

    public static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Make the web service request

        // I'll put the code to call the Retrofit Service in the onHandleIntent() method.
        // This method is called automatically as the service is started up.

        // Create an instance of MyWebService. That's the Java interface where I've defined
        // the service, and it has the Retrofit object. I'll instantiate it like this, and
        // now I have an instance with that interface.
        MyWebService webService =
                MyWebService.retrofit.create(MyWebService.class);

        // Next I need an instance of the Retrofit Call class. This is the same class that I
        // defined in the WebService. The dataItems() method returns an instance of that class.
        // I retrieve it by calling webService.dataItems().

        // Instantiate a Call object for the HTTP query which we want to perform
        // Create the Call to the feed
        Call<DataItem[]> call = webService.dataItems();

        // When I get data back, it'll be in the form of an array of DataItems, so I'll declare
        // that array here. For the moment I won't instantiate it.
        DataItem[] dataItems;

        try {

            // Now there are two different ways to trigger the call, synchronously and asynchronously.
            // To make the call to the web service synchronous, use a method named execute().
            // The execute() method makes the call, and synchronously, the data is returned, and
            // immediately, I'm calling the method body to get the data that came back from the service.
            // Execute the call to the feed
            dataItems = call.execute().body();

        } catch (IOException e) {

            // Print exception
            e.printStackTrace();

            // Log exception
            Log.i(TAG, "onHandleIntent: " + e.getMessage());

            // Return from the method because there is else left to do
            return;
        }

        // Now I have my dataItems object here, and I'm ready to return the data to the MainActivity.
        // Return the data to MainActivity
        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

}
