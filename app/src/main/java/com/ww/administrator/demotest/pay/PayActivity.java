package com.ww.administrator.demotest.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.unionpay.UPPayAssistEx;
import com.ww.administrator.demotest.CommitOrderActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.PayTypeAdapter;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.beecloud.BCPay;
import cn.beecloud.BCQuery;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCBillOrder;
import cn.beecloud.entity.BCPayResult;
import cn.beecloud.entity.BCQueryBillResult;
import cn.beecloud.entity.BCReqParams;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/10/8.
 */
public class PayActivity extends AppCompatActivity {
    private static final String TAG = "PayActivity";
    CoordinatorLayout mContainer;
    Toolbar mtbPay;

    RecyclerView mrvType;

    LinearLayoutManager manager;
    MaterialDialog mDialog;
    MaterialDialog mOtherDialog;

    ProgressWheel mpbLoad;
    TextView mtvLoad;

    PayTypeAdapter mAdapter;

    int ordNum = -1;
    String erpNum = "";
    String payTitle = "";
    String value = "";

    int payMoney = -1;

    String billNum = "";
    String billChannel = "";
    String tradeNum = "";
    String price = "";
    Gson mGson = new Gson();

    TextView mtvTitle, mtvMoney, mtvOrderNum;
    //支付结果返回入口
    BCCallback bcCallback = new BCCallback() {
        @Override
        public void done(final BCResult bcResult) {
            final BCPayResult bcPayResult = (BCPayResult)bcResult;
            //此处关闭loading界面
            mDialog.dismiss();

            //根据你自己的需求处理支付结果
            //需要注意的是，此处如果涉及到UI的更新，请在UI主进程或者Handler操作
            PayActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String result = bcPayResult.getResult();

                    /*
                      注意！
                      所有支付渠道建议以服务端的状态金额为准，此处返回的RESULT_SUCCESS仅仅代表手机端支付成功
                    */

                    if (result.equals(BCPayResult.RESULT_SUCCESS)) {
                        if (bcPayResult.getId() != null) {
                            //你可以把这个id存到你的订单中，下次直接通过这个id查询订单
                            Log.w(TAG, "bill id retrieved : " + bcPayResult.getId());

                            //根据ID查询，此处只是演示如何通过id查询订单，并非支付必要部分
                            getBillInfoByID(bcPayResult.getId());

                        }

                    } else if (result.equals(BCPayResult.RESULT_CANCEL))
                        Toast.makeText(PayActivity.this, "取消支付！", Toast.LENGTH_LONG).show();
                    else if (result.equals(BCPayResult.RESULT_FAIL)) {
                        String toastMsg = "支付失败, 原因: " + bcPayResult.getErrCode() +
                                " # " + bcPayResult.getErrMsg() +
                                " # " + bcPayResult.getDetailInfo();

                        /**
                         * 你发布的项目中不应该出现如下错误，此处由于支付宝政策原因，
                         * 不再提供支付宝支付的测试功能，所以给出提示说明
                         */
                        if (bcPayResult.getErrMsg().equals("PAY_FACTOR_NOT_SET") &&
                                bcPayResult.getDetailInfo().startsWith("支付宝参数")) {
                            toastMsg = "支付失败：由于支付宝政策原因，故不再提供支付宝支付的测试功能，给您带来的不便，敬请谅解";
                        }

                        /**
                         * 以下是正常流程，请按需处理失败信息
                         */

                        //Toast.makeText(PayActivity.this, toastMsg, Toast.LENGTH_LONG).show();
                        Log.e(TAG, toastMsg);

                        if (bcPayResult.getErrMsg().equals(BCPayResult.FAIL_PLUGIN_NOT_INSTALLED)) {
                            //银联需要重新安装控件
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }

                    } else if (result.equals(BCPayResult.RESULT_UNKNOWN)) {
                        //可能出现在支付宝8000返回状态
                        Toast.makeText(PayActivity.this, "订单状态未知", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(PayActivity.this, "invalid return", Toast.LENGTH_LONG).show();
                    }


                }
            });
        }
    };

    private void getBillInfoByID(String id) {

        BCQuery.getInstance().queryBillByIDAsync(id,
                new BCCallback() {
                    @Override
                    public void done(BCResult result) {
                        BCQueryBillResult billResult = (BCQueryBillResult) result;

                        Log.d(TAG, "------ response info ------");
                        Log.d(TAG, "------getResultCode------" + billResult.getResultCode());
                        Log.d(TAG, "------getResultMsg------" + billResult.getResultMsg());
                        Log.d(TAG, "------getErrDetail------" + billResult.getErrDetail());

                        if (billResult.getResultCode() != 0)
                            return;

                        Log.d(TAG, "------- bill info ------");
                        BCBillOrder billOrder = billResult.getBill();
                        Log.d(TAG, "订单唯一标识符：" + billOrder.getId());
                        Log.d(TAG, "订单号:" + billOrder.getBillNum());
                        Log.d(TAG, "订单金额, 单位为分:" + billOrder.getTotalFee());
                        Log.d(TAG, "渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getChannel()));
                        Log.d(TAG, "子渠道类型:" + BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getSubChannel()));
                        Log.d(TAG, "订单是否成功:" + billOrder.getPayResult());

                        if (billOrder.getPayResult())
                            Log.d(TAG, "渠道返回的交易号，未支付成功时，是不含该参数的:" + billOrder.getTradeNum());
                        else
                            Log.d(TAG, "订单是否被撤销，该参数仅在线下产品（例如二维码和扫码支付）有效:"
                                    + billOrder.getRevertResult());

                        Log.d(TAG, "订单创建时间:" + new Date(billOrder.getCreatedTime()));
                        Log.d(TAG, "扩展参数:" + billOrder.getOptional());
                        Log.w(TAG, "订单是否已经退款成功(用于后期查询): " + billOrder.getRefundResult());
                        Log.w(TAG, "渠道返回的详细信息，按需处理: " + billOrder.getMessageDetail());

                        billNum = billOrder.getBillNum();
                        billChannel = BCReqParams.BCChannelTypes.getTranslatedChannelName(billOrder.getChannel());
                        tradeNum = billOrder.getTradeNum();
                        price = billOrder.getTotalFee() + "";


                        value = value + billNum + ";" + billChannel + "_apk;" + tradeNum + ";" + payMoney;
                        paySuccess();
                    }
                });
    }

    // 通过Handler.Callback()可消除内存泄漏警告
    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                //如果用户手机没有安装银联支付控件,则会提示用户安装
                final MaterialDialog builder = new MaterialDialog(PayActivity.this);
                builder.setTitle("提示");
                builder.setMessage("完成支付需要安装或者升级银联支付控件，是否安装？");
                builder.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UPPayAssistEx.installUPPayPlugin(PayActivity.this);
                        builder.dismiss();
                    }
                });


                builder.setNegativeButton("取消",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });
                builder.show();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_layout);
        initParams();
        initViews();
        initWechatPay();
    }

    private void initParams(){
        ordNum = getIntent().getIntExtra("ordNum", -1);
        erpNum = ordNum + "";
        payTitle = getIntent().getStringExtra("title");
        payMoney = getIntent().getIntExtra("payMoney", 200000);
        //value =  value + ordNum + ";" + billChannel + "_apk;" + TradeNum + ";" + payMoney;
    }

    private void initViews(){
        mContainer = (CoordinatorLayout) findViewById(R.id.cdl_container);
        mtvTitle = (TextView) findViewById(R.id.tv_goods_name);
        mtvMoney = (TextView) findViewById(R.id.tv_money);
        mtvOrderNum = (TextView) findViewById(R.id.tv_goods_order_num);

        mrvType = (RecyclerView) findViewById(R.id.rv_pay_type);
        mtbPay = (Toolbar) findViewById(R.id.tb_pay);
        setSupportActionBar(mtbPay);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mtvTitle.setText(payTitle);
        int moneyShow = payMoney / 100;
        if (moneyShow == 0){
            mtvMoney.setText("￥0.01");
        }else {
            mtvMoney.setText("￥" + payMoney / 100);
        }

        mtvOrderNum.setText(ordNum + "");

        manager = new LinearLayoutManager(PayActivity.this, LinearLayoutManager.VERTICAL, false);
        mrvType.setLayoutManager(manager);
        mAdapter = new PayTypeAdapter(PayActivity.this);
        mrvType.setAdapter(mAdapter);

        initDialog();


        mAdapter.setOnTypeClickListener(new PayTypeAdapter.TypeClickListener() {
            @Override
            public void getItem(int pos) {
                switch (pos) {
                    case 0: //微信支付
                        mDialog.show();
                        useWechatPay();

                        break;

                    case 1: //支付宝支付
                        mOtherDialog.setMessage("抱歉尚未开通支付宝！");
                        mOtherDialog.show();
                        break;

                    case 2: //银联
                        /*mOtherDialog.setMessage("抱歉尚未开通银联支付！");
                        mOtherDialog.show();*/
                        mDialog.show();
                        useUnionPay();
                        break;
                }
            }
        });

    }

    /**
     * 初始化微信支付
     */
    private void initWechatPay(){
        // 如果用到微信支付，在用到微信支付的Activity的onCreate函数里调用以下函数.
        // 第二个参数需要换成你自己的微信AppID.
        String initInfo = BCPay.initWechatPay(PayActivity.this, "wxe822c67255912a6e");
        if (initInfo != null) {
            Snackbar.make(mContainer, "微信初始化失败：" + initInfo, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * 使用微信支付
     */
    private void useWechatPay(){
        //对于微信支付, 手机内存太小会有OutOfResourcesException造成的卡顿, 以致无法完成支付
        //这个是微信自身存在的问题
        Map<String, String> mapOptional = new HashMap<String, String>();

        mapOptional.put("客户端", "android");
        mapOptional.put("商品订单", ordNum + "");
        mapOptional.put("erpNum", erpNum);
        mapOptional.put("预约金", payMoney + "");

        if (BCPay.isWXAppInstalledAndSupported() &&
                BCPay.isWXPaySupported()) {

            BCPay.getInstance(PayActivity.this).reqWXPaymentAsync(
                    "家瓦商城:" + ordNum,               //订单标题
                    payMoney,                           //订单金额(分)
                    ordNum + "",  //订单流水号
                    mapOptional,            //扩展参数(可以null)
                    bcCallback);            //支付完成后回调入口

        } else {
            Toast.makeText(PayActivity.this,
                    "您尚未安装微信或者安装的微信版本不支持", Toast.LENGTH_LONG).show();
            mDialog.dismiss();
        }

    }


    /**
     * 使用银联支付
     */

    private void useUnionPay(){
        BCPay.PayParams payParam = new BCPay.PayParams();

        payParam.channelType = BCReqParams.BCChannelTypes.UN_APP;

        //商品描述, 32个字节内, 汉字以2个字节计
        payParam.billTitle = "家瓦商城:" + ordNum;

        //支付金额，以分为单位，必须是正整数
        payParam.billTotalFee = payMoney;

        //商户自定义订单号
        payParam.billNum = ordNum +"";

        BCPay.getInstance(PayActivity.this).reqPaymentAsync(payParam,
                bcCallback);
    }

    //保存支付单
    private void paySuccess(){

        HttpUtil.postAsyn(Constants.BASE_URL + "save_bill.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    Toast.makeText(PayActivity.this, "支付成功！去 '我的订单' 查询订单", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(PayActivity.this, CommitOrderActivity.class);
                    Log.d(TAG, "=============value============" + value);
                    intent.putExtra("value", value);
                    setResult(200, intent);
                    finish();
                }else {
                    Toast.makeText(PayActivity.this, info.getInfo(), Toast.LENGTH_LONG).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("tradeNo", tradeNum),
                new HttpUtil.Param("superBillId", billNum),
                new HttpUtil.Param("price", price),
                new HttpUtil.Param("payType", billChannel)
        });
    }

    private void initDialog(){
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("正在启动第三方支付...");
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_commit, null);
        mpbLoad = (ProgressWheel) view.findViewById(R.id.pb_common);
        mtvLoad = (TextView) view.findViewById(R.id.tv_dialog_commit_show);
        mtvLoad.setVisibility(View.GONE);
        mpbLoad.setVisibility(View.VISIBLE);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        mOtherDialog = new MaterialDialog(this);
        mOtherDialog.setTitle("提示");
        mOtherDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtherDialog.dismiss();
            }
        });
        mOtherDialog.setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理当前的activity引用
        BCPay.clear();

        //使用微信的，在initWechatPay的activity结束时detach
        BCPay.detachWechat();

        //使用百度支付的，在activity结束时detach
        BCPay.detachBaiduPay();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
