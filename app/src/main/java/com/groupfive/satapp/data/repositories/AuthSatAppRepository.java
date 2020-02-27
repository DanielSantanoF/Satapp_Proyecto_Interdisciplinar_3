package com.groupfive.satapp.data.repositories;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.groupfive.satapp.commons.SharedPreferencesManager;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.retrofit.LoginServiceGenerator;
import com.groupfive.satapp.retrofit.SatAppService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthSatAppRepository {

    SatAppService service;
    AuthLogin u;


    public AuthSatAppRepository(){
        service = LoginServiceGenerator.createService(SatAppService.class);
    }


    public void login(String username, String password){

        String base = username + ":" + password;
        String authHeader = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        Call<AuthLogin> call = service.postLogin(authHeader,Constants.MASTER_ACCES_TOKEN);
        call.enqueue(new Callback<AuthLogin>() {
            @Override
            public void onResponse(Call<AuthLogin> call, Response<AuthLogin> response) {
                if (response.isSuccessful()) {
                    SharedPreferencesManager.setStringValue(Constants.SHARED_PREFERENCES_AUTH_TOKEN,response.body().getToken());
                    Log.i("user", "" + response.body().token);
                }else{
                    Toast.makeText(MyApp.getContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("user", "viene vacio");
                }
            }

            @Override
            public void onFailure(Call<AuthLogin> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error al hacer la petición.", Toast.LENGTH_SHORT).show();
                Log.i("user", "peor");
            }
        });
    }

    public void register(MultipartBody.Part avatar, RequestBody email, RequestBody password, RequestBody name){

        Call<AuthLoginUser> call = service.register(Constants.MASTER_ACCES_TOKEN,name,email,password,avatar);

        call.enqueue(new Callback<AuthLoginUser>() {
            @Override
            public void onResponse(Call<AuthLoginUser> call, Response<AuthLoginUser> response) {
                if (response.isSuccessful()) {
                    Log.i("usuario",""+response.body());
                    Toast.makeText(MyApp.getContext(), "Usuario creado Correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Upload error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AuthLoginUser> call, Throwable t) {
                Log.e("Error en la petición", t.getMessage());
            }
        });


    }
}
