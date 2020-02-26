package com.groupfive.satapp.retrofit;

import com.groupfive.satapp.models.AuthLogin;
import com.groupfive.satapp.models.tickets.TicketApiResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import retrofit2.http.Query;

public interface SatAppService {

    @GET("/auth")
    Call<AuthLogin> postLogin(@Query("access_token") String masterAccesToken,
                              @Path("username") String username,
                              @Path("password") String password);

    @Multipart
    @POST("/ticket")
    Call<TicketApiResponse> doRegisterWithAvatar(@Part MultipartBody.Part fotos1,
                                                 @Part MultipartBody.Part fotos2,
                                                 @Part("titulo") RequestBody titulo,
                                                 @Part("descripcion") RequestBody descripcion);

}
