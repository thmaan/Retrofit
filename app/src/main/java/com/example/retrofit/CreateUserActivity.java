package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateUserActivity extends AppCompatActivity {
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token = "";
    private TextView usernameTextView;
    private TextView passwordTextView;
    private Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Intent intent = getIntent();
        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);
        usernameTextView = findViewById(R.id.add_username);
        passwordTextView = findViewById(R.id.add_password);

        confirmButton = findViewById(R.id.register_button);
        api();
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

    }
    private void api(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://twomoods.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
    }
    private void callLogin(){
        Intent intent = new Intent(CreateUserActivity.this, LoginActivity.class);
        intent.putExtra("FROM_CREATE","sim");
        intent.putExtra("NEW_USERNAME",usernameTextView.getText().toString());
        intent.putExtra("NEW_PASSWORD",passwordTextView.getText().toString());
        startActivity(intent);
    }
    private void createUser() {
        String username = usernameTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        Login login = new Login(username, password);
        Call<ResponseBody> call = jsonPlaceHolderApi.createUser(login);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CreateUserActivity.this, "Code: " + response.code() +" "+ response.message(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(CreateUserActivity.this, "Something happened, try again.", Toast.LENGTH_SHORT).show();
                    callLogin();
                    return;
                }
                String content = response.message();
                Toast.makeText(CreateUserActivity.this, content, Toast.LENGTH_SHORT).show();
                callLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateUserActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
