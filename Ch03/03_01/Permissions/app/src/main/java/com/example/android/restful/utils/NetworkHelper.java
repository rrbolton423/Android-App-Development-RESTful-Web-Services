package com.example.android.restful.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by romellbolton on 2/7/18.
 */

// This class checks for Network connectivity.
// This code is in a helper class, wrapped in a static method.
public class NetworkHelper {

    // To check network access, you need to use a context, so this method
    // will receive once as an argument
    public static boolean hasNetworkAccess(Context context) {

        // To check for network connectivity, use a class named ConnectivityManager.
        // The getSystemService() method returns an object, you have to be specific about
        // what you're expecting, so cast it as an (ConnectivityManager)
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            // Next, I'll get an object called NetworkInfo
            // NetworkInfo describes the status of a network interface
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            // If the activeNetwork is not null, and is connected or connecting,
            // return the activeNetwork
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }

}
