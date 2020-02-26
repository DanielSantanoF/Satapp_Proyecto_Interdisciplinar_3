package com.groupfive.satapp;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.groupfive.satapp.data.repositories.SatAppRepository;

public class RegisterActivity extends AppCompatActivity {

    EditText email,passwor1,password2,name;
    Uri uri;
    Button avatar, register;
    SatAppRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        repository = new SatAppRepository();

        email = findViewById(R.id.editTextEmail);
        passwor1 = findViewById(R.id.editTextPassword1);
        password2 = findViewById(R.id.editTextPassword2);
        name = findViewById(R.id.lottie_layer_name);

    }
}
