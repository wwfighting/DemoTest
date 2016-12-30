package com.ww.administrator.demotest;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.model.Around;
import com.ww.administrator.demotest.model.AroundInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class NearActivity extends AppCompatActivity{

    Toolbar mtb;

    Gson mGson = new Gson();

    private MapView mMapView;
    BaiduMap mBaiduMap;
    private boolean isDownAnimate = true; //标记动画
    //定位相关
    /**
     * 当前定位的模式
     */
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirstIn = true;//记录是否是第一次进入
    private double mLatitude;//记录经度
    private double mLongitude;//记录纬度


    /**
     * 详细信息的 布局
     */
    private RelativeLayout mMarkerInfoLy;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    BitmapDescriptor houseIcon = BitmapDescriptorFactory
            .fromResource(R.drawable.ic_around_house_32);
    BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);

    List<Around> mInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_layout);
        initViews();
        initLocation();

    }

    private void initViews(){
        mtb = (Toolbar) findViewById(R.id.tb_common);
        mtb.setTitle("附近的房子");
        mtb.setLogo(R.mipmap.logo);
        setSupportActionBar(mtb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMapView = (MapView) findViewById(R.id.map_near);
        mBaiduMap = mMapView.getMap();

        mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
        //设置地图的显示比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 根据经纬度，从服务器获取周围用户信息
     */
    private void loadDatas(double latitude, double longitude){
        HttpUtil.postAsyn(Constants.BASE_TEST_URL + "get_around_info.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                System.out.println("=====附近用户信息====");
                System.out.println(response.toString());
                System.out.println("=====附近用户信息====");

                try {
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")) {
                        AroundInfo info = mGson.fromJson(response, AroundInfo.class);
                        for (int i = 0; i < info.getData().size(); i++) {
                            Around around = new Around();
                            around.setPhone(info.getData().get(i).getPhone());
                            around.setLatitude(info.getData().get(i).getLatitude());
                            around.setLongitude(info.getData().get(i).getLongitude());
                            around.setNickname(info.getData().get(i).getNickname());
                            around.setUid(info.getData().get(i).getUid());
                            mInfos.add(around);
                        }
                        initOverlay(mInfos);
                        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                            public boolean onMarkerClick(final Marker marker) {
                                Around aroundInfo = (Around) marker.getExtraInfo().get("info");
                                InfoWindow mInfoWindow;
                                /*Button button = new Button(getApplicationContext());
                                button.setBackgroundResource(R.drawable.popup);
                                button.setText(aroundInfo.getPhone());*/
                                //生成一个TextView用户在地图中显示InfoWindow
                                TextView location = new TextView(getApplicationContext());
                                location.setBackgroundResource(R.drawable.popup);
                                location.setPadding(30, 20, 30, 50);
                                location.setText(aroundInfo.getPhone());
                                location.setTextColor(Color.parseColor("#000000"));
                                //将marker所在的经纬度的信息转化成屏幕上的坐标
                                final LatLng ll = marker.getPosition();
                                Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                                p.y -= 47;
                                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                                //为弹出的InfoWindow添加点击事件

                                //显示InfoWindow

                                InfoWindow.OnInfoWindowClickListener listener = null;

                                listener = new InfoWindow.OnInfoWindowClickListener() {
                                    public void onInfoWindowClick() {
                                        // 隐藏InfoWindow
                                        mBaiduMap.hideInfoWindow();
                                    }
                                };

                                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(location), llInfo, -47, listener);
                                // 显示InfoWindow
                                mBaiduMap.showInfoWindow(mInfoWindow);
                                // 设置详细信息布局为可见
                                mMarkerInfoLy.setVisibility(View.VISIBLE);
                                // 根据商家信息为详细信息布局设置信息
                                popupInfo(mMarkerInfoLy, aroundInfo);
                                return true;
                            }
                        });

                    } else {
                        Toast.makeText(NearActivity.this, jsonRoot.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", "155"),
                new HttpUtil.Param("latitude", latitude + ""),
                new HttpUtil.Param("longitude", longitude + "")
        });
    }

    /**
     * 载入个人定位地址
     */
    private void initLocation(){
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);//注册定位接口
        //设置定位相关配置
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        //option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    /**
     * 初始化覆盖物（即周围人的信息）
     */
    private void initOverlay(List<Around> infos){

        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;

        for (Around info : infos){
            //位置
            System.out.println("=====经纬度====");
            System.out.println(Double.parseDouble(info.getLatitude()) + "  " + Double.parseDouble(info.getLongitude()));
            System.out.println("=====经纬度====");
            latLng = new LatLng(Double.parseDouble(info.getLatitude()), Double.parseDouble(info.getLongitude()));
            //图标
            overlayOptions = new MarkerOptions().position(latLng).icon(houseIcon)
                    .zIndex(9);

            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        // 将地图移到到最后一个经纬度位置
        //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        //mBaiduMap.setMapStatus(u);
        /*
        LatLng llA = new LatLng(31.928787, 118.846325);
        LatLng llB = new LatLng(31.919787, 118.836315);
        LatLng llC = new LatLng(31.916687, 118.856415);
        LatLng llD = new LatLng(31.938887, 118.836385);
        MarkerOptions ooB = new MarkerOptions().position(llB).icon(bdB)
                .zIndex(5);
        if (isDownAnimate) {
            // 掉下动画
            ooB.animateType(MarkerOptions.MarkerAnimateType.drop);
        }
        mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));

        MarkerOptions ooC = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
        if (isDownAnimate) {
            // 生长动画
            ooC.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));

        MarkerOptions ooD = new MarkerOptions().position(llC).icon(bdC)
                .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
        if (isDownAnimate) {
            // 生长动画
            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(bdA);
        giflist.add(bdB);
        giflist.add(bdC);
        MarkerOptions ooD = new MarkerOptions().position(llD).icons(giflist)
                .zIndex(0).period(10);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
        if (isDownAnimate) {
            // 生长动画
            ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        }
        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));

        // 添加周围覆盖物
        LatLng southwest = new LatLng(31.919757, 118.836345);
        LatLng northeast = new LatLng(31.919387, 118.836315);
        LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
                .include(southwest).build();

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
        mBaiduMap.addOverlay(ooGround);

        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bounds.getCenter());
        mBaiduMap.setMapStatus(u);

        mBaiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        NearActivity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });
        */
    }
    /**
     * 根据info为布局上的控件设置信息
     *
     * @param mMarkerLy
     * @param info
     */
    protected void popupInfo(RelativeLayout mMarkerLy, Around info) {
        ViewHolder viewHolder = null;
        if (mMarkerLy.getTag() == null) {
            viewHolder = new ViewHolder();
            viewHolder.infoImg = (ImageView) mMarkerLy
                    .findViewById(R.id.info_img);
            viewHolder.infoName = (TextView) mMarkerLy
                    .findViewById(R.id.info_name);
            viewHolder.infoDistance = (TextView) mMarkerLy
                    .findViewById(R.id.info_distance);
            viewHolder.infoZan = (TextView) mMarkerLy
                    .findViewById(R.id.info_zan);

            mMarkerLy.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) mMarkerLy.getTag();
        //viewHolder.infoImg.setImageResource(info.getImgId());
        //viewHolder.infoDistance.setText(info.getDistance());
        viewHolder.infoName.setText(info.getNickname());
        viewHolder.infoZan.setText(info.getPhone());
    }

    /**
     * 复用弹出面板mMarkerLy的控件
     *
     */
    private class ViewHolder {
        ImageView infoImg;
        TextView infoName;
        TextView infoDistance;
        TextView infoZan;
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            /*37度50
            144度58分*/
            //构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360，这里给它100°
                    .direction(1)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();

            MyLocationConfiguration config = new MyLocationConfiguration(
                    mCurrentMode, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);

            // 第一次定位时，将地图位置移动到当前位置
            if (isFirstIn) {
                isFirstIn = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                //31.918807 118.816115
                Log.d("NearActivity", mLatitude + "");
                Log.d("NearActivity", mLongitude + "");
                loadDatas(mLatitude, mLongitude);

            }
        }
    }

   @Override
    protected void onStart() {
        // TODO 自动生成的方法存根
        //开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        mLocationClient.start();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
        houseIcon.recycle();
        mCurrentMarker.recycle();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
