package com.foodgeene.profile;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.droidsonroids.gif.GifImageView;
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
    TextView name, email, mobile,contact,mail;
   TextView redeem;
    ImageView propic;
    String userID;
    GifImageView progressBar;
    LinearLayout goNow;
    LinearLayout rateUs;
    boolean isOnline;
    RecyclerView mRvCoins;
    String supportNumber;

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
        mRvCoins=rootView.findViewById(R.id.rv_coin);
        name = rootView.findViewById(R.id.userName);
        email = rootView.findViewById(R.id.userEmail);
        mobile = rootView.findViewById(R.id.userPhone);
        coins = rootView.findViewById(R.id.realcoins);
        redeem = rootView.findViewById(R.id.redeemNow);
        rateUs = rootView.findViewById(R.id.rateus);
        propic = rootView.findViewById(R.id.profilePicture);
        progressBar = rootView.findViewById(R.id.startProgress);
        contact=rootView.findViewById(R.id.tv_contact);
        mail=rootView.findViewById(R.id.tv_mail);
        goNow = rootView.findViewById(R.id.lal);
        goNow.setVisibility(View.INVISIBLE);
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

                    if(retrievedModelUsers.getSupportmobile()!=null){
                        supportNumber=retrievedModelUsers.getSupportmobile().toString();
                        contact.setText("Contact Us: "+retrievedModelUsers.getSupportmobile());
                        contact.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View view) {
                                if (isPermissionGranted()) {
                                    call_action();
                                }

                            }
                        });
                    }

                    if(retrievedModelUsers.getSupportemail()!=null){
                        mail.setText("Mail Us: "+retrievedModelUsers.getSupportemail());
                        mail.setOnClickListener(new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View view) {
                                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                emailIntent.setType("text/plain");
                                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, retrievedModelUsers.getSupportemail());
                                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Reg:Customer Support");
                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Add Message here");


                                emailIntent.setType("message/rfc822");

                                try {
                                    startActivity(Intent.createChooser(emailIntent,
                                            "Send email using..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(getActivity(),
                                            "No email clients installed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }





                    if(retrievedModelUsers.getCoinburners().size()>0){
                        mRvCoins.setVisibility(View.VISIBLE);
                        mRvCoins.setLayoutManager(new LinearLayoutManager(getContext()));
                        CoinAdapter adapter = new CoinAdapter(getContext(), retrievedModelUsers.getCoinburners());
                        mRvCoins.setAdapter(adapter);
                    }else mRvCoins.setVisibility(View.GONE);

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

    public void call_action() {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + supportNumber));
        startActivity(callIntent);
    }
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                call_action();
                return true;
            } else {
                //isPermissionGranted();
                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            call_action();
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
                    call_action();
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
