package com.foodgeene.foodpreference.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.foodpreference.adapter.AfterOrderAdapter;
import com.foodgeene.foodpreference.model.AfterOrderModel;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AfterOrder extends AppCompatActivity {

    RecyclerView recyclerView;
    Button submitButton;
    SessionManager sessionManager;
    String userToken;
    String orderId;
    Intent get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        get = getIntent();
        orderId = get.getStringExtra("orderId");
        initViews();
        setupReycler();
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
                        AfterOrderAdapter afterOrderAdapter = new AfterOrderAdapter(response.body().getOrders().getProducts());
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

        recyclerView = findViewById(R.id.preferenceRecycler);
        submitButton = findViewById(R.id.submitPrefer);
    }
}
