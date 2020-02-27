package com.groupfive.satapp.data.repositories;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSatAppRepository {

    SatAppService service;

    public UserSatAppRepository() {
        service = SatAppServiceGenerator.createService(SatAppService.class);
    }

    public MutableLiveData<AuthLoginUser> getUser(){
        final MutableLiveData<AuthLoginUser> data = new MutableLiveData<>();

        Call<AuthLoginUser> call = service.getUser();
        call.enqueue(new Callback<AuthLoginUser>() {
            @Override
            public void onResponse(Call<AuthLoginUser> call, Response<AuthLoginUser> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }else {
                    Toast.makeText(MyApp.getContext(), "Error al recivir el usuario.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthLoginUser> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error al realizar la petición de usuario.", Toast.LENGTH_SHORT).show();
            }
        });

        return data;
    }
}
