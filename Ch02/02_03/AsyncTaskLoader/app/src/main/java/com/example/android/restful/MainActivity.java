package com.example.android.restful;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

// Make the Activity implement the LoaderCallback interface
public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    TextView output;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);
    }

    public void runClickHandler(View view) {

        Log.i(TAG, "runClickHandler: ");

        // Initialize the loader
        // The args include an ID, args for the loader,
        // and the object implementing the Loader interface.
        // "getSupportLoaderManager()" returns the LoaderManager object.
        // "forceLoad()" triggers the loadInBackground() method
        // and starts the chain the events that goes an gets my data and
        // returns it to the Activity
        // "restartLoader()" will recreate the loader object each time
        // you want to call it
        getSupportLoaderManager().restartLoader(
                0, null, this).forceLoad();
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

    // When I ask to initialize the loader, that will trigger a call to the
    // onCreateLoader() method, and the job of that method is to create an
    // instance "MyTaskLoader"
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        Log.i(TAG, "onCreateLoader: ");

        output.append("creating loader\n");

        // Create the Loader class and return it
        return new MyTaskLoader(this);

    }

    // I need to handle what happens when data is returned from the Loader.
    // I'll put that code into the onLoadFinished() method
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        Log.i(TAG, "onLoadFinished: ");

        // Update UI with the String I'm getting back
        output.append("loader finished, returned: " + data + "\n");
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    // Create a static class that extends AsyncTaskLoader,
    // and specify the return type in the generic declaration
    private static class MyTaskLoader extends AsyncTaskLoader<String> {

        private static final String TAG = "MainActivity";

        // Implement required constructor method
        public MyTaskLoader(Context context) {
            super(context);
        }

        // Method will run in the background thread an won't have access to
        // the user interface.
        // When you start up the Loader, that will trigger the loadInBackground method
        @Override
        public String loadInBackground() {

            Log.i(TAG, "loadInBackground: ");

            // Sleep the thread for 1 second to see the process
            try {

                Thread.sleep(1000);

            } catch (InterruptedException e) {

                e.printStackTrace();
            }

            // Return the data
            return "from the loader";
        }

        // If you need to make changes to the data before it's returned, you can either
        // do that work in the loadInBackground() method or you can override another method
        // called deliverResult(). The job of this method is to return the data to you
        // and then give you a chance to modify it any way you like.
        @Override
        public void deliverResult(String data) {

            // The original data will be passed through the deliverResult() method
            // and this String will be appended, and then it will be delivered by
            // calling the super class' version of the method.
            data += ", delivered";

            super.deliverResult(data);
        }
    }

}
