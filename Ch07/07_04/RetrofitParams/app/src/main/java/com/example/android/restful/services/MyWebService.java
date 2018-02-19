package com.example.android.restful.services;

import com.example.android.restful.model.DataItem;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyWebService {

    String BASE_URL = "http://560057.youcanlearnit.net/";
    String FEED = "services/json/itemsfeed.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // This dataItems() method returns everything
    @GET(FEED)
    Call<DataItem[]> dataItems();

    // This dataItems() method has a single String parameter that filters the
    // data by category. If you want ti call the dataItems() method and do a filter,
    // just pass in that String parameter, and you'll call the new version of the method.

    // Receives an argument typed as a String named category
    // To transform that into a query String variable, I'll prefix the argument
    // with an annotation named @Query, which is a member of the Retrofit library.
    // It takes a single argument and this is the name of the argument or parameter
    // as it's known in the web service "category".
    // So now I'm saying that when the user uses this version of the method,
    // the value that they pass in as the Java argument "category", should be turned
    // into the request parameter "category".
    // All of the mechanics will be handled for you ... the addition of the question mark
    // to make it a query string variable, the encoding of the value using URL encoding,
    // which is required, but it's all handled by Retrofit.
    @GET(FEED)
    Call<DataItem[]> dataItems(@Query("category") String category);

}
