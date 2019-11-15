package com.foodgeene.profile;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.profile.userdetails.UserModel;
import com.foodgeene.profile.userdetails.Users;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import updateprofile.UpdateProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    LinearLayout editProfile,logout;
    SessionManager sessionManager;
    String token;
    TextView coins;
    TextView name, email, mobile;

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
        setupProfile();
        name = rootView.findViewById(R.id.userName);
        email = rootView.findViewById(R.id.userEmail);
        mobile = rootView.findViewById(R.id.userPhone);
        coins = rootView.findViewById(R.id.realcoins);
        logout = rootView.findViewById(R.id.logout);
        editProfile = rootView.findViewById(R.id.editProfile);
        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), UpdateProfile.class));
        });
        logout.setOnClickListener(view -> {
            sessionManager.logout();
        });
        return rootView;


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

}
