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
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private EditText productName;
    private EditText productPrice;
    private EditText productDescription;
    private Button registerButton;
    private String token = "";
    private Spinner spinner;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        Intent intent = getIntent();

        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);
        if( token == null)
            token = intent.getStringExtra(ProductActivity.AUTH_TOKEN);
        productName = findViewById(R.id.productNameTextView);
        productPrice = findViewById(R.id.productPriceTextView);
        productDescription = findViewById(R.id.descriptionTextView);
        spinner = findViewById(R.id.spinner1);
        registerButton = findViewById(R.id.register_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        api();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProduct();
            }
        });
    }
    private void callProducts(){
        Intent intent = new Intent(CreateProduct.this, ProductActivity.class);
        intent.putExtra(ProductActivity.AUTH_TOKEN,token);
        startActivity(intent);
    }
    public void createProduct(){
            String name = productName.getText().toString();
            String price = productPrice.getText().toString();
            String description = productDescription.getText().toString();
            Product product = new Product(name,Float.parseFloat(price),category,description);

        Call<ResponseBody> call = jsonPlaceHolderApi.createProduct(token,product);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(CreateProduct.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = response.message();
                Toast.makeText(CreateProduct.this, content, Toast.LENGTH_SHORT).show();
                callProducts();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateProduct.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        category = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
