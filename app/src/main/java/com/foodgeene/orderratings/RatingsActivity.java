package com.foodgeene.orderratings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingsActivity extends AppCompatActivity implements RatingDialogListener {

    Intent intent;
    String oId = null;
    SessionManager sessionManager;
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        userToken = user.get(sessionManager.USER_ID);
        intent = getIntent();
        oId = intent.getStringExtra("orderId");
        showDailog();


    }

    @Override
    public void onNegativeButtonClicked() {

        cancelThisWindow();

    }

    private void cancelThisWindow() {
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<CancelModel> cancelModelCall = foodGeneeAPI.cancelFeed("close-feedback", oId,userToken,"application/x-www-form-urlencoded");
        cancelModelCall.enqueue(new Callback<CancelModel>() {
            @Override
            public void onResponse(Call<CancelModel> call, Response<CancelModel> response) {

                if(response.body().getStatus().equals("1")){
                    finish();
                }
                else if(response.body().getStatus().equals("0")){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<CancelModel> call, Throwable t) {
                finish();

            }
        });
    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<RatingModel> call = foodGeneeAPI.orderFeedback("add-feedback", "1",String.valueOf(i), s,oId, userToken, "application/x-www-form-urlencoded");
        call.enqueue(new Callback<RatingModel>() {
            @Override
            public void onResponse(Call<RatingModel> call, Response<RatingModel> response) {
                finish();
            }

            @Override
            public void onFailure(Call<RatingModel> call, Throwable t) {

                finish();


            }
        });

    }


    public void showDailog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this order")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setStarColor(R.color.colorPrimary)
                .setNoteDescriptionTextColor(R.color.humara_black)
                .setTitleTextColor(R.color.colorAccent)
                .setDescriptionTextColor(R.color.black)
                .setHint("Write something")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.black)
                .setCommentBackgroundColor(R.color.none)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(RatingsActivity.this)
                .show();


    }
}
