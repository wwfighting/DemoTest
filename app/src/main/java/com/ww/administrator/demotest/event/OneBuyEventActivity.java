package com.ww.administrator.demotest.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.LoginActivity;
import com.ww.administrator.demotest.OrderActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.GoodsDetailInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/11/1.
 */
public class OneBuyEventActivity extends AppCompatActivity {

    ImageView mivOff;
    Button mbtnBuy;
    RelativeLayout rlBg;
    MyApp mApp;
    MaterialDialog mDialog;
    MaterialDialog mDialogWarn;
    private String uid = "-1";

    private String gid = "3505";
    private int num = 1;

    String strGoodsDetailInfo = "";   //将GoodsDetailInfo对象通过String跳转
    private Gson mGson = new Gson();
    int orderMode = 500; //代表活动订单


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onebuyevent_layout);
        mApp = (MyApp) getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }
        initViews();
        doBussiness();
    }



    private void initViews(){
        mivOff = (ImageView) findViewById(R.id.iv_event_off);
        mbtnBuy = (Button) findViewById(R.id.btn_event_buy);
        rlBg = (RelativeLayout) findViewById(R.id.rl_bg);
        rlBg.getBackground().setAlpha(190);
        initDialog();

        mivOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void initDialog() {
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(OneBuyEventActivity.this, LoginActivity.class);
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

    private void initDialogWarn(){
        mDialogWarn = new MaterialDialog(this);
        mDialogWarn.setTitle("注意！");
        mDialogWarn.setMessage("一元抢购的礼券仅限11.14-11.27期间下预约金的订单使用");
        mDialogWarn.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogWarn.dismiss();
                loadDatas();
            }
        });
        mDialogWarn.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogWarn.dismiss();
            }
        });

        //点击对话框外，对话框消失
        mDialogWarn.setCanceledOnTouchOutside(true);
    }

    private void doBussiness(){
        mbtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断有没有登录
                if (uid.equals("-1")) {
                    mDialog.show();
                } else {
                    //loadDatas();
                    hasBuy();
                }
            }
        });
    }


    /**
     * 判断是否购买过礼品券
     */
    private void hasBuy(){
        HttpUtil.postAsyn(Constants.BASE_URL + "onebuy_has.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(OneBuyEventActivity.this, "请检查您的网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    initDialogWarn();
                    mDialogWarn.show();

                }else if (info.getCode().equals("401")){
                    Toast.makeText(OneBuyEventActivity.this, "您已抢购过！可以去 '我的礼券' 查看", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    Toast.makeText(OneBuyEventActivity.this, info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsId", gid)
        });
    }

    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "product_detail.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(OneBuyEventActivity.this, "请检查您的网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                GoodsDetailInfo info = mGson.fromJson(response, GoodsDetailInfo.class);
                if (info.getCode().equals("200")){
                    strGoodsDetailInfo = response;
                    Intent intent = new Intent(OneBuyEventActivity.this, OrderActivity.class);
                    intent.putExtra("imgurl", Constants.BASE_IMG_URL + info.getData().getDetail().getPicurl());
                    intent.putExtra("gid", gid);
                    intent.putExtra("orderMode", orderMode);
                    intent.putExtra("response", strGoodsDetailInfo);
                    startActivity(intent);
                    finish();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("gid", gid)
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            uid = ((MyApp) getApplicationContext()).getUser().getId();
            Toast.makeText(OneBuyEventActivity.this, "登录成功，请点击抢购！", Toast.LENGTH_SHORT).show();
        }
    }
}
