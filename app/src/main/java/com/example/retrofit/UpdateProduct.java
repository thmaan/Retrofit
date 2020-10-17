package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText productName;
    private EditText productPrice;
    private EditText productDescription;
    private String name;
    private String price;
    private String category;
    private String description;
    private String id;
    private Button registerButton;
    private String token = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        Intent intent = getIntent();
        name = intent.getStringExtra(ProductActivity.PRODUCT_NAME);
        price = intent.getStringExtra(ProductActivity.PRODUCT_PRICE);
        category = intent.getStringExtra(ProductActivity.PRODUCT_CATEGORY);
        description = intent.getStringExtra(ProductActivity.PRODUCT_DESCRIPTION);
        id = intent.getStringExtra(ProductActivity.PRODUCT_ID);
        token = intent.getStringExtra(ProductActivity.AUTH_TOKEN);

        spinner = findViewById(R.id.categorySpinner);

        registerButton = findViewById(R.id.register_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        productName = findViewById(R.id.productNameTextView);
        productPrice = findViewById(R.id.productPriceTextView);
        productDescription = findViewById(R.id.descriptionTextView);

        productName.setText(name);
        productPrice.setText(price);
        productDescription.setText(description);

        api();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

    }
    private void updateProduct(){
        name = productName.getText().toString();
        price = productPrice.getText().toString();
        description = productDescription.getText().toString();
        Product product = new Product(name,Float.parseFloat(price),category,description);
        Call<Product> call = jsonPlaceHolderApi.putProduct(token, Integer.parseInt(id),product);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(UpdateProduct.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Product productResponse = response.body();
                String context ="";
                context += productResponse.getName();
                Toast.makeText(UpdateProduct.this, context + " Updated", Toast.LENGTH_SHORT).show();
                callProducts();
            }
            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(UpdateProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void callProducts(){
        Intent intent = new Intent(UpdateProduct.this, ProductActivity.class);
        intent.putExtra(ProductActivity.AUTH_TOKEN,token);
        startActivity(intent);
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        category = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
