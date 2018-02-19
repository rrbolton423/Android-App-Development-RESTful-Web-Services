package com.example.android.restful;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.restful.model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {

    private static final String TAG = "DataItemAdapter";
    public static final String ITEM_ID_KEY = "item_id_key";
    public static final String ITEM_KEY = "item_key";
    private List<DataItem> mItems;

    // As I load each image from the web, I'll store it here. As the user scrolls, I'll be able to
    // load it from memory after the first time it's displayed.
    private Map<String, Bitmap> mBitmaps = new HashMap<>();

    private Context mContext;
    private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

    public DataItemAdapter(Context context, List<DataItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SharedPreferences settings =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                  String key) {
                Log.i("preferences", "onSharedPreferenceChanged: " + key);
            }
        };
        settings.registerOnSharedPreferenceChangeListener(prefsListener);

        boolean grid = settings.getBoolean(
                mContext.getString(R.string.pref_display_grid), false);
        int layoutId = grid ? R.layout.grid_item : R.layout.list_item;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataItemAdapter.ViewHolder holder, int position) {
        final DataItem item = mItems.get(position);

        try {
            holder.tvName.setText(item.getItemName());
            //display image

            // I'm already displaying the name of the DataItem. Now, I'll find out whether
            // I have the related image in memory already.
            if (mBitmaps.containsKey(item.getItemName())) {

                // Get a reference to the Bitmap from the map interface
                Bitmap bitmap = mBitmaps.get(item.getItemName());

                // Then display it
                holder.imageView.setImageBitmap(bitmap);

            } else {
                // Now if I don't have that Bitmap already in memory,
                // now I need to launch my AsyncTask.

                // Instantiate the Task
                ImageDownloadTask task = new ImageDownloadTask();

                // Next, I'll pass in the reference to the ViewHolder.
                // By doing this, the Task knows where to display the image.
                task.setViewHolder(holder);

                // Execute the task, passing in the current DataItem
                task.execute(item);

                // This onBindViewHolder() method will be called over and over again by the
                // Adapter, as the user scrolls up and down to look at all of the data.
                // The first time each DataItem is encountered, it'll result in creating an
                // instance of the Task, and executing it, and thereafter, the bitmaps will
                // be drawn from memory.

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(ITEM_KEY, item);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "You long clicked " + item.getItemName(),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView imageView;
        public View mView;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.itemNameText);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mView = itemView;
        }
    }

    // Each instance of this class will only be responsible for a single image file.
    // Each time I need an image, I'll create an instance of this AsyncTask, and then
    // I'll execute it. The image will be downloaded and then I'll save it back to the
    // rest of the application. I want to make sure I'm only loading each image once.
    // So to store the images and make them persist for the lifetime of the current activity,
    // I'm going to instantiate the map that keeps track of the images. I'll do this as the
    // Adapter is constructed.
    private class ImageDownloadTask extends AsyncTask<DataItem, Void, Bitmap> {

        // Declare a constant, keeping track of location of my image files.
        private static final String PHOTOS_BASE_URL =
                "http://560057.youcanlearnit.net/services/images/";

        // Declare a private field representing the current DataItem
        private DataItem mDataItem;

        // Declare a private field representing the current ViewHolder. The ViewHolder is
        // provided by the Adapter, and it references the current visual row.
        private ViewHolder mHolder;

        // Also added a method to this class setViewHolder(), and that's where I'm going to
        // get that reference
        public void setViewHolder(ViewHolder holder) {
            mHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(DataItem... dataItems) {

            // When I create the new AsyncTask object and then execute it, I'll be passing in the
            // current reference to the DataItem. I'll save that reference here in mDataItem,
            // so I can reference it a couple of different times.
            mDataItem = dataItems[0];

            // Use that DataItem to build the Url with the images and then, I download and return
            // the Bitmap.
            String imageUrl = PHOTOS_BASE_URL + mDataItem.getImage();
            InputStream in = null;

            try {
                in = (InputStream) new URL(imageUrl).getContent();

                // Return the Bitmap to the onPostExecute() method of the AsynTask
                return BitmapFactory.decodeStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        // AsyncTask method that runs in the foreground
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            // Display the image
            // This displays it when this Task is executed.
            mHolder.imageView.setImageBitmap(bitmap);

            // But then to make sure it's available for future display,
            // I'll add it to my map. I've saved it in memory
            mBitmaps.put(mDataItem.getItemName(), bitmap);
        }
    }
}