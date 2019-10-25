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
import com.foodgeene.restraunt.RestrauntActivity;
import com.foodgeene.scanner.ScannerActivity;
import com.foodgeene.success.PodSuccess;
import com.foodgeene.success.SuccessActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.PaytmRetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethod extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    CardView paytm,pod;
    TextView paytm_tv,pod_tv;
    Intent get;
    String amount;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        paytm = findViewById(R.id.paytm_card);
        pod = findViewById(R.id.pod_card);
        paytm_tv = findViewById(R.id.amount_tv_paytm);
        pod_tv = findViewById(R.id.amount_tv_pod);
        get = getIntent();
        amount = get.getStringExtra("amount");
        paytm_tv.setText(amount);
        pod_tv.setText(amount);
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
                i.putExtra("amount",amount);
                startActivity(i);
            }
        });
    }

    private void generateCheckSum() {


        FoodGeneeAPI foodGeneeAPI = PaytmRetrofitClient.getApiClient().create(FoodGeneeAPI.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                amount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<checksum> call = foodGeneeAPI.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<checksum>() {
            @Override
            public void onResponse(Call<checksum> call, Response<checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                if(paytm.getTxnAmount().equals("0")){}
                else{
                    initializePaytmPayment(response.body().getChecksumHash(), paytm);
                }

            }

            @Override
            public void onFailure(Call<checksum> call, Throwable t) {

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
            i.putExtra("amount",amount);
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
