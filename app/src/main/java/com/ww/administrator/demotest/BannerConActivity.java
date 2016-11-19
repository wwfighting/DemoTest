package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.pnikosis.materialishprogress.ProgressWheel;

/**
 * Created by Administrator on 2016/8/25.
 */
public class BannerConActivity extends AppCompatActivity {

    private final String TAG = "BannerConActivity";
    private String bannerUrl;
    private WebView mWebView;

    private ProgressWheel mpbLoading;

    Button btnBuy, btnBottom;

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
        btnBuy = (Button) findViewById(R.id.btn_buy);
        btnBottom = (Button) findViewById(R.id.btn_bottom);
        mpbLoading = (ProgressWheel) findViewById(R.id.pb_common);
        mpbLoading.setVisibility(View.VISIBLE);

        if (bannerUrl.equals("http://www.jvawa.com/app/game/AprilSpecial/nov.php") || bannerUrl.equals("http://www.jvawa.com/app/game/AprilSpecial/nov2.php")){
            btnBuy.setVisibility(View.VISIBLE);
            btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(BannerConActivity.this, ProductListActivity.class);
                    intent.putExtra("keyName", "11特惠");
                    intent.putExtra("isRecom", "-1");
                    startActivity(intent);
                    finish();
                }
            });

        }else {
            btnBuy.setVisibility(View.GONE);
        }

        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
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

        mWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });

    }

}
