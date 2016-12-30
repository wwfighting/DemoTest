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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.OrderInfo;
import com.ww.administrator.demotest.model.OrderPartsInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.pay.EventPayActivity;
import com.ww.administrator.demotest.pay.PayActivity;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import cn.beecloud.BCPay;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/28.
 */
public class CommitOrderActivity extends AppCompatActivity implements View.OnClickListener{

    private final static int DIALOG_DISMISS = 100;

    private String uid = "-1";

    //根据传来的morderMode参数区分订单类型
    int morderMode = -1; //订单类型 100：配件订单 200：橱柜订单 300：全屋订单

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
    String mishu1 = ""; //吊柜米数
    String mishu2 = ""; //地柜米数

    int orderNum = 1;

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

    TextView mtvOrderMoneyLabel;
    TextView mtvMoneyBottom;
    String action = "";

    TextView mtvQiang;

    String mQid = "";

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

        //配件订单 strorderMoney和strallMoney是一样的，即付款总价，没有预约价
        strorderMoney = getIntent().getExtras().getString("orderMoney");
        strallMoney = getIntent().getExtras().getString("allMoney");
        strTip = getIntent().getExtras().getString("tip");
        mstoreId = getIntent().getExtras().getString("storeid");
        msalerNo = getIntent().getExtras().getString("salerNo").substring(2);
        mcity = getIntent().getExtras().getString("city");

        action = getIntent().getExtras().getString("action", "");
        mQid = getIntent().getExtras().getString("qid", "");

        //橱柜参数 多了台面地柜以及颜色和米数
        if (morderMode == 200){
            mDoorId = getIntent().getExtras().getString("doorid");
            mColorId = getIntent().getExtras().getString("colorid");
            mTaimianId = getIntent().getExtras().getString("taimianid");
            strDoor = getIntent().getExtras().getString("door");
            strColor = getIntent().getExtras().getString("color");
            strTaimian = getIntent().getExtras().getString("taimian");
            mishu1 = getIntent().getExtras().getString("mishu1");
            mishu2 = getIntent().getExtras().getString("mishu2");
        }

        //配件参数 多了订购数量
        if (morderMode == 100 || morderMode == 500){
            orderNum = getIntent().getExtras().getInt("num");
        }

        if (!mQid.equals("") && !mQid.equals("0")){
            if (ToolsUtil.isCMEventExpire()){
                loadDatas();
            }else {
                Toast.makeText(CommitOrderActivity.this, "一元抢定减100活动只限12.24一天！", Toast.LENGTH_LONG).show();
            }


        }

    }

    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "has_qiangding.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                ResultInfo result = mGson.fromJson(response, ResultInfo.class);
                if (result.getCode().equals("401")){
                    mtvQiang.setVisibility(View.VISIBLE);
                    int jianMoney = Integer.parseInt(strorderMoney.substring(1)) - 100;
                    strorderMoney = "￥" + jianMoney;
                    mtvOrderMoney.setText(strorderMoney);
                    mtvBottomMoney.setText(strorderMoney);
                }else {
                    mtvQiang.setVisibility(View.GONE);
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("qid", mQid),
        });
    }

    private void initViews(){
        mtbCommit = (Toolbar) findViewById(R.id.tb_commit);
        mtbCommit.setTitle("确认订单");
        setSupportActionBar(mtbCommit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtvOrderMoneyLabel = (TextView) findViewById(R.id.tv_commit_goods_order_money_label);
        mtvMoneyBottom = (TextView) findViewById(R.id.tv_commit_bottom_money_label);
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
        mtvQiang = (TextView) findViewById(R.id.tv_commit_goods_qiang);

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

        if (morderMode == 500 && action.equals("")){
            mtvActivity.setText("活动内容：" + "2016双十一1元抢购活动！");
        }

        if (morderMode == 500 && action.equals("qd")){
            mtvActivity.setText("活动内容：" + "2016圣诞特惠1元抢定活动！");
        }

        if (morderMode == 100 || morderMode == 500){ //配件
            mtvMode.setText("数  量：" + orderNum + " 个");
            mtvOrderMoneyLabel.setText("支付金额：");
            mtvMoneyBottom.setText("实付金额：");
            mtvAllMoney.setVisibility(View.GONE);
            mtvDoor.setVisibility(View.GONE);
            mtvColor.setVisibility(View.GONE);
            mtvTaimian.setVisibility(View.GONE);
        }

        if (morderMode == 200){ //橱柜
            mtvMode.setText("规  格：" + mishu2 + "米地柜 + " +  mishu2  + "米台面 + " + mishu1 + "米吊柜");
        }

        if (morderMode == 300){ //全屋
            mtvMode.setVisibility(View.GONE);
            mtvAllMoney.setVisibility(View.GONE);
            mtvDoor.setVisibility(View.GONE);
            mtvColor.setVisibility(View.GONE);
            mtvTaimian.setVisibility(View.GONE);
        }
        mtvOrderMoney.setText(strorderMoney);
        mtvBottomMoney.setText(strorderMoney);
        mtvAllMoney.setText("预估价：" + strallMoney + " 起");

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


                if (morderMode == 100 || morderMode == 500){
                    commitPartsOrder(); //配件订单
                }

                if (morderMode == 200){
                    commitMainOrder();  //橱柜订单
                }

                if (morderMode == 300){
                    commitHomeOrder();  //全屋订单
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
     * 提交全屋订单
     */
    private void commitHomeOrder(){
        HttpUtil.postAsyn(Constants.BASE_URL + "generate_home_order.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbLoad.setVisibility(View.GONE);
                mtvLoad.setVisibility(View.VISIBLE);
                mtvLoad.setText("下单失败！请检查您的网络");

                mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
            }

            @Override
            public void onResponse(String response) {

                OrderPartsInfo info = mGson.fromJson(response, OrderPartsInfo.class);
                if (info.getCode().equals("200")){
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText("下单成功，去付款");
                    //Toast.makeText(CommitOrderActivity.this, ((OrderInfo.T)info.getData()).getAllSchedprice() + "", Toast.LENGTH_LONG).show();
                    mDialog.getPositiveButton().setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);

                    Intent intent = new Intent(CommitOrderActivity.this, PayActivity.class);
                    intent.putExtra("title", ((OrderPartsInfo.T)info.getData()).getGoodsName());
                    intent.putExtra("ordNum", ((OrderPartsInfo.T)info.getData()).getSuperbillid());
                    intent.putExtra("payMoney", ((OrderPartsInfo.T)info.getData()).getAllSchedprice());
                    startActivityForResult(intent, 100);
                }else {
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText(info.getInfo());
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
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
                new HttpUtil.Param("color", "android,全屋定制"),
                new HttpUtil.Param("isperfe", "0"),
                new HttpUtil.Param("desingerprice", "0")
        });
    }

    /**
     * 提交橱柜订单
     * @param
     */

    private void commitMainOrder(){
        HttpUtil.postAsyn(Constants.BASE_URL + "generate_main_order.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

                mpbLoad.setVisibility(View.GONE);
                mtvLoad.setVisibility(View.VISIBLE);
                mtvLoad.setText("下单失败！请检查您的网络");

                mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);

            }

            @Override
            public void onResponse(String response) {

                OrderInfo info = mGson.fromJson(response, OrderInfo.class);
                if (info.getCode().equals("200")){
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText("下单成功！");
                    //Toast.makeText(CommitOrderActivity.this, ((OrderInfo.T)info.getData()).getAllSchedprice() + "", Toast.LENGTH_LONG).show();
                    mDialog.getPositiveButton().setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
                    Intent intent = new Intent(CommitOrderActivity.this, PayActivity.class);
                    intent.putExtra("title", ((OrderInfo.T) info.getData()).getGoodsName());
                    intent.putExtra("ordNum", ((OrderInfo.T) info.getData()).getSuperbillid());
                    intent.putExtra("payMoney", ((OrderInfo.T) info.getData()).getAllSchedprice());
                    startActivityForResult(intent, 100);
                }else {
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText(info.getInfo());
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
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
                new HttpUtil.Param("color", "android,橱柜"),
                new HttpUtil.Param("isperfe", "0"),
                new HttpUtil.Param("mishu1", mishu1),
                new HttpUtil.Param("mishu2", mishu2),
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

                mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
            }

            @Override
            public void onResponse(String response) {

                OrderPartsInfo info = mGson.fromJson(response, OrderPartsInfo.class);
                if (info.getCode().equals("200")){
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText("下单成功，去付款");
                    //Toast.makeText(CommitOrderActivity.this, ((OrderInfo.T)info.getData()).getAllSchedprice() + "", Toast.LENGTH_LONG).show();
                    mDialog.getPositiveButton().setVisibility(View.GONE);
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
                    if (morderMode == 500 && !action.equals("qd")){
                        Intent intent = new Intent(CommitOrderActivity.this, EventPayActivity.class);
                        intent.putExtra("title", ((OrderPartsInfo.T)info.getData()).getGoodsName());
                        intent.putExtra("ordNum", ((OrderPartsInfo.T)info.getData()).getSuperbillid());
                        intent.putExtra("payMoney", ((OrderPartsInfo.T)info.getData()).getAllSchedprice());
                        startActivityForResult(intent, 100);
                    }else {
                        Intent intent = new Intent(CommitOrderActivity.this, PayActivity.class);
                        intent.putExtra("title", ((OrderPartsInfo.T)info.getData()).getGoodsName());
                        intent.putExtra("ordNum", ((OrderPartsInfo.T)info.getData()).getSuperbillid());
                        intent.putExtra("payMoney", ((OrderPartsInfo.T)info.getData()).getAllSchedprice());
                        startActivityForResult(intent, 100);
                    }

                }else {
                    mpbLoad.setVisibility(View.GONE);
                    mtvLoad.setVisibility(View.VISIBLE);
                    mtvLoad.setText(info.getInfo());
                    mHandler.sendEmptyMessageDelayed(DIALOG_DISMISS, 800);
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
                new HttpUtil.Param("color", "android,配件"),
                new HttpUtil.Param("isperfe", "0"),
                new HttpUtil.Param("num", orderNum + ""),
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
                Intent intent = new Intent(CommitOrderActivity.this, OrderActivity.class);
                setResult(200, intent);
                finish();
            }
        }
    }
}
