package com.groupfive.satapp.data.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.groupfive.satapp.data.repositories.InventariableRepository;
import com.groupfive.satapp.models.inventariable.Inventariable;

import java.util.List;

public class InventariableViewModel extends AndroidViewModel {
    InventariableRepository inventariableRepository;
    Context ctx;
    LiveData<List<Inventariable>> direct;

    public InventariableViewModel(@NonNull Application application) {
        super(application);
        this.inventariableRepository = new InventariableRepository();
        this.ctx = application.getApplicationContext();
    }

    public LiveData<List<Inventariable>> getAllSeries() {
        direct = inventariableRepository.getAllInventariables();
        return direct;
    }
}
