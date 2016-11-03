package com.ww.administrator.demotest.event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.LoginActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/11/2.
 */
public class SignInEventActivity extends AppCompatActivity {


    private Button mbtnSign;
    MyApp mApp;
    private String uid = "-1";

    MaterialDialog mDialog;

    private Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_event_layout);
        mApp = (MyApp) getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }
        initViews();
        doBussiness();
    }

    private void initViews() {
        mbtnSign = (Button) findViewById(R.id.btn_event_sign);
        initDialog();
    }
    private void initDialog() {
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(SignInEventActivity.this, LoginActivity.class);
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


    private void doBussiness(){
        mbtnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断有没有登录
                if (uid.equals("-1")) {
                    mDialog.show();
                } else {
                    loadDatas();
                }
            }
        });
    }

    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "signin_event.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SignInEventActivity.this, "请检查您的网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    Toast.makeText(SignInEventActivity.this, "签到成功！可以去 '我的礼券' 查看", Toast.LENGTH_SHORT).show();
                    SignInEventActivity.this.finish();
                }else if (info.getCode().equals("401")){
                    Toast.makeText(SignInEventActivity.this, "您已抢购过！可以去 '我的礼券' 查看", Toast.LENGTH_SHORT).show();
                    SignInEventActivity.this.finish();
                }else {
                    Toast.makeText(SignInEventActivity.this, info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsId", "3506")

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 200:
                uid = ((MyApp)getApplicationContext()).getUser().getId();
                break;
        }
    }
}
