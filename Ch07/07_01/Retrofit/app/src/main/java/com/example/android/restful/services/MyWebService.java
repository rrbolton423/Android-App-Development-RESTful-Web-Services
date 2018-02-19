package com.example.android.restful.services;

import com.example.android.restful.model.DataItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by romellbolton on 2/15/18.
 */

public interface MyWebService {

    // Using Retrofit you need to split those two values up. Retrofit needs to know what's the
    // base URL for the entire service, and then what's the specific part of the URL for a
    // particular feed.
    String BASE_URL = "http://560057.youcanlearnit.net/";
    String FEED = "services/json/itemsfeed.php";

    // The next step is to create an instance of a class called Retrofit, and I'll instantiate
    // it using a Builder object.
    // 1. Set the base URL using the constant
    // 2. Next, add a method called addConverterFactory(), passing in GsonConverterFactory
    // from the Retrofit2.converter package. To instantiate that, I'll call it's create() method.
    // 3. Finally call the build() method. That gives me a Retrofit object.

    // Define a Retrofit field which defines the base URL and the converter factory.
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // 4. Next, I need to tell Retrofit how I'm going to make the request. Retrofit supports both
    // GET and POST requests. You define the kind of request using an annotation.
    // The annotation receives the "feed", the part of the URL that represents where this
    // particular feed is.
    // 5. Next, I'll define a method, and this method will actually execute the call. It's going to
    // return an instance of something called the "Call" class. When you declare the call, you use
    // a generic declaration to indicate what type of data will be returned. I'm going to receive an
    // array of DataItem objects, and this is the same data item class that I've used previously
    // that's in my model package. I'll name the method DataItems.
    // That's all you need to do to declare the web service.

    // Define a GET request that's going to return an array of DataItems.
    @GET(FEED)
    Call<DataItem[]> dataItems();
}
