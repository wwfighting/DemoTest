package com.ww.administrator.demotest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/28.
 */
public class TabVPAdapter extends FragmentStatePagerAdapter {

    private String [] titles;
    private List<Fragment> mList;

    public TabVPAdapter(FragmentManager fm, String[] titles, List<Fragment> list) {
        super(fm);
        this.titles =titles;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
}
