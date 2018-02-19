package com.example.android.restful.utils;

/**
 * Created by romellbolton on 2/7/18.
 */

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 */
public class HttpHelper {

    /**
     * Returns text from a URL on a web server
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address) throws IOException {

        // Takes input stream of bytes
        InputStream is = null;

        try {

            // Tak a String named address, and wrap it in a URL object
            URL url = new URL(address);

            // Call openConnection() method of the URL, and that returns the
            // HttpURLConnection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set some properties on the connection
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Call the connect() method on the connection
            conn.connect();

            // getResponseCode() reaches out to the server, and gets back a response code
            int responseCode = conn.getResponseCode();

            // The response code should be 200, that's the standard HTTP code that
            // means everything's okay.
            if (responseCode != 200) {

                // If I don't get back that response code, I throw an exception.
                throw new IOException("Got response code " + responseCode);
            }

            // If everything's okay, I continue and I get an InputStream object
            // from the connection
            is = conn.getInputStream();

            // Then I call another method called readStream(), passing in the InputStream
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

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        // Create a buffer, a byte array
        // Reading data in 1024 bytes at a time
        byte[] buffer = new byte[1024];

        // Create a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Create a BufferedOutputStream
        BufferedOutputStream bufferedOutputStream = null;

        try {

            // Used to store how many bytes were read from the InputStream stream
            int length = 0;

            // Create BufferedOutputStream, passing the byteArrayOutputStream to write to
            bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

            /*
            loop through the incoming data and save into the buffer
             */

            // While there are bytes that we've read into the buffer
            // Buffer grows as data is read into it
            while ((length = stream.read(buffer)) > 0) {

                // Write to the bufferedOutputStream from the buffer
                // We're using the buffer to write to the bufferedOutputStream
                bufferedOutputStream.write(buffer, 0, length);
            }

            // Flush the stream, which takes the data form the BufferedOutputStream,
            // and writes it to the ByteArrayOutputStream
            bufferedOutputStream.flush();

            // At the end, return the ByteArrayOutputStream as a String
            return byteArrayOutputStream.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bufferedOutputStream != null) {
                bufferedOutputStream.close();
            }
        }
    }

}
