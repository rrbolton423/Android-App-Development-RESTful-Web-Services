package com.example.android.restful.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.android.restful.model.DataItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by romellbolton on 2/11/18.
 */

public class ImageCacheManager {

    // getBitmap receives a context reference, and a data item object.
    public static Bitmap getBitmap(Context context, DataItem dataItem) {

        // It calculates a URL where that image might be stored.
        // It's looking for the image in the cache directory.
        // This is a directory that's managed automatically by the Android framework.
        // As long as you have available space on your device, images stored there will be saved.
        // But if the device gets low on storage, it can eliminate those images as needed.
        // Once I have the location of the file, it's a simple matter to go get the related
        // bitmap image.

        // Get the file from the cache directory in persistent storage, by specifying
        // the name of the name of the image in the cache directory
        String fileName = context.getCacheDir() + "/" + dataItem.getImage();

        // Create a File object with that specific image file name
        File file = new File(fileName);

        // If the file exists, I read it into memory using BitmapFactory.decodeStream, and return it
        // from this method.

        // If the file exists ...
        if (file.exists()) {
            try {

                // Return the Bitmap image
                return BitmapFactory.decodeStream(new FileInputStream(file));

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            }
        }

        // Otherwise I will return null.
        return null;

    }

    // The putBitmap() method goes in the other direction. It receives a context reference,
    // a DataItem object, and a Bitmap that's already read into memory. It then uses the same
    // calculation of the location of the file, and saves it to persistent storage using a
    // FileOutputStream.
    public static void putBitmap(Context context, DataItem dataItem, Bitmap bitmap) {

        // Create a File name representing where the image will be stored in the cache
        // directory in persistent storage on the device
        String fileName = context.getCacheDir() + "/" + dataItem.getImage();

        // Create the File object
        File file = new File(fileName);

        // Define a FileOutputStream
        FileOutputStream outputStream = null;

        try {

            // Create FileOutputStream to write the image file to the cache directory
            outputStream = new FileOutputStream(file);

            // Notice that I'm using the bitmap's compress() method, which will reduce the size
            // of the file as much as possible.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
