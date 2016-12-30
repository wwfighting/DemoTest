package com.ww.administrator.demotest.fragment;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.About4Acitivity;
import com.ww.administrator.demotest.AboutActivity;
import com.ww.administrator.demotest.AddressManageActivity;
import com.ww.administrator.demotest.AllOrderActivity;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.CouponsActivity;
import com.ww.administrator.demotest.HotActivity;
import com.ww.administrator.demotest.LoginActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.SettingsActivity;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.User;
import com.ww.administrator.demotest.util.CircleTransform;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.ToolsUtil;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/7/19.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener{

    ImageView mivHead, mivHotActy;
    TextView mtvName, mtvLoginName;
    FloatingActionButton mfbScan;
    RelativeLayout mrlOrder, mrlCoupons, mrlActivity, mrlAddress, mrlSettings, mrlAbout;

    MyApp mApp;
    User mUser;
    CircleTransform circleCrop;

    MaterialDialog mTipDialog;

    @Override
    protected void getArgs() {
        mApp = (MyApp) getActivity().getApplicationContext();
        mUser = mApp.getUser();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initViews(View view) {
        mivHead = (ImageView) view.findViewById(R.id.iv_round_head);
        mivHotActy = (ImageView) view.findViewById(R.id.iv_my_acty);
        mtvName = (TextView) view.findViewById(R.id.tv_name);
        mtvLoginName = (TextView) view.findViewById(R.id.tv_loginname);
        mfbScan = (FloatingActionButton) view.findViewById(R.id.fab_scan);
        mrlOrder = (RelativeLayout) view.findViewById(R.id.rl_order);
        mrlCoupons = (RelativeLayout) view.findViewById(R.id.rl_coupons);
        mrlActivity = (RelativeLayout) view.findViewById(R.id.rl_activity);
        mrlAddress = (RelativeLayout) view.findViewById(R.id.rl_address);
        mrlSettings = (RelativeLayout) view.findViewById(R.id.rl_settings);
        mrlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);
        circleCrop = new CircleTransform(getActivity());
        if (mUser == null){
            Glide.with(this)
                    .load(R.mipmap.ic_launcher)
                    .crossFade()
                    .transform(circleCrop)
                    .into(mivHead);
        }else {
            mtvName.setVisibility(View.GONE);
            mtvLoginName.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mApp.getUser().getNickname())){
                mtvLoginName.setText(mApp.getUser().getPhone());
            }else {
                mtvLoginName.setText(mApp.getUser().getNickname());
            }

            if (TextUtils.isEmpty(mApp.getUser().getHeadImg())){
                Glide.with(this)
                        .load(R.mipmap.ic_launcher)
                        .crossFade()
                        .transform(circleCrop)
                        .into(mivHead);
            }else {
                Glide.with(getActivity())
                        .load(mApp.getUser().getHeadImg())
                        .crossFade()
                        .transform(circleCrop)
                        .into(mivHead);
            }
        }

        Glide.with(this).load(R.drawable.h_043).into(mivHotActy);
        initDialog();
    }

    private void initDialog(){
        mTipDialog = new MaterialDialog(getActivity());
        mTipDialog.setTitle("提示");
        mTipDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mTipDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTipDialog.dismiss();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        mTipDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTipDialog.dismiss();
            }
        });

        //点击对话框外，对话框消失
        mTipDialog.setCanceledOnTouchOutside(true);
    }
    @Override
    protected void doBusiness() {
        initEvents();
    }

    private void initEvents() {
        mtvName.setOnClickListener(this);
        mfbScan.setOnClickListener(this);
        mrlOrder.setOnClickListener(this);
        mrlCoupons.setOnClickListener(this);
        mrlActivity.setOnClickListener(this);
        mrlAddress.setOnClickListener(this);
        mrlSettings.setOnClickListener(this);
        mrlAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_round_head:

                break;

            case R.id.fab_scan: //扫描二维码
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {
                    /*Intent qrCode = new Intent(getActivity(), CaptureActivity.class);
                    startActivityForResult(qrCode, 100);*/
                }
                break;

            case R.id.tv_name:  //未登录时跳转到登录界面
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 100);

                }
                break;

            case R.id.rl_order: //全部订单
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {
                    Intent order = new Intent(getActivity(), AllOrderActivity.class);
                    startActivity(order);
                }

                break;

            case R.id.rl_coupons:   //我的礼券
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {
                    Intent coupons = new Intent(getActivity(), CouponsActivity.class);
                    startActivity(coupons);
                }

                break;

            case R.id.rl_activity:  //最新活动
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {

                    //Intent activity = new Intent(getActivity(), RotateEventActivity.class);
                    Intent activity = new Intent(getActivity(), HotActivity.class);
                    startActivity(activity);
                }

                break;

            case R.id.rl_address:   //地址管理
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {
                    Intent address = new Intent(getActivity(), AddressManageActivity.class);
                    startActivity(address);
                }

                break;

            case R.id.rl_settings:  //设置
                if (((MyApp)getActivity().getApplicationContext()).getUser() == null){
                    mTipDialog.show();
                }else {
                    Intent settings = new Intent(getActivity(), SettingsActivity.class);
                    startActivityForResult(settings, 100);
                }

                break;

            case R.id.rl_about: //关于
                if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP){
                    startActivity(new Intent(getActivity(), About4Acitivity.class));
                }else {

                    startActivity(new Intent(getActivity(), AboutActivity.class));
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 200:   //登录成功
                mtvName.setVisibility(View.GONE);
                mtvLoginName.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mApp.getUser().getNickname())){
                    mtvLoginName.setText(mApp.getUser().getPhone());
                }else {
                    mtvLoginName.setText(mApp.getUser().getNickname());
                }

                if (TextUtils.isEmpty(mApp.getUser().getHeadImg())){
                    Glide.with(this)
                            .load(R.mipmap.ic_launcher)
                            .crossFade()
                            .transform(circleCrop)
                            .into(mivHead);
                }else {
                    Glide.with(getActivity())
                            .load(mApp.getUser().getHeadImg())
                            .crossFade()
                            .transform(circleCrop)
                            .into(mivHead);
                }

                break;

            case 400:   //用户退出
                mtvName.setVisibility(View.VISIBLE);
                mtvLoginName.setVisibility(View.GONE);
                Glide.with(this)
                        .load(R.mipmap.ic_launcher)
                        .crossFade()
                        .transform(circleCrop)
                        .into(mivHead);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApp app =(MyApp) getActivity().getApplicationContext();
        Object obj = app.GetActivityIntent(Constants.MY_REFRESH);
        if (null != obj) {
            int iValue = (Integer) obj;
            if (iValue == 10) {  //10为登录
                mtvName.setVisibility(View.GONE);
                mtvLoginName.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(app.getUser().getNickname())){
                    mtvLoginName.setText(app.getUser().getPhone());
                }else {
                    mtvLoginName.setText(app.getUser().getNickname());
                }

                if (TextUtils.isEmpty(app.getUser().getHeadImg())){
                    Glide.with(this)
                            .load(R.mipmap.ic_launcher)
                            .crossFade()
                            .transform(circleCrop)
                            .into(mivHead);
                }else {
                    Glide.with(getActivity())
                            .load(app.getUser().getHeadImg())
                            .crossFade()
                            .transform(circleCrop)
                            .into(mivHead);
                }

            }
        }
    }
}
