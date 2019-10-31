package updateprofile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends AppCompatActivity {

    TextInputEditText userName, userEmail, userPassword;
    Button updateButton;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        initViews();
        updateButton.setOnClickListener(view -> {
            updateUserDetails();

        });

    }

    private void updateUserDetails() {

        String newname = userName.getText().toString().trim();
        String newemail = userEmail.getText().toString().trim();
        String newPass = userPassword.getText().toString().trim();
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
       String userToken = user.get(sessionManager.USER_ID);


        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);

        Call<UpdateModel> call = foodGeneeAPI.updateUser("updation", newname, newemail, newPass,  userToken,"application/x-www-form-urlencoded");

        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {

                UpdateModel reModel = response.body();
                assert reModel != null;
                if(reModel.getStatus()==1){

                    Toast.makeText(UpdateProfile.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                }
                else if(reModel.getStatus()==0){

                    Toast.makeText(UpdateProfile.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                }
                else {

                    Toast.makeText(UpdateProfile.this, "Some error occured.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {

            }
        });

    }

    private void initViews() {
        userName = findViewById(R.id.changeName);
        userEmail = findViewById(R.id.changeEmail);
        userPassword = findViewById(R.id.changePassword);

        updateButton = findViewById(R.id.updateButton);


    }
}
