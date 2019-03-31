package com.example.injuries.apis;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.injuries.apis.API_Constants.BASE_URL;


public class NetworkCaller {
    private static RESTApis restApis;

    public static RESTApis getAPIs() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Retrofit mainAPIs = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        restApis = mainAPIs.create(RESTApis.class);
        return restApis;
    }

}
