package com.foodgeene.scanner;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.foodgeene.MainActivity;
import com.foodgeene.R;
import com.foodgeene.SessionManager.SessionManager;
import com.foodgeene.restraunt.RestrauntActivity;
import com.google.zxing.Result;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import network.ConnectivityReceiver;
import network.FoodGeneeAPI;
import network.MyApplication;
import network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScannerActivity extends AppCompatActivity implements  ConnectivityReceiver.ConnectivityReceiverListener {
    SessionManager sessionManager;
    String UserToken;
    Dialog loadingDialog;
    boolean close=true;
    final int RequestCameraPermissionID = 1001;
    String ActivityString="Home";
    boolean isOnline;
    FrameLayout frame;
    RelativeLayout dummy;
    private CodeScanner mCodeScanner;
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
             frame=findViewById(R.id.frame);
             dummy=findViewById(R.id.dummy);

            req_camera_permission();
            sessionManager = new SessionManager(this);
            HashMap<String, String> user = sessionManager.getUserDetail();
            UserToken = user.get(sessionManager.USER_ID);
             isOnline=ConnectivityReceiver.isConnected();
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScannerActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isOnline)
                            CallScannerApi(result.getText());
                        else Toast.makeText(ScannerActivity.this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
                        mCodeScanner.stopPreview();

                        loadingDialog = new Dialog(ScannerActivity.this);
                        loadingDialog.setContentView(R.layout.loading_dialog_layout);
                        loadingDialog.show();
                        loadingDialog.setCancelable(false);
                        loadingDialog.setCanceledOnTouchOutside(false);
                    }
                });
            }
        });

    }


    public void scan(View view){
        frame.setVisibility(View.VISIBLE);
        dummy.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void CallScannerApi(final String result){
        FoodGeneeAPI foodGeneeAPI = RetrofitClient.getApiClient().create(FoodGeneeAPI.class);
        Call<ScannerModel> call = foodGeneeAPI.submitScannedQR("qrcode",result,0,UserToken,"application/x-www-form-urlencoded");
        call.enqueue(new Callback<ScannerModel>() {
            @Override
            public void onResponse(Call<ScannerModel> call, Response<ScannerModel> response) {

                try {
                    String status = response.body().getStatus().trim();
                    if (status.equals("1")) {

                        String store = response.body().getStore().trim();
                        String table = response.body().getTablename().trim();
                        String cover = response.body().getLogo().trim();
                        String verify=response.body().getVerify();
                        String servingtype=response.body().getServingtype();
                        if(verify==null)
                            verify="0";
                      //  Log.e("verify",verify);
                        Intent intent = new Intent(ScannerActivity.this, RestrauntActivity.class);
                        intent.putExtra("encKey",result);
                        intent.putExtra("store",store);
                        intent.putExtra("table",table);
                        intent.putExtra("cover",cover);
                        intent.putExtra("from","scanner");
                        intent.putExtra("orderId","");
                        intent.putExtra("verify",verify);
                        intent.putExtra("servingtype",servingtype);
                        startActivity(intent);
                        finish();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();

                    } else if (status.equals("0")) {
                        String text = response.body().getText().trim();
                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                        mCodeScanner.startPreview();
                        loadingDialog.cancel();
                        loadingDialog.dismiss();
                    }


                }
                catch (Exception e){
                    loadingDialog.cancel();
                    loadingDialog.dismiss();
                    mCodeScanner.startPreview();
                    Toast.makeText(ScannerActivity.this, "Invalid QR-code Scan Again", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ScannerModel> call, Throwable t) {
                Toast.makeText(ScannerActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                mCodeScanner.startPreview();
                loadingDialog.cancel();
                loadingDialog.dismiss();
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isOnline=isConnected;
    }
}
