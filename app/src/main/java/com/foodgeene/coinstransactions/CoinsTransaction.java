package com.foodgeene.coinstransactions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Trace;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_coins_transaction);
        recyclerView = findViewById(R.id.transactionRecycler);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
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
                    TransactionAdapter adapter = new TransactionAdapter(CoinsTransaction.this, list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(CoinsTransaction.this));
                    recyclerView.setAdapter(adapter);


                }
                catch (Exception e){

                    Toast.makeText(CoinsTransaction.this, token, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Toast.makeText(CoinsTransaction.this, "Some error occured", Toast.LENGTH_SHORT).show();


            }
        });
    }
}
