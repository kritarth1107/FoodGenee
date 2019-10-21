package com.foodgeene.scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.foodgeene.R;

public class ScannerResult extends AppCompatActivity {
    TextView codeTV;
    String code;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);
        codeTV = findViewById(R.id.codeTV);
        i = getIntent();
        code = i.getStringExtra("code");
        codeTV.setText(code);

    }
}
