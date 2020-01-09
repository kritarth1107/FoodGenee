package com.foodgeene.forgot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.forgot.changereal.PasswordChangeModel;
import com.foodgeene.login.LoginActivity;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {

EditText changedPasswordInput;
EditText confirmPassword;
Button changedPassoword;
Intent spd;
String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        spd = getIntent();
        userId = spd.getStringExtra("userId");
        changedPasswordInput = findViewById(R.id.new_password);
        changedPassoword = findViewById(R.id.reset_password);
        confirmPassword = findViewById(R.id.confirmpassword);
        changedPassoword.setOnClickListener(view -> startAcall());


    }

    private void startAcall() {

        String newPassword = changedPasswordInput.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();

        if(newPassword.equals(confirm)){
            FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);

            Call<PasswordChangeModel> call = foodGeneeAPI.changePassword("changepassword", newPassword, userId);
            call.enqueue(new Callback<PasswordChangeModel>() {
                @Override
                public void onResponse(Call<PasswordChangeModel> call, Response<PasswordChangeModel> response) {

                    try{
                        PasswordChangeModel ps = response.body();

                        if(ps.getStatus().equals("1")){

                            Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    }
                    catch (Exception e){

                        Toast.makeText(ChangePassword.this, "Invalid User", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PasswordChangeModel> call, Throwable t) {

                    Toast.makeText(ChangePassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if(!newPassword.equals(confirm)){

            Toast.makeText(this, "Password not same, Please try again with same password.", Toast.LENGTH_SHORT).show();
        }

        else {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }
}