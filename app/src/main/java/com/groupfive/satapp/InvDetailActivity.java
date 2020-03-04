package com.groupfive.satapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.InventariableViewModel;
import com.groupfive.satapp.models.inventariable.Inventariable;
import com.groupfive.satapp.retrofit.SatAppInvService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvDetailActivity extends AppCompatActivity {
    private TextView tvName, tvDescription, tvCreate, tvUpdate, tvLocation;
    private ImageView ivPhoto, ivType;
    private InventariableViewModel inventariableViewModel;
    private SatAppInvService service;
    private Inventariable inventariable;
    private String id;
    private String photoCode;
    private LocalDate changerCreated, changerUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inv_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InvDetailActivity.this, AddInvActivity.class);
                i.putExtra("id", id);
                startActivity(i);
            }
        });

        service = SatAppServiceGenerator.createService(SatAppInvService.class);

        inventariableViewModel = ViewModelProviders.of(InvDetailActivity.this).get(InventariableViewModel.class);

        tvName = findViewById(R.id.textViewName);
        tvDescription = findViewById(R.id.textViewDescription);
        tvCreate = findViewById(R.id.textViewCreate);
        tvUpdate = findViewById(R.id.textViewUpdate);
        tvLocation = findViewById(R.id.textViewLocation);
        ivPhoto = findViewById(R.id.imageViewPhotoPlus);
        ivType = findViewById(R.id.imageViewType);

        id = getIntent().getExtras().getString("id");

        getInventariable();


    }

    public void getInventariable() {
        inventariableViewModel.getInventariable(id).observe(InvDetailActivity.this, new Observer<Inventariable>() {
            @Override
            public void onChanged(Inventariable inventariable) {

                changerCreated = LocalDate.parse(inventariable.getCreatedAt().split("T")[0], DateTimeFormat.forPattern("yyyy-mm-dd"));
                changerUpdated = LocalDate.parse(inventariable.getUpdatedAt().split("T")[0], DateTimeFormat.forPattern("yyyy-mm-dd"));
                tvName.setText(inventariable.getNombre());
                tvDescription.setText(inventariable.getDescripcion());
                tvCreate.setText(Constants.FORMATTER.print(changerCreated));
                tvUpdate.setText(Constants.FORMATTER.print(changerUpdated));
                tvDescription.setText(inventariable.getDescripcion());
                tvLocation.setText(inventariable.getUbicacion());

                switch(inventariable.getTipo()) {
                    case "PC":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_ordenador_personal)).into(ivType);
                        break;
                    case "MONITOR":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_television)).into(ivType);
                        break;
                    case "RED":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_wifi)).into(ivType);
                        break;
                    case "IMPRESORA":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_herramientas_y_utensilios)).into(ivType);
                        break;
                    case "OTRO":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_desconocido)).into(ivType);
                        break;
                    case "PERIFERICO":
                        Glide.with(InvDetailActivity.this).load(getResources().getDrawable(R.drawable.ic_teclado)).into(ivType);
                        break;
                }
                photoCode = inventariable.getImagen().split("/")[3];
                Call<ResponseBody> call = service.getInventariableImage(photoCode);
//        Toast.makeText(ctx, photoCode, Toast.LENGTH_SHORT).show();

                call.enqueue(new Callback<ResponseBody>() {
                    @SneakyThrows
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                                Glide
                                        .with(InvDetailActivity.this)
                                        .load(bmp)
                                        .error(Glide.with(InvDetailActivity.this).load(R.drawable.ic_interrogation))
                                        .thumbnail(Glide.with(InvDetailActivity.this).load(Constants.LOADING_GIF))
                                        .into(ivPhoto);
                            } else {
                                Glide
                                        .with(InvDetailActivity.this)
                                        .load(R.drawable.ic_faqs)
                                        .error(Glide.with(InvDetailActivity.this).load(R.drawable.ic_interrogation))
                                        .thumbnail(Glide.with(InvDetailActivity.this).load(Constants.LOADING_GIF))
                                        .into(ivPhoto);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(InvDetailActivity.this, "Error loading picture", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
