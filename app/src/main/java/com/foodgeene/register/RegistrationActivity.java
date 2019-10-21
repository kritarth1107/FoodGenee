package com.foodgeene.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.R;
import com.google.android.material.snackbar.Snackbar;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    TextView NavigateToLogin;
    EditText Email,Number,Name,Password;
    Button RegisterButton;
    ProgressBar progressBarReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        NavigateToLogin = findViewById(R.id.NavigateToLogin);
        Email = findViewById(R.id.EditTextEmail);
        Number = findViewById(R.id.EditTextNumber);
        Name = findViewById(R.id.EditTextName);
        Password = findViewById(R.id.EditTextPassword);
        RegisterButton = findViewById(R.id.RegisterButton);
        progressBarReg = findViewById(R.id.progressBarReg);

        NavigateToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String String_Name = Name.getText().toString().trim();
                String String_Email = Email.getText().toString().trim();
                String String_Number = Number.getText().toString().trim();
                String String_Password = Password.getText().toString().trim();

                if(String_Name.isEmpty()){
                        Name.setError("Name is required");
                }
                else if(String_Email.isEmpty()){
                    Email.setError("Email is required");
                }
                else if(String_Number.isEmpty()){
                    Number.setError("Number is required");
                }
                else if(String_Password.isEmpty()){
                    Password.setError("Password is required");
                }
                else if(String_Number.length()<10){
                    Number.setError("Invalid Mobile Number");
                }
                else{
                    Register(String_Name,String_Email,String_Number,String_Password);
                }


            }
        });
    }

    public void Register(final String Name,final String Email, final String Phone, final String Password){

        progressBarReg.setVisibility(View.VISIBLE);
        RegisterButton.setVisibility(View.GONE);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RegisterModel> call = foodGeneeAPI.submitRegistration("registration",Name,Email,Phone,Password
        );
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {

                String status = response.body().getStatus().trim();
                String response_text = response.body().getText().trim();
                if(status.equals("1")){
                    String user_id = response.body().getUsersid().trim();
                    Toast.makeText(RegistrationActivity.this, response_text+" - "+user_id, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(status.equals("0")){
                    Toast.makeText(RegistrationActivity.this, response_text, Toast.LENGTH_SHORT).show();
                }


                progressBarReg.setVisibility(View.GONE);
                RegisterButton.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                progressBarReg.setVisibility(View.GONE);
                RegisterButton.setVisibility(View.VISIBLE);
                Toast.makeText(RegistrationActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}