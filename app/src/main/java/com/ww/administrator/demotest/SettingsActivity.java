package com.ww.administrator.demotest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.DataUtil;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/19.
 */
public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int REQUEST_UPDATE_TEXTVIEW = 1;
    Toolbar mtbSet;
    TextView mtvCache;
    RelativeLayout mrlPwd, mrlClear, mrlExit;
    private MaterialDialog mExitAppDialog;
    private MaterialDialog mChangePwdDialog,mClearDialog;

    String mstrCache;

    LinearLayout mllContainer;
    ProgressWheel mpb;

    MyApp mApp;

    TextView mtvResult;
    TextView mtvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        mApp = (MyApp) getApplicationContext();
        initViews();
        initEvents();
        calculateCache();
    }

    private void initViews() {
        mtbSet = (Toolbar) findViewById(R.id.tb_common);
        setToolbar();
        mrlPwd = (RelativeLayout) findViewById(R.id.rl_changepwd);
        mrlClear = (RelativeLayout) findViewById(R.id.rl_clearcache);
        mrlExit = (RelativeLayout) findViewById(R.id.rl_exit);
        mtvCache = (TextView) findViewById(R.id.tv_cache);
        mtvVersion = (TextView) findViewById(R.id.tv_version);
        mExitAppDialog = new MaterialDialog(this);
        mChangePwdDialog = new MaterialDialog(this);
        mClearDialog = new MaterialDialog(this);

        mtvVersion.setText("Jvawa Version " + ToolsUtil.getAppVersionName(SettingsActivity.this));
    }

    private void setToolbar() {
        mtbSet.setTitle("设 置");
        mtbSet.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbSet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEvents(){
        mrlPwd.setOnClickListener(this);
        mrlClear.setOnClickListener(this);
        mrlExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_changepwd:
                popUpChangePwdDialog();
                break;
            case R.id.rl_clearcache:
                clearCache();
                popUpIsClearing();
                break;
            case R.id.rl_exit:
                popUpExitDialog();
                break;
        }
    }
    private void popUpChangePwdDialog(){
        mChangePwdDialog.setTitle("修改密码");
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_changepwd_layout, null);

        mllContainer = (LinearLayout) dialogView.findViewById(R.id.ll_container);
        mpb = (ProgressWheel) dialogView.findViewById(R.id.pb_common);
        mtvResult = (TextView) dialogView.findViewById(R.id.tv_pwd_change_result);
        mChangePwdDialog.setContentView(dialogView);
        mChangePwdDialog.setCanceledOnTouchOutside(true);

        final MaterialEditText metOrgPwd = (MaterialEditText) dialogView.findViewById(R.id.mtet_orgpwd);
        final MaterialEditText metNewPwd = (MaterialEditText) dialogView.findViewById(R.id.mtet_newpwd);


        if (mChangePwdDialog.getPositiveButton() != null){

            mChangePwdDialog.getPositiveButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(metNewPwd.getText()) || TextUtils.isEmpty(metOrgPwd.getText())) {
                        Toast.makeText(SettingsActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {

                        changePwd(metOrgPwd.getText().toString(), metNewPwd.getText().toString());
                    }
                }
            });

        }

        mChangePwdDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(metNewPwd.getText()) || TextUtils.isEmpty(metOrgPwd.getText())) {
                    Toast.makeText(SettingsActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {

                    changePwd(metOrgPwd.getText().toString(), metNewPwd.getText().toString());
                }

            }
        });

        mChangePwdDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangePwdDialog.dismiss();
            }
        });

        mChangePwdDialog.show();
    }

    /**
     * 修改密码
     */
    private void changePwd(String orgPwd, final String newPwd){

        mllContainer.setVisibility(View.GONE);
        mpb.setVisibility(View.VISIBLE);

        HttpUtil.postAsyn(Constants.BASE_URL + "change_pwd.php", new HttpUtil.ResultCallback<ResultInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                mpb.setVisibility(View.GONE);
                mtvResult.setVisibility(View.VISIBLE);
                mtvResult.setText("请检查一下网络！");
            }

            @Override
            public void onResponse(ResultInfo info) {
                mpb.setVisibility(View.GONE);
                mtvResult.setVisibility(View.VISIBLE);
                mtvResult.setText(info.getInfo());
                mChangePwdDialog.getPositiveButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChangePwdDialog.dismiss();
                    }
                });
                if (info.getCode().equals("200")){
                    mApp.getUser().setPassword(newPwd);

                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", mApp.getUser().getId()),
                new HttpUtil.Param("orgPwd", orgPwd),
                new HttpUtil.Param("newPwd", newPwd)
        });
    }

    /**
     * 计算缓存
     */
    private void calculateCache(){

        try {
            mstrCache = DataUtil.getTotalCacheSize(SettingsActivity.this);
            if (mstrCache.equals("0.0Byte")){
                mtvCache.setText("0.00MB");
            }else {
                mtvCache.setText(mstrCache);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    /**
     * 清理缓存
     */
    private void clearCache(){

        DataUtil.clearAllCache(SettingsActivity.this);

    }
    private void popUpIsClearing(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_common, null);
        mClearDialog.setContentView(view);
        mClearDialog.setCanceledOnTouchOutside(true);
        mClearDialog.show();
        if (mstrCache.equals("0.0Byte")){
            /*((TextView)view.findViewById(R.id.tv_dialogcontent)).setText("缓存已经为0！");
            view.findViewById(R.id.progress_iloading).setVisibility(View.GONE);*/
            Toast.makeText(SettingsActivity.this, "缓存已经为0！", Toast.LENGTH_SHORT).show();
        }else {
            ((TextView)view.findViewById(R.id.tv_dialog_name_show)).setText("正在清理...");
            view.findViewById(R.id.progress_image).setVisibility(View.VISIBLE);
            Timer timer = new Timer();
            timer.schedule(new CacheTimerTask(), 2500);
        }

    }

    private void popUpExitDialog(){
        mExitAppDialog.setTitle("退出");
        mExitAppDialog.setMessage("确定要退出家瓦商城吗？");
        mExitAppDialog.setCanceledOnTouchOutside(true);

        mExitAppDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出app
                ((MyApp) getApplicationContext()).setUser(null);
                mExitAppDialog.dismiss();
                mApp.SetActivityIntent(Constants.SHOPPING_CART_REFRESH, 0);

                setResult(400);
                finish();
            }
        });

        mExitAppDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExitAppDialog.dismiss();
            }
        });

        mExitAppDialog.show();
    }

    class CacheTimerTask extends TimerTask {
        @Override
        public void run() {
            mClearDialog.dismiss();
            Message msg = new Message();
            msg.what = REQUEST_UPDATE_TEXTVIEW;
            mHandler.sendMessage(msg);
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REQUEST_UPDATE_TEXTVIEW:{
                    calculateCache();
                    break;
                }

            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
