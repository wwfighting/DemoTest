package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ww.administrator.demotest.adapter.TabVPAdapter;
import com.ww.administrator.demotest.catefragment.OrderFragment;
import com.ww.administrator.demotest.fragment.OrderHasPayFragment;
import com.ww.administrator.demotest.fragment.OrderNoPayFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class AllOrderActivity extends AppCompatActivity {

    SmartTabLayout mTab;
    Toolbar mTb;
    private ViewPager mvp;
    private List<Fragment> mFralist = new ArrayList<>();
    private String[] mTitle = {"全部订单", "待付款", "已付款"};
    private TabVPAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //和 "我的礼券" 共用一个layout
        setContentView(R.layout.activity_coupons_layout);
        initViews();
        initTab();

    }

    private void initTab() {

        mFralist.add(new OrderFragment());
        mFralist.add(new OrderNoPayFragment());
        mFralist.add(new OrderHasPayFragment());

        mAdapter = new TabVPAdapter(getSupportFragmentManager(), mTitle, mFralist);
        mvp.setAdapter(mAdapter);
        mTab.setViewPager(mvp);
        /*mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[0]));
        mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[1]));
        mtabLayout.addTab(mtabLayout.newTab().setText(mTitle[2]));*/
        //mtabLayout.setupWithViewPager(mvp);
    }

    private void initViews() {
        //mtabLayout = (TabLayout)findViewById(R.id.tl_coupon);
        mTb = (Toolbar) findViewById(R.id.tb_common);
        mTab = (SmartTabLayout) findViewById(R.id.stl_coupon);
        mvp = (ViewPager) findViewById(R.id.vp_coupons);
        mTb.setTitle("我的订单");
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
