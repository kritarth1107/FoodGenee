package com.foodgeene.forgot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.register.signupotp.OtpModel;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPasswors extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4;
    Intent get;
    String userId;
    Button verifyhere;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_passwors);
        get = getIntent();
        userId = get.getStringExtra("userId");


        ed1 = findViewById(R.id.et1);
        ed2 = findViewById(R.id.et2);
        ed3 = findViewById(R.id.et3);
        ed4 = findViewById(R.id.et4);

        verifyhere = findViewById(R.id.verify_otp);
        verifyhere.setOnClickListener(view -> {

            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            validateOtp();
        });

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed2.requestFocus();
                } else if (s.length() == 0) {
                    ed1.clearFocus();
                }
            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed3.requestFocus();
                } else if (s.length() == 0) {
                    ed1.requestFocus();
                }
            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    ed4.requestFocus();
                } else if (s.length() == 0) {
                    ed2.requestFocus();
                }
            }
        });

       ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {
                    ed3.requestFocus();
                }
            }

        });


    }



    private void validateOtp() {

        String newOtp = ed1.getText().toString() + ed2.getText().toString() + ed3.getText().toString() + ed4.getText().toString();

        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ForgotOto> call = foodGeneeAPI.verifyForOtp("forgotpassword-otp", newOtp, userId);

        call.enqueue(new Callback<ForgotOto>() {
            @Override
            public void onResponse(Call<ForgotOto> call, Response<ForgotOto> response) {

                ForgotOto retrievedResult = response.body();

                try{
                   if(retrievedResult.getStatus().equals("1")){

                       Toast.makeText(VerifyPasswors.this, "OTP Verified", Toast.LENGTH_SHORT).show();
                       Intent intent  = new Intent(VerifyPasswors.this, ChangePassword.class);
                       intent.putExtra("userId", retrievedResult.getUsersid());
                       startActivity(intent);
                       Log.d("TEST", retrievedResult.getStatus());
                       loadingDialog.cancel();
                       loadingDialog.dismiss();
                   }
                   else if(retrievedResult.getStatus().equals("0")){

                       Toast.makeText(VerifyPasswors.this, "OTP Invalid", Toast.LENGTH_SHORT).show();
                       Log.d("TEST", retrievedResult.getStatus());
                       loadingDialog.cancel();
                       loadingDialog.dismiss();

                   }

                }
                catch (Exception e){

                    Toast.makeText(VerifyPasswors.this, "OTP Invalid", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ForgotOto> call, Throwable t) {
                loadingDialog.cancel();
                loadingDialog.dismiss();

            }
        });


    }
}
