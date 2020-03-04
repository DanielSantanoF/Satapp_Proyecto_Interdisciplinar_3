package com.groupfive.satapp.data.repositories;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SatAppRepository {

    SatAppService service;
    SatAppServiceGenerator serviceGenerator;
    MutableLiveData<List<TicketModel>> allTickets;
    MutableLiveData<List<TicketModel>> allMyTickets;
    MutableLiveData<List<TicketModel>> assignedTickets;
    MutableLiveData<List<AuthLoginUser>> allUsers;
    MutableLiveData<TicketModel> ticketById;

    public SatAppRepository() {
        service = serviceGenerator.createService(SatAppService.class);
    }

    public MutableLiveData<List<TicketModel>> getAllTickets() {
        final MutableLiveData<List<TicketModel>> data = new MutableLiveData<>();

        Call<List<TicketModel>> call = service.getAllTickets(null, null, null,null, null);
        call.enqueue(new Callback<List<TicketModel>>() {
            @Override
            public void onResponse(Call<List<TicketModel>> call, Response<List<TicketModel>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error on the response from the Api", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TicketModel>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error in the connection", Toast.LENGTH_SHORT).show();
            }
        });
        allTickets = data;
        return data;
    }

    public MutableLiveData<TicketModel> getTicketById(String id) {
        final MutableLiveData<TicketModel> data = new MutableLiveData<>();

        Call<TicketModel> call = service.getTicketById(id);
        call.enqueue(new Callback<TicketModel>() {
            @Override
            public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error on the response from the Api", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketModel> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error in the connection at ticket by id", Toast.LENGTH_SHORT).show();
            }
        });
        ticketById = data;
        return data;
    }

    public MutableLiveData<List<AuthLoginUser>> getAllUsers() {
        final MutableLiveData<List<AuthLoginUser>> data = new MutableLiveData<>();

        Call<List<AuthLoginUser>> call = service.getallUsers(null, null, null, null, null);
        call.enqueue(new Callback<List<AuthLoginUser>>() {
            @Override
            public void onResponse(Call<List<AuthLoginUser>> call, Response<List<AuthLoginUser>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error on the response from the Api", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AuthLoginUser>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error in the connection", Toast.LENGTH_SHORT).show();
            }
        });
        allUsers = data;
        return data;
    }

    public MutableLiveData<List<TicketModel>> getAllMyTickets() {
        final MutableLiveData<List<TicketModel>> data = new MutableLiveData<>();

        Call<List<TicketModel>> call = service.getAllMyTickets(null, null, null, null, null);
        call.enqueue(new Callback<List<TicketModel>>() {
            @Override
            public void onResponse(Call<List<TicketModel>> call, Response<List<TicketModel>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Error on the response from the Api", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TicketModel>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error in the connection", Toast.LENGTH_SHORT).show();
            }
        });
        allMyTickets = data;
        return data;
    }

    public MutableLiveData<List<TicketModel>> getAssignedTickets() {
        final MutableLiveData<List<TicketModel>> data = new MutableLiveData<>();

        Call<List<TicketModel>> call = service.getAssignedTickets(null, null, null, null, null);
        call.enqueue(new Callback<List<TicketModel>>() {
            @Override
            public void onResponse(Call<List<TicketModel>> call, Response<List<TicketModel>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "You dont have any ticket assigned", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TicketModel>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error in the connection", Toast.LENGTH_SHORT).show();
            }
        });
        assignedTickets = data;
        return data;
    }

}
