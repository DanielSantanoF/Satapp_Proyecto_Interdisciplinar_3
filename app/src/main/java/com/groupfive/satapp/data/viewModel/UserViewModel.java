package com.groupfive.satapp.data.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.groupfive.satapp.data.repositories.UserSatAppRepository;
import com.groupfive.satapp.models.auth.AuthLoginUser;

public class UserViewModel extends AndroidViewModel {

    private UserSatAppRepository userSatAppRepository;
    private MutableLiveData<AuthLoginUser> user;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userSatAppRepository = new UserSatAppRepository();
    }

    public MutableLiveData<AuthLoginUser> getUser(){
        user = userSatAppRepository.getUser();
        return user;
    }
}
