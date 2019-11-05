package com.foodgeene.restraunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.register.signupotp.OtpModel;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_otp2);

        sessionManager = new SessionManager(this);
        one = findViewById(R.id.et1);
        two = findViewById(R.id.et2);
        three = findViewById(R.id.et3);
        four = findViewById(R.id.et4);


        verify = findViewById(R.id.verify_otp);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oneText = one.getText().toString().trim();
                String twoText = two.getText().toString().trim();
                String threeText = three.getText().toString().trim();
                String fourText = four.getText().toString().trim();



                verifyOtp(oneText, twoText, threeText, fourText);
            }
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

        four.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return false;
            }
        });
    }

    private void verifyOtp(String oneText, String twoText, String threeText, String fourText) {
        String otp = oneText + twoText + threeText + fourText;
        RegisterModel registerModel = sessionManager.getUserId();
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<OtpModel> call = foodGeneeAPI.verifyOtp("verifyotp", otp, registerModel.getUsersid());
        call.enqueue(new Callback<OtpModel>() {
            @Override
            public void onResponse(Call<OtpModel> call, Response<OtpModel> response) {
                OtpModel otpModel = response.body();

               Intent intent = new Intent(SignupOtp.this, MainActivity.class);
               startActivity(intent);
            }

            @Override
            public void onFailure(Call<OtpModel> call, Throwable t) {

            }
        });

    }
}
