package com.groupfive.satapp.data.repositories;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.groupfive.satapp.SharedPreferencesManager;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.AuthLogin;
import com.groupfive.satapp.models.Login;
import com.groupfive.satapp.retrofit.LoginServiceGenerator;
import com.groupfive.satapp.retrofit.SatAppService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SatAppRepository {

    SatAppService service;
    AuthLogin u;


    public SatAppRepository(){
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
                    SharedPreferencesManager.setStringValue("token",response.body().getToken());
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
}
