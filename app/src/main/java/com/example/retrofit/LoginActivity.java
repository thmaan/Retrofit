package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    public static final String AUTH_TOKEN = "com.example.retrofit.AUTH_TOKEN";
    public static final String AUTO_LOGIN = "com.example.retrofit.AUTO_LOGIN";
    private TextView usernameTextView;
    private TextView passwordTextView;
    private TextView registerTextView;
    private RelativeLayout relativeLayout;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private User user;
    private String token = "";
    private Intent intent;
    private Switch switch1;
    private String username = "";
    private String password = "";
    private boolean switchOnOff;
    private String autoLogin = "yes";

    public static final String SHARED_PREFS = "SHAREDpREFS";
    public static final String USER = "User";
    public static final String PASSWORD = "Password";
    public static final String TOKEN = "token";
    public static final String SWITCH1 = "switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameTextView = findViewById(R.id.usernameTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        switch1 = findViewById(R.id.switch1);
        relativeLayout = findViewById(R.id.relativeLayout);
        registerTextView = findViewById(R.id.register);

        api();
        loadData();
        autoLogin();
        updateViews();

        if(autoLogin == null && !usernameTextView.getText().toString().isEmpty() )
            login();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);
            }
        });
    }
    private void autoLogin(){
        try {
            Intent intent = getIntent();
            autoLogin = intent.getStringExtra(AUTO_LOGIN);
        }catch (NullPointerException e){
            e.getStackTrace();
        }
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
    public void openMainActivity(){
        login();
    }
    private void login() {
        username = usernameTextView.getText().toString();
        password = passwordTextView.getText().toString();
        Login login = new Login(username, password);
        Call<User> call = jsonPlaceHolderApi.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User responseFromServer = response.body();
                token = "Token " + responseFromServer.getToken();
                Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
                saveData();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(AUTH_TOKEN, token);
                LoginActivity.this.startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void saveData(){
        if (switch1.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(USER, usernameTextView.getText().toString());
            editor.putString(PASSWORD, passwordTextView.getText().toString());
            editor.putString(TOKEN, token);
            editor.putBoolean(SWITCH1,switch1.isChecked());

            editor.apply();

            Toast.makeText(this, "Remember me activated, Data saved", Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(USER, "");
            editor.putString(PASSWORD, "");
            editor.putString(TOKEN, " ");
            editor.putBoolean(SWITCH1,switch1.isChecked());

            editor.apply();

        }

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        username = sharedPreferences.getString(USER," ");
        password = sharedPreferences.getString(PASSWORD," ");
        token = sharedPreferences.getString(TOKEN," ");
        switchOnOff = sharedPreferences.getBoolean(SWITCH1,false);
    }

    public void updateViews(){
        usernameTextView.setText(username);
        passwordTextView.setText(password);
        switch1.setChecked(switchOnOff);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }
}