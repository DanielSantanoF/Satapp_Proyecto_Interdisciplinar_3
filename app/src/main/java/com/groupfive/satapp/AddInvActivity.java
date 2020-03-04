package com.groupfive.satapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.InventariableViewModel;
import com.groupfive.satapp.models.inventariable.Inventariable;
import com.groupfive.satapp.retrofit.SatAppInvService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInvActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etName, etDescription;
    ImageView ivPhoto;
    Spinner spinnerTypes;
    String id, photoCode;
    SatAppInvService service;
    InventariableViewModel inventariableViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inv);

        service = SatAppServiceGenerator.createService(SatAppInvService.class);

        tvTitle = findViewById(R.id.textViewTitle);
        etName = findViewById(R.id.editTextName);
        etDescription = findViewById(R.id.editTextDescription);
        ivPhoto = findViewById(R.id.imageViewPhoto);
        spinnerTypes = findViewById(R.id.spinnerType);

        inventariableViewModel = ViewModelProviders.of(AddInvActivity.this).get(InventariableViewModel.class);

        if(getIntent().getExtras().getString("id") != null) {
            tvTitle.setText(getResources().getString(R.string.editInv));
            id = getIntent().getExtras().getString("id");

            getInventariable();

        } else {
            tvTitle.setText(getResources().getString(R.string.addInv));
        }
    }

    public void getInventariable() {
        inventariableViewModel.getInventariable(id).observe(AddInvActivity.this, new Observer<Inventariable>() {
            @Override
            public void onChanged(Inventariable inventariable) {
                etName.setText(inventariable.getNombre());
                etDescription.setText(inventariable.getDescripcion());

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
                                        .with(AddInvActivity.this)
                                        .load(bmp)
                                        .error(Glide.with(AddInvActivity.this).load(R.drawable.ic_interrogation))
                                        .thumbnail(Glide.with(AddInvActivity.this).load(Constants.LOADING_GIF))
                                        .into(ivPhoto);
                            } else {
                                Glide
                                        .with(AddInvActivity.this)
                                        .load(R.drawable.ic_faqs)
                                        .error(Glide.with(AddInvActivity.this).load(R.drawable.ic_interrogation))
                                        .thumbnail(Glide.with(AddInvActivity.this).load(Constants.LOADING_GIF))
                                        .into(ivPhoto);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(AddInvActivity.this, "Error loading picture", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
