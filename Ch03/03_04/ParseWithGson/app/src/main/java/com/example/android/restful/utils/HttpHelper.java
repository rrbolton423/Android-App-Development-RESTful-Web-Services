package com.example.android.restful.utils;

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

        InputStream is = null;
        try {

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
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

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        // Create array of bytes to write the stream to
        byte[] buffer = new byte[1024];

        // Create a ByteOutputStream for data to be put into a byte array output stream
        // From Byte Array to a String
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        // Create a BufferedOutputStream to write the buffer data to the byte array output stream
        // From Buffer to BufferedOutputStream
        BufferedOutputStream out = null;
        try {
            int length = 0;

            // Instantiate the BufferedOutputStream,
            // and specify the outputStream to write the data to
            out = new BufferedOutputStream(byteArray);

            // While there are items to be read from the stream into the buffer ...
            while ((length = stream.read(buffer)) > 0) {

                // Store the bytes from the stream (buffer) into the bufferedOutputStream
                // which in turn, DOES WHAT WITH THE BYTEARRAY
                out.write(buffer, 0, length);
            }

            // Force any bytes to be written to the underlying output stream (byteArray)
            out.flush();

            // Return the String version of thr byteArray output stream
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