package com.ww.administrator.demotest.weclome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.MainActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/11.
 */
public class WelStartActivity extends AppCompatActivity {

    ImageView mivWel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_layout);
        initViews();
        hideStatusBar();
        showGIF();
        Timer timer = new Timer();
        timer.schedule(new WelTask(), 2000);

    }

    private void initViews(){
        mivWel= (ImageView) findViewById(R.id.iv_welcome);
    }


    /**
     * 隐藏状态栏
     */
    private void hideStatusBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void showGIF(){
        //使用Glide加载gif图片
        Glide.with(WelStartActivity.this)
                .load("file:///android_asset/gif/loading0.gif")
                .crossFade()
                .into(mivWel);
    }

    class WelTask extends TimerTask {
        @Override
        public void run() {
           if ((Boolean) SharedPreUtil.getData(WelStartActivity.this, "isFirst", false)){
                startActivity(new Intent(WelStartActivity.this, MainActivity.class));
                finish();
            }else {
                SharedPreUtil.saveData(WelStartActivity.this, "isFirst", true);
                startActivity(new Intent(WelStartActivity.this, WelGuideActivity.class));
                finish();

            }

        }
    }
}
