package com.groupfive.satapp.ui.tickets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EdtiTicketDialogFragment extends DialogFragment {
    View v;
    Context ctx;
    EditText edTitle, edDescription;
    SatAppService service;
    String idTicket;

    public EdtiTicketDialogFragment(Context ctx, String ticketId) {
        this.ctx = ctx;
        this.idTicket = ticketId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.edit_ticket_dialog_title));
        builder.setMessage(getResources().getString(R.string.edit_ticket_dialog_message));

        builder.setCancelable(true);

        v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_ticket, null);
        builder.setView(v);

        edTitle = v.findViewById(R.id.editTextTitleEditTicket);
        edDescription = v.findViewById(R.id.editTextDescriptionEditTicket);

        builder.setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = edTitle.getText().toString();
                String description = edDescription.getText().toString();

                if(title.isEmpty() || description.isEmpty()) {
                    if(title.isEmpty()) {
                        edTitle.setError(getResources().getString(R.string.new_ticket_dialog_error_need_title));
                    }
                    if(description.isEmpty()) {
                        edDescription.setError(getResources().getString(R.string.new_ticket_dialog_error_need_description));
                    }
                } else {
                    service = SatAppServiceGenerator.createService(SatAppService.class);
                    Call<TicketModel> call = service.updateTicketById(idTicket, title, description);
                    call.enqueue(new Callback<TicketModel>() {
                        @Override
                        public void onResponse(Call<TicketModel> call, Response<TicketModel> response) {
                            Toast.makeText(MyApp.getContext(), "Ticket edited", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<TicketModel> call, Throwable t) {
                            Toast.makeText(MyApp.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void onDismiss(DialogInterface.OnDismissListener onDismissListener) {
    }
}
