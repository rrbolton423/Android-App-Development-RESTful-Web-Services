package com.example.android.restful.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpHelper {

    public static String downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        // Create an instance of the OkHttpClient class
        // OkHttpClient configures and creates HTTP connections
        OkHttpClient client = new OkHttpClient();

        // Next, construct an OKHttpRequest. To construct a request, you'll use a request
        // builder. So I'll create the Builder object first. Immediately pass in the address
        // with the method named url().
        // Pass the address I received from my own request package.
        // RequestBuilder builds the request for the OkHttpClient object
        Request.Builder requestBuilder = new Request.Builder()
                .url(address);

        // Build the request on a separate line
        // Create an OkHttpRequest object
        // Request is an HTTP Request
        Request request = requestBuilder.build();

        // Now I'm ready to make the request. A lot of the code that you have to do with
        // HttpURLConnection isn't required at all.
        // I'll create an OkHttp response object, and I'll get it's response by making a call
        // to a method named newCall(). newCall() prepares the request to be executed
        // I'll pass in the request object, and then call
        // a method named execute().
        // This request is synchronous. You could do it asynchronously if you like, but we're
        // already working in a background thread, so i can make the request the simplest way possible.
        // Response is an HTTP response from the WebService
        Response response = client.newCall(request).execute();

        // Next you need to check whether the request was successful. With HttpURLConnection, you
        // would explicitly check the connection code, but with OkHttp, there's a method named
        // isSuccessful(), and it's a method of the response object.
        if (response.isSuccessful()) {

            // If this returns "true", I'll return the resulting String.
            return response.body().string();

        } else {

            // If I don't get back a successful response, I'll throw an Exception
            throw new IOException("Exception: response code " + response.code());
        }
    }
}