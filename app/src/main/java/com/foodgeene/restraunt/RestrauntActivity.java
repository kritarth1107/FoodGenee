package com.foodgeene.restraunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.scanner.ScannerModel;
import com.foodgeene.success.SuccessActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

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
    String encKey,Store_Name,Table_Name;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView table_number,store,Total_amount,Quanity_item_tv;
    LinearLayout OrderSheet,ViewCartLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restraunt);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        table_number = findViewById(R.id.table_number);
        store = findViewById(R.id.store);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RestrauntActivity.this,ScannerActivity.class));
                finish();
            }
        });
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        OrderSheet = findViewById(R.id.OrderSheet);
        Quanity_item_tv = findViewById(R.id.Quanity_item_tv);
        ViewCartLayout = findViewById(R.id.ViewCartLayout);
        Total_amount = findViewById(R.id.Total_amount);
        recyclerView = findViewById(R.id.ActsRecyclerView);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        getIntent = getIntent();
        encKey = getIntent.getStringExtra("encKey");
        Store_Name = getIntent.getStringExtra("store");
        Table_Name = getIntent.getStringExtra("table");
        collapsingToolbarLayout.setTitle(Store_Name);
        collapsingToolbarLayout.setStatusBarScrim(getResources().getDrawable(R.drawable.bg3));
        store.setText(Store_Name);
        table_number.setText(Table_Name);
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
                        RestrauntAdapter restrauntAdapter = new RestrauntAdapter(getApplicationContext(),productlists,OrderSheet,Total_amount,Quanity_item_tv,ViewCartLayout) ;
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

    @Override
    public void onBackPressed() {

        Intent i = new Intent(RestrauntActivity.this, ScannerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
