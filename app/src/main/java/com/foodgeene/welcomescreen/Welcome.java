package com.foodgeene.welcomescreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.firebaseservices.FirebaseMessagingModel;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    String userToken;

    public static String TAG = "EWE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeTextUser = findViewById(R.id.welcomeBro);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        setupWelcomeScreenWithUsername(userToken);
        sendFirebaseTokenToServer();


    }

    private void sendFirebaseTokenToServer() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    String token = task.getResult().getToken();
                    networkCall(token);


                });

    }

    private void networkCall(String token) {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<FirebaseMessagingModel> call = foodGeneeAPI.sendPushId("pushid", token, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<FirebaseMessagingModel>() {
            @Override
            public void onResponse(Call<FirebaseMessagingModel> call, Response<FirebaseMessagingModel> response) {

                try{
                    FirebaseMessagingModel fm = response.body();

                }
                catch (Exception e){


                }
            }

            @Override
            public void onFailure(Call<FirebaseMessagingModel> call, Throwable t) {

            }
        });
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
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Welcome.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


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

