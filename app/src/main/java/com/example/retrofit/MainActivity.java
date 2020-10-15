package com.example.retrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String AUTH_TOKEN = "com.example.retrofit.AUTH_TOKEN";
    public static final String AUTO_LOGIN = "com.example.retrofit.AUTO_LOGIN";
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private User user;
    private String token = "";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Button createUserButton;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        token = intent.getStringExtra(LoginActivity.AUTH_TOKEN);

        textViewResult = findViewById(R.id.text_view_result);
        //createUserButton = findViewById(R.id.create_user_button);

       api();
        /*createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, CreateUserActivity.class);

                intent1.putExtra(AUTH_TOKEN,token);
                startActivity(intent1);
            }
        });*/
        toolbarCode();
        //getPosts();
        //getComments();
        //createPost();
        //updatePost();
        //deletePost();
        hello();

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
    public void hello(){
        Login login = new Login("admin","admin");

        Call<Message> call = jsonPlaceHolderApi.hello(token);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Code: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Message responseMessage = response.body();
                String content = "";
                content = responseMessage.getMessage();
                textViewResult.setText(content);
                Toast.makeText(MainActivity.this, content,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
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
           intent1.putExtra(AUTO_LOGIN,false);
           startActivity(intent1);
        }
        if (item.getItemId() == R.id.viewCustomerMenu){
            Intent intent1 = new Intent(MainActivity.this, CustomerActivity.class);

            intent1.putExtra(AUTH_TOKEN,token);
            startActivity(intent1);
        }
        return true;
    }

    private void getPosts(){
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,3,6},"id",
                "desc");

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();
                for ( Post post : posts){
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID:: " + post.getUserId() + "\n";
                    content += "TITLE: " + post.getTitle() + "\n";
                    content += "TEXT: " + post.getText() + "\n\n";
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void getComments(){
       Call<List<Comment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");

       call.enqueue(new Callback<List<Comment>>() {
           @Override
           public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
               if (!response.isSuccessful()) {
                   textViewResult.setText("Code: " + response.code());
                   return;
               }
               List<Comment> comments = response.body();
                    for (Comment comment : comments){
                        String content = "";
                        content += "ID: " + comment.getId() + "\n";
                        content += "Post ID:: " + comment.getPostId() + "\n";
                        content += "NAME: " + comment.getName() + "\n";
                        content += "EMAIL: " + comment.getEmail() + "\n";
                        content += "TEXT: " + comment.getText() + "\n\n";

                        textViewResult.append(content);
                    }
           }

           @Override
           public void onFailure(Call<List<Comment>> call, Throwable t) {
               textViewResult.setText(t.getMessage());
           }
       });
    }
    private void createPost(){
        Post post = new Post(23,"new title ", "new text");

        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Post postResponse = response.body();
                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "TITLE: " + postResponse.getTitle() + "\n";
                content += "TEXT: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void updatePost(){
        Post post = new Post (12,null,"NEW TEXT");
        Call<Post> call =jsonPlaceHolderApi.putPost(5,post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    textViewResult.setText("Code: " + response.code());
                    return;
                }
                Post postResponse = response.body();
                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "TITLE: " + postResponse.getTitle() + "\n";
                content += "TEXT: " + postResponse.getText() + "\n\n";

                textViewResult.setText(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
    private void deletePost(){
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                textViewResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }
}
