package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ww.administrator.demotest.fragment.CateFragment;
import com.ww.administrator.demotest.fragment.HomeFragment;
import com.ww.administrator.demotest.fragment.MyFragment;
import com.ww.administrator.demotest.fragment.ShoppingCartFragment;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mRadioGroup;
    private RadioButton homeRadBtn,publishRadBtn,searchRadBtn,myRadBtn;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    public static HomeFragment mHomeFragment;
    public static CateFragment mCateFragment;
    public static ShoppingCartFragment mShoppingCartFragment;
    public static MyFragment mMyFragment;


    public boolean getmIsLogin() {
        return mIsLogin;
    }

    public void setmIsLogin(boolean mIsLogin) {
        this.mIsLogin = mIsLogin;
    }

    private boolean mIsLogin;


    long miExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragment();
        mFragmentManager = getSupportFragmentManager();
        homeRadBtn.setChecked(true);
        changefragment(mHomeFragment);
        mRadioGroup.setOnCheckedChangeListener(this);
        setmIsLogin(false);
    }



    private void initViews(){
        mRadioGroup = (RadioGroup) findViewById(R.id.rgp_main);
        homeRadBtn = (RadioButton) findViewById(R.id.rdb_home);
        publishRadBtn = (RadioButton) findViewById(R.id.rdb_publish);
        searchRadBtn = (RadioButton) findViewById(R.id.rdb_search);
        myRadBtn = (RadioButton) findViewById(R.id.rdb_my);
    }

    public void changefragment(Fragment fragment){
        FragmentTransaction fTransaction = mFragmentManager.beginTransaction();
        fTransaction.add(R.id.frame_content, fragment);
        fTransaction.commit();
    }

    private void initFragment(){
        mHomeFragment = new HomeFragment();
        mCateFragment = new CateFragment();
        mShoppingCartFragment = new ShoppingCartFragment();
        mMyFragment = new MyFragment();
        mFragment = mHomeFragment;
    }
    public void switchContent(Fragment to){
        if (mFragment != to) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) { //判断是否被add过
                transaction.hide(mFragment).add(R.id.frame_content, to).commit();
            }else {
                transaction.hide(mFragment).show(to).commit();
            }
        }
        mFragment = to;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - miExitTime) > 2000) {
                Snackbar.make(mRadioGroup, "再按一次退出程序！", Snackbar.LENGTH_SHORT).show();

                miExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rdb_home:
                switchContent(mHomeFragment);
                break;
            case R.id.rdb_publish:
                switchContent(mCateFragment);
                break;
            case R.id.rdb_search:
                /*if (getmIsLogin()){
                    ((TextView)mShoppingCartFragment.getView().findViewById(R.id.tv_shoppingcart)).setText("isLgoin");
                }*/

                switchContent(mShoppingCartFragment);
                break;
            case R.id.rdb_my:
                switchContent(mMyFragment);
                break;
            default:
                break;
        }
    }




}
