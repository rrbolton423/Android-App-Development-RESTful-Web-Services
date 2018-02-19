package com.example.android.restful.utils;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    // Refactor this downloadFromFeed() method to instead of receiving just a String,
    // it's going to receive that RequestPackage. All of the work that has to be done
    // to get the information out of the package will happen in the HTTPHelper.
    public static String downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        // Extract all of the information I need from the requestPackage

        // First, I need the address, also known as the endpoint.
        String address = requestPackage.getEndpoint();

        // Then I'm going to request any parameters. The RequestPackage class
        // has a method called getEncodedParams. It's doing all the work to take those
        // parameters, separate them with ampersands where there's more than one,
        // and then encode the values so that the values can be passed as part of the URL.
        String encodedParams = requestPackage.getEncodedParams();

        // Next, I'm going to check the method of the request.
        // If the method is "GET" and there's at least one parameter, then I want to append
        // the parameters as a query string.

        // If it is a GET method and there are any encoded parameters...
        // Append the parameters as a query String
        if (requestPackage.getMethod().equals("GET") && encodedParams.length() > 0) {

            // If that entire expression is true, then I want to rewrite the address.
            // Create the URL pattern with "address + ? + encodedParams"
            // EX : http://560057.youcanlearnit.net/services/json/itemsfeed.php?category=Desserts
            address = String.format("%s?%s", address, encodedParams);

            Log.i("TAG", "downloadFromFeed: " + address);
        }

        InputStream is = null;
        try {

            // Pass the address
            URL url = new URL(address);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            // Pass the requestPackage as the request method
            conn.setRequestMethod(requestPackage.getMethod());

            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Got response code " + responseCode);
            }
            is = conn.getInputStream();
            return readStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return null;
    }

    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}