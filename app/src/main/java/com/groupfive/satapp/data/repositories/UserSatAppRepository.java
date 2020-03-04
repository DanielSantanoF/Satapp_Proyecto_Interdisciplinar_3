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

import okhttp3.MultipartBody;
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

    public void putValidated(String id){
        Call<AuthLoginUser> call = service.putValidated(id);
        call.enqueue(new Callback<AuthLoginUser>() {
            @Override
            public void onResponse(Call<AuthLoginUser> call, Response<AuthLoginUser> response) {
                if (response.isSuccessful()){
                    Log.i("Validated","Usuario Validado");
                }else {
                    Log.e("Validated","Error al devolver el usuario validado");
                }
            }

            @Override
            public void onFailure(Call<AuthLoginUser> call, Throwable t) {
                Log.e("Validated","Error al realizar la petición de validación");
            }
        });
    }

    public void deleteUser(String id){
        Call<ResponseBody> call = service.deleteUser(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.i("Deleted","Usuario borrado");
                }else {
                    Log.e("Deleted","Error al devolver el usuario borrado.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Deleted","Error al realizar la paticion de borrado de usuario.");
            }
        });
    }

    public void putTecnico(String id){
        Call<AuthLoginUser> call = service.putTecnico(id);
        call.enqueue(new Callback<AuthLoginUser>() {
            @Override
            public void onResponse(Call<AuthLoginUser> call, Response<AuthLoginUser> response) {
                if (response.isSuccessful()){
                    Log.i("Tecnico","Usuario ascendido a tecnico.");
                }else {
                    Log.e("Tecnico","Error al devolver el usuario ascendido a tecnico.");
                }
            }

            @Override
            public void onFailure(Call<AuthLoginUser> call, Throwable t) {
                Log.e("Tecnico","Error realizar la petición de ascender a tecnico.");
            }
        });
    }

    public void updatePhoto(String id, MultipartBody.Part avatar){
        Call<AuthLoginUser> call = service.updatePhoto(id,avatar);
        call.enqueue(new Callback<AuthLoginUser>() {
            @Override
            public void onResponse(Call<AuthLoginUser> call, Response<AuthLoginUser> response) {
                if (response.isSuccessful()){
                    Log.i("uptade","Foto actualizada correctamente");
                }else{
                    Log.e("update","Error al recivir el usuario cuando se cambia la foto");
                }
            }

            @Override
            public void onFailure(Call<AuthLoginUser> call, Throwable t) {
                Log.e("update","Error al realizar la peticion de actualización de foto");
            }
        });
    }

    public void deletePhoto (String id){
        Call<ResponseBody> call = service.deletePhoto(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.i("deletePhoto","Foto borrada correctamente");
                }else{
                    Log.e("deletePhoto","Error al recivir el usuario sn foto");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("deletePhoto","Error al realizar la petición de borrado de foto");
            }
        });
    }
}
