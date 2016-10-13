package com.ww.administrator.demotest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.GlideUtil;

/**
 * Created by Administrator on 2016/8/31.
 */
public class TestActivity extends AppCompatActivity {

    Button mbtn;
    ImageView mIv1, mIv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        mbtn = (Button) findViewById(R.id.btn_test);
        mIv1 = (ImageView) findViewById(R.id.iv_test1);
        mIv2 = (ImageView) findViewById(R.id.iv_test2);
        loadImage();
    }

    private void loadImage(){
        Glide.with(this) .load(Constants.BASE_IMG_URL + getIntent().getStringExtra("imgurl"))
            .listener(shotLoadListener)
            .centerCrop()
            .crossFade()
            .into(mIv1);

        Glide.with(this) .load(Constants.BASE_IMG_URL + getIntent().getStringExtra("imgurl"))
                .listener(shotLoadListener)
                .centerCrop()
                .crossFade()
                .into(mIv2);

        mIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv1.setBackground(getResources().getDrawable(R.drawable.iv_background_shape));
            }
        });

        mIv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIv2.setBackground(getResources().getDrawable(R.drawable.iv_background_shape));
            }
        });
    }

    private RequestListener shotLoadListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target,
                                   boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model,
                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                       boolean isFirstResource) {
            final Bitmap bitmap = GlideUtil.getBitmap(resource);
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.getWidth() - 1, bitmap.getHeight() - 1)
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            //mbtn.setBackgroundColor(ColorUtil.getImageColor(bitmap));
                        }
                    });
            return false;
        }
    };
}
