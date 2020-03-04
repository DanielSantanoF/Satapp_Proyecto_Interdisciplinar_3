package com.groupfive.satapp.ui.tickets.changestate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

public class ChangeStateTicketActivity extends AppCompatActivity {

    String ticketId;
    SatAppService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_state_ticket);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();
        service = SatAppServiceGenerator.createService(SatAppService.class);
    }
}
