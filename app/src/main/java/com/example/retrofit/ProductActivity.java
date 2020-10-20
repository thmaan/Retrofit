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

public class ProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static ArrayList<Product> products;
    private String token = "";
    RecyclerView recyclerView;
    ProductAdapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    public static final String AUTH_TOKEN="AUTH_TOKEN";
    public static final String PRODUCT_NAME= "PRODUCT_NAME";
    public static final String PRODUCT_PRICE= "PRODUCT_PRICE";
    public static final String PRODUCT_CATEGORY= "PRODUCT_CATEGORY";
    public static final String PRODUCT_DESCRIPTION= "PRODUCT_DESCRIPTION";
    public static final String PRODUCT_ID = "PRODUCT_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        products = new ArrayList<>();
        Intent intent = getIntent();

        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);
        if (token == null)
            token = intent.getStringExtra(ProductActivity.AUTH_TOKEN);

        api();
        showProduct();
        toolbarCode();

    }
    public void toolbarCode(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
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
        if(item.getItemId() == R.id.homeMenu) {
            Intent intent1 = new Intent(ProductActivity.this, MainActivity.class);

            intent1.putExtra(AUTH_TOKEN, token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.createUserMenu){
            Intent intent1 = new Intent(ProductActivity.this, CreateUserActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.logoutMenu){
            Intent intent1 = new Intent(ProductActivity.this, LoginActivity.class);
            intent1.putExtra(LoginActivity.AUTO_LOGIN,"false");
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.viewCustomerMenu){
            Intent intent1 = new Intent(ProductActivity.this, CustomerActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.productListMenu){
            Intent intent1 = new Intent(ProductActivity.this, ProductActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        if (item.getItemId() == R.id.createProductMenu){
            Intent intent1 = new Intent(ProductActivity.this, CreateProduct.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        return true;
    }
    private void deleteProduct(int position) {
        int p = Integer.parseInt(products.get(position).getId());
        Call<Void> call = jsonPlaceHolderApi.deleteProduct(token,p);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this, "Code: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "failed bro", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showProduct() {
        Call<List<Product>> call = jsonPlaceHolderApi.getProducts(token);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProductActivity.this, "Code: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Product> productsResponse = response.body();
                buildRecyclerView();

                for (Product product : productsResponse){
                    products.add(new Product(product.getId(),product.getName(),product.getPrice(),product.getCategory(),product.getDescription()));
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductActivity.this, "failed bro", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateProduct(int position){
        Intent intent = new Intent(ProductActivity.this, UpdateProduct.class);
        intent.putExtra(PRODUCT_NAME,products.get(position).getName());
        intent.putExtra(PRODUCT_PRICE,""+products.get(position).getPrice());
        intent.putExtra(PRODUCT_CATEGORY,products.get(position).getCategory());
        intent.putExtra(PRODUCT_DESCRIPTION,products.get(position).getDescription());
        intent.putExtra(PRODUCT_ID,products.get(position).getId());
        intent.putExtra(AUTH_TOKEN,token);
        startActivity(intent);
    }
    private void removeItem(int position){
        products.remove(position);
        myAdapter.notifyItemRemoved(position);
    }
    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.productList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        myAdapter = new ProductAdapter(products);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteProduct(position);
                removeItem(position);
            }

            @Override
            public void onUpdateClick(int position) {
                updateProduct(position);
            }
        });
    }
    public void api() {
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
}
