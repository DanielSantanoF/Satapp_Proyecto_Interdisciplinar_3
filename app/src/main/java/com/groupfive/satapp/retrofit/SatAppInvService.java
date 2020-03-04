package com.groupfive.satapp.retrofit;

import com.groupfive.satapp.models.inventariable.Inventariable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface SatAppInvService {

    @GET("/inventariable")
    Call<List<Inventariable>> getInventariables();

    @GET("/inventariable/{id}")
    Call<Inventariable> getInventariable(@Path("id") String id);

    @Multipart
    @PUT("/inventariable/{id}")
    Call<Inventariable> putInvenatriable(@Path("id") String id,
                                         @Part MultipartBody.Part imagen,
                                         @Part("nombre")RequestBody nombre,
                                         @Part("descripcion")RequestBody descripcion,
                                         @Part("ubicacion")RequestBody ubicacion);

    @GET("/inventariable/img/{img_url}")
    Call<ResponseBody> getInventariableImage(@Path("img_url") String img_url);

    @Multipart
    @POST("/inventariable")
    Call<Inventariable> addInventariable(@Part MultipartBody.Part imagen,
                                         @Part("tipo")RequestBody tipo,
                                         @Part("nombre")RequestBody nombre,
                                         @Part("descripcion")RequestBody descripcion,
                                         @Part("ubicacion")RequestBody ubicacion);
}
