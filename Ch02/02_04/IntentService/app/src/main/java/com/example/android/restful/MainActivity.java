package com.example.android.restful;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.restful.services.MyService;

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
    }

    public void runClickHandler(View view) {

        // In the Activity, create an instance of the service,
        // and send it information in an intent
        Intent intent = new Intent(this, MyService.class);

        // Pass in the data as an URI object
        intent.setData(Uri.parse(JSON_URL));

        // Launch the service
        startService(intent);

        // You can create as many instances of your service as you want, and call them
        // each individually, and each time you'll be creating an instance of the service.
        // and when it's onHandleIntent() method is finished executing, the service will
        // be destroyed automatically.
        // If you have more than 1 task to execute all at the same time, you can run them
        // in a sequence. They'll share a single worker thread and a single instance of the
        // Service object, but they'll be queued up, and the onHandleIntent() method will
        // be called as many times as it's needed. Only 1 instance of the Service object is
        // created though, so the onCreate() and onDestroy() methods will be called only once.
        startService(intent);
        startService(intent);

        // Only one copy of the IntentService can be run at a time, and that insures that you're
        // not overloading the device's resources.

    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
