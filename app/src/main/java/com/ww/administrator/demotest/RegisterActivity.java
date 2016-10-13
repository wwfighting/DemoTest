package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
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
 * Created by Administrator on 2016/8/19.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SMSSENDTASK = 2;  //60秒倒计时的定时器

    Toolbar mtbRegister;
    TextView mtvAgreement, mtvDialogName;
    TextInputLayout mtxtInPhone, mtxtInCode, mtxtInPwd, mtxtInAgainPwd;
    AppCompatCheckBox mchbAgreement;
    AppCompatButton mbtnRegister, mbtnGetCode;

    ProgressWheel mpgShow;
    ImageView mivShow;

    MaterialDialog mRegisterDialog;
    Gson mGson = new Gson();

    int mCurSecond = 0;//60秒倒计时
    Timer mTimer;
    TimerTask mTask;
    ResultInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initEvents();
    }

    private void initViews() {
        mtbRegister = (Toolbar) findViewById(R.id.tb_common);
        mtvAgreement = (TextView) findViewById(R.id.tv_agreement);
        mtxtInPhone = (TextInputLayout) findViewById(R.id.txtinput_username);
        mtxtInCode = (TextInputLayout) findViewById(R.id.txtinput_code);
        mtxtInPwd = (TextInputLayout) findViewById(R.id.txtinput_newpwd);
        mtxtInAgainPwd = (TextInputLayout) findViewById(R.id.txtinput_againpwd);
        mchbAgreement = (AppCompatCheckBox) findViewById(R.id.chb_agreement);
        mbtnRegister = (AppCompatButton) findViewById(R.id.btn_register);
        mbtnGetCode = (AppCompatButton) findViewById(R.id.btn_getcode);
        mtbRegister.setTitle("注 册");
        setSupportActionBar(mtbRegister);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initEvents(){
        mtvAgreement.setOnClickListener(this);
        mbtnRegister.setOnClickListener(this);
        mbtnGetCode.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_agreement:
                startActivity(new Intent(RegisterActivity.this, RegAgreementActivity.class));
                break;
            case R.id.btn_getcode:
                //发送验证码
                getCode();
                break;
            case R.id.btn_register:
                //注册
                if (!ButtonUtil.isFastDoubleClick(R.id.btn_register)) {
                    if (mtxtInPhone.getEditText().getText() == null || !RegexUtil.isMobileNumber(mtxtInPhone.getEditText().getText().toString())){
                        mtxtInPhone.setError("请输入正确的手机号！");
                        return;
                    }

                    if (mtxtInCode.getEditText().getText() == null || mtxtInCode.getEditText().getText().length() != 4){
                        mtxtInCode.setError("验证码有误！");
                        return;
                    }

                    if (mtxtInPwd.getEditText().getText() == null || !RegexUtil.isPwd(mtxtInPwd.getEditText().getText().toString())){
                        mtxtInPwd.setError("密码只能是数字,字母或下划线并且长度小于20");
                        return;
                    }

                    if (mtxtInAgainPwd.getEditText().getText() == null || !mtxtInAgainPwd.getEditText().getText().toString().equals(mtxtInPwd.getEditText().getText().toString())){
                        mtxtInAgainPwd.setError("密码不一致");
                        return;
                    }

                    if (!mchbAgreement.isChecked()){
                        Snackbar.make(mbtnRegister, "您未同意家瓦商城用户注册协议！", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    if (mInfo == null || mInfo.getCode().equals("400")){
                        Snackbar.make(mbtnRegister, "无法注册!", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    initDialog();
                    HttpUtil.postAsyn(Constants.BASE_URL + "register_new_user.php", new HttpUtil.ResultCallback<String>() {
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
                            if (info.getCode().equals("200")){
                               /* mtvDialogName.setText("注册成功");
                                mpgShow.setVisibility(View.GONE);
                                mivShow.setVisibility(View.VISIBLE);
                                mivShow.setBackground(getDrawable(R.drawable.ic_success));*/
                                mRegisterDialog.dismiss();
                                Snackbar.make(mtxtInPhone, "注册成功！返回登录", Snackbar.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("phoneNum", mtxtInPhone.getEditText().getText().toString());
                                setResult(200, intent);
                                finish();
                            }else {
                                /*mtvDialogName.setText(info.getInfo());
                                mivShow.setVisibility(View.VISIBLE);
                                mivShow.setBackground(getDrawable(R.drawable.ic_error));
                                mRegisterDialog.dismiss();*/
                                mRegisterDialog.dismiss();
                                Snackbar.make(mtxtInPhone, info.getInfo(), Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }, new HttpUtil.Param[]{

                            //{"phoneNum":"13770936421","identifyCode":"5565","password":"******"}
                            new HttpUtil.Param("phoneNum", mtxtInPhone.getEditText().getText().toString()),
                            new HttpUtil.Param("identifyCode", mtxtInCode.getEditText().getText().toString()),
                            new HttpUtil.Param("password", mtxtInAgainPwd.getEditText().getText().toString())

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
        mtvDialogName.setText("注册中...");

        mRegisterDialog.setContentView(view);
        mRegisterDialog.show();
    }

    private void getCode(){
        if (mCurSecond != 0){
            return;
        }

        //先判断再计时、网络请求
        if (mtxtInPhone.getEditText().getText() == null || !RegexUtil.isMobileNumber(mtxtInPhone.getEditText().getText().toString())){
            mtxtInPhone.setError("请输入正确的手机号！");
            Snackbar.make(mtxtInPhone, "请先输入正确的手机号！", Snackbar.LENGTH_LONG).show();
            return;
        }

        startTimer();
        HttpUtil.postAsyn(Constants.BASE_URL + "sms/register.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mbtnRegister, "发送失败！请检查您的网络", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                mInfo = mGson.fromJson(response, ResultInfo.class);
                if (mInfo.getCode().equals("400")){
                    Snackbar.make(mbtnRegister, mInfo.getInfo(), Snackbar.LENGTH_LONG).show();
                }
            }
        },new HttpUtil.Param[]{
                new HttpUtil.Param("phoneNum", mtxtInPhone.getEditText().getText().toString())
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

    public void OnSmsTask() {
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
