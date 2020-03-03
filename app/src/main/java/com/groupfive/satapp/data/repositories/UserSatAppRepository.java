package com.groupfive.satapp.data.repositories;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSatAppRepository {

    SatAppService service;
    MutableLiveData<AuthLoginUser> user;
    MutableLiveData<List<AuthLoginUser>> allUser;
    MutableLiveData<List<AuthLoginUser>> usersValidated;

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
        user = data;
        return data;
    }


    public MutableLiveData<ResponseBody> getPicture(String id){
        final MutableLiveData<ResponseBody> data = new MutableLiveData<>();
        Call<ResponseBody> call = service.getImg(id);
        call .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        return data;
    }

    public MutableLiveData<List<AuthLoginUser>> getAllUsers(){
        final MutableLiveData<List<AuthLoginUser>> data = new MutableLiveData<>();

        Call<List<AuthLoginUser>> call = service.getAllUsers();
        call.enqueue(new Callback<List<AuthLoginUser>>() {
            @Override
            public void onResponse(Call<List<AuthLoginUser>> call, Response<List<AuthLoginUser>> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }else {
                    Toast.makeText(MyApp.getContext(), "Error al recivir los usuarios.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<AuthLoginUser>> call, Throwable t) {

            }
        });
        allUser = data;
        return data;
    }


    public MutableLiveData<List<AuthLoginUser>> getUsersValidate(){
        final MutableLiveData<List<AuthLoginUser>> data = new MutableLiveData<>();

        Call<List<AuthLoginUser>> call = service.getUsersValidated();
        call.enqueue(new Callback<List<AuthLoginUser>>() {
            @Override
            public void onResponse(Call<List<AuthLoginUser>> call, Response<List<AuthLoginUser>> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }else {
                    Toast.makeText(MyApp.getContext(), "Error al recivir los usuarios sin validar.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<AuthLoginUser>> call, Throwable t) {

            }
        });
        usersValidated = data;
        return data;
    }
}
