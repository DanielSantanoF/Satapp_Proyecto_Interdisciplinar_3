package com.groupfive.satapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.groupfive.satapp.R;
import com.groupfive.satapp.data.repositories.SatAppRepository;
import com.groupfive.satapp.models.AuthLogin;

public class LoginActivity extends AppCompatActivity {

    ImageView logo;
    EditText username, password;
    Button login, register;
    SatAppRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo = findViewById(R.id.imageViewLogo);
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        login = findViewById(R.id.buttonLogin);
        register = findViewById(R.id.buttonRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthLogin user = repository.login(username.getText().toString(),password.getText().toString());
            }
        });

    }
}
