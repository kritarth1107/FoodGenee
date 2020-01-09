package com.foodgeene.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.payment.coupon.Coupon;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.success.PodSuccess;
import com.foodgeene.success.SuccessActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.PaytmRetrofitClient;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethod extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    CardView paytm,pod;
    TextView paytm_tv,pod_tv,amtv,newtc,newtcs;
    Intent get;
    String totalamount,merchantid,productid,count,price,table;
    Toolbar toolbar;
    String UserToken;
    SessionManager sessionManager;
    TextView HaveACouponTV,applyButton,removeButton,appliedTV;
    LinearLayout CouponLayout;
    EditText couponEditText;
    ProgressBar ProgressBarCoupon;
    String Coupon;
    String BackupAmount;
    TextView realPrice, discountedPrice;
    CardView offerdetails;
    TextView savedprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        sessionManager = new SessionManager(this);
        CouponLayout = findViewById(R.id.CouponLayout);
        CouponLayout.setVisibility(View.GONE);
        realPrice = findViewById(R.id.realprice);
        discountedPrice = findViewById(R.id.discountedPrice);
        savedprice = findViewById(R.id.savedAmount);
        amtv = findViewById(R.id.amtv);
        newtc = findViewById(R.id.newtc);
        newtcs = findViewById(R.id.newtcs);
        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        paytm = findViewById(R.id.paytm_card);
        pod = findViewById(R.id.pod_card);
        appliedTV = findViewById(R.id.appliedTV);
        applyButton = findViewById(R.id.applyButton);
        removeButton = findViewById(R.id.removeButton);
        couponEditText = findViewById(R.id.couponEditText);
        ProgressBarCoupon = findViewById(R.id.ProgressBarCoupon);
        paytm_tv = findViewById(R.id.amount_tv_paytm);
        pod_tv = findViewById(R.id.amount_tv_pod);
        offerdetails = findViewById(R.id.offerdetails);
        get = getIntent();
        merchantid = get.getStringExtra("merchandid");
        productid = get.getStringExtra("productid");
        count = get.getStringExtra("count");
        price = get.getStringExtra("price");
        table = get.getStringExtra("table");
        totalamount = get.getStringExtra("totalamount");
        BackupAmount=totalamount;
        paytm_tv.setText(totalamount);
        pod_tv.setText(totalamount);
        amtv.setText(totalamount);
        newtc.setText(totalamount);
        newtcs.setText(totalamount);
        toolbar = findViewById(R.id.toolbar);
        HaveACouponTV = findViewById(R.id.HaveACouponTV);
        HaveACouponTV.setOnClickListener(view -> {
            HaveACouponTV.setVisibility(View.GONE);
            CouponLayout.setVisibility(View.VISIBLE);
        });
        applyButton.setOnClickListener(view -> {
            Coupon = couponEditText.getText().toString().trim();
            if (!Coupon.isEmpty()){
                applyCoupon(Coupon);
            }
        });
        removeButton.setOnClickListener(view -> {
            totalamount = BackupAmount;
            pod_tv.setText(BackupAmount);
            paytm_tv.setText(BackupAmount);
            amtv.setText(BackupAmount);
            newtc.setText(BackupAmount);
            newtcs.setText(BackupAmount);
            applyButton.setVisibility(View.VISIBLE);
            couponEditText.setText("");
            removeButton.setVisibility(View.GONE);
            couponEditText.setVisibility(View.VISIBLE);
            appliedTV.setVisibility(View.GONE);
            offerdetails.setVisibility(View.GONE);
        });


        paytm.setOnClickListener(view -> generateCheckSum());
        toolbar.setNavigationOnClickListener(view -> finish());

        pod.setOnClickListener(view -> {
            Intent i = new Intent(PaymentMethod.this, PodSuccess.class);
            i.putExtra("totalamount",totalamount);
            i.putExtra("productid",productid);
            i.putExtra("count",count);
            i.putExtra("price",price);
            i.putExtra("merchandid",merchantid);
            i.putExtra("table",table);
            startActivity(i);


        });
    }
    void applyCoupon(String cpn){
        applyButton.setVisibility(View.GONE);
        ProgressBarCoupon.setVisibility(View.VISIBLE);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<com.foodgeene.payment.coupon.Coupon> call = foodGeneeAPI.applyCoupon("apply-coupon",cpn , totalamount, merchantid,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                try {
                    String status = response.body().getStatus().trim();


                    if(status.equals("1")){
                        String amnt = response.body().getTotalamt().toString().trim();
                        totalamount = amnt;
                        paytm_tv.setText(amnt);
                        pod_tv.setText(amnt);
                        amtv.setText(amnt);
                        newtc.setText(amnt);
                        newtcs.setText(amnt);
                        removeButton.setVisibility(View.VISIBLE);
                        ProgressBarCoupon.setVisibility(View.GONE);
                        couponEditText.setVisibility(View.GONE);
                        appliedTV.setVisibility(View.VISIBLE);
                        offerdetails.setVisibility(View.VISIBLE);
                        realPrice.setText("Rs. "+response.body().getTotalamt());
                        discountedPrice.setText("Rs. "+response.body().getCoupanamt());
                        savedprice.setText("Rs. "+response.body().getSavingamt());


                    }
                    else{
                        applyButton.setVisibility(View.VISIBLE);
                        ProgressBarCoupon.setVisibility(View.GONE);
                        Toast.makeText(PaymentMethod.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(PaymentMethod.this, "Invalid Coupon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Coupon> call, Throwable t) {
                Toast.makeText(PaymentMethod.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateCheckSum() {


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);


        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                totalamount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = foodGeneeAPI.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate Checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the Checksum we will initiailize the payment.
                //the method is taking the Checksum we got and the paytm object as the parameter
                if(paytm.getTxnAmount().equals("0")){}
                else{
                    initializePaytmPayment(response.body().getChecksumHash(), paytm);
                }

            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
    }
    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constants.M_ID);
        paramMap.put("ORDER_ID", paytm.getOrderId());
        paramMap.put("CUST_ID", paytm.getCustId());
        paramMap.put("CHANNEL_ID", paytm.getChannelId());
        paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
        paramMap.put("WEBSITE", paytm.getWebsite());
        paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put("CHECKSUMHASH", checksumHash);
        paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


        PaytmOrder order = new PaytmOrder(paramMap);

        Service.initialize(order, null);

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    public void onTransactionResponse(Bundle inResponse) {
        String status = inResponse.getString("STATUS");
        String checksumhash = inResponse.getString("CHECKSUMHASH");
        String TXNID = inResponse.getString("TXNID");
        String ORDERID = inResponse.getString("ORDERID");
        String TXNDATE = inResponse.getString("TXNDATE");
        String RESPCODE = inResponse.getString("RESPCODE");
        String BANKTXNID = inResponse.getString("BANKTXNID");
        String TXNAMOUNT = inResponse.getString("TXNAMOUNT");
        String PAYMENTMODE = inResponse.getString("PAYMENTMODE");
        String CURRENCY = inResponse.getString("CURRENCY");
        String GATEWAYNAME = inResponse.getString("GATEWAYNAME");
        String RESPMSG = inResponse.getString("RESPMSG");
        String BANKNAME = inResponse.getString("BANKNAME");
        String shareBody = inResponse.toString();

        if(status.equals("TXN_SUCCESS")){
            Intent i = new Intent(PaymentMethod.this, SuccessActivity.class);
            i.putExtra("totalamount",totalamount);
            i.putExtra("productid",productid);
            i.putExtra("count",count);
            i.putExtra("price",price);
            i.putExtra("merchandid",merchantid);
            i.putExtra("table",table);
            i.putExtra("TXNID",TXNID);
            i.putExtra("TXNDATE",TXNDATE);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void clientAuthenticationFailed(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void someUIErrorOccurred(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
    }

}