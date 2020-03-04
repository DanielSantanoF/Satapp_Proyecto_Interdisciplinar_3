package com.groupfive.satapp.ui.tickets.ticketdetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.TicketByIdViewModel;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;
import com.groupfive.satapp.transformations.DateTransformation;
import com.groupfive.satapp.ui.tickets.addtechnician.AddThechnicianShowActivity;
import com.groupfive.satapp.ui.tickets.changestate.ChangeStateTicketActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailActivity extends AppCompatActivity {

    DateTransformation dateTransformer = new DateTransformation();
    String ticketId;
    TicketByIdViewModel ticketByIdViewModel;
    ImageView ivToolbar;
    SatAppService service;
    TextView txtTitle, txtCreatedByName, txtEmailCreatedBy, txtDate, txtState, txtDescription;
    Button btnImgs;
    TicketModel ticketDetail;
    FloatingActionButton fab;
    //ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        ivToolbar = findViewById(R.id.imageViewDetailTicketToolbar);
        txtTitle = findViewById(R.id.textViewTitleTicketDetail);
        txtCreatedByName = findViewById(R.id.textViewCreatedByTicketDetail);
        txtEmailCreatedBy = findViewById(R.id.textViewCreatedByEmailTicektDetail);
        txtDate = findViewById(R.id.textViewDateTicketDetail);
        txtState = findViewById(R.id.textViewStateTicketDetail);
        txtDescription = findViewById(R.id.textViewDescriptionTicketDetail);
        btnImgs = findViewById(R.id.buttonFotosTicektDetail);
       // progressBar = findViewById(R.id.progressBarTicketDetail);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();

        ticketByIdViewModel = new ViewModelProvider(this).get(TicketByIdViewModel.class);
        ticketByIdViewModel.setTicketId(ticketId);
        service = SatAppServiceGenerator.createService(SatAppService.class);

        loadTicket();

        fab = (FloatingActionButton) findViewById(R.id.fabEditTicketDetail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EdtiTicketDialogFragment dialog = new EdtiTicketDialogFragment(TicketDetailActivity.this, ticketId);
                dialog.show(getSupportFragmentManager(), "EdtiTicketDialogFragment");
                //TODO REVISAR RECARGAR TICKET AL EDITARLO
                dialog.onDismiss(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadTicket();
                    }
                });
            }
        });



        btnImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTicket();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticket_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_ticket:
                AlertDialog.Builder alert = new AlertDialog.Builder(TicketDetailActivity.this);
                alert.setTitle(getResources().getString(R.string.delete_ticket_title));
                alert.setMessage(getResources().getString(R.string.delete_ticket_message));
                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<ResponseBody> call = service.deleteTicketById(ticketId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 204){
                                    Toast.makeText(TicketDetailActivity.this, getResources().getString(R.string.ticket_deleted), Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(TicketDetailActivity.this, getResources().getString(R.string.error_ticket_deleted), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            case R.id.action_share_ticket:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ticket: " + ticketDetail.getTitulo() + ", " + getResources().getString(R.string.share_ticket_content) + " " + ticketDetail.getDescripcion());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Compartir");
                startActivity(shareIntent);
                return true;
            case R.id.action_add_thecnical:
                Intent addTechnicanl = new Intent(TicketDetailActivity.this, AddThechnicianShowActivity.class);
                addTechnicanl.putExtra(Constants.EXTRA_TICKET_ID, String.valueOf(ticketId));
                startActivity(addTechnicanl);
                return true;
            case R.id.action_change_state:
                Intent changeState = new Intent(TicketDetailActivity.this, ChangeStateTicketActivity.class);
                changeState.putExtra(Constants.EXTRA_TICKET_ID, String.valueOf(ticketId));
                startActivity(changeState);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadTicket(){
        ticketByIdViewModel.getTicketById().observe(this, new Observer<TicketModel>() {
            @Override
            public void onChanged(TicketModel ticketModel) {
                ticketDetail = ticketModel;
                String string = ticketModel.getFotos().get(0);
                String[] parts = string.split("/");
                String part1 = parts[3];
                String part2 = parts[4];
                Call<ResponseBody> call = service.getTicketImg(part1, part2);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                Glide
                                        .with(TicketDetailActivity.this)
                                        .load(bmp)
                                        .error(Glide.with(TicketDetailActivity.this).load(R.drawable.image_not_loaded_icon))
                                        .thumbnail(Glide.with(TicketDetailActivity.this).load(R.drawable.loading_gif))
                                        .into(ivToolbar);
                            }
                        } else {
                            Glide
                                    .with(TicketDetailActivity.this)
                                    .load(R.drawable.image_not_loaded_icon)
                                    .error(Glide.with(TicketDetailActivity.this).load(R.drawable.image_not_loaded_icon))
                                    .thumbnail(Glide.with(TicketDetailActivity.this).load(R.drawable.loading_gif))
                                    .into(ivToolbar);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(TicketDetailActivity.this, getResources().getString(R.string.error_loading_ticket), Toast.LENGTH_SHORT).show();
                    }
                });
                txtTitle.setText(ticketModel.getTitulo());
                txtCreatedByName.setText(getResources().getString(R.string.name_created_by) + " " + ticketModel.getCreadoPor().getName());
                txtEmailCreatedBy.setText(getResources().getString(R.string.contact_created_by) + " " + ticketModel.getCreadoPor().getEmail());
                String stringDate = ticketModel.getFechaCreacion();
                String[] partsDate = stringDate.split("T");
                String date = partsDate[0];
                String dateToShow = dateTransformer.dateTransformation(date);
                txtDate.setText(dateToShow);
                txtState.setText(ticketModel.getEstado());
                txtDescription.setText(ticketModel.getDescripcion());
                //progressBar.setVisibility(View.INVISIBLE);
                ivToolbar.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                txtCreatedByName.setVisibility(View.VISIBLE);
                txtEmailCreatedBy.setVisibility(View.VISIBLE);
                txtDate.setVisibility(View.VISIBLE);
                txtState.setVisibility(View.VISIBLE);
                txtDescription.setVisibility(View.VISIBLE);
                btnImgs.setVisibility(View.VISIBLE);
                fab.show();
                //progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

}
