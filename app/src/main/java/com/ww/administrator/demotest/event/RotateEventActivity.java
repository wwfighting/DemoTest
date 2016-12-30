package com.ww.administrator.demotest.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.CouponsActivity;
import com.ww.administrator.demotest.LoginActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.model.RotateCouponsInfo;
import com.ww.administrator.demotest.model.RotateNumInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/11/18.
 */
public class RotateEventActivity extends AppCompatActivity implements View.OnClickListener{


    Toolbar mtb;
    ImageView mivBg, mivArrow, mivStart;
    ImageView mivOk, mivCoupons;
    TextView mtvCouponName;
    RelativeLayout mrlTip;
    String uid = "-1";
    MaterialDialog mDialog;

    int num = -1;   //可以转动的次数
    MyApp mApp;
    boolean isRotating = false;  //表示正在转动

    Gson mGson = new Gson();
    HashMap<String, String> hmMy = new HashMap<>();
    HashMap<Integer, RotateCouponsInfo.DataBean> hmAll = new HashMap<>();

    RotateCouponsInfo mInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotateevent_layout);
        mApp = (MyApp) getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }
        initViews();
        initDialog();
        initDatas();
        loadDatas();


    }

    private void initViews(){
        mtb = (Toolbar) findViewById(R.id.tb_common);
        mtb.setTitle("双十二大转盘抽奖");
        setSupportActionBar(mtb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mivBg = (ImageView) findViewById(R.id.iv_rotate_bg);
        mivArrow = (ImageView) findViewById(R.id.iv_rotate_arrow);
        mivStart = (ImageView) findViewById(R.id.iv_rotate_start);
        mivOk = (ImageView) findViewById(R.id.iv_ok);
        mivCoupons = (ImageView) findViewById(R.id.iv_my_coupons);
        mtvCouponName = (TextView) findViewById(R.id.tv_coupons_name);
        mrlTip = (RelativeLayout) findViewById(R.id.rl_tip);
        mrlTip.setVisibility(View.GONE);
        mivOk.setOnClickListener(this);
        mivCoupons.setOnClickListener(this);
    }
    private void initDialog() {
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(RotateEventActivity.this, LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        mDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        //点击对话框外，对话框消失
        mDialog.setCanceledOnTouchOutside(true);
    }
    private void initDatas(){
        hmMy.clear();
        hmMy.put("3215", "套锅");
        hmMy.put("3216", "乳胶枕*2个");
        hmMy.put("3217", "套锅");
        hmMy.put("3218", "我乐调味篮（BZ-300）");
        hmMy.put("3219", "烤箱一台");
        hmMy.put("3220", "我乐烟机\r\n客户中奖后至店面领取，具体以店面实物为准");
    }

    /**
     * 判断用户有无抽奖资格
     */
    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "event_rotate.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(RotateEventActivity.this, "抱歉，请检查您的网络！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {

                System.out.println("=====action为0======");
                System.out.println(response.toString());
                System.out.println("=====action为0======");
                try {
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")) {
                        RotateNumInfo info = mGson.fromJson(response, RotateNumInfo.class);
                        num = info.getData().getNum();
                        getRotateCoupons();
                        mivStart.setOnClickListener(RotateEventActivity.this);
                    } else {
                        Toast.makeText(RotateEventActivity.this, jsonRoot.getString("info"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("action", "0")
        });
    }

    /**
     * 得到所有的可用礼品券
     */
    private void getRotateCoupons(){
        HttpUtil.postAsyn(Constants.BASE_URL + "event_rotate.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(RotateEventActivity.this, "抱歉，请检查您的网络！", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {

                System.out.println("=====action为1======");
                System.out.println(response.toString());
                System.out.println("=====action为1======");
                try{
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")){
                        mInfo = mGson.fromJson(response, RotateCouponsInfo.class);
                        hmAll.clear();
                        for (int i = 0; i < mInfo.getData().size(); i++){
                            hmAll.put(i, mInfo.getData().get(i));
                        }

                    }else {
                        num = -1;
                        Toast.makeText(RotateEventActivity.this, jsonRoot.getString("info"), Toast.LENGTH_LONG).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("action", "1")
        });
    }

    /**
     * 计算用户可能抽到的奖品
     * @return
     */
    private RotateCouponsInfo.DataBean calculateCoupons(){
        int random = (int)(0 + Math.random() * (mInfo.getData().size()) - 1);
        Log.d("RotateActivity", "lid：" + hmAll.get(random).getId() + "， mode：" + hmAll.get(random).getMode());
        Log.d("RotateActivity", "转到的奖品为：" + hmMy.get(hmAll.get(random).getMode()));
        return hmAll.get(random);
    }

    /**
     * 开始转动转盘
     *
     */
    private void startRotate(){
        //"3215"=>"套锅",
        //"3216"=>"乳胶枕*2个",
        //"3217"=>"套锅",
        //"3218"=>"我乐调味篮（BZ-300）",
        //"3219"=>"烤箱一台",
        //"3220"=>"我乐烟机\r\n客户中奖后至店面领取，具体以店面实物为准",
        //计算转过的角度
        float angle = 3600f;
        final RotateCouponsInfo.DataBean couponData = calculateCoupons();
        final String mode = couponData.getMode();
        //转之前已知道奖品
        Log.d("RotateActivity", "转到的奖品为：" + hmMy.get(mode));
        switch (mode){
            case "3215":    //套锅
                angle = 3780f;
                break;
            case "3216":    //乳胶枕*2个
                angle = 3720f;
                break;
            case "3217":    //套锅
                angle = 3600f;
                break;
            case "3218":    //我乐调味篮（BZ-300）
                angle = 3840f;
                break;
            case "3219":    //烤箱一台
                angle = 3900f;
                break;
            case "3220":    //我乐烟机\r\n客户中奖后至店面领取，具体以店面实物为准
                angle = 3660f;
                break;
        }

        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new RotateAnimation(0f, angle, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        animationSet.setDuration(4000);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isRotating = true;
                updateCoupons(couponData);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRotating = false;
                mrlTip.setVisibility(View.VISIBLE);
                mtvCouponName.setText(hmMy.get(mode));

            }
        });
        // 实现转动的View
        mivArrow.startAnimation(animationSet);
    }

    private void updateCoupons(RotateCouponsInfo.DataBean data){
        HttpUtil.postAsyn(Constants.BASE_URL + "event_rotate.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                System.out.println("=====action为2======");
                System.out.println(response.toString());
                System.out.println("=====action为2======");
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    num = -1;
                }else {
                    Toast.makeText(RotateEventActivity.this, info.getInfo(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("lid", data.getId()),
                new HttpUtil.Param("action", "2")
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_rotate_start:

                if (uid.equals("-1")) {
                    mDialog.show();
                } else {
                    if (isRotating){  //表示正在转动
                        return;
                    }

                    if (num == -1){
                        Toast.makeText(RotateEventActivity.this, "抱歉，无法抽奖！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    startRotate();
                }


                break;
            case R.id.iv_ok:
                Intent intent = new Intent(RotateEventActivity.this, CouponsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_my_coupons:
                Intent coupons = new Intent(RotateEventActivity.this, CouponsActivity.class);
                startActivity(coupons);
                break;

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            uid = ((MyApp) getApplicationContext()).getUser().getId();
            Toast.makeText(RotateEventActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
        }
    }
}
