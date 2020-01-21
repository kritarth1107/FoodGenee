package com.foodgeene.preoder.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.preoder.preorderadapter.PreOrderAdapter;
import com.foodgeene.preoder.preordermodel.BookTableModel;
import com.foodgeene.preoder.preordermodel.PreOrderModel;
import com.foodgeene.preoder.preordermodel.Tablelist;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreOrder extends AppCompatActivity implements PreOrderAdapter.BookClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    RecyclerView recyclerView;
    ImageView coverImage, logo;
    TextView restarauntName;
    Intent okIntent;
    String merchantId;
    String userToken;
    List<Tablelist> list;
    String tableId;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String dateSelected;
    String timeSelected;
    ImageView mIvBack;
    TextView mTvRestName,mTvRestDesc,mTvPhone,mTvMail,mTvAddress;
    LinearLayout mLLNavigation;
    boolean isOnLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        okIntent = getIntent();
        merchantId = okIntent.getStringExtra("merchantId");
        isOnLine=ConnectivityReceiver.isConnected();

        initViews();
        if(isOnLine)
        setupRecycler();
        else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void setupRecycler() {
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();

         userToken = user.get(sessionManager.USER_ID);

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<PreOrderModel> call = foodGeneeAPI.getPreOrder("tablenames", merchantId, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<PreOrderModel>() {
            @Override
            public void onResponse(Call<PreOrderModel> call, Response<PreOrderModel> response) {


                assert response.body() != null;
                if(response.body().getStatus().equals(1)) {

                        list = response.body().getTablelist();
                        recyclerView.setLayoutManager(new LinearLayoutManager(PreOrder.this));
                        PreOrderAdapter adapter = new PreOrderAdapter(list, PreOrder.this, PreOrder.this);
                        recyclerView.setAdapter(adapter);
                        Glide.with(PreOrder.this)
                                .load(response.body().getCoverpic())
                                .into(coverImage);

                        Glide.with(PreOrder.this)
                                .load(response.body().getLogo())
                                .into(logo);

                        restarauntName.setText(response.body().getStorename());
                        mTvRestName.setText(response.body().getStorename());
                    mTvPhone.setText(response.body().getMobile());
                    mTvMail.setText(response.body().getEmail());
                    mTvAddress.setText(response.body().getAddress()+","+response.body().getLocation()+","+response.body().getCity()+","+response.body().getState());

                    mLLNavigation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("google.navigation:q="+Double.valueOf(response.body().getLatitude())+","+Double.valueOf(response.body().getLongitude())+"&mode=d"));
                            intent.setPackage("com.google.android.apps.maps");
                          startActivity(intent);
                        }
                    });
                    }

                    else if(response.body().getStatus().equals(0)){

                        Toast.makeText(PreOrder.this, "No Tables Now", Toast.LENGTH_SHORT).show();

                    }

                    else {

                        Toast.makeText(PreOrder.this, "Error", Toast.LENGTH_SHORT).show();
                    }







            }

            @Override
            public void onFailure(Call<PreOrderModel> call, Throwable t) {

            }
        });

    }

    private void initViews() {
        mTvRestName=findViewById(R.id.tv_rest_name);
        mTvRestDesc=findViewById(R.id.tv_rest_desc);
        mTvPhone=findViewById(R.id.tv_mobile);
        mTvMail=findViewById(R.id.tv_mail);
        mIvBack=findViewById(R.id.iv_back);
        mTvAddress=findViewById(R.id.tv_address);
        recyclerView = findViewById(R.id.tableRecycler);
        coverImage = findViewById(R.id.allHotelCoverImage);
        restarauntName = findViewById(R.id.restNameHere);
        logo = findViewById(R.id.preOrderImage);
        mLLNavigation=findViewById(R.id.ll_navigate);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    @Override
    public void buttonClick(int position) {
    list.get(position);
    tableId = list.get(position).getTableid();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth)

                -> {

        String dayNew = String.valueOf(dayOfMonth);
        String monthNew = String.valueOf(Calendar.MONTH);
        String yearNew = String.valueOf(year);
        if(monthNew.startsWith("0"))
            monthNew=monthNew;
        else monthNew="0"+monthNew;

        dateSelected = yearNew +"-"+ monthNew +"-"+ dayNew;
        openTimePicker(dateSelected);

        }, mYear,mMonth,mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();


    }

    private void openTimePicker(String dateSelected) {
       /* final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);*/

    /*    TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {

            String hour = String.valueOf(hourOfDay);
            String minuteNew = String.valueOf(minute);

            timeSelected = hour +":"+minuteNew;

        }, mHour, mMinute, false);

        timePickerDialog.show();*/


        TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String hours = String.valueOf(hour);
                String minuteNew = String.valueOf(minute);

                timeSelected = hours +":"+minuteNew;
                Log.e("timeSelected",""+timeSelected);
                if(isOnLine)
                reserveATableRequest(dateSelected, timeSelected);
                else Toast.makeText(PreOrder.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();

            }
        };
        Calendar c = Calendar.getInstance();

        final TimePickerDialog timePickerDialog = new TimePickerDialog(PreOrder.this,timePickerListener,
                c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE)+5,false);
        timePickerDialog.show();



    }

    private void reserveATableRequest(String dateSelected, String timeSelected) {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<BookTableModel> call = foodGeneeAPI.bookATable("booktable", merchantId, tableId,dateSelected, timeSelected, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<BookTableModel>() {
            @Override
            public void onResponse(Call<BookTableModel> call, Response<BookTableModel> response) {

                if(response.body().getStatus().equals("1")){

                    new AlertDialog.Builder(PreOrder.this)
                .setMessage("Booking confirmed successfully")
                .setCancelable(false)
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                   }
                })
                .show();

                }
                else if(response.body().getStatus().equals("0")){



                }



            }

            @Override
            public void onFailure(Call<BookTableModel> call, Throwable t) {

            }
        });
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnLine=isConnected;
    }
}
