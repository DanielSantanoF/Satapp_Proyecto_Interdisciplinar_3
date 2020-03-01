package com.groupfive.satapp.ui.tickets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.TicketByIdViewModel;
import com.groupfive.satapp.models.tickets.TicketModel;
import com.groupfive.satapp.retrofit.SatAppService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;
import com.groupfive.satapp.transformations.DateTransformation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketDetailScrollingActivity extends AppCompatActivity {

    DateTransformation dateTransformer = new DateTransformation();
    String ticketId;
    TicketByIdViewModel ticketByIdViewModel;
    ImageView ivToolbar;
    SatAppService service;
    TextView txtTitle, txtCreatedByName, txtEmailCreatedBy, txtDate, txtState, txtDescription;
    CollapsingToolbarLayout toolbarLayout;
    Button btnImgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        ivToolbar = findViewById(R.id.imageViewDetailTicketToolbar);
        txtTitle = findViewById(R.id.textViewTitleTicketDetail);
        txtCreatedByName = findViewById(R.id.textViewCreatedByTicketDetail);
        txtEmailCreatedBy = findViewById(R.id.textViewCreatedByEmailTicektDetail);
        txtDate = findViewById(R.id.textViewDateTicketDetail);
        txtState = findViewById(R.id.textViewStateTicketDetail);
        txtDescription = findViewById(R.id.textViewDescriptionTicketDetail);
        btnImgs = findViewById(R.id.buttonFotosTicektDetail);

        ticketId = getIntent().getExtras().get(Constants.EXTRA_TICKET_ID).toString();
        ticketByIdViewModel = new ViewModelProvider(this).get(TicketByIdViewModel.class);
        ticketByIdViewModel.setTicketId(ticketId);
        service = SatAppServiceGenerator.createService(SatAppService.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadTicket();

        btnImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticket_detail_scrolling, menu);
        return true;
    }

    public void loadTicket(){
        ticketByIdViewModel.getTicketById().observe(this, new Observer<TicketModel>() {
            @Override
            public void onChanged(TicketModel ticketModel) {
                toolbarLayout.setTitle(ticketModel.getTitulo());
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
                                        .with(TicketDetailScrollingActivity.this)
                                        .load(bmp)
                                        .error(Glide.with(TicketDetailScrollingActivity.this).load(R.drawable.image_not_loaded_icon))
                                        .thumbnail(Glide.with(TicketDetailScrollingActivity.this).load(R.drawable.loading_gif))
                                        .into(ivToolbar);
                            }
                        } else {
                            Glide
                                    .with(TicketDetailScrollingActivity.this)
                                    .load(R.drawable.image_not_loaded_icon)
                                    .error(Glide.with(TicketDetailScrollingActivity.this).load(R.drawable.image_not_loaded_icon))
                                    .thumbnail(Glide.with(TicketDetailScrollingActivity.this).load(R.drawable.loading_gif))
                                    .into(ivToolbar);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

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
            }
        });
    }

}
