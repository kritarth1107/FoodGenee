package com.foodgeene.rewardsfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.rewards.RewardsAdapter;
import com.foodgeene.rewards.rewardmodels.RModel;
import com.foodgeene.rewards.rewardmodels.RedeemCount;
import com.foodgeene.rewards.rewardmodels.Text;
import com.foodgeene.transactionlists.CoinsTransactionsList;

import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RewardsFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener{

    RecyclerView recyclerView;
    RewardsAdapter rewardsAdapter;
    SessionManager sessionManager;
    GifImageView progressBar;
    String token;
    TextView redeemCount;
    TextView check;
    boolean isOnLine;

    public RewardsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rewards, container, false);

        recyclerView = rootView.findViewById(R.id.rewardrecycler);
        sessionManager = new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
        progressBar = rootView.findViewById(R.id.gif);
        progressBar.setVisibility(View.VISIBLE);
        check = rootView.findViewById(R.id.checkTransaction);
        redeemCount = rootView.findViewById(R.id.redeemCount);
        isOnLine=ConnectivityReceiver.isConnected();

        check.setOnClickListener(view -> startActivity(new Intent(getContext(), CoinsTransactionsList.class)));
        if(isOnLine) {
            setupRecycler();
            setRedeemCount();
        }else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void setRedeemCount() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RedeemCount> countCall = foodGeneeAPI.redeemCount("rewards-count", token, "application/x-www-form-urlencoded");
        countCall.enqueue(new Callback<RedeemCount>() {
            @Override
            public void onResponse(Call<RedeemCount> call, Response<RedeemCount> response) {

                try{
                    RedeemCount retrievedCount = response.body();
                    redeemCount.setText(retrievedCount.getText());

                }
                catch (Exception e){


                }
            }

            @Override
            public void onFailure(Call<RedeemCount> call, Throwable t) {

            }
        });

    }

    private void setupRecycler() {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RModel> call  = foodGeneeAPI.rewardsList("rewards-list", token,"application/x-www-form-urlencoded");

        call.enqueue(new Callback<RModel>() {
            @Override
            public void onResponse(Call<RModel> call, Response<RModel> response) {
                try{
                    RModel newModel = response.body();
                    List<Text> offers = newModel.getText();
                    rewardsAdapter = new RewardsAdapter(getContext(), offers);
                    GridLayoutManager mGridLayoutManager = new GridLayoutManager(getContext(), 2);

                    recyclerView.setLayoutManager(mGridLayoutManager);
                    recyclerView.setAdapter(rewardsAdapter);
                    progressBar.setVisibility(View.INVISIBLE);

                }
                catch (Exception e){

                    progressBar.setVisibility(View.INVISIBLE);


                }
            }

            @Override
            public void onFailure(Call<RModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
