package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ww.administrator.demotest.adapter.TabVPAdapter;
import com.ww.administrator.demotest.catefragment.LightFra;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class CouponsActivity extends AppCompatActivity {

    //TabLayout mtabLayout;
    SmartTabLayout mTab;
    Toolbar mTb;
    private ViewPager mvp;
    private List<Fragment> mFralist = new ArrayList<>();
    private String[] mTitle = {"未使用", "已使用", "已过期"};
    private TabVPAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_layout);

        initViews();
        initTab();
    }

    private void initTab() {

        mFralist.add(new LightFra());
        mFralist.add(new LightFra());
        mFralist.add(new LightFra());

        mAdapter = new TabVPAdapter(getSupportFragmentManager(), mTitle, mFralist);
        mvp.setAdapter(mAdapter);
        mTab.setViewPager(mvp);
       /* mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[0]));
        mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[1]));
        mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[2]));
        mtabLayout.setupWithViewPager(mvp);*/

    }

    private void initViews() {
        //mtabLayout = (TabLayout)findViewById(R.id.tl_coupon);
        mTb = (Toolbar) findViewById(R.id.tb_common);
        mTab = (SmartTabLayout) findViewById(R.id.stl_coupon);
        mvp = (ViewPager) findViewById(R.id.vp_coupons);
        mTb.setTitle("我的礼券");
        mTb.setLogo(R.mipmap.logo);
        setSupportActionBar(mTb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
