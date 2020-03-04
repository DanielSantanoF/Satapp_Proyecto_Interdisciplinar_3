package com.groupfive.satapp.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.repositories.UserSatAppRepository;
import com.groupfive.satapp.data.viewModel.UserViewModel;
import com.groupfive.satapp.models.auth.AuthLoginUser;

import okhttp3.ResponseBody;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private UserSatAppRepository userSatAppRepository;
    private AuthLoginUser user;
    private ImageView foto;
    private TextView nombre, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userSatAppRepository = new UserSatAppRepository();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        foto= findViewById(R.id.imageViewDetallePerfil);
        nombre = findViewById(R.id.textViewNombre);
        email = findViewById(R.id.textViewEmailDetail);


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
                                    .into(foto);
                        }
                    });
                }else {
                    Glide.with(MyApp.getContext())
                            .load(R.drawable.ic_perfil)
                            .centerCrop()
                            .into(foto);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
