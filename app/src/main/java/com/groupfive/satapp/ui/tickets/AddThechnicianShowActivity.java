package com.groupfive.satapp.ui.tickets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.listeners.IAddTechnicianListener;
import com.groupfive.satapp.models.auth.AuthLogin;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddThechnicianShowActivity extends AppCompatActivity implements IAddTechnicianListener {

    String ticketId;
    SatAppService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thechnician_show);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();

    }

    @Override
    public void onAllTicketsItemClick(AuthLogin authLogin) {
        service = SatAppServiceGenerator.createService(SatAppService.class);
        Call<TicketModel> call = service.updateTickeAddTechnician(ticketId, authLogin.getUser().getId());
        call.enqueue(new Callback<TicketModel>() {
            @Override
            public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                Toast.makeText(AddThechnicianShowActivity.this, "Technician added", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<TicketModel> call, Throwable t) {
                Toast.makeText(AddThechnicianShowActivity.this, "Error in the assignament", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
