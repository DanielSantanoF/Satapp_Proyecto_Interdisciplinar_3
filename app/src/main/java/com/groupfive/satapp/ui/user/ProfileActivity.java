package com.groupfive.satapp.ui.user;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ImageView;

import com.groupfive.satapp.R;
import com.groupfive.satapp.data.repositories.UserSatAppRepository;
import com.groupfive.satapp.data.viewModel.UserViewModel;
import com.groupfive.satapp.models.auth.AuthLoginUser;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private UserSatAppRepository userSatAppRepository;
    private AuthLoginUser user;
    private ImageView foto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userSatAppRepository = new UserSatAppRepository();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        foto= findViewById(R.id.imageViewDetallePerfil);


        userViewModel.getUser().observe(this, new Observer<AuthLoginUser>() {
            @Override
            public void onChanged(AuthLoginUser authLoginUser) {
                user = authLoginUser;
                userSatAppRepository.printImg(foto,user.picture);
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
