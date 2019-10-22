package com.foodgeene.restraunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.scanner.ScannerModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestrauntActivity extends AppCompatActivity {
    private RecyclerView recyclerView ;
    SessionManager sessionManager;
    String UserToken;
    Intent getIntent;
    String encKey,Store_Name;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restraunt);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);


        recyclerView = findViewById(R.id.ActsRecyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        getIntent = getIntent();
        encKey = getIntent.getStringExtra("encKey");
        Store_Name = getIntent.getStringExtra("store");
        collapsingToolbarLayout.setTitle(Store_Name);
        CallScannerApi(encKey);

    }

    public void CallScannerApi(final String result){

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ScannerModel> call = foodGeneeAPI.submitScannedQR("qrcode",result,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ScannerModel>() {
            @Override
            public void onResponse(Call<ScannerModel> call, Response<ScannerModel> response) {
                try {
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {
                        String merchandid = response.body().getMerchantid().trim();
                        String coverpic = response.body().getCoverpic().trim();
                        String logo = response.body().getLogo().trim();
                        String store = response.body().getStore().trim();
                        String table = response.body().getTable().trim();
                        List<Productlist> productlists = response.body().getProductlist();

                        RestrauntAdapter restrauntAdapter = new RestrauntAdapter(getApplicationContext(),productlists) ;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(restrauntAdapter);
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);



                    } else if (status.equals("0")) {
                        String text = response.body().getText().trim();
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }


                }
                catch (Exception e){
                        finish();
                }

            }
            @Override
            public void onFailure(Call<ScannerModel> call, Throwable t) {
                Toast.makeText(RestrauntActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
