package com.foodgeene.success;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodgeene.R;
import com.foodgeene.scanner.ScannerActivity;

public class PodSuccess extends AppCompatActivity {
    TextView Amount_text;
    Intent get;
    LinearLayout place_order_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pod_success);
        Amount_text = findViewById(R.id.Amount_text);
        place_order_layout = findViewById(R.id.place_order_layout);
        get = getIntent();
        String amount = get.getStringExtra("amount");
        Amount_text.setText("Amount Due - Rs. "+amount);
        place_order_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PodSuccess.this, ScannerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PodSuccess.this, ScannerActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
