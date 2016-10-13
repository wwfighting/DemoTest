package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ww.administrator.demotest.cityselect.CitySelecterActivity;

/**
 * Created by Administrator on 2016/7/20.
 */
public class LocatCityActivity extends AppCompatActivity{

    Toolbar mtb;
    TextView mtvLocate;

    //定位相关
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean IsFirstIn = true;//记录是否是第一次进入
    private double mLatitude;//记录经度
    private double mLongtitude;//记录纬度
    private String CurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();
        initLocation();

    }

    private void initViews(){
        mtvLocate = (TextView) findViewById(R.id.tv_loaction);
        mtb = (Toolbar) findViewById(R.id.tb_common);
        mtb.setTitle("城市列表 + 定位 Demo");
        setSupportActionBar(mtb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtvLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LocatCityActivity.this, CitySelecterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("locatCity",mtvLocate.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,100);
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
                if (location.getAddrStr().isEmpty()) {
                    mtvLocate.setText("无法定位到您的位置！");
                    CurrentLocation = "无法定位到您的位置！";
                }else {
                    Log.d("ww","在定位。。。。。");

                    mtvLocate.setText(getFilterCity(location.getCity().toString()));
                    CurrentLocation = getFilterCity(location.getCity().toString());

                }

            }
        }
    }

    public String getFilterCity(String cityName){
        String str = cityName.substring(cityName.length()-1);
        if (str.equals("市")){
            return cityName.substring(0, cityName.length()-1);
        }else {
            return cityName;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        // TODO 自动生成的方法存根
        super.onStart();
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mLocationClient.start();
    }

    @Override
    protected void onStop() {
        // TODO 自动生成的方法存根
        super.onStop();
        mLocationClient.stop();
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 1110){
            mtvLocate.setText(data.getStringExtra("selectCityName"));
        }
    }
}
