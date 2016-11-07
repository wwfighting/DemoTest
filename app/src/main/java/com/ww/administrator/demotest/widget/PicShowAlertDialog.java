package com.ww.administrator.demotest.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.ww.administrator.demotest.R;

/**
 * Created by Administrator on 2016/11/7.
 */
public class PicShowAlertDialog extends Dialog {
    private static int mTheme = R.style.NobackDialog;

    private Info mInfo;
    private Bitmap mBitmap;

    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    public PicShowAlertDialog(Context context, Info info, Bitmap bitmap) {
        super(context, mTheme);
        mInfo = info;
        mBitmap = bitmap;

    }

    public PicShowAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_showpic);

        final PhotoView pvShow = (PhotoView) findViewById(R.id.pv_show);
        pvShow.setImageBitmap(mBitmap);
        pvShow.startAnimation(in);
        pvShow.enable();
        pvShow.animaFrom(mInfo);

        pvShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvShow.startAnimation(out);
                pvShow.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        pvShow.setVisibility(View.GONE);
                        onBackPressed();
                    }
                });
            }
        });
    }

}
