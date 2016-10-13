package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/10/12.
 */
public class IntroduceJvawa extends AppCompatActivity{

    Toolbar mtbRegAgr;
    WebView mwvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_agreement);
        initViews();
        loadDatas();
    }

    private void initViews() {
        mtbRegAgr = (Toolbar) findViewById(R.id.tb_common);
        mwvShow = (WebView) findViewById(R.id.wv_agreement);
        mtbRegAgr.setTitle("家瓦简介");
        setSupportActionBar(mtbRegAgr);
        mtbRegAgr.setNavigationIcon(R.drawable.ic_clear_24dp);
        mtbRegAgr.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadDatas() {
        mwvShow.loadUrl("file:///android_asset/localhtml/introduce.html");
    }
}
