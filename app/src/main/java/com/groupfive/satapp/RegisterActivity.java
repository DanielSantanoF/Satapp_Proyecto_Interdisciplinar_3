package com.groupfive.satapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.groupfive.satapp.data.repositories.SatAppRepository;
import com.groupfive.satapp.models.AuthLoginUser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
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
        avatar = findViewById(R.id.buttonAvatar);
        register = findViewById(R.id.buttonRegister);
        uri = null;

        register.setEnabled(false);

        if (
                email.getText().toString().isEmpty() || name.getText().toString().isEmpty() || passwor1.getText().toString().isEmpty() || password2.getText().toString().isEmpty() ||
        ){
            Toast.makeText(RegisterActivity.this, "Debe rellenar todos los campos", Toast.LENGTH_SHORT).show();
        }else if(
                !passwor1.getText().toString().equals(password2.getText().toString())
        ){
            if (uri != null) {

                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    int cantBytes;
                    byte[] buffer = new byte[1024*4];

                    while ((cantBytes = bufferedInputStream.read(buffer,0,1024*4)) != -1) {
                        baos.write(buffer,0,cantBytes);
                    }


                    RequestBody requestFile =
                            RequestBody.create(baos.toByteArray(),
                                    MediaType.parse(getContentResolver().getType(uri)));


                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("avatar", "avatar", requestFile);


                    RequestBody emailRequest = RequestBody.create(MultipartBody.FORM,email.getText().toString());
                    RequestBody nameRequest = RequestBody.create(MultipartBody.FORM,name.getText().toString());
                    RequestBody passwordRequest = RequestBody.create(MultipartBody.FORM, passwor1.getText().toString());

                    repository.
                (username.getText().toString(),password.getText().toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
                uri = uri;
                register.setEnabled(true);
            }
        }
    }
}
