package com.foodgeene.forgot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity {
    EditText forgotPasswordEd;
    TextView NavigateToLogin;
    Button forgotPassword;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        forgotPasswordEd = findViewById(R.id.etPasswd);
        forgotPassword = findViewById(R.id.forgotButton);
        NavigateToLogin = findViewById(R.id.NavigateToLogin);
        NavigateToLogin.setOnClickListener(view -> finish());


        forgotPassword.setOnClickListener(view -> {
            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
            String userName = forgotPasswordEd.getText().toString();

            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
            Call<ForgotPasswordModel> call = foodGeneeAPI.forgotPassword("forgotpassword", userName);

            call.enqueue(new Callback<ForgotPasswordModel>() {
                @Override
                public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {

                    ForgotPasswordModel fm = response.body();

                    if(fm.getStatus().equals("1")){
                        String userid = fm.getUsersid();
                        Intent intent = new Intent(ForgotActivity.this, VerifyPasswors.class);
                        intent.putExtra("userId", userid);
                        startActivity(intent);
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                    }
                    else if(fm.getStatus().equals("0")){

                        Toast.makeText(ForgotActivity.this, fm.getText(), Toast.LENGTH_SHORT).show();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                    loadingDialog.cancel();
                    loadingDialog.dismiss();

                }
            });
        });
    }

}
