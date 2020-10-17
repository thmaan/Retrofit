package com.example.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.retrofit.ProductActivity.AUTH_TOKEN;

public class CustomerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static ArrayList<Customer> customers;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        customers = new ArrayList<>();

        Intent intent = getIntent();
        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);
        if(token == null)
            token = intent.getStringExtra(AUTH_TOKEN);
        api();
        toolbarCode();
        showCustomer();
    }
    public void toolbarCode(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.customerDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }
    public void show(){
        RecyclerView recyclerView;
        RecyclerView.Adapter myAdapter;
        RecyclerView.LayoutManager layoutManager;

        recyclerView = findViewById(R.id.customerList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new CustomerAdapter(customers);
        recyclerView.setAdapter(myAdapter);


    }
    public void api(){
        //Gson gson = new GsonBuilder().serializeNulls().create();

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
    public void showCustomer(){
        Call<List<Customer>> call = jsonPlaceHolderApi.getCustomers(token);
        call.enqueue(new Callback<List<Customer>>() {
            @Override
            public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CustomerActivity.this, "Code: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Customer> customersResponse = response.body();
                for (Customer customer: customersResponse){
                    customers.add(new Customer(customer.getAuto_increment_id(),customer.getName(),customer.getEmail()));
                }
                show();
            }
            @Override
            public void onFailure(Call<List<Customer>> call, Throwable t) {
                Toast.makeText(CustomerActivity.this, "failed bro", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.createUserMenu){
            Intent intent1 = new Intent(CustomerActivity.this, CreateUserActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.logoutMenu){
            Intent intent1 = new Intent(CustomerActivity.this, LoginActivity.class);
            intent1.putExtra(LoginActivity.AUTO_LOGIN,"false");
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.viewCustomerMenu){
            Intent intent1 = new Intent(CustomerActivity.this, CustomerActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.productListMenu){
            Intent intent1 = new Intent(CustomerActivity.this, ProductActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.createProductMenu){
            Intent intent1 = new Intent(CustomerActivity.this, CreateProduct.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        return true;
    }
}
