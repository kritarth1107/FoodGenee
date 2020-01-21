package com.foodgeene.profile;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.coinstransactions.CoinsTransaction;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;
import com.foodgeene.updateprofile.UpdateProfile;

import java.util.HashMap;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    LinearLayout editProfile,logout;
    SessionManager sessionManager;
    String token;
    TextView coins;
    TextView name, email, mobile;
   TextView redeem;
    ImageView propic;
    String userID;
    ProgressBar progressBar;
    LinearLayout goNow;
    LinearLayout rateUs;
    boolean isOnline;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager = new SessionManager(Objects.requireNonNull(getActivity()));
        HashMap<String, String> user = sessionManager.getUserDetail();
        token = user.get(sessionManager.USER_ID);
        isOnline=ConnectivityReceiver.isConnected();

        if(isOnline)
        setupProfile();
        else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
        name = rootView.findViewById(R.id.userName);
        email = rootView.findViewById(R.id.userEmail);
        mobile = rootView.findViewById(R.id.userPhone);
        coins = rootView.findViewById(R.id.realcoins);
        redeem = rootView.findViewById(R.id.redeemNow);
        rateUs = rootView.findViewById(R.id.rateus);
        propic = rootView.findViewById(R.id.profilePicture);
        progressBar = rootView.findViewById(R.id.startProgress);
        goNow = rootView.findViewById(R.id.lal);
        goNow.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        redeem.setOnClickListener(view -> startActivity(new Intent(getContext(), CoinsTransaction.class)));
        logout = rootView.findViewById(R.id.logout);
        editProfile = rootView.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), UpdateProfile.class));
        });
        logout.setOnClickListener(view -> {
            sessionManager.logout();
        });

        rateUs.setOnClickListener(view -> {
            Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
            }
        });
        return rootView;


    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
        if(isOnline)
            setupProfile();
        else Toast.makeText(getActivity(), "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }

    private void setupProfile() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI
        .class);

        Call<UserModel> call = foodGeneeAPI.userDetails("users",token, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                try {
                    UserModel retrievedModel = response.body();
                    Users retrievedModelUsers = retrievedModel.getUsers();

                    name.setText(retrievedModelUsers.getName());
                    email.setText(retrievedModelUsers.getEmail());
                    mobile.setText(retrievedModelUsers.getMobile());
                    coins.setText(retrievedModelUsers.getCoins());

                    Glide.with(getContext())
                            .load(retrievedModel.getUsers().getProfilepic())
                            .into(propic);

                    goNow.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);


                }
                catch (Exception e){

                    Toast.makeText(getContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
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
