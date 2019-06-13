package com.prodev.assignment_waycool.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface api_interface {

    String JSONURL = "http://www.recipepuppy.com/";

    @GET("api")
    Call<String> getString();

}
