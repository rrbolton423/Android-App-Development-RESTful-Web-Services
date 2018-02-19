package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import com.example.android.restful.utils.NetworkHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Map<String, Bitmap>> {

    private static final int SIGNIN_REQUEST = 1001;
    public static final String MY_GLOBAL_PREFS = "my_global_prefs";
    private static final String TAG = "MainActivity";
    private static final String JSON_URL =
            "http://560057.youcanlearnit.net/services/json/itemsfeed.php";

    //    DataSource mDataSource;
    List<DataItem> mItemList;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    String[] mCategories;
    RecyclerView mRecyclerView;
    DataItemAdapter mItemAdapter;
    boolean networkOk;

    // Create a Map field that has a key of a string, and an associated DataItem of a Bitmap.
    Map<String, Bitmap> mBitmaps;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DataItem[] dataItems = (DataItem[]) intent
                    .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
            Toast.makeText(MainActivity.this,
                    "Received " + dataItems.length + " items from service",
                    Toast.LENGTH_SHORT).show();

            mItemList = Arrays.asList(dataItems);

            // Initialize ImageLoader and trigger the download
            getSupportLoaderManager().initLoader(0, null, MainActivity.this)
                    .forceLoad();

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
                displayDataItems(category);
            }
        });
//      end of navigation drawer

//        mDataSource = new DataSource(this);
//        mDataSource.open();
//        mDataSource.seedDatabase(dataItemList);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean grid = settings.getBoolean(getString(R.string.pref_display_grid), false);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
        if (grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

//        displayDataItems(null);

        networkOk = NetworkHelper.hasNetworkAccess(this);
        if (networkOk) {
            Intent intent = new Intent(this, MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        } else {
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));

    }

    private void displayDataItems(String category) {
//        mItemList = mDataSource.getAllItems(category);
        if (mItemList != null) {

            // When creating the DataItemAdapter, pass in the Bitmaps HashMap,
            // containing the downloaded images to the constructor
            mItemAdapter = new DataItemAdapter(this, mItemList, mBitmaps);
            mRecyclerView.setAdapter(mItemAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mDataSource.open();
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
                displayDataItems(null);
                return true;
            case R.id.action_choose_category:
                //open the drawer
                mDrawerLayout.openDrawer(mDrawerList);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public Loader<Map<String, Bitmap>> onCreateLoader(int id, Bundle args) {

        // Create the Loader and pass in the ArrayList of items
        return new ImageDownloader(this, mItemList);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Bitmap>> loader, Map<String, Bitmap> data) {

        // Save the data that's being passed in
        mBitmaps = data;

        // Then display the DataItems
        displayDataItems(null);

    }

    @Override
    public void onLoaderReset(Loader<Map<String, Bitmap>> loader) {

    }

    // The AsyncTaskLoader's return data type is a Map with a
    // String key and a Bitmap value
    private static class ImageDownloader extends AsyncTaskLoader<Map<String, Bitmap>> {

        // Constant with URL to the photos in the web service
        private static final String PHOTOS_BASE_URL =
                "http://560057.youcanlearnit.net/services/images/";

        // Place to store my current item list
        private static List<DataItem> mItemList;

        // When I instantiate the Loader, I'm passing the reference to that list of data in here.
        public ImageDownloader(Context context, List<DataItem> itemList) {
            super(context);

            // Receive the ArrayList of items and save it in this AsyncTask class
            mItemList = itemList;
        }

        // Conventional java code for getting the bitmap and storing it locally
        @Override
        public Map<String, Bitmap> loadInBackground() {
            //download image files here

            // Declare my Map object
            Map<String, Bitmap> map = new HashMap<>();

            // For each through my list of DataItems that were passed in,
            // and download them each, adding them to the map data structure
            for (DataItem item : mItemList) {

                // In the loop, take the name of the image file and append it to the base URL
                // EX: http://560057.youcanlearnit.net/services/images/apple_pie.jpg
                String imageUrl = PHOTOS_BASE_URL + item.getImage();

                // Declare the InputStream
                InputStream in = null;

                try {

                    // Instantiate the InputStream

                    // Create a URL object containing the URL of the specific image,
                    // get it's contents and returns an InputStream
                    in = (InputStream) new URL(imageUrl).getContent();

                    // Download the Bitmap file

                    // Declare a BitMap, and get it's value by using a class named
                    // BitmapFactory, calling the decodeStream() method, passing the
                    // InputStream containing the current photo's contents
                    // The decodeStream() method turns the inputStream into the Bitmap image
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    // Store the Bitmap object by putting it into the map object,
                    // passing the key of the current item's name, as well as the downloaded
                    // Bitmap image
                    map.put(item.getItemName(), bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // Close the input stream
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            // After the loop is done, return the Map
            return map;
        }
    }


}
