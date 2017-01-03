package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.ButtonUtil;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.RegexUtil;

import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/13.
 */
public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SMSSENDTASK = 2;  //60秒倒计时的定时器
    Toolbar mtbForget;
    TextInputLayout mtxtInName, mtxtInCode, mtxtInNewPwd, mtxtInPwdAgain;
    AppCompatButton mbtnGetCode, mbtnResetPwd;

    ProgressWheel mpgShow;
    ImageView mivShow;

    MaterialDialog mRegisterDialog;
    TextView mtvDialogName;
    Gson mGson = new Gson();

    int mCurSecond = 0;//60秒倒计时
    Timer mTimer;
    TimerTask mTask;
    ResultInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
        initViews();
        initEvents();
    }

    private void initViews() {
        mtbForget = (Toolbar) findViewById(R.id.tb_common);
        mtxtInName = (TextInputLayout) findViewById(R.id.txtinput_username);
        mtxtInCode = (TextInputLayout) findViewById(R.id.txtinput_code);
        mtxtInNewPwd = (TextInputLayout) findViewById(R.id.txtinput_newpwd);
        mtxtInPwdAgain = (TextInputLayout) findViewById(R.id.txtinput_pwdagain);
        mbtnGetCode = (AppCompatButton) findViewById(R.id.btn_getcode);
        mbtnResetPwd = (AppCompatButton) findViewById(R.id.btn_restpwd);
        mtbForget.setTitle("找回密码");
        setSupportActionBar(mtbForget);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initEvents(){
        mbtnGetCode.setOnClickListener(this);
        mbtnResetPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_getcode:
                //发送验证码
                getCode();
                break;
            case R.id.btn_restpwd:
                if (!ButtonUtil.isFastDoubleClick(R.id.btn_restpwd)) {
                    if (mtxtInName.getEditText().getText() == null || !RegexUtil.isMobileNumber(mtxtInName.getEditText().getText().toString())){
                        mtxtInName.setError("请输入正确的手机号！");
                        return;
                    }

                    if (mtxtInCode.getEditText().getText() == null || mtxtInCode.getEditText().getText().length() != 4){
                        mtxtInCode.setError("验证码有误！");
                        return;
                    }

                    if (mtxtInNewPwd.getEditText().getText() == null || !RegexUtil.isPwd(mtxtInNewPwd.getEditText().getText().toString())){
                        mtxtInNewPwd.setError("密码只能是数字,字母或下划线并且长度大于4小于20");
                        return;
                    }

                    if (mtxtInPwdAgain.getEditText().getText() == null || !mtxtInPwdAgain.getEditText().getText().toString().equals(mtxtInPwdAgain.getEditText().getText().toString())){
                        mtxtInPwdAgain.setError("密码不一致");
                        return;
                    }


                    if (mInfo == null || mInfo.getCode().equals("400")){
                        Snackbar.make(mbtnResetPwd, "无法重置!", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    initDialog();
                    HttpUtil.postAsyn(Constants.BASE_URL + "reset_pwd.php", new HttpUtil.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            mtvDialogName.setText("注册失败");
                            mivShow.setVisibility(View.VISIBLE);
                            mivShow.setBackground(getDrawable(R.drawable.ic_error));
                            mRegisterDialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response) {
                            ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                            if (info.getCode().equals("200")) {
                               /* mtvDialogName.setText("注册成功");
                                mpgShow.setVisibility(View.GONE);
                                mivShow.setVisibility(View.VISIBLE);
                                mivShow.setBackground(getDrawable(R.drawable.ic_success));*/
                                mRegisterDialog.dismiss();
                                Snackbar.make(mtxtInName, "注册成功！返回登录", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                                intent.putExtra("phoneNum", mtxtInName.getEditText().getText().toString());
                                setResult(200, intent);
                                finish();
                            } else {
                                /*mtvDialogName.setText(info.getInfo());
                                mivShow.setVisibility(View.VISIBLE);
                                mivShow.setBackground(getDrawable(R.drawable.ic_error));
                                mRegisterDialog.dismiss();*/
                                mRegisterDialog.dismiss();
                                Snackbar.make(mtxtInName, info.getInfo(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, new HttpUtil.Param[]{

                            //{"phoneNum":"13770936421","identifyCode":"5565","password":"******"}
                            new HttpUtil.Param("phoneNum", mtxtInName.getEditText().getText().toString()),
                            new HttpUtil.Param("identifyCode", mtxtInCode.getEditText().getText().toString()),
                            new HttpUtil.Param("password", mtxtInPwdAgain.getEditText().getText().toString())

                    });
                }
                break;
        }
    }
    private void initDialog(){
        mRegisterDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_common, null);
        mtvDialogName = (TextView) view.findViewById(R.id.tv_dialog_name_show);
        mpgShow = (ProgressWheel) view.findViewById(R.id.progress_image);
        mivShow = (ImageView) view.findViewById(R.id.iv_dialog_result);
        mpgShow.setVisibility(View.VISIBLE);
        mivShow.setVisibility(View.GONE);
        mtvDialogName.setText("密码重置中...");

        mRegisterDialog.setContentView(view);
        mRegisterDialog.show();
    }
    public void getCode() {
        if (mCurSecond != 0){
            return;
        }

        //先判断再计时、网络请求
        if (mtxtInName.getEditText().getText() == null || !RegexUtil.isMobileNumber(mtxtInName.getEditText().getText().toString())){
            mtxtInName.setError("请输入正确的手机号！");
            Snackbar.make(mtxtInName, "请先输入正确的手机号！", Snackbar.LENGTH_LONG).show();
            return;
        }

        startTimer();
        HttpUtil.postAsyn(Constants.BASE_URL + "sms/forgetpwd.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mbtnGetCode, "发送失败！请检查您的网络", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                mInfo = mGson.fromJson(response, ResultInfo.class);
                if (mInfo.getCode().equals("400")) {
                    Snackbar.make(mbtnGetCode, mInfo.getInfo(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("phoneNum", mtxtInName.getEditText().getText().toString())
        });

    }

    private void startTimer() {
        if(null != mTask) {
            mTask.cancel();
            mTask = null;
        }
        if(null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        mTask = new TimerTask() {
            public void run() {
                Message message = new Message();
                message.what = SMSSENDTASK;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer(true);
        mCurSecond = 0;
        mTimer.schedule(mTask, 0, 1000);
    }

    private void OnSmsTask() {
        if (mCurSecond == 60) {
            mbtnGetCode.setText("重获验证码");
            StopTimer();

        } else {
            mbtnGetCode.setText(String.format("请稍后(%d秒)",
                    60 - mCurSecond));
            mCurSecond++;

        }
    }

    private void StopTimer() {
        if(null != mTask) {
            mTask.cancel();
            mTask = null;
        }
        if(null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        mCurSecond = 0;
    }


    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SMSSENDTASK: {
                    OnSmsTask();
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
