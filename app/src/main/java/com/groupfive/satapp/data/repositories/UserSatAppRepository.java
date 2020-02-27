package com.groupfive.satapp.data.repositories;

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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSatAppRepository {

    SatAppService service;
    MutableLiveData<AuthLoginUser> u;

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
        u = data;
        return data;
    }

    public void printImg(final ImageView perfil, String id){
        Call<ResponseBody> call = service.getImg(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Glide.with(MyApp.getContext())
                            .load(response.body())
                            .into(perfil);
                }else {
                    Toast.makeText(MyApp.getContext(), "Error al recivir la imagen de perfil.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Glide.with(MyApp.getContext())
                        .load(R.drawable.ic_perfil)
                        .transform(new CircleCrop())
                        .into(perfil);
                Toast.makeText(MyApp.getContext(), "Error al realizar la petición de imagen de perfil.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
