package com.groupfive.satapp.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.ui.MainActivity;
import com.groupfive.satapp.R;
import com.groupfive.satapp.commons.MyApp;
import com.groupfive.satapp.data.repositories.AuthSatAppRepository;

public class LoginActivity extends AppCompatActivity {

    ImageView logo;
    EditText username, password;
    Button login, register;
    AuthSatAppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        repository = new AuthSatAppRepository();

        logo = findViewById(R.id.imageViewLogo);
        username = findViewById(R.id.editTextTitleNewTicket);
        password = findViewById(R.id.editTextPassword1);
        login = findViewById(R.id.buttonLogin);
        register = findViewById(R.id.buttonRegister);

        Glide.with(this)
                .load(R.drawable.ic_launcher_foreground)
                .into(logo);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.login(username.getText().toString(),password.getText().toString());

                Intent i =  new Intent(MyApp.getContext(),
                        MainActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MyApp.getContext(),
                        RegisterActivity.class);
                startActivity(i);
            }
        });

    }
}
