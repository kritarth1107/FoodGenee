package com.foodgeene.preoder.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.*;

public class PreOrder extends AppCompatActivity implements PreOrderAdapter.BookClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_order);
        okIntent = getIntent();
        merchantId = okIntent.getStringExtra("merchantId");
        initViews();
        setupRecycler();
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

        recyclerView = findViewById(R.id.tableRecycler);
        coverImage = findViewById(R.id.allHotelCoverImage);
        restarauntName = findViewById(R.id.restNameHere);
        logo = findViewById(R.id.preOrderImage);
    }


    @Override
    public void buttonClick(int position) {
    list.get(position);
    tableId = list.get(position).getTableid();
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {

        String dayNew = String.valueOf(dayOfMonth);
        String monthNew = String.valueOf(month);
        String yearNew = String.valueOf(year);

        dateSelected = dayNew + monthNew + yearNew;
        openTimePicker(dateSelected);

        }, mYear,mMonth,mDay);
        datePickerDialog.show();


    }

    private void openTimePicker(String dateSelected) {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {

            String hour = String.valueOf(hourOfDay);
            String minuteNew = String.valueOf(minute);

            timeSelected = hour + minuteNew;

        }, mHour, mMinute, false);
        timePickerDialog.show();

        reserveATableRequest(dateSelected, timeSelected);


    }

    private void reserveATableRequest(String dateSelected, String timeSelected) {

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<BookTableModel> call = foodGeneeAPI.bookATable("booktable", merchantId, tableId,dateSelected, timeSelected, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<BookTableModel>() {
            @Override
            public void onResponse(Call<BookTableModel> call, Response<BookTableModel> response) {

                if(response.body().getStatus().equals("1")){



                }
                else if(response.body().getStatus().equals("0")){



                }



            }

            @Override
            public void onFailure(Call<BookTableModel> call, Throwable t) {

            }
        });
    }


}
