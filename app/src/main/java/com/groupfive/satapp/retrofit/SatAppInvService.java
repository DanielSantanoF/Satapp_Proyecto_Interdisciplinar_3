package com.groupfive.satapp.retrofit;

import com.groupfive.satapp.models.inventariable.Inventariable;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SatAppInvService {

    @GET("/inventariable")
    Call<List<Inventariable>> getInventariables();

    @Multipart
    @POST("/inventariable")
    Call<Inventariable> addInventariable(@Part MultipartBody.Part imagen,
                                         @Part("tipo")RequestBody tipo,
                                         @Part("nombre")RequestBody nombre,
                                         @Part("descripcion")RequestBody descripcion,
                                         @Part("ubicacion")RequestBody ubicacion);
}
