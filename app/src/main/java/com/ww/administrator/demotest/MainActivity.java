package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.ww.administrator.demotest.fragment.CateFragment;
import com.ww.administrator.demotest.fragment.HomeFragment;
import com.ww.administrator.demotest.fragment.MyFragment;
import com.ww.administrator.demotest.fragment.ShoppingCartFragment;
import com.ww.administrator.demotest.util.DisplayUtil;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup mRadioGroup;
    private RadioButton homeRadBtn,publishRadBtn,searchRadBtn,myRadBtn;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    public static HomeFragment mHomeFragment;
    public static CateFragment mCateFragment;
    public static ShoppingCartFragment mShoppingCartFragment;
    public static MyFragment mMyFragment;

    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean IsFirstIn = true;//记录是否是第一次进入
    private double mLatitude;//记录经度
    private double mLongtitude;//记录纬度

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

        checkUpdate();
        checkDeviceInfo();

        //initLocation();

    }

    private void checkDeviceInfo(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
         Log.d("MainActivity", "最大内存是" + maxMemory + "KB");
         Log.d("MainActivity", "屏幕分辨率width：" + DisplayUtil.getScreenWidth(this));
         Log.d("MainActivity", "屏幕分辨率height：" + DisplayUtil.getScreenHeight(this));
    }

    private void checkUpdate(){
        PgyUpdateManager.register(MainActivity.this,
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        final MaterialDialog updateDialog = new MaterialDialog(MainActivity.this);
                        updateDialog.setTitle("更新提示");
                        updateDialog.setMessage("发现新版本！");
                        updateDialog.setPositiveButton("马上升级", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {   //进行更新
                                startDownloadTask(
                                        MainActivity.this,
                                        appBean.getDownloadURL());
                            }
                        });

                        updateDialog.setNegativeButton("以后再说", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {   //取消更新
                                updateDialog.dismiss();
                            }
                        });

                        updateDialog.show();

                    }

                    @Override
                    public void onNoUpdateAvailable() {

                    }
                });
    }

    private void initLocation(){
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);//注册接口
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);

        mLocationClient.setLocOption(option);
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            mLatitude = location.getLatitude();
            mLongtitude = location.getLongitude();

            if (IsFirstIn) {
                IsFirstIn = false;
                if (!location.getAddrStr().isEmpty()) {
                    Log.d("ww","在定位。。。。。");
                    Log.d("MainActivity", "Lat：" + mLatitude);
                    Log.d("MainActivity", "Long：" + mLongtitude);
                }
            }
        }
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

    @Override
    protected void onStart() {
        // TODO 自动生成的方法存根
        super.onStart();
        /*if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mLocationClient.start();*/
    }

    @Override
    protected void onStop() {
        // TODO 自动生成的方法存根
        super.onStop();
        //mLocationClient.stop();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


}
