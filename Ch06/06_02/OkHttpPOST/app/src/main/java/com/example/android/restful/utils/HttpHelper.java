package com.example.android.restful.utils;

import java.io.IOException;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {

    public static String downloadFromFeed(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod().equals("GET") &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        OkHttpClient client = new OkHttpClient();

        Request.Builder requestBuilder = new Request.Builder()
                .url(address);

        // Add code that looks for a POST request
        if (requestPackage.getMethod().equals("POST")) {

            // Just like I did with HttpUrlConnection, my job is to take the parameters that
            // are being passed in to me, and then wrap them into the body of the request. I don't
            // have to do that as manually as I did with HttpUrlConnection, instead, I'm going to use
            // an API from OkHttp called  a multipart body, and a request body.
            // Then I'll immediately chain a connection "setType(MultipartBody.FORM)"
            // This is telling OkHttp that I am simulating a web form.
            // MultipartBody : used while breaking up the data in a POST request into different
            // discrete types and send to server.

            // Add a form data part to the body
            // A web form or HTML form on a web page allows a user to enter data that is
            // sent to a server for processing.
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            // Next I'll extract the parameters from my RequestPackage that I passed in as a
            // parameter to this method. Those will be returned as a Map object with a key
            // and a value of type string. These parameters are being passed in the MainActivity

            // Get the parameters from the Map
            Map<String, String> params = requestPackage.getParams();

            // Loop through those params. I'll use a for each loop, and for each time through the
            // loop I'll be dealing with a key and one item in that Map, and I'll be looking in
            // the key set of the Map. I'll get that with params.keySet()
            for (String key : params.keySet()) {

                // Within the loop, I'll take the parameter and pass it into the Builder object
                // with builder.addFormDataPart(), passing the 2 Strings, the key and the associated
                // value
                builder.addFormDataPart(key, params.get(key));

                // Now my Multipart Body Builder is ready to go and I'll use it to create an instance
                // of a class named RequestBody, also a member of OkHttp. I'll get that reference
                // by typing ...
                // RequestBody : used while sending data to server in simple way.
                RequestBody requestBody = builder.build();

                // Then I'm ready to tell the request object that this is going to be a POST request.
                // I'll call a method of the requestBuilder named method() which takes 2 parameters,
                // the method and the body.
                requestBuilder.method("POST", requestBody);

                // So now I have all the code I need in my HTTP helper class to do either GET or
                // POST requests using OKHttp, it's still a good chunk of code but you no longer
                // have to deal at the low level of Java with Input and Output Streams. Most of the
                // hard work of getting the String from the Web Service is done for you and the coding
                // style is more maintainable and readable then when you're working with
                // HttpURLConnection.

            }
        }

        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception: response code " + response.code());
        }
    }

}