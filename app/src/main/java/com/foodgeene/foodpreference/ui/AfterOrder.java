package com.foodgeene.foodpreference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.foodpreference.adapter.AfterOrderAdapter;
import com.foodgeene.foodpreference.model.AfterOrderModel;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfterOrder extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    RecyclerView recyclerView;
    Button submitButton;
    SessionManager sessionManager;
    String userToken;
    String orderId;
    Intent get;
    ImageView mIvBack;
    boolean isOnLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        get = getIntent();
        orderId = get.getStringExtra("orderId");
        isOnLine=ConnectivityReceiver.isConnected();

        initViews();
        if(isOnLine)
        setupReycler();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void setupReycler() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<AfterOrderModel> call = foodGeneeAPI.afterOrderDetails("order", orderId, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<AfterOrderModel>() {
            @Override
            public void onResponse(Call<AfterOrderModel> call, Response<AfterOrderModel> response) {

                try{

                    if(response.body().getStatus().equals("1")){

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        AfterOrderAdapter afterOrderAdapter = new AfterOrderAdapter(response.body().getOrders().getProducts(),orderId,userToken,AfterOrder.this);
                        recyclerView.setAdapter(afterOrderAdapter);

                    }
                    else if(response.body().getStatus().equals("0")){



                    }


                }
                catch (Exception e){



                }

            }

            @Override
            public void onFailure(Call<AfterOrderModel> call, Throwable t) {

            }
        });
    }

    private void initViews() {
        mIvBack=findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.preferenceRecycler);
        submitButton = findViewById(R.id.submitPrefer);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
