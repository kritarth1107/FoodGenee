package com.foodgeene.coinstransactions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.coinstransactions.adapter.TransactionAdapter;
import com.foodgeene.coinstransactions.model.Text;
import com.foodgeene.coinstransactions.model.Transaction;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoinsTransaction extends AppCompatActivity {

    RecyclerView recyclerView;
    String token;
    SessionManager sessionManager;
    TextView coins;
    ImageView nocoins;
    ProgressBar pro;
    TransactionAdapter adapter;
    TextView notransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coins_transaction);
        recyclerView = findViewById(R.id.transactionRecycler);
        coins = findViewById(R.id.transctionstat);
        nocoins = findViewById(R.id.norewardImg);
        sessionManager = new SessionManager(this);
        pro = findViewById(R.id.transcprogres);
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
        notransaction = findViewById(R.id.notransaaction);
        setupRecycler();



    }

    private void setupRecycler() {
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<Transaction> call = foodGeneeAPI.transCoins("coins-transactions",token, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                try{


                    Transaction transaction = response.body();
                    List<Text> list = transaction.getText();
                    if(transaction.getStatus().equals("1")){

                        adapter = new TransactionAdapter(CoinsTransaction.this, list);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CoinsTransaction.this);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                        pro.setVisibility(View.GONE);

                    }
                    else if(transaction.getStatus().equals("0")) {

                        coins.setVisibility(View.VISIBLE);
                        nocoins.setVisibility(View.VISIBLE);
                        pro.setVisibility(View.GONE);
                        notransaction.setVisibility(View.VISIBLE);
                    }



                }
                catch (Exception e){
                    coins.setVisibility(View.VISIBLE);
                    nocoins.setVisibility(View.VISIBLE);
                    pro.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                coins.setVisibility(View.VISIBLE);
                nocoins.setVisibility(View.VISIBLE);
                pro.setVisibility(View.GONE);

            }
        });
    }
}
