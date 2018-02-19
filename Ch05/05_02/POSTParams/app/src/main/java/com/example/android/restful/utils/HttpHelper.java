package com.example.android.restful.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    public static String downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        InputStream is = null;
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestPackage.getMethod());

            // The setDoInput() method means that I'm going to be receiving data from the web
            // service into the application, but if I also want to send data to the request,
            // I need to also set a parameter named DoOutput.
            conn.setDoInput(true);

            // Check for a POST request
            // If the request is a POST request and it contains parameters ...
            if (requestPackage.getMethod().equals("POST") &&
                    encodedParams.length() > 0) {

                // By saying "conn.setDoOutput(true);", I can now send data to the connection
                conn.setDoOutput(true);

                // Create an instance of a class named OutputStreamWriter
                // Now I have a way to send information to the connection.
                // In the parameter, specify the connection's outputStream you are writing to
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                // Write to the connection
                // So the structure of the parameters, they're separators with ampersands and their
                // encoding for passing in a URL are exactly the same for both GET and POST, but the
                // place where you write them is different. For POST, you write them to the body of
                // the URL request, unlike in a GET request where you write the parameters to the end of
                // URL request.
                // Write the encoded parameters to the body of the web service
                writer.write(requestPackage.getEncodedParams());

                // Next I'll flush and close the writer object, and everything else about this request
                // remains the same.
                // Commit / flush the changes to the connection / stream
                writer.flush();

                // Close the stream
                writer.close();

                // The Android app sends the request with the POST parameter now, that request is
                // received in the web service and it's interpreted into an SQL filter and the data
                // that I get back only matches that requested filter.

                // So now you have all the code you need to send parameters for both GET and POST
                // requests. Any web service that expects simple parameters wth GET or POST can
                // be used with an Android app.
            }

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