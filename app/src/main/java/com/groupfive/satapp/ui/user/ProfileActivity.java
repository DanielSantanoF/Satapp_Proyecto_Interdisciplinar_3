package com.groupfive.satapp.ui.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.repositories.UserSatAppRepository;
import com.groupfive.satapp.data.viewModel.UserViewModel;
import com.groupfive.satapp.models.auth.AuthLoginUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class ProfileActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private UserViewModel userViewModel;
    private UserSatAppRepository userSatAppRepository;
    private AuthLoginUser user;
    private TextView nombre, email;
    private CircularImageView circularImageView;
    private FloatingActionButton fab,fab2;
    Uri uriS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userSatAppRepository = new UserSatAppRepository();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        nombre = findViewById(R.id.textViewNameProfile);
        email = findViewById(R.id.textViewEmailProfile);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);

        circularImageView = findViewById(R.id.imageViewDetallePerfil);
        circularImageView.setBorderWidth(3);
        circularImageView.setBorderColor(Color.WHITE);

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });


        userViewModel.getUser().observe(this, new Observer<AuthLoginUser>() {
            @Override
            public void onChanged(AuthLoginUser authLoginUser) {
                user = authLoginUser;
                nombre.setText(user.name);
                email.setText(user.email);

                if (user.picture != null) {
                    userViewModel.getPicture(user.id).observeForever(new Observer<ResponseBody>() {
                        @Override
                        public void onChanged(ResponseBody responseBody) {
                            Bitmap bmp = BitmapFactory.decodeStream(responseBody.byteStream());
                            Glide.with(MyApp.getContext())
                                    .load(bmp)
                                    .centerCrop()
                                    .into(circularImageView);
                        }
                    });
                }else {
                    Glide.with(MyApp.getContext())
                            .load(R.drawable.ic_perfil)
                            .centerCrop()
                            .into(circularImageView);
                }
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ProfileActivity.this);
                alertDialogBuilder.setTitle(R.string.validacion);
                alertDialogBuilder
                        .setMessage(R.string.mensageValidacion)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                userViewModel.deletePhoto(user.id);

                                Glide.with(MyApp.getContext())
                                        .load(R.drawable.ic_perfil)
                                        .centerCrop()
                                        .into(circularImageView);
                            }
                        })
                        .setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uriS != null) {

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

                        userViewModel.updatePhoto(user.id,body);
                        Toast.makeText(ProfileActivity.this, R.string.savePhoto, Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(ProfileActivity.this, R.string.selecPhoto, Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                        .centerCrop()
                        .into(circularImageView);
                uriS = uri;
            }
        }
    }
}
