package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;

import com.ww.administrator.demotest.adapter.AboutPagerAdapter;
import com.ww.administrator.demotest.widget.ElasticDragDismissFrameLayout;
import com.ww.administrator.demotest.widget.InkPageIndicator;

/**
 * Created by Administrator on 2016/9/19.
 */
public class AboutActivity extends AppCompatActivity {

    ElasticDragDismissFrameLayout draggableFrame;
    ViewPager pager;
    InkPageIndicator pageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_layout);
        initViews();

        pager.setAdapter(new AboutPagerAdapter(this));
        pager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.spacing_normal));
        pageIndicator.setViewPager(pager);

        draggableFrame.addListener(
                new ElasticDragDismissFrameLayout.SystemChromeFader(getWindow()) {
                    @Override
                    public void onDragDismissed() {
                        // if we drag dismiss downward then the default reversal of the enter
                        // transition would slide content upward which looks weird. So reverse it.
                        if (draggableFrame.getTranslationY() > 0) {
                            getWindow().setReturnTransition(
                                    TransitionInflater.from(AboutActivity.this)
                                            .inflateTransition(R.transition.about_return_downward));
                        }
                        finishAfterTransition();
                    }
                });
    }

   private void initViews(){
        draggableFrame = (ElasticDragDismissFrameLayout) findViewById(R.id.draggable_frame);
        pager = (ViewPager) findViewById(R.id.vp_about);
        pageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
    }



}
