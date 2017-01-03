package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.model.User;
import com.ww.administrator.demotest.model.UserLogin;
import com.ww.administrator.demotest.util.ButtonUtil;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.RegexUtil;

import org.json.JSONException;
import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/13.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar mtbLogin;
    AppCompatButton mbtnLogin, mbtnRegister;
    AppCompatCheckBox mchbRemName;
    AppCompatTextView mtvForgetPwd;
    TextInputLayout mtxtInName, mtxtInPwd;

    TextView mtvDialogName;

    ProgressWheel mpgShow;
    ImageView mivShow;

    MaterialDialog mLoginDialog;
    Gson mGson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initViews();
        initPre();
        initEvents();
        initDialog();
    }

    private void initPre() {

        if (SharedPreUtil.getData(LoginActivity.this,"isRemName", false) != null
                && SharedPreUtil.getData(LoginActivity.this,"userName", "jvawa") != null){
            if ((boolean)SharedPreUtil.getData(LoginActivity.this,"isRemName", false)){
                mchbRemName.setChecked(true);
                mtxtInName.getEditText().setText((String)SharedPreUtil.getData(LoginActivity.this,"userName", "jvawa"));
            }else {
                mchbRemName.setChecked(false);
            }
        }

    }

    private void initViews() {
        mtbLogin = (Toolbar) findViewById(R.id.tb_common);
        mtbLogin.setTitle("登 录");
        setSupportActionBar(mtbLogin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mbtnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        mbtnRegister = (AppCompatButton) findViewById(R.id.btn_toreg);
        mchbRemName = (AppCompatCheckBox) findViewById(R.id.chb_remname);
        mtvForgetPwd = (AppCompatTextView) findViewById(R.id.txt_forgetpwd);
        mtxtInName = (TextInputLayout) findViewById(R.id.txtinput_username);
        mtxtInPwd = (TextInputLayout) findViewById(R.id.txtinput_password);
    }

    private void initEvents(){
        mbtnLogin.setOnClickListener(this);
        mbtnRegister.setOnClickListener(this);
        mtvForgetPwd.setOnClickListener(this);
        /*mchbRemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemName(isChecked);
            }
        });*/
    }

    private void isRemName(boolean b){
        if (b){
            SharedPreUtil.saveData(LoginActivity.this, "isRemName", true);
            if (!mtxtInName.getEditText().getText().equals(null)){
                SharedPreUtil.saveData(LoginActivity.this, "userName", mtxtInName.getEditText().getText().toString());
            }

        }else {
            SharedPreUtil.saveData(LoginActivity.this, "isRemName", false);

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                //登录
                if (!ButtonUtil.isFastDoubleClick(R.id.btn_login)) {
                    if (mtxtInName.getEditText().getText() == null ||
                            !RegexUtil.isMobileNumber(mtxtInName.getEditText().getText().toString())) {
                        mtxtInName.setError("请输入正确的手机号！");
                        return;
                    }

                    if (mtxtInPwd.getEditText().getText() == null ||
                            !RegexUtil.isPwd(mtxtInPwd.getEditText().getText().toString())) {
                        mtxtInPwd.setError("密码有误！");
                        return;
                    }

                    //是否记住用户名
                    isRemName(mchbRemName.isChecked());

                    mLoginDialog.show();
                    HttpUtil.postAsyn(Constants.BASE_URL + "login.php", new HttpUtil.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            mLoginDialog.dismiss();
                            Snackbar.make(mbtnRegister, "登录失败！请检查您的网络", Snackbar.LENGTH_LONG).show();
                        }
                        @Override
                        public void onResponse(String response) {
                            mLoginDialog.dismiss();
                            try{
                                JSONObject jsonRoot = new JSONObject(response);
                                String strCode = jsonRoot.getString("code");
                                if (strCode.equals("200")){
                                    UserLogin user = mGson.fromJson(response, UserLogin.class);
                                    //登录成功
                                    loginSuccess(user);
                                }else {
                                    //登录有误
                                    Snackbar.make(mbtnRegister, jsonRoot.getString("info"), Snackbar.LENGTH_LONG).show();
                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        }
                    }, new HttpUtil.Param[]{
                            new HttpUtil.Param("username",mtxtInName.getEditText().getText().toString()),
                            new HttpUtil.Param("password",mtxtInPwd.getEditText().getText().toString())
                    });
                }
                break;

            case R.id.btn_toreg:
                Intent toreg = new Intent();
                toreg.setClass(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(toreg, 100);
                break;

            case R.id.txt_forgetpwd:
                Intent forgetpwd = new Intent();
                forgetpwd.setClass(LoginActivity.this, ForgetPwdActivity.class);
                startActivityForResult(forgetpwd, 100);

                break;
        }

    }

    /**
     * 登录成功
     */
    private void loginSuccess(UserLogin login){
        User user = new User();

        user.setId(login.getData().getId());
        user.setUsername(login.getData().getUsername());
        user.setNickname(login.getData().getNickname());
        user.setPassword(login.getData().getPassword());
        user.setPhone(login.getData().getPhone());
        user.setWx_openid(login.getData().getWx_openid());
        user.setStatus(login.getData().getStatus());
        user.setCreatetime(login.getData().getCreatetime());
        user.setHeadImg(login.getData().getHeadImg());
        user.setArea(login.getData().getArea());
        user.setCredit(login.getData().getCredit());
        user.setLasttime(login.getData().getLasttime());
        user.setNum(login.getData().getNum());
        user.setAuth_key(login.getData().getAuth_key());
        user.setUpdated_at(login.getData().getUpdated_at());
        user.setEmail(login.getData().getEmail());

        MyApp app = (MyApp) getApplicationContext();
        app.setUser(user);

        SharedPreUtil.saveData(LoginActivity.this, "userName", user.getPhone());
        SharedPreUtil.saveData(LoginActivity.this, "passWord", user.getPassword());

        mLoginDialog.dismiss();
        app.SetActivityIntent(Constants.SHOPPING_CART_REFRESH, 1);
        app.SetActivityIntent(Constants.MY_REFRESH, 10);
        setResult(200);
        finish();

    }

    /**
     * 初始化Dialog
     */
    private void initDialog(){
        mLoginDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_common, null);
        mtvDialogName = (TextView) view.findViewById(R.id.tv_dialog_name_show);
        mpgShow = (ProgressWheel) view.findViewById(R.id.progress_image);
        mivShow = (ImageView) view.findViewById(R.id.iv_dialog_result);
        mpgShow.setVisibility(View.VISIBLE);
        mivShow.setVisibility(View.GONE);
        mtvDialogName.setText("登录中...");
        mLoginDialog.setContentView(view);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            mtxtInName.getEditText().setText(data.getStringExtra("phoneNum"));
        }
    }
}
