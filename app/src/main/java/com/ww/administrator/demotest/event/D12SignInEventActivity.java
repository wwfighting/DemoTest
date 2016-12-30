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
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/11/23.
 */
public class D12SignInEventActivity extends AppCompatActivity {

    RelativeLayout mrlBg;
    ImageView mivOff;
    MyApp mApp;
    private String uid = "-1";
    MaterialDialog mDialog;
    Button mbtnSign;

    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d12signin_layout);
        mApp = (MyApp) getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }
        initViews();

    }

    private void initViews(){
        mbtnSign = (Button) findViewById(R.id.btn_event_sign);
        mrlBg = (RelativeLayout) findViewById(R.id.rl_bg);
        mrlBg.getBackground().setAlpha(190);
        mivOff = (ImageView) findViewById(R.id.iv_event_off);
        mivOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initDialog();
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

    private void initDialog() {
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(D12SignInEventActivity.this, LoginActivity.class);
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

    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "d12_signin.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                System.out.println("======连续签到======");
                System.out.println(response.toString());
                System.out.println("============");
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    Toast.makeText(D12SignInEventActivity.this, info.getInfo(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    Toast.makeText(D12SignInEventActivity.this, info.getInfo(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid)
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            uid = ((MyApp) getApplicationContext()).getUser().getId();
            Toast.makeText(D12SignInEventActivity.this, "登录成功，请点击签到！", Toast.LENGTH_SHORT).show();
        }
    }


}
