package com.foodgeene.welcomescreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class Welcome extends AppCompatActivity {

    TextView welcomeTextUser;
    SessionManager sessionManager;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextUser = findViewById(R.id.welcomeBro);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String token = user.get(sessionManager.USER_ID);
        setupWelcomeScreenWithUsername(token);


    }

    private void setupWelcomeScreenWithUsername(String token) {

//
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.level(HttpLoggingInterceptor.Level.BASIC);
//        OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(logging)
//                .build();


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI
                .class);

        Call<UserModel> call = foodGeneeAPI.userDetails("users",token, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                try {
                    UserModel retrievedModel = response.body();
                    Users retrievedModelUsers = retrievedModel.getUsers();
                    welcomeTextUser.setText(retrievedModelUsers.getName());
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(Welcome.this, MainActivity.class);
                            startActivity(intent);


                        }
                    }, 2500
                    );



                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }


    }

