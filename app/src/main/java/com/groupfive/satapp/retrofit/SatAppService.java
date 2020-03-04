package com.groupfive.satapp.retrofit;


import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.models.tickets.AddTechnician;
import com.groupfive.satapp.models.tickets.EditTicketBody;
import com.groupfive.satapp.models.tickets.TicketModel;

import java.util.List;

import kotlin.PublishedApi;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SatAppService {

    @POST("/auth")
    Call<AuthLogin> postLogin(@Header("Authorization") String authHeader,
            @Query("access_token") String masterAccesToken);


    @Multipart
    @POST("/users")
    Call<AuthLoginUser> register(@Query("access_token") String masterAccesToken,
                                 @Part("name") RequestBody name,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part MultipartBody.Part avatar);

    @GET("/users/me")
    Call<AuthLoginUser> getUser();

    @GET("/users")
    Call<List<AuthLoginUser>> getAllUsers();

    @GET("/users/no-validated")
    Call<List<AuthLoginUser>> getUsersValidated();

    @GET("users/img/{id}")
    Call<ResponseBody> getImg(@Path("id")String id);

    @PUT("/users/{id}/validate")
    Call<AuthLoginUser> putValidated(@Path("id")String id);

    @PUT("/users/{id}/tecnico")
    Call<AuthLoginUser> putTecnico(@Path("id")String id);

    @DELETE("/users/{id}")
    Call<ResponseBody> deleteUser(@Path("id")String id);

//    @Multipart
//    @POST("/ticket")
//    Call<TicketModel> postNewTicket(@Part MultipartBody.Part fotos1,
//                                    @Part MultipartBody.Part fotos2,
//                                    @Part("titulo") RequestBody titulo,
//                                    @Part("descripcion") RequestBody descripcion);


    @Multipart
    @POST("/ticket")
    Call<TicketModel> postNewTicket(@Part List<MultipartBody.Part> fotos,
                                    @Part("titulo") RequestBody titulo,
                                    @Part("descripcion") RequestBody descripcion);

    /* ALL QUERYS ARE OPTIONALS IF ISN'T NEED SEND IT AS A null
    Query -> q: Query to search
    Query -> page: Page number
    Query -> limit: Amount of returned items
    Query -> sort: Order of returned items
    Query -> fields: Fields to be returned
     */
    @GET("/ticket")
    Call<List<TicketModel>> getAllTickets(@Query("q") String toSearch,
                                          @Query("page") Integer pageNumber,
                                          @Query("limit") Integer limitOfElements,
                                          @Query("sort") String orderOfReturnedItems,
                                          @Query("fields") String fieldsToBeReturned);

    @GET("/ticket/{id}")
    Call<TicketModel> getTicketById(@Path("id") String id);

    @DELETE("/ticket/{id}")
    Call<ResponseBody> deleteTicketById(@Path("id") String id);

    @PUT("/ticket/{id}")
    Call<TicketModel> updateTicketById(@Path("id") String id, @Body EditTicketBody editTicketBody);

    @GET("/ticket/img/{imgUrl}/{imgNumber}")
    Call<ResponseBody> getTicketImg(@Path("imgUrl") String url, @Path("imgNumber") String urlNumber);

    @GET("/users")
    Call<List<AuthLoginUser>> getallUsers(@Query("q") String toSearch,
                                          @Query("page") Integer pageNumber,
                                          @Query("limit") Integer limitOfElements,
                                          @Query("sort") String orderOfReturnedItems,
                                          @Query("fields") String fieldsToBeReturned);

    @PUT("/ticket/{id}/asignar")
    Call<TicketModel> updateTickeAddTechnician(@Path("id") String id, @Body AddTechnician addTechnician);

    @Multipart
    @PUT("/users/{id}/img")
    Call<AuthLoginUser> updatePhoto(@Path("id")String id,
                                    @Part MultipartBody.Part avatar);

    @DELETE("/users/{id}/img")
    Call<ResponseBody> deletePhoto(@Path("id")String id);
}
