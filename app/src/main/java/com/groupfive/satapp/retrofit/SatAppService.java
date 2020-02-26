package com.groupfive.satapp.retrofit;

import com.groupfive.satapp.models.AuthLogin;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SatAppService {

    @GET("/auth")
    Call<AuthLogin> postLogin(@Query("access_token") String masterAccesToken);
}
