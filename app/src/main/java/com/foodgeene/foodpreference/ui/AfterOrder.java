package com.foodgeene.foodpreference.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.foodpreference.adapter.AfterOrderAdapter;
import com.foodgeene.foodpreference.model.AfterOrderModel;
import com.foodgeene.foodpreference.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
    AfterOrderAdapter afterOrderAdapter;
    List<Product> afterList;
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public
        boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(afterList, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    private void setupReycler() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<AfterOrderModel> call = foodGeneeAPI.afterOrderDetails("order", orderId, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<AfterOrderModel>() {
            @Override
            public void onResponse(Call<AfterOrderModel> call, Response<AfterOrderModel> response) {

                try{

                    if(response.body().getStatus().equals("1")){

                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        afterOrderAdapter = new AfterOrderAdapter(response.body().getOrders().getProducts());
                        recyclerView.setAdapter(afterOrderAdapter);
                        afterList = response.body().getOrders().getProducts();
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
