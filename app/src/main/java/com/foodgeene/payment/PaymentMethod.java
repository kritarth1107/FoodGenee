package com.foodgeene.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
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
    TextView paytm_tv,pod_tv;
    Intent get;
    String totalamount,merchantid,productid,count,price,table;
    Toolbar toolbar;
    String UserToken;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        UserToken = user.get(sessionManager.USER_ID);
        paytm = findViewById(R.id.paytm_card);
        pod = findViewById(R.id.pod_card);
        paytm_tv = findViewById(R.id.amount_tv_paytm);
        pod_tv = findViewById(R.id.amount_tv_pod);
        get = getIntent();
        merchantid = get.getStringExtra("merchandid");
        productid = get.getStringExtra("productid");
        count = get.getStringExtra("count");
        price = get.getStringExtra("price");
        table = get.getStringExtra("table");
        totalamount = get.getStringExtra("totalamount");
        paytm_tv.setText(totalamount);
        pod_tv.setText(totalamount);
        toolbar = findViewById(R.id.toolbar);


        paytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateCheckSum();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethod.this, PodSuccess.class);
                i.putExtra("totalamount",totalamount);
                i.putExtra("productid",productid);
                i.putExtra("count",count);
                i.putExtra("price",price);
                i.putExtra("merchandid",merchantid);
                i.putExtra("table",table);

                FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
                Call<RegisterModel> call = foodGeneeAPI.OrderByCash("cash", merchantid, table, productid, count, price,totalamount,UserToken,"application/x-www-form-urlencoded");
                call.enqueue(new Callback<RegisterModel>() {
                    @Override
                    public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                        try {
                            startActivity(i);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(PaymentMethod.this, "Order Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterModel> call, Throwable t) {

                    }
                });

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
        PaytmPGService Service = PaytmPGService.getStagingService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
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


        //creating a paytm order object using the hashmap
        PaytmOrder order = new PaytmOrder(paramMap);

        //intializing the paytm service
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
