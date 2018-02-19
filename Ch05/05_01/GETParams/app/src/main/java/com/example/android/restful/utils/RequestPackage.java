package com.example.android.restful.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by romellbolton on 2/12/18.
 */

// Class implements the Parcelable interface making it possible
// to pass it around in Intents
public class RequestPackage implements Parcelable {

    // String specifying the endpoint, or the original base JSON URL
    private String endPoint;

    // String specifying the type of request for the web service
    private String method = "GET";

    // A Map that is parcelable due to both data types in the Map being strings,
    // the key and the value
    private Map<String, String> params = new HashMap<>();

    public String getEndpoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setParam(String key, String value) {
        params.put(key, value);
    }

    // The RequestPackage class
    // has a method called getEncodedParams. It's doing all the work to take those
    // parameters, separate them with ampersands where there's more than one,
    // and then encode the values so that the values can be passed as part of the URL.
    public String getEncodedParams() {

        // Create a StringBuilder object to build up and manipulate a string
        StringBuilder sb = new StringBuilder();

        // Loop through the keys of the map
        // For each key, i will get it's value, convert it to UTF-8, and put it into the URL
        for (String key : params.keySet()) {

            // Create a String initially with a null value
            String value = null;


            try {

                // Try to encode the keys of the map, and return them to a String
                // Encode the values so that they can be apart of the URL
                value = URLEncoder.encode(params.get(key), "UTF-8");


            } catch (UnsupportedEncodingException e) {


                e.printStackTrace();
            }

            // If the length of the StringBuilder is greater than 0
            // If there is not the first key value pair ...
            if (sb.length() > 0) {

                // Add an ampersand to the StringBuilder
                sb.append("&");

            }

            // Use the StringBuilder to append the parameters after the ampersand
            // EX: category=Deserts
            sb.append(key).append("=").append(value);
        }
        Log.i("TAG", "getEncodedParams: " + sb.toString());
        return sb.toString();
    }

    // Code below is generated for the Parcelable interface

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endPoint);
        dest.writeString(this.method);
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    public RequestPackage() {
    }

    protected RequestPackage(Parcel in) {
        this.endPoint = in.readString();
        this.method = in.readString();
        int paramsSize = in.readInt();
        this.params = new HashMap<String, String>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
    }

    public static final Parcelable.Creator<RequestPackage> CREATOR = new Parcelable.Creator<RequestPackage>() {
        @Override
        public RequestPackage createFromParcel(Parcel source) {
            return new RequestPackage(source);
        }

        @Override
        public RequestPackage[] newArray(int size) {
            return new RequestPackage[size];
        }
    };
}
