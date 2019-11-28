package com.foodgeene.transactionlists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.coinstransactions.CoinsTransaction;
import com.foodgeene.transactionlists.model.ListModel;
import com.foodgeene.transactionlists.model.Text;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsTransactionsList extends AppCompatActivity {

    RecyclerView recyclerView;
    ListAdapter adapter;
    ProgressBar listProgress;
    SessionManager sessionManager;
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins_transactions_list);
        recyclerView = findViewById(R.id.reepeatlist);
        listProgress = findViewById(R.id.listProgress);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        setupRecyclerView();
    }

    private void setupRecyclerView() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ListModel> call  = foodGeneeAPI.listTrans("coins-transactionslist", userToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ListModel>() {
            @Override
            public void onResponse(Call<ListModel> call, Response<ListModel> response) {

                try{
                    if(response.body().getStatus().equals("1")){
                        ListModel res = response.body();
                        List<Text> newText = res.getText();

                        recyclerView.setLayoutManager(new LinearLayoutManager(CoinsTransactionsList.this));
                        adapter = new ListAdapter(newText, CoinsTransactionsList.this);
                        recyclerView.setAdapter(adapter);
                        listProgress.setVisibility(View.GONE);

                    }
                    else if(response.body().getStatus().equals("0")){

                        listProgress.setVisibility(View.GONE);


                    }
                    else {
                        listProgress.setVisibility(View.GONE);


                    }
                }
                catch (Exception e){


                    listProgress.setVisibility(View.GONE);

                }


            }

            @Override
            public void onFailure(Call<ListModel> call, Throwable t) {

                listProgress.setVisibility(View.GONE);

            }
        });


    }
}
