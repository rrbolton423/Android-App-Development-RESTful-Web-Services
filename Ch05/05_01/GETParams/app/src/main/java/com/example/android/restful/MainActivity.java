package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.services.MyService;
import com.example.android.restful.utils.NetworkHelper;
import com.example.android.restful.utils.RequestPackage;

// In the MainActivity, I'm currently passing just the URL. Instead, I now want to pass
// a package that includes the URL and the parameters.

public class MainActivity extends AppCompatActivity {

    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

    private boolean networkOk;
    TextView output;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataItem[] dataItems = (DataItem[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            for (DataItem item : dataItems) {
               output.append(item.getItemName() + "\n");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        networkOk = NetworkHelper.hasNetworkAccess(this);
        output.append("Network ok: " + networkOk);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    public void runClickHandler(View view) {

        if (networkOk) {

            // I've described how to make simple HTTP requests retrieving text and binary
            // based content, but to work with most web services, you need to send parameters,
            // values that the webservice interprets as requests to filter data or take other
            // actions.

            // The goal is to make this a more complex request, to be able to include parameters,
            // and specifically I want to be able to filter the request based on a category.

            // First of all, I'm no longer going to pass the URL as data. Instead
            // it'll be part of a package. In the conditional block after I've checked
            // to make sure that I have network connectivity, I'll ...

            // Create an instance of my RequestPackage class
            RequestPackage requestPackage = new RequestPackage();

            // Next, I'll call a method named setEndPoint(), and pass in the same URL,
            // but this time because of the way the class is structured, I don't need
            // wrap it inside a URI object.
            requestPackage.setEndPoint(JSON_URL);

            // Then I'm going to pass a parameter
            // The key is going to be the name of the parameter that the service expects,
            // This would change depending on which web service you're working with
            // And pass in a value that I want to filter on
            requestPackage.setParam("category", "Entrees");

            Intent intent = new Intent(this, MyService.class);

//            intent.setData(Uri.parse(JSON_URL));
            // Then, instead of passing the URI as a data value, I'm going to say ...
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            // and I'll pass in that new constant from the service, and the request package object

            // So now everything you need to know about the request is in that
            // requestPackage object, and it's going to be received in the service.

            startService(intent);
        } else {
            Toast.makeText(this, "Network not available!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

}
