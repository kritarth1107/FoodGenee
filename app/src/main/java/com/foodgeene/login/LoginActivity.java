package com.foodgeene.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.forgot.ForgotActivity;
import com.foodgeene.register.RegistrationActivity;
import com.foodgeene.restraunt.SignupOtp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    EditText Email,Password;
    TextView NavigateToReg,forgotPassword;
    Button login;
    GifImageView progressBarLogin;
    SessionManager sessionManager;
    boolean isOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sessionManager
        sessionManager = new SessionManager(this);

        setContentView(R.layout.loginnew);
        Email = findViewById(R.id.EtEmail);
        Password = findViewById(R.id.EtPassword);
        NavigateToReg = findViewById(R.id.NavigateToReg);
        login = findViewById(R.id.LoginButton);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        forgotPassword = findViewById(R.id.forotPassword);
        isOnline=ConnectivityReceiver.isConnected();

        NavigateToReg.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));
        forgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ForgotActivity.class)));
        login.setOnClickListener(view -> {
            String string_email = Email.getText().toString().trim();
            String string_password = Password.getText().toString().trim();

            if(string_email.isEmpty()){
                Email.setError("Email is required");
            }
            else if(string_password.isEmpty()){
                Email.setError("Password is required");
            }
            else{
                if(isOnline)
                login(string_email,string_password);
                else Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void login(final String username, final String Password){
        progressBarLogin.setVisibility(View.VISIBLE);
        login.setVisibility(View.GONE);
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<LoginModel> call = foodGeneeAPI.submitLogin("login",username,Password);
        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                try {
                    assert response.body() != null;
                    String status = response.body().getStatus();
                    String response_text = response.body().getText().trim();
                    if (status.equals("1")) {
                        String user_id = response.body().getUsersid().trim();
                        sessionManager.createSession(user_id);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else if (status.equals("0")) {
                        Toast.makeText(LoginActivity.this, response_text, Toast.LENGTH_SHORT).show();
                    }else if(status.equalsIgnoreCase("2")){
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Your account is inactive . Click ok to active your account! ")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialog, id) ->{
                                    String user_id = response.body().getUsersid().trim();
                                    Intent intent = new Intent(LoginActivity.this, SignupOtp.class);
                                    intent.putExtra("userid", user_id);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);})

                                .show();

                    }

                    progressBarLogin.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);


                } catch (Exception e) {

                    Toast.makeText(LoginActivity.this, "Something went wrong, Try again later", Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);
                    login.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                progressBarLogin.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline=isConnected;
    }
}
