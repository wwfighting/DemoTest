package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.OrderInfo;
import com.ww.administrator.demotest.model.OrderPartsInfo;
import com.ww.administrator.demotest.pay.PayActivity;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import cn.beecloud.BCPay;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/28.
 */
public class CommitOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private final static int DIALOG_DISMISS = 100;

    private String uid = "-1";

    int morderMode = -1; //订单类型 100：配件订单 200：橱柜订单
    String mGid = "";
    String mimgUrl = "";
    String mcity = "";
    String mstoreName = "";  //记录选中的门店名
    String mstaffName = "";  //记录选中的员工信息
    String strGoodsName = "";   //记录商品名
    String strReceiverInfo = "";    //记录收货人信息
    String strDoor = "";    //记录所选门型
    String strColor = "";   //记录所选颜色
    String strTaimian = ""; //记录所选台面
    String strStandard = ""; //规格
    String strorderMoney = "";  //预约金
    String strallMoney = "";  //预估价
    String strTip = ""; //备注
    String mstoreId = "";   //商品Id
    String msalerNo = "";   //店员工号
    String mDoorId = "";   //门型Id
    String mColorId = "";   //颜色Id
    String mTaimianId = "";   //台面Id

    String[] strInfo;

    Toolbar mtbCommit;

    CoordinatorLayout mContainer;

    TextView mtvReName, mtvRePhone, mtvReAddress, mtvMode, mtvGoodName, mtvOrderMoney, mtvAllMoney,
            mtvDoor, mtvColor, mtvTaimian, mtvGuide, mtvActivity, mtvCustom, mtvMoneyTips, mtvTips, mtvBottomMoney;

    ImageView mivShow;

    MaterialDialog mDialog;

    FloatingActionButton mfabCommit;

    ProgressWheel mpbLoad;
    TextView mtvLoad;

    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitorder_layout);
        initParams();
        initViews();
        initWechatPay();
    }

    private void initParams() {
        uid = ((MyApp) getApplicationContext()).getUser().getId();
        morderMode = getIntent().getExtras().getInt("ordermode");
        mGid = getIntent().getExtras().getString("gid");
        mimgUrl = getIntent().getExtras().getString("imgurl");
        strGoodsName = getIntent().getExtras().getString("goodsName");
        strReceiverInfo = getIntent().getExtras().getString("receiverInfo");
        mstaffName = getIntent().getExtras().getString("staffName");
        mstoreName = getIntent().getExtras().getString("storeName");
        strorderMoney = getIntent().getExtras().getString("orderMoney");
        strallMoney = getIntent().getExtras().getString("allMoney");
        strTip = getIntent().getExtras().getString("tip");
        mstoreId = getIntent().getExtras().getString("storeid");
        msalerNo = getIntent().getExtras().getString("salerNo").substring(2);
        mcity = getIntent().getExtras().getString("city");

        if (morderMode == 200){
            mDoorId = getIntent().getExtras().getString("doorid");
            mColorId = getIntent().getExtras().getString("colorid");
            mTaimianId = getIntent().getExtras().getString("taimianid");
            strDoor = getIntent().getExtras().getString("door");
            strColor = getIntent().getExtras().getString("color");
            strTaimian = getIntent().getExtras().getString("taimian");
        }

    }

    private void initViews(){
        mtbCommit = (Toolbar) findViewById(R.id.tb_commit);
        mtbCommit.setTitle("确认订单");
        setSupportActionBar(mtbCommit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContainer = (CoordinatorLayout) findViewById(R.id.cdl_container);
        mivShow = (ImageView) findViewById(R.id.iv_commit_goods_show);
        mtvReName = (TextView) findViewById(R.id.tv_commit_receive_name);
        mtvRePhone = (TextView) findViewById(R.id.tv_commit_receive_phone);
        mtvReAddress = (TextView) findViewById(R.id.tv_commit_receive_address);
        mtvGoodName = (TextView) findViewById(R.id.tv_commit_goods_name);
        mtvMode = (TextView) findViewById(R.id.tv_commit_goods_mode);
        mtvOrderMoney = (TextView) findViewById(R.id.tv_commit_goods_order_money);
        mtvAllMoney = (TextView) findViewById(R.id.tv_commit_goods_all_money);
        mtvDoor = (TextView) findViewById(R.id.tv_commit_goods_door);
        mtvColor = (TextView) findViewById(R.id.tv_commit_goods_color);
        mtvTaimian = (TextView) findViewById(R.id.tv_commit_goods_taimian);
        mtvGuide = (TextView) findViewById(R.id.tv_commit_goods_guide);
        mtvActivity = (TextView) findViewById(R.id.tv_commit_goods_activity);
        mtvCustom = (TextView) findViewById(R.id.tv_commit_goods_custom_require);
        mtvMoneyTips = (TextView) findViewById(R.id.tv_commit_goods_other_money);
        mtvTips = (TextView) findViewById(R.id.tv_commit_goods_tip);
        mtvBottomMoney = (TextView) findViewById(R.id.tv_bottom_money);
        mfabCommit = (FloatingActionButton) findViewById(R.id.fab_commit);

        mDialog = new MaterialDialog(this);

        Glide.with(this)
                .load(mimgUrl)
                .crossFade()
                .into(mivShow);

        strInfo = strReceiverInfo.split(";");

        mtvReName.setText("收货人：" + strInfo[0]);
        mtvRePhone.setText("手机号：" + strInfo[1]);
        mtvReAddress.setText("收货地址：" + strInfo[2]);

        mtvGoodName.setText(strGoodsName);

        mtvMode.setText("规  格：" + "3米地柜 + 3米台面 + 1米吊柜");

        mtvOrderMoney.setText(strorderMoney);
        mtvAllMoney.setText("预约价：" + strallMoney + " 起");

        mtvDoor.setText("门  型：" + strDoor);
        mtvColor.setText("颜  色：" + strColor);
        mtvTaimian.setText("台  面：" + strTaimian);
        mtvGuide.setText("导  购：" + mstoreName + "  " + mstaffName);

        mtvTips.setText("备  注：" + strTip);

        mfabCommit.setOnClickListener(this);

    }

    private void initWechatPay(){
        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
        // 第二个参数需要换成你自己的微信AppID.
       String initInfo = BCPay.initWechatPay(CommitOrderActivity.this, "wxc5f464b3efe72010");
        if (initInfo != null) {
            Snackbar.make(mContainer, "微信初始化失败：" + initInfo, Snackbar.LENGTH_LONG).show();
        }
    }
    private void showDialog(){
        mDialog.setTitle("下单");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_commit, null);
        mpbLoad = (ProgressWheel) view.findViewById(R.id.pb_common);
        mtvLoad = (TextView) view.findViewById(R.id.tv_dialog_commit_show);
        mpbLoad.setVisibility(View.GONE);
        mtvLoad.setText("核对信息无误，确认下单了吗？");
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        if (mDialog.getPositiveButton() != null){
            mDialog.getPositiveButton().setVisibility(View.VISIBLE);
        }

        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出app
               /* ((GlobalApplication) getApplicationContext()).setUser(null);
                setResult(Constant.REQUEST_EXIT_CODE);*/
                mpbLoad.setVisibility(View.VISIBLE);
                mtvLoad.setVisibility(View.GONE);



                if (morderMode == 100){
                    commitPartsOrder(); //配件订单
                }

                if (morderMode == 200){
                    commitMainOrder();  //橱柜订单
                }

            }
        });

        mDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DIALOG_DISMISS:{
                    mDialog.dismiss();
                    break;
                }

            }
        }
    };

    /**
     * 提交橱柜订单
     * @param
     */

    private void commitMainOrder(){

        Log.d("CommitOrder", uid);
        Log.d("CommitOrder", mGid);
        Log.d("CommitOrder", strallMoney.substring(1));
        Log.d("CommitOrder", mstoreId);
        Log.d("CommitOrder", strInfo[0]);
        Log.d("CommitOrder", strInfo[1]);
        Log.d("CommitOrder", strInfo[2]);
        Log.d("CommitOrder", strGoodsName);
        Log.d("CommitOrder", msalerNo);

        HttpUtil.postAsyn(Constants.BASE_URL + "generate_main_order.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

                mpbLoad.setVisibility(View.GONE);
                mtvLoad.setVisibility(View.VISIBLE);
                mtvLoad.setText("下单失败！请检查您的网络");

                mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);

            }

            @Override
            public void onResponse(String response) {

                System.out.println("================");
                System.out.println(response.toString());
                System.out.println("================");

                OrderInfo info = mGson.fromJson(response, OrderInfo.class);
                if (info.getCode().equals("200")){
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText("下单成功！");
                    //Toast.makeText(CommitOrderActivity.this, ((OrderInfo.T)info.getData()).getAllSchedprice() + "", Toast.LENGTH_LONG).show();
                    mDialog.getPositiveButton().setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);
                    Intent intent = new Intent(CommitOrderActivity.this, PayActivity.class);
                    intent.putExtra("title", ((OrderInfo.T) info.getData()).getGoodsName());
                    intent.putExtra("ordNum", ((OrderInfo.T) info.getData()).getSuperbillid());
                    intent.putExtra("payMoney", ((OrderInfo.T) info.getData()).getAllSchedprice());
                    startActivityForResult(intent, 100);
                }else {
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText(info.getInfo());
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsid", mGid),
                new HttpUtil.Param("billprice", strallMoney.substring(1)),
                new HttpUtil.Param("schedprice", strorderMoney.substring(1)),
                new HttpUtil.Param("storeid", mstoreId),
                new HttpUtil.Param("receivername", strInfo[0]),
                new HttpUtil.Param("phone", strInfo[1]),
                new HttpUtil.Param("receiveraddress", strInfo[2]),
                new HttpUtil.Param("goodsname", strGoodsName),
                new HttpUtil.Param("salerno", msalerNo),
                new HttpUtil.Param("doorid", mDoorId),
                new HttpUtil.Param("colorid", mColorId),
                new HttpUtil.Param("taimianid", mTaimianId),
                new HttpUtil.Param("beizhu", strTip),
                new HttpUtil.Param("picurl", mimgUrl),
                new HttpUtil.Param("cityName", mcity),
                new HttpUtil.Param("num", "1"),
                new HttpUtil.Param("color", "android"),
                new HttpUtil.Param("isperfe", "0"),
                new HttpUtil.Param("mishu1", "1"),
                new HttpUtil.Param("mishu2", "3"),
                new HttpUtil.Param("huodongneirong", "无"),
                new HttpUtil.Param("kehuyaoqiu", "无"),
                new HttpUtil.Param("lingjifeixiang", "无"),
                new HttpUtil.Param("desingerprice", "0"),
                new HttpUtil.Param("activityid", "0"),
        });
    }

    /**
     * 提交配件订单
     */
    private void commitPartsOrder(){
        HttpUtil.postAsyn(Constants.BASE_URL + "generate_parts_order.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbLoad.setVisibility(View.GONE);
                mtvLoad.setVisibility(View.VISIBLE);
                mtvLoad.setText("下单失败！请检查您的网络");

                mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);
            }

            @Override
            public void onResponse(String response) {
                System.out.println("================");
                System.out.println(response.toString());
                System.out.println("================");

                OrderPartsInfo info = mGson.fromJson(response, OrderPartsInfo.class);
                if (info.getCode().equals("200")){
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText("下单成功，去付款");
                    //Toast.makeText(CommitOrderActivity.this, ((OrderInfo.T)info.getData()).getAllSchedprice() + "", Toast.LENGTH_LONG).show();
                    mDialog.getPositiveButton().setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);
                    Intent intent = new Intent(CommitOrderActivity.this, PayActivity.class);
                    intent.putExtra("title", ((OrderPartsInfo.T)info.getData()).getGoodsName());
                    intent.putExtra("ordNum", ((OrderPartsInfo.T)info.getData()).getSuperbillid());
                    intent.putExtra("payMoney", ((OrderPartsInfo.T)info.getData()).getAllSchedprice());
                    startActivityForResult(intent, 100);
                }else {
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText(info.getInfo());
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 3000);
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsid", mGid),
                new HttpUtil.Param("goodsname", strGoodsName),
                new HttpUtil.Param("billprice", strallMoney.substring(1)),
                new HttpUtil.Param("schedprice", strorderMoney.substring(1)),
                new HttpUtil.Param("storeid", mstoreId),
                new HttpUtil.Param("receivername", strInfo[0]),
                new HttpUtil.Param("phone", strInfo[1]),
                new HttpUtil.Param("receiveraddress", strInfo[2]),
                new HttpUtil.Param("salerno", msalerNo),
                new HttpUtil.Param("picurl", mimgUrl),
                new HttpUtil.Param("cityName", mcity),
                new HttpUtil.Param("color", "android"),
                new HttpUtil.Param("isperfe", "0"),
                new HttpUtil.Param("num", "1"),
                new HttpUtil.Param("desingerprice", "0")
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_commit:
                showDialog();
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
        if (data != null && resultCode == 200){
            if (data.getStringExtra("value") != null){
                setResult(200);
                finish();
            }
        }
    }
}