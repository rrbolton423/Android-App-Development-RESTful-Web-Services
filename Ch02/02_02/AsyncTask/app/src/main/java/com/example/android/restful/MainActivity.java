package com.example.android.restful;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

    }

    public void runClickHandler(View view) {
//        output.append("Button clicked\n");

        // Create an instance of the AsyncTask
        MyAsyncTask task = new MyAsyncTask();

        // To execute code, call a method of the task object named "execute()"
        // The execute method will accept any number of arguments, but they all have
        // to match the type that is declared in th first generic.
        task.execute("String 1", "String 2", "String 3");

        // When you call the execute() method, that passes those values to the
        // doInBackground() method, it'll receive the values as an array.
    }

    public void clearClickHandler(View view) {
        output.setText("");
    }

    // Make class extend AsyncTask, passing in the required generics
    private class MyAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            // When you call the execute() method, that passes those values to the
            // doInBackground() method, it'll receive the values as an array.
            // Within the method you can then iterate through the array with a for loop.

            for (String string: strings) {

                // To share the result with the rest of the application,
                // call publishProgress(), passing the value.
                publishProgress(string);

                // To show that something is happening in the backgroun, call
                // Thread.sleep(). When called, you must surround it with a try/catch block
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        // The doInBackground method is going to run in the background,
        // on it's own thread. When you call publishProgress, you're pushing
        // information to another method that will run in the foreground.
        // That method is also an override of AsyncTask, and it's name onProgressUpdate.
        // It will receive values that match the type of the second generic
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            // Work with the first item in the array
            // Because onProgressUpdate is running in the Main UI thread, it has access
            // to the widgets in the app
            output.append(values[0] + "\n");
        }

        // There are some other methods that run on the Main Thread

        // onPreExecute() doesn't receive any arguments, and can be used to set up the
        // app before running the task.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // onPostExecute() receives the values that match the type of the last generic on
        // the AsyncTask extension declaration. When you use an AsyncTask to make a web service
        // call, you might return the data from the call here.
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        // These are the basics of AsyncTask. There's one major complication to it.
        // When used on it's own, its owned by the containing activity. If there's a configuration
        // change while the task is running, that task will be killed.

        // So you only want to use AsyncTask for very quick background tasks. If you want
        // to run a longer task, you can wrap the task in a fragment that doesn't have a visual
        // interface.
    }
}
