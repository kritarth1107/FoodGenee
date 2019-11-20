package com.foodgeene.scanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.login.LoginActivity;
import com.foodgeene.login.LoginModel;
import com.foodgeene.restraunt.RestrauntActivity;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.zxing.Result;

import java.io.IOException;
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
    Dialog loadingDialog;
    boolean close=true;
    final int RequestCameraPermissionID = 1001;
    String ActivityString="Home";
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ScannerActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                RequestCameraPermissionID);
                        return;
                    }

                }
            }
            break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scanner);
            req_camera_permission();
            sessionManager = new SessionManager(this);
            HashMap<String, String> user = sessionManager.getUserDetail();
            UserToken = user.get(sessionManager.USER_ID);
    }
        public void scan(View view){
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        ActivityString="Scanner";

    }

    @Override
    public void handleResult(Result result) {
        CallScannerApi(result.getText());
            loadingDialog = new Dialog(this);
            loadingDialog.setContentView(R.layout.loading_dialog_layout);
            loadingDialog.show();
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
        zXingScannerView.resumeCameraPreview(this);

    }

    public void CallScannerApi(final String result){
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ScannerModel> call = foodGeneeAPI.submitScannedQR("qrcode",result,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ScannerModel>() {
            @Override
            public void onResponse(Call<ScannerModel> call, Response<ScannerModel> response) {

                try {
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {

                        String store = response.body().getStore().trim();
                        String table = response.body().getTablename().trim();
                        String cover = response.body().getLogo().trim();
                        Intent intent = new Intent(ScannerActivity.this, RestrauntActivity.class);
                        intent.putExtra("encKey",result);
                        intent.putExtra("store",store);
                        intent.putExtra("table",table);
                        intent.putExtra("cover",cover);
                        startActivity(intent);
                        finish();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    } else if (status.equals("0")) {
                        String text = response.body().getText().trim();
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }


                }
                catch (Exception e){
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                    Toast.makeText(ScannerActivity.this, "Invalid QR-code Scan Again", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ScannerModel> call, Throwable t) {
                Toast.makeText(ScannerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void req_camera_permission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ScannerActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    RequestCameraPermissionID);
            return;
        }
    }


    @Override
    public void onBackPressed() {
        if(ActivityString.equals("Home")){

            Intent intent = new Intent(ScannerActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        }
        else{
            ActivityString="Home";
            setContentView(R.layout.activity_scanner);
        }

    }
}
