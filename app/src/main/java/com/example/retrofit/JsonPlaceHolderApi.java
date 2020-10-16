package com.example.retrofit;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    //Call<User> postUser;

    //porra de barra do krl
    @POST("create-product-api/")
    Call<ResponseBody> createProduct(@Header("Authorization") String tokenHeader, @Body Product product);

    @POST("update-product-api/{id}/")
    Call<Product> putProduct(@Header("Authorization") String tokenHeader, @Path("id") int id, @Body Product product);

    @DELETE("delete-product-api/{id}/")
    Call<Void> deleteProduct(@Header("Authorization") String tokenHeader,@Path("id") int id);

    @DELETE("delete-customer-api/{id}/")
    Call<Void> deleteCustomer(@Path("auto_increment_id") int id);

    @POST("api-token-auth/")
    Call<User> login(@Body Login login);

    //@POST("create-user-api/")
    //Call<ResponseBody> createUser(@Header("Authorization") String tokenHeader, @Body Login login);
    @GET("products-api/")
    Call<List<Product>> getProducts(@Header("Authorization") String tokenHeader);

    @GET("customers-api/")
    Call<List<Customer>> getCustomers(@Header("Authorization") String tokenHeader);

    @POST("create-user-api/")
    Call<ResponseBody> createUser(@Body Login login);

    @GET("hello/")
    Call<Message> hello(@Header("Authorization") String tokenHeader);

    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[]  userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String > parameters);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComments(@Url String url);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    @PUT("posts/{id}")
    Call<Post> putPost(@Path("id") int id, @Body Post post);

    @PATCH("posts/{id}")
    Call<Post> patchPost(@Path("id") int id, @Body Post post);



}

