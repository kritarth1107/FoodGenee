package com.foodgeene.welcomescreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.firebaseservices.FirebaseMessagingModel;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Welcome extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    TextView welcomeTextUser;
    SessionManager sessionManager;
    Handler handler;
    String userToken;
    ImageView propic;
    boolean isOnline;
    public static String TAG = "EWE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        propic = findViewById(R.id.profilePicture);
        welcomeTextUser = findViewById(R.id.welcomeBro);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        isOnline=ConnectivityReceiver.isConnected();
        if(isOnline) {
            setupWelcomeScreenWithUsername(userToken);
            sendFirebaseTokenToServer();
        }  else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();



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

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
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
                    if(retrievedModel.getStatus().equalsIgnoreCase("1")){
                        Users retrievedModelUsers = retrievedModel.getUsers();
                        welcomeTextUser.setText(retrievedModelUsers.getName());

                        Glide.with(Welcome.this)
                                .load(retrievedModel.getUsers().getProfilepic())
                                .into(propic);

                        handler = new Handler();
                        handler.postDelayed(() -> {
                                    Intent intent = new Intent(Welcome.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);


                                }, 2500
                        );
                    }else{
                        Intent intent = new Intent(Welcome.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }




                }
                catch (Exception e){
                   /// Toast.makeText(Welcome.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Welcome.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline=isConnected;
    }
}

