package com.groupfive.satapp.ui.tickets.assignedtickets;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.listeners.IAssignedTicketsListener;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.ui.tickets.ticketdetail.TicketDetailActivity;
import com.groupfive.satapp.ui.tickets.usertickets.ShowAllMyTicketsActivity;

public class ShowAssignedTicketsActivity extends AppCompatActivity implements IAssignedTicketsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_assigned_tickets);
    }

    @Override
    public void onAssignedTicketItemClick(TicketModel ticketModel) {
        Intent i = new Intent(ShowAssignedTicketsActivity.this, TicketDetailActivity.class);
        i.putExtra(Constants.EXTRA_TICKET_ID, String.valueOf(ticketModel.getId()));
        startActivity(i);
    }
}
