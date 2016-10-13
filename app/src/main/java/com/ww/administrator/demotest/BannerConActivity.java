package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/8/25.
 */
public class BannerConActivity extends AppCompatActivity {

    private String bannerUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannercon);
        initParams();
        initViews();
        doBussiness();
    }

    private void initParams() {
        bannerUrl = getIntent().getStringExtra("bannerUrl");
    }

    private void initViews(){
        mWebView = (WebView) findViewById(R.id.wv_banner);
    }

    private void doBussiness(){
        mWebView.loadUrl(bannerUrl);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

}
