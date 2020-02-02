package com.foodgeene.allhotels.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.allhotels.hotelsadapter.HotelsAdapter;
import com.foodgeene.allhotels.hotelsmodel.HotelsModel;
import com.foodgeene.allhotels.hotelsmodel.Merchantlist;
import com.foodgeene.home.Home;
import com.foodgeene.preoder.ui.PreOrder;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRestraunts extends AppCompatActivity implements HotelsAdapter.OnCardClick, ConnectivityReceiver.ConnectivityReceiverListener {


    RecyclerView recyclerView;
    ImageView noRestImage,mIvBack;
    TextView noRestText;
    GifImageView progressBar;
    List<Merchantlist> list;
    boolean isOnLine;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_restraunts);
        initViews();
        isOnLine=ConnectivityReceiver.isConnected();

        if(isOnLine)
        setupRecycler();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void setupRecycler() {

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();

        String userToken = user.get(sessionManager.USER_ID);


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<HotelsModel> call = foodGeneeAPI.getAllHotel("restaurants",String.valueOf(Home.lati) ,String.valueOf(Home.longi),userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<HotelsModel>() {
            @Override
            public void onResponse(Call<HotelsModel> call, Response<HotelsModel> response) {

                try{

                    if(response.body().getStatus().equals("1")){

                        HotelsModel hotelsModel = response.body();
                        list = hotelsModel.getMerchantlist();
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllRestraunts.this));
                        HotelsAdapter hotelsAdapter = new HotelsAdapter(getApplicationContext(), hotelsModel.getMerchantlist(), AllRestraunts.this);
                        recyclerView.setAdapter(hotelsAdapter);
                        progressBar.setVisibility(View.GONE);


                    }
                    else if(response.body().getStatus().equals("0")){

                        noRestImage.setVisibility(View.VISIBLE);
                        noRestText.setText(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);



                    }


                }
                catch (Exception e){

                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<HotelsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);




            }
        });

    }

    private void initViews() {
        mIvBack=findViewById(R.id.iv_back);
        recyclerView = findViewById(R.id.allrecyclerView);
        noRestImage = findViewById(R.id.noRestImage);
        noRestText = findViewById(R.id.noRestText);
        progressBar = findViewById(R.id.allRestProgress);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(int position) {

        Intent intent = new Intent(AllRestraunts.this, PreOrder.class);
        intent.putExtra("merchantId", list.get(position).getId());
        startActivity(intent);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
