package com.foodgeene.transactionlists;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.transactionlists.model.ListModel;
import com.foodgeene.transactionlists.model.Text;

import java.util.HashMap;
import java.util.List;

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

public class CoinsTransactionsList extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    RecyclerView recyclerView;
    ListAdapter adapter;
    ProgressBar listProgress;
    SessionManager sessionManager;
    String userToken;
    TextView notransaction;
    ImageView mIvBack;
    boolean isOnLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coins_transactions_list);
        mIvBack=findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.reepeatlist);
        listProgress = findViewById(R.id.listProgress);
        notransaction = findViewById(R.id.notransactionsyet);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        isOnLine=ConnectivityReceiver.isConnected();

        if(isOnLine)
        setupRecyclerView();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
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

                        if(adapter.getItemCount()==0){

                            notransaction.setVisibility(View.VISIBLE);
                        }

                    }
                    else if(response.body().getStatus().equals("0")){

                        listProgress.setVisibility(View.GONE);
                        notransaction.setVisibility(View.VISIBLE);

                    }
                    else {
                        listProgress.setVisibility(View.GONE);
                        notransaction.setVisibility(View.VISIBLE);


                    }
                }
                catch (Exception e){


                    listProgress.setVisibility(View.GONE);
                    notransaction.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onFailure(Call<ListModel> call, Throwable t) {

                listProgress.setVisibility(View.GONE);
                notransaction.setVisibility(View.VISIBLE);

            }
        });


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
