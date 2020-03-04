package com.groupfive.satapp.ui.tickets.fotosticketdetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.TicketByIdViewModel;
import com.groupfive.satapp.listeners.IFotoTicketDetailListener;

public class ShowFotosTicketDetailActivity extends AppCompatActivity implements IFotoTicketDetailListener {

    String ticketId;
    TicketByIdViewModel ticketByIdViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fotos_ticket_detail);

        ticketByIdViewModel = new ViewModelProvider(this).get(TicketByIdViewModel.class);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();

        ticketByIdViewModel.setTicketId(ticketId);

    }

    @Override
    public void onFotoItemClick(Bitmap bitmap) {

    }
}
