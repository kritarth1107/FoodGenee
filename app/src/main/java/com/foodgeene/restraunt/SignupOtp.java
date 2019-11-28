package com.foodgeene.restraunt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.register.RegistrationActivity;
import com.foodgeene.register.signupotp.OtpModel;
import com.foodgeene.register.signupotp.ResendOtpModel;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupOtp extends AppCompatActivity {

    EditText one, two, three, four;
    Button verify;
    TextView resendOtp;
    SessionManager sessionManager;
    Intent get;
    String userId, email, mobile, password;
    Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_otp2);

        sessionManager = new SessionManager(this);
        get = getIntent();
        userId = get.getStringExtra("userid");

        one = findViewById(R.id.et1);
        two = findViewById(R.id.et2);
        three = findViewById(R.id.et3);
        four = findViewById(R.id.et4);
        resendOtp = findViewById(R.id.resendOtpHere);

        resendOtp.setOnClickListener(view -> {
            resendOtpFromThisMethod();
        });

        verify = findViewById(R.id.verify_otp);

        verify.setOnClickListener(view -> {
            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            String oneText = one.getText().toString().trim();
            String twoText = two.getText().toString().trim();
            String threeText = three.getText().toString().trim();
            String fourText = four.getText().toString().trim();



            verifyOtp(oneText, twoText, threeText, fourText);
        });


        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 1){

                    two.requestFocus();
                }
                else if(editable.length()==0){
                    one.requestFocus();
                }



            }
        });

        two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.length() == 1){

                    three.requestFocus();
                }
                else if(editable.length()==0){
                    two.requestFocus();
                }



            }

        });

        three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 1){

                    four.requestFocus();
                }
                else if(editable.length()==0){
                    three.requestFocus();
                }



            }


        });

        four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0){

                    three.requestFocus();
                }



            }
        });

        four.setOnEditorActionListener((textView, i, keyEvent) -> false);
    }

    private void resendOtpFromThisMethod() {
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_dialog_layout);
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);

        Call<ResendOtpModel>  call = foodGeneeAPI.resendOtp("resend-otp", userId);
        call.enqueue(new Callback<ResendOtpModel>() {
            @Override
            public void onResponse(Call<ResendOtpModel> call, Response<ResendOtpModel> response) {

                Toast.makeText(SignupOtp.this, "OTP Resent ", Toast.LENGTH_SHORT).show();

                loadingDialog.cancel();
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResendOtpModel> call, Throwable t) {

                loadingDialog.cancel();
                loadingDialog.dismiss();
            }
        });
    }

    private void verifyOtp(String oneText, String twoText, String threeText, String fourText) {
        String otp = oneText + twoText + threeText + fourText;
        RegisterModel registerModel = sessionManager.getUserId();
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<OtpModel> call = foodGeneeAPI.verifyOtp("register-otp", otp, userId);
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                OtpModel otpModel = response.body();

                if(otpModel.getStatus().equals("1")){

                    Intent intent = new Intent(SignupOtp.this, LoginActivity.class);
                    Toast.makeText(SignupOtp.this, "Please login now", Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
                else if(otpModel.getStatus().equals("0")){

                    Toast.makeText(SignupOtp.this, response.body().getText(), Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
                else {

                    Toast.makeText(SignupOtp.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
//               Intent intent = new Intent(SignupOtp.this, MainActivity.class);
//               startActivity(intent);
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {
                Toast.makeText(SignupOtp.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                loadingDialog.cancel();
                loadingDialog.dismiss();

            }
        });

    }


}
