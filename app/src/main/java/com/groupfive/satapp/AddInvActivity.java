package com.groupfive.satapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.groupfive.satapp.commons.Constants;
import com.groupfive.satapp.data.viewModel.InventariableViewModel;
import com.groupfive.satapp.models.inventariable.Inventariable;
import com.groupfive.satapp.models.inventariable.TipoInventariable;
import com.groupfive.satapp.retrofit.SatAppInvService;
import com.groupfive.satapp.retrofit.SatAppServiceGenerator;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddInvActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    TextView tvTitle;
    EditText etName, etDescription, etLocation;
    Button btAction;
    ImageView ivPhoto;
    Uri uriS;
    Spinner spinnerTypes;
    String id, photoCode;
    SatAppInvService service;
    ArrayAdapter arrayAdapter;
    InventariableViewModel inventariableViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inv);

        service = SatAppServiceGenerator.createService(SatAppInvService.class);

        tvTitle = findViewById(R.id.textViewTitle);
        etName = findViewById(R.id.editTextName);
        etDescription = findViewById(R.id.editTextDescription);
        etLocation = findViewById(R.id.editTextUbicacion);
        ivPhoto = findViewById(R.id.imageViewPhoto);
        spinnerTypes = findViewById(R.id.spinnerType);
        btAction = findViewById(R.id.buttonAction);

        arrayAdapter = new ArrayAdapter(AddInvActivity.this, R.layout.support_simple_spinner_dropdown_item, TipoInventariable.values());
        spinnerTypes.setAdapter(arrayAdapter);

        inventariableViewModel = ViewModelProviders.of(AddInvActivity.this).get(InventariableViewModel.class);

        if(getIntent().getExtras().getString("id") != null) {
            tvTitle.setText(getResources().getString(R.string.editInv));
            id = getIntent().getExtras().getString("id");

            getInventariable();

            btAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uriS);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        int cantBytes;
                        byte[] buffer = new byte[1024*4];

                        while ((cantBytes = bufferedInputStream.read(buffer,0,1024*4)) != -1) {
                            baos.write(buffer,0,cantBytes);
                        }


                        RequestBody requestFile =
                                RequestBody.create(baos.toByteArray(),
                                        MediaType.parse(getContentResolver().getType(uriS)));


                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("avatar", "avatar", requestFile);


                        RequestBody nameRequest = RequestBody.create(MultipartBody.FORM,etName.getText().toString());
                        RequestBody descriptionRequest = RequestBody.create(MultipartBody.FORM,etDescription.getText().toString());
                        RequestBody locationRequest = RequestBody.create(MultipartBody.FORM,etLocation.getText().toString());


//                        service.putInventariable(id, body,nameRequest,descriptionRequest,locationRequest);
                        finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performFileSearch();
                }
            });

        } else {
            tvTitle.setText(getResources().getString(R.string.addInv));
        }
    }

    public void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i("Filechooser URI", "Uri: " + uri.toString());
                Glide
                        .with(this)
                        .load(uri)
                        .into(ivPhoto);
                uriS = uri;
            }
        }
    }

    public void getInventariable() {
        inventariableViewModel.getInventariable(id).observe(AddInvActivity.this, new Observer<Inventariable>() {
            @Override
            public void onChanged(Inventariable inventariable) {
                etName.setText(inventariable.getNombre());
                etDescription.setText(inventariable.getDescripcion());
                etLocation.setText(inventariable.getUbicacion());



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
