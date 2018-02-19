package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.restful.services.MyService;

public class MainActivity extends AppCompatActivity {

    // To communicate from an Intent Service back to the user interface,
    // you can use Broadcast Messages. We will use local messages since they
    // stay within a single app's process and aren't available to other apps
    // on the device.
    // You'll use a class named LocalBroadcastManager to send the messages,
    // and a BroadcastReceiver to receive and handle the messages.

    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    TextView output;

    // Set up the listening architecture
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // In the onReceive() method, I'm going to receive a context,
            // and the Intent object that was passed when I broadcast the message.

            // Get the value from the intent by
            // passing the key to the message I'm looking for
            String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);

            // Output the results
            output.append(message + "\n");

            // Now whenever the message is received by the BroadcastReceiver,
            // I'll simply display the message in my TextView.

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        // Register to listen to the message using an IntentFilter that looks
        // for the message identifier String in the onCreate() method.
        // When calling registerReceiver(), pass the BroadcastReceiver field
        // and the Intent Filter containing the constant that identifies
        // the message itself.
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        // If you want to listen for more than 1 message, you just call the
        // registerReceiver() method as many times as you need to, and the code will
        // execute each time the activity is created
    }

    // To make sure I don't have a resource leak, unregister the listener object
    // whenever the Activity is closing down.
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the Broadcast Receiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {
        Intent intent = new Intent(this, MyService.class);
        intent.setData(Uri.parse(JSON_URL));
        startService(intent);
        startService(intent);
        startService(intent);
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
