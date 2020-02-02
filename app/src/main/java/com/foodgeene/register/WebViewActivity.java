package com.foodgeene.register;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.foodgeene.R;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    WebView wv_terms;
    ImageView mIvBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        wv_terms=findViewById(R.id.wv_terms);
        mIvBack=findViewById(R.id.iv_back);
        WebSettings webSettings = wv_terms.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wv_terms.loadUrl("https://foodqonline.com/terms.html");
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
