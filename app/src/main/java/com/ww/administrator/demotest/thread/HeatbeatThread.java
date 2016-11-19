package com.ww.administrator.demotest.thread;

import android.app.Activity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/14.
 */
public class HeatbeatThread extends Thread {

    private ImageView iv;
    private Activity activity;
    public HeatbeatThread() {
    }

    public void setImageView(ImageView iv){
        this.iv = iv;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }


    @Override
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        while (true) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    playHeartbeatAnimation(iv);
                }
            });

        }

    }

    private void playHeartbeatAnimation(final ImageView iv) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(0.3f, 1.0f));
        animationSet.setDuration(900);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.3f, 1.0f, 1.3f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(1.0f, 0.7f));
                animationSet.setDuration(1000);
                animationSet.setInterpolator(new LinearInterpolator());
                animationSet.setFillAfter(true);
                // 实现跳动的View
                iv.startAnimation(animationSet);
            }
        });
        // 实现跳动的View
        iv.startAnimation(animationSet);
    }
}
