package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.services.MyService;
import com.example.android.restful.services.MyWebService;
import com.example.android.restful.utils.NetworkHelper;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";

    List<DataItem> mItemList;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;
    boolean networkOk;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataItem[] dataItems = (DataItem[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            Toast.makeText(MainActivity.this,
                    "Received " + dataItems.length + " items from service",
                    Toast.LENGTH_SHORT).show();

            mItemList = Arrays.asList(dataItems);
            displayData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Code to manage sliding navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCategories = getResources().getStringArray(R.array.categories);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.drawer_list_item, mCategories));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = mCategories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                        Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawer(mDrawerList);
                requestData(category);
            }
        });
//      end of navigation drawer

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

        networkOk = NetworkHelper.hasNetworkAccess(this);
        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

        requestData();

    }

    private void displayData() {
        if (mItemList != null) {
            mItemAdapter = new DataItemAdapter(this, mItemList);
            mRecyclerView.setAdapter(mItemAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signin:
                Intent intent = new Intent(this, SigninActivity.class);
                startActivityForResult(intent, SIGNIN_REQUEST);
                return true;
            case R.id.action_settings:
                // Show the settings screen
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_all_items:
                // display all items
                requestData();
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestData() {
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);

        // No longer using the IntentService
        // Just like I did in the IntentService, I'll create an istance of the WebService
        // interface.

        // Create the WebService object
        MyWebService webService =
                MyWebService.retrofit.create(MyWebService.class);

        // Instantiate a Call object for the HTTP query which we want to perform
        Call<DataItem[]> call = webService.dataItems();

        // This is almost the same as the IntentService's implementation, but there I used the
        // call object's "execute()" method to make a request synchronously. Instead, I'm now
        // going to use a method named "enqueue()". You pass in an instance of a class named
        // "CallBack" that's a member of the Retrofit library. It's an interface, and just like
        // the Call object, it wraps an instance of your data. Two required call back methods are
        // created, "onResponse()" and "onFailure()". If I get back data from the WebService, I'll
        // get an onResponse() method call, and if there is a failure of any kind, I'll get an
        // onFailure() method call.

        // The enqueue() method asynchronously sends the request and notifies the callback methods
        // of the response, or of a failed response
        call.enqueue(new Callback<DataItem[]>() {
            @Override
            public void onResponse(Call<DataItem[]> call, Response<DataItem[]> response) {

                // Processing the data will be almost exactly like it was before. Previously, I was
                // receiving the data in the MainActivity's BroadcastReceiver object.

                // This time, I'm getting the DataItems not from an intent, but from the call object.
                // I do this by typing "DataItem[] dataItems = response.body();"
                // This is just like "call.execute.body", when you make the call synchronously.
                    DataItem[] dataItems = response.body();

                    // Use Toast message to show the user what happened.
                    Toast.makeText(MainActivity.this,
                            "Received " + dataItems.length + " items from service",
                            Toast.LENGTH_SHORT).show();

                    // Transforming the Array into a List
                    mItemList = Arrays.asList(dataItems);

                    // Displaying the updated Data
                    displayData();
                }


            @Override
            public void onFailure(Call<DataItem[]> call, Throwable t) {

            }
        });
    }

    private void requestData(String category) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SIGNIN_REQUEST) {
            String email = data.getStringExtra(SigninActivity.EMAIL_KEY);
            Toast.makeText(this, "You signed in as " + email, Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor =
                    getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();
            editor.putString(SigninActivity.EMAIL_KEY, email);
            editor.apply();

        }

    }

}
