package com.foodgeene.forgot;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.foodgeene.R;

public class ForgotActivity extends AppCompatActivity {
    TextView NavigateToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        NavigateToLogin = findViewById(R.id.NavigateToLogin);
        NavigateToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
