package com.foodgeene.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.login.LoginModel;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import network.FoodGeneeAPI;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    SessionManager sessionManager;
    private ZXingScannerView zXingScannerView;
    String UserToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scanner);
            sessionManager = new SessionManager(this);
            HashMap<String, String> user = sessionManager.getUserDetail();
            UserToken = user.get(sessionManager.USER_ID);
    }
        public void scan(View view){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    public void handleResult(Result result) {
        CallScannerApi(result.getText());
        zXingScannerView.resumeCameraPreview(this);
    }

    public void CallScannerApi(String result){
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ScannerModel> call = foodGeneeAPI.submitScannedQR("qrcode",result,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ScannerModel>() {
            @Override
            public void onResponse(Call<ScannerModel> call, Response<ScannerModel> response) {

                try {
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {

                        String merchantid = response.body().getMerchantid().trim();
                        String table = response.body().getTable().trim();
                        String store = response.body().getStore().trim();
                        String logo = response.body().getLogo().trim();
                        String coverpic = response.body().getCoverpic().trim();
                        Toast.makeText(getApplicationContext(), "Correct QR", Toast.LENGTH_LONG).show();

                    } else if (status.equals("0")) {
                        String text = response.body().getText().trim();
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }


                }
                catch (Exception e){

                    Toast.makeText(ScannerActivity.this, "Wrong QR-code Scan Again", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ScannerModel> call, Throwable t) {
                Toast.makeText(ScannerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
