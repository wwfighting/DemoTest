package com.ww.administrator.demotest.weclome;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.GuideAdapter;

/**
 * Created by Administrator on 2016/10/11.
 */
public class WelGuideActivity extends AppCompatActivity {

    SmartTabLayout mTab;
    ViewPager mvpGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_layout);
        initViews();
    }

    //初始化视图
    private void initViews() {
        mvpGuide = (ViewPager) findViewById(R.id.viewpager);
        mTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        mvpGuide.setAdapter(new GuideAdapter(this));
        mTab.setViewPager(mvpGuide);
    }


}
