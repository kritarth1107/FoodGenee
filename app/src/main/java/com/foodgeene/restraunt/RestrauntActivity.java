package com.foodgeene.restraunt;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.scanner.Productlist;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.scanner.ScannerModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestrauntActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView recyclerView ;
    SessionManager sessionManager;
    String UserToken;
    Intent getIntent;
    String encKey,Store_Name,Table_Name,cover,orderID;
    Toolbar toolbar;
    ShimmerFrameLayout shimmerFrameLayout;
    TextView table_number,store,Total_amount,Quanity_item_tv;
    LinearLayout OrderSheet,ViewCartLayout;
    ImageView logoHere,iv_filter;
    RequestOptions option;
    String from;
    boolean isOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restraunt);
        toolbar = findViewById(R.id.toolbar);
        option = new RequestOptions().centerCrop().placeholder(R.drawable.ic_image).error(R.drawable.ic_image);
        setSupportActionBar(toolbar);
        logoHere = findViewById(R.id.aa_thumbnail);
        table_number = findViewById(R.id.table_number);
        store = findViewById(R.id.store);
        getIntent = getIntent();
        encKey = getIntent.getStringExtra("encKey");
        Store_Name = getIntent.getStringExtra("store");
        Table_Name = getIntent.getStringExtra("table");
        cover = getIntent.getStringExtra("cover");
        from=getIntent.getStringExtra("from");
        orderID=getIntent.getStringExtra("orderId");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(from.equalsIgnoreCase("order"))
                    startActivity(new Intent(RestrauntActivity.this, MainActivity.class));
                else
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
        iv_filter=findViewById(R.id.iv_filter);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);

        collapsingToolbarLayout.setTitle(Store_Name);
        Glide.with(this).load(cover).apply(option).into(logoHere);
        store.setText(Store_Name);
        table_number.setText(Table_Name);
        isOnline=ConnectivityReceiver.isConnected();

        if(isOnline)
        CallScannerApi(encKey,0);
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

        iv_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(RestrauntActivity.this);
                dialog.setContentView(R.layout.lay_filter);
                //dialog.setCancelable(false);
                RadioGroup radioGroup =  dialog.findViewById(R.id.myRadioGroup);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // find which radio button is selected
                        dialog.dismiss();
                        if(checkedId == R.id.all) {
                            if(isOnline)
                            CallScannerApi(encKey,0);
                            else Toast.makeText(RestrauntActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

                        } else if(checkedId == R.id.egg) {
                            if(isOnline)
                                CallScannerApi(encKey,2);
                            else Toast.makeText(RestrauntActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                        } else if(checkedId == R.id.veg) {
                            if(isOnline)
                                CallScannerApi(encKey,1);
                            else Toast.makeText(RestrauntActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

                        } else if(checkedId == R.id.non_veg) {
                            if(isOnline)
                                CallScannerApi(encKey,3);
                            else Toast.makeText(RestrauntActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                        } else {
                            if(isOnline)
                                CallScannerApi(encKey,0);
                            else Toast.makeText(RestrauntActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

                dialog.show();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void CallScannerApi(final String result, int foodType){

        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ScannerModel> call = foodGeneeAPI.submitScannedQR("qrcode",result,foodType,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ScannerModel>() {
            @Override
            public void onResponse(Call<ScannerModel> call, Response<ScannerModel> response) {
                try {
                    ScannerModel scannerModel = response.body();
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {
                        String merchandid = response.body().getMerchantid().trim();
                        String coverpic = response.body().getCoverpic().trim();
                        String logo = response.body().getLogo().trim();
                        String store = response.body().getStore().trim();
                        String table = response.body().getTable().trim();

                        List<Productlist> productlists = response.body().getProductlist();
                        RestrauntAdapter restrauntAdapter = new RestrauntAdapter(getApplicationContext(),productlists,OrderSheet,Total_amount,Quanity_item_tv,ViewCartLayout,table,merchandid,orderID) ;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(restrauntAdapter);
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);


                        Glide.with(RestrauntActivity.this)
                               .load(scannerModel.getLogo())
                                .into(logoHere);



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
        Intent i;
        if(from.equalsIgnoreCase("order"))
             i = new Intent(RestrauntActivity.this, MainActivity.class);
        else
             i = new Intent(RestrauntActivity.this, ScannerActivity.class);


        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline=isConnected;
    }
}
