package com.foodgeene.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.forgot.ForgotActivity;
import com.foodgeene.register.RegisterModel;
import com.foodgeene.register.RegistrationActivity;
import com.foodgeene.scanner.ScannerActivity;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText Email,Password;
    TextView NavigateToReg,forotPassword;
    Button login;
    ProgressBar progressBarLogin;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sessionManager
        sessionManager = new SessionManager(this);

        setContentView(R.layout.activity_login);
        Email = findViewById(R.id.EtEmail);
        Password = findViewById(R.id.EtPassword);
        NavigateToReg = findViewById(R.id.NavigateToReg);
        login = findViewById(R.id.LoginButton);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        forotPassword = findViewById(R.id.forotPassword);

        NavigateToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
        forotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string_email = Email.getText().toString().trim();
                String string_password = Password.getText().toString().trim();

                if(string_email.isEmpty()){
                    Email.setError("Email is required");
                }
                else if(string_password.isEmpty()){
                    Email.setError("Password is required");
                }
                else{
                    login(string_email,string_password);
                }
            }
        });
    }

    public void login(final String username,final String Password){
        progressBarLogin.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<LoginModel> call = foodGeneeAPI.submitLogin("login",username,Password);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                String status = response.body().getStatus().trim();
                String response_text = response.body().getText().trim();
                if(status.equals("1")){
                    String user_id = response.body().getUsersid().trim();
                    sessionManager.createSession(user_id);
                    Intent i = new Intent(LoginActivity.this, ScannerActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else if(status.equals("0")){
                    Toast.makeText(LoginActivity.this, response_text, Toast.LENGTH_SHORT).show();
                }


                progressBarLogin.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                progressBarLogin.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
