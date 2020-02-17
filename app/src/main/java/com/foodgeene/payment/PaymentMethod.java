package com.foodgeene.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.payment.coupon.Coupon;
import com.foodgeene.success.PodSuccess;
import com.foodgeene.success.SuccessActivity;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.text.DecimalFormat;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethod extends AppCompatActivity implements PaytmPaymentTransactionCallback, ConnectivityReceiver.ConnectivityReceiverListener {
    CardView paytm,pod;
    TextView paytm_tv,pod_tv,amtv,newtc,newtcs;
    Intent get;
    String totalamount,merchantid,productid,count,price,table,orderID;
    RelativeLayout toolbar;
    String UserToken;
    SessionManager sessionManager;
    TextView HaveACouponTV,applyButton,removeButton,appliedTV;
    LinearLayout CouponLayout,mLLSubscription,mLLTip,mLLDiscount,mLLSave;
    EditText couponEditText;
    GifImageView ProgressBarCoupon;
    String Coupon;
    String BackupAmount;
    TextView realPrice, discountedPrice,mTvTax,mTvSubscription,mTvTip,mTvActualPrice;
    CardView offerdetails;
    TextView savedprice;
    ImageView mIvBack;
    boolean isOnLine;
    String couponAmount="0";
    String coupon="";
    CheckBox mCSub,mCTip;
    String tax,tips,subscription;
    String discountAmount;
    DecimalFormat formText;
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
        mLLSubscription=findViewById(R.id.ll_subscription);
        mLLTip=findViewById(R.id.ll_tip);
        mCSub=findViewById(R.id.c_sub);
        mCTip=findViewById(R.id.c_tip);
        mTvSubscription=findViewById(R.id.tv_subscription);
        mTvTip=findViewById(R.id.tv_tip);
        mTvActualPrice=findViewById(R.id.actualprice);
        formText = new DecimalFormat("0.00");

        mTvTax=findViewById(R.id.tv_tax);
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
        mLLDiscount=findViewById(R.id.ll_discount);
        mLLSave=findViewById(R.id.ll_saving);
       // offerdetails.setVisibility(View.GONE);
        get = getIntent();
        merchantid = get.getStringExtra("merchandid");
        productid = get.getStringExtra("productid");
        Log.e("productid",productid);
        count = get.getStringExtra("count");
        price = get.getStringExtra("price");
        table = get.getStringExtra("table");
        totalamount = get.getStringExtra("totalamount");
        orderID=get.getStringExtra("orderID");
        BackupAmount=totalamount;
        paytm_tv.setText(totalamount);
        pod_tv.setText(totalamount);
        amtv.setText(totalamount);
        newtc.setText(totalamount);
        newtcs.setText(totalamount);
        toolbar = findViewById(R.id.toolbar);
        mIvBack=findViewById(R.id.iv_back);
        HaveACouponTV = findViewById(R.id.HaveACouponTV);
        HaveACouponTV.setOnClickListener(view -> {
            HaveACouponTV.setVisibility(View.GONE);
            CouponLayout.setVisibility(View.VISIBLE);
        });
        isOnLine=ConnectivityReceiver.isConnected();
        if(isOnLine) {
            applyCoupon("","new");
            coupon="";
        } else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();



        applyButton.setOnClickListener(view -> {
            Coupon = couponEditText.getText().toString().trim();
            if (!Coupon.isEmpty()){
                if(isOnLine) {
                    applyCoupon(Coupon,"apply");
                    coupon=Coupon;
                } else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

            }else coupon="";
        });
        removeButton.setOnClickListener(view -> {
            if(isOnLine) {
                totalamount = get.getStringExtra("totalamount");
                applyCoupon("","remove");
                coupon="";
            } else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            applyButton.setVisibility(View.VISIBLE);
            couponEditText.setText("");
            removeButton.setVisibility(View.GONE);
            couponEditText.setVisibility(View.VISIBLE);
            appliedTV.setVisibility(View.GONE);
        });


        paytm.setOnClickListener(view -> generateCheckSum());
        mIvBack.setOnClickListener(view -> finish());
       // toolbar.setNavigationOnClickListener(view -> finish());

        pod.setOnClickListener(view -> {
            Intent i = new Intent(PaymentMethod.this, PodSuccess.class);
            i.putExtra("totalamount",totalamount);
            i.putExtra("productid",productid);
            i.putExtra("count",count);
            i.putExtra("price",price);
            i.putExtra("merchandid",merchantid);
            i.putExtra("table",table);
            i.putExtra("orderID",orderID);
            i.putExtra("couponAmount",couponAmount);
            i.putExtra("coupon",coupon);
            i.putExtra("tax",tax);
            i.putExtra("tips",tips);
            i.putExtra("subscription",subscription);

            startActivity(i);


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    void applyCoupon(String cpn,String action){
        applyButton.setVisibility(View.GONE);
        ProgressBarCoupon.setVisibility(View.VISIBLE);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<com.foodgeene.payment.coupon.Coupon> call = foodGeneeAPI.applyCoupon("apply-coupon",cpn , get.getStringExtra("totalamount"), merchantid,productid,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                try {
                    String status = response.body().getStatus().trim();


                    if(status.equals("1")){
                        mLLSave.setVisibility(View.VISIBLE);
                        mLLDiscount.setVisibility(View.VISIBLE);
                        offerdetails.setVisibility(View.VISIBLE);
                        String amnt = response.body().getTotalamt().trim();
                        totalamount = amnt;
                        paytm_tv.setText(amnt);
                        pod_tv.setText(amnt);
                        amtv.setText(amnt);
                        newtc.setText(amnt);
                        newtcs.setText(amnt);

                        if(action.equalsIgnoreCase("apply")){
                            ProgressBarCoupon.setVisibility(View.GONE);
                            removeButton.setVisibility(View.VISIBLE);
                            couponEditText.setVisibility(View.GONE);
                            applyButton.setVisibility(View.GONE);
                            appliedTV.setVisibility(View.VISIBLE);
                        }else{
                            ProgressBarCoupon.setVisibility(View.GONE);
                            removeButton.setVisibility(View.GONE);
                            couponEditText.setVisibility(View.VISIBLE);
                            applyButton.setVisibility(View.VISIBLE);
                            appliedTV.setVisibility(View.GONE);
                        }

                        offerdetails.setVisibility(View.VISIBLE);
                        realPrice.setText("₹ "+response.body().getTotalamt());
                        mTvActualPrice.setText("₹ "+response.body().getAmount());
                        discountedPrice.setText("₹ "+response.body().getCoupanamt());
                        savedprice.setText("₹ "+response.body().getSavingamt());
                        mTvTax.setText("₹ "+response.body().getTax());
                        tax=response.body().getTax();
                        discountAmount=response.body().getCoupanamt();
                        couponAmount=response.body().getCoupanamt();
                        if(response.body().getTips()!=null) {
                            mCTip.setChecked(true);
                            mLLTip.setVisibility(View.VISIBLE);
                            tips=response.body().getTips();
                            mTvTip.setText("₹ "+response.body().getTips());
                            totalamount= formText.format(Double.valueOf(totalamount) + Double.valueOf(response.body().getTips()));
                            BackupAmount=totalamount;
                            paytm_tv.setText(totalamount);
                            pod_tv.setText(totalamount);
                            amtv.setText(totalamount);
                            newtc.setText(totalamount);
                            newtcs.setText(totalamount);
                            realPrice.setText("₹ "+totalamount);
                            mCTip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        totalamount= formText.format(Double.valueOf(totalamount) + Double.valueOf(response.body().getTips()));
                                        BackupAmount=totalamount;
                                        paytm_tv.setText(totalamount);
                                        pod_tv.setText(totalamount);
                                        amtv.setText(totalamount);
                                        newtc.setText(totalamount);
                                        newtcs.setText(totalamount);
                                        realPrice.setText("₹ "+totalamount);
                                        tips=response.body().getTips();
                                    }else{
                                   /*     if(totalamount.equalsIgnoreCase(amnt)){
                                            paytm_tv.setText(totalamount);
                                            pod_tv.setText(totalamount);
                                            amtv.setText(totalamount);
                                            newtc.setText(totalamount);
                                            newtcs.setText(totalamount);
                                            realPrice.setText(totalamount);
                                            tips="0";
                                            BackupAmount=totalamount;
                                        }else{*/
                                            totalamount= formText.format(Double.valueOf(totalamount) - Double.valueOf(response.body().getTips()));
                                        BackupAmount=totalamount;
                                            paytm_tv.setText(totalamount);
                                            pod_tv.setText(totalamount);
                                            amtv.setText(totalamount);
                                            newtc.setText(totalamount);
                                            newtcs.setText(totalamount);
                                            realPrice.setText("₹ "+totalamount);
                                            tips="0";
                                       // }
                                    }
                                }
                            });
                        }else{
                            mLLTip.setVisibility(View.GONE);
                        }

                        if(response.body().getSubscription()!=null) {
                            mCSub.setChecked(true);
                            mLLSubscription.setVisibility(View.VISIBLE);
                            subscription=response.body().getSubscription();
                            mTvSubscription.setText("₹ "+response.body().getSubscription());
                            totalamount=formText.format(Double.valueOf(totalamount)+Double.valueOf(response.body().getSubscription()));
                            BackupAmount=totalamount;
                            paytm_tv.setText(totalamount);
                            pod_tv.setText(totalamount);
                            amtv.setText(totalamount);
                            newtc.setText(totalamount);
                            newtcs.setText(totalamount);
                            realPrice.setText("₹ "+totalamount);
                            mCSub.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                    if(b){
                                        totalamount=formText.format(Double.valueOf(totalamount)+Double.valueOf(response.body().getSubscription()));
                                        BackupAmount=totalamount;
                                        paytm_tv.setText(totalamount);
                                        pod_tv.setText(totalamount);
                                        amtv.setText(totalamount);
                                        newtc.setText(totalamount);
                                        newtcs.setText(totalamount);

                                        realPrice.setText("₹ "+totalamount);
                                        subscription=response.body().getSubscription();
                                    }else{
                                      /*  if(totalamount.equalsIgnoreCase(amnt)){
                                            paytm_tv.setText(totalamount);
                                            pod_tv.setText(totalamount);
                                            amtv.setText(totalamount);
                                            newtc.setText(totalamount);
                                            newtcs.setText(totalamount);
                                            realPrice.setText(totalamount);
                                            subscription="0";
                                            BackupAmount=totalamount;
                                        }else{*/
                                            totalamount= formText.format(Double.valueOf(totalamount) - Double.valueOf(response.body().getSubscription()));
                                            BackupAmount=totalamount;
                                            paytm_tv.setText(totalamount);
                                            pod_tv.setText(totalamount);
                                            amtv.setText(totalamount);
                                            newtc.setText(totalamount);
                                            newtcs.setText(totalamount);
                                            realPrice.setText("₹ "+totalamount);
                                            subscription="0";

                                      //  }
                                    }
                                }
                            });
                        }else{
                            mLLSubscription.setVisibility(View.GONE);
                        }
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

        //Testing service
       // PaytmPGService Service = PaytmPGService.getStagingService();

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
            i.putExtra("couponAmount",couponAmount);
            i.putExtra("coupon",coupon);
            i.putExtra("tax",tax);
            i.putExtra("tips",tips);
            i.putExtra("subscription",subscription);
            i.putExtra("orderID",orderID);
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}