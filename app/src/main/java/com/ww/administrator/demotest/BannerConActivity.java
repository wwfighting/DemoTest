package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by Administrator on 2016/8/25.
 */
public class BannerConActivity extends AppCompatActivity {

    private final String TAG = "BannerConActivity";
    private String bannerUrl;
    private WebView mWebView;

    private ProgressWheel mpbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannercon);
        initParams();
        initViews();
        showWebView();
        doBussiness();
    }

    private void initParams() {

        bannerUrl = getIntent().getStringExtra("bannerUrl");
    }

    private void initViews(){
        mWebView = (WebView) findViewById(R.id.wv_banner);
        mpbLoading = (ProgressWheel) findViewById(R.id.pb_common);
        mpbLoading.setVisibility(View.VISIBLE);
    }

    private void showWebView(){
        try{
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    BannerConActivity.this.setTitle("载入中...");
                    BannerConActivity.this.setProgress(newProgress);
                    if (newProgress > 80) {
                        BannerConActivity.this.setTitle("家瓦商城");
                    }
                }
            });

            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            mWebView.setVerticalScrollBarEnabled(true);
            mWebView.setHorizontalScrollBarEnabled(true);
            webSettings.setDefaultTextEncodingName("utf-8");
            //mWebView.addJavascriptInterface(JavaScriptObject(), "jsObj");

            mWebView.loadUrl(bannerUrl);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void doBussiness(){
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                if (mpbLoading.isSpinning()) {
                    mpbLoading.stopSpinning();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);

            }

        });

    }

}
