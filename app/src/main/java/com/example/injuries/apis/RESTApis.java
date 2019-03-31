package com.example.injuries.apis;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RESTApis {
    @POST("apis/save-test/")
    Call<Object> SaveTest(@Body TestData testData);
}
