package com.groupfive.satapp.data.repositories;

import android.util.Log;
import android.widget.Toast;

import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.AuthLogin;
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

    public AuthLogin login(String username, String password){

        Call<AuthLogin> call = service.postLogin(Constants.MASTER_ACCES_TOKEN,username,password);
        call.enqueue(new Callback<AuthLogin>() {
            @Override
            public void onResponse(Call<AuthLogin> call, Response<AuthLogin> response) {
                if (response.isSuccessful()) {
                    u = response.body();
                    Log.i("user", "" + response.body());
                }else{
                    Toast.makeText(MyApp.getContext(), "Error al loguearte.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthLogin> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error al hacer la petición.", Toast.LENGTH_SHORT).show();
            }
        });

        return u;
    }
}
