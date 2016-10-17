package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ww.administrator.demotest.adapter.AboutPagerAdapter;

/**
 * Created by Administrator on 2016/10/16.
 */
public class About4Acitivity extends AppCompatActivity{


    SmartTabLayout mTab;
    ViewPager mvp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about4);
        initViews();

    }

    private void initViews(){
        mTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        mvp = (ViewPager) findViewById(R.id.viewpager);
        mvp.setAdapter(new AboutPagerAdapter(this));
        mTab.setViewPager(mvp);
    }



}
