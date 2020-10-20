package com.example.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String AUTH_TOKEN = "com.example.retrofit.AUTH_TOKEN";
    private TextView total_orders_tv;
    private TextView orders_delivered_tv;
    private TextView orders_pending_tv;
    private TextView total_customers_tv;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token = "";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);

        total_orders_tv = findViewById(R.id.total_orders_tv);
        orders_delivered_tv = findViewById(R.id.orders_delivered_tv);
        orders_pending_tv = findViewById(R.id.orders_pending_tv);
        total_customers_tv = findViewById(R.id.total_customers_tv);
        api();

        toolbarCode();
        dashboard();

    }
    public void api(){
        Gson gson = new GsonBuilder().serializeNulls().create();

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
    public void dashboard(){
        Call<Dashboard> call = jsonPlaceHolderApi.hello(token);

        call.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Dashboard dashboard = response.body();
                String content = "";
                content = "Total Orders " + "\n"+ dashboard.getTotal_orders();
                total_orders_tv.setText(content);
                content = "Orders Delivered" + "\n"+ dashboard.getDelivered() + "\n";
                orders_delivered_tv.setText(content);
                content = "Orders Pending" + "\n"+ dashboard.getPending() + "\n";
                orders_pending_tv.setText(content);
                content = "Total Customers"+ "\n" +dashboard.getTotal_customers();
                total_customers_tv.setText(content);
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                Toast.makeText(MainActivity.this, "failed bro", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void toolbarCode(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.createUserMenu){
            Intent intent1 = new Intent(MainActivity.this, CreateUserActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
       if (item.getItemId() == R.id.logoutMenu){
           Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
           intent1.putExtra("LOGOUT","sim");
           startActivity(intent1);
        }
        if (item.getItemId() == R.id.viewCustomerMenu){
            Intent intent1 = new Intent(MainActivity.this, CustomerActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.productListMenu){
            Intent intent1 = new Intent(MainActivity.this, ProductActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.createProductMenu){
            Intent intent1 = new Intent(MainActivity.this, CreateProduct.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        return true;
    }
}
