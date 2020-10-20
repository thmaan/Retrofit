package com.example.retrofit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface JsonPlaceHolderApi {

    //barra
    @POST("create-product-api/")
    Call<ResponseBody> createProduct(@Header("Authorization") String tokenHeader, @Body Product product);

    @POST("update-product-api/{id}/")
    Call<Product> putProduct(@Header("Authorization") String tokenHeader, @Path("id") int id, @Body Product product);

    @DELETE("delete-product-api/{id}/")
    Call<Void> deleteProduct(@Header("Authorization") String tokenHeader, @Path("id") int id);

    @POST("api-token-auth/")
    Call<User> login(@Body Login login);

    @GET("products-api/")
    Call<List<Product>> getProducts(@Header("Authorization") String tokenHeader);

    @GET("customers-api/")
    Call<List<Customer>> getCustomers(@Header("Authorization") String tokenHeader);

    @POST("create-user-api/")
    Call<ResponseBody> createUser(@Body Login login);

    @GET("hello/")
    Call<Dashboard> hello(@Header("Authorization") String tokenHeader);
}
