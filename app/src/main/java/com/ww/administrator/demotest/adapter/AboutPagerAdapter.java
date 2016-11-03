package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ww.administrator.demotest.IntroduceJvawa;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.util.ToolsUtil;

import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2016/9/20.
 */
public class AboutPagerAdapter extends PagerAdapter {

    private View abouJvawa, aboutIcon, aboutLibs;

    private RecyclerView mrvLib;

    private final LayoutInflater layoutInflater;
    LibraryAdapter mLibAdapter;
    public AboutPagerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View layout = getPage(position, collection);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private View getPage(int position, final ViewGroup parent) {
        switch (position) {
            case 0:
                if (abouJvawa == null){
                    if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                        abouJvawa = layoutInflater.inflate(R.layout.about4_jvawa_layout, parent, false);
                    } else {

                        abouJvawa = layoutInflater.inflate(R.layout.about_jvawa_layout, parent, false);
                    }

                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parent.getContext().startActivity(new Intent(parent.getContext(), IntroduceJvawa.class));
                        }
                    };
                    abouJvawa.findViewById(R.id.cv_intro).setOnClickListener(listener);
                    abouJvawa.findViewById(R.id.cv_in).setOnClickListener(listener);
                    abouJvawa.findViewById(R.id.cv_copyright).setOnClickListener(listener);
                    abouJvawa.findViewById(R.id.tv_intro).setOnClickListener(listener);
                    abouJvawa.findViewById(R.id.tv_in).setOnClickListener(listener);
                    abouJvawa.findViewById(R.id.tv_copyright).setOnClickListener(listener);


                }
                return abouJvawa;
            case 1:
                if (aboutIcon == null){
                    aboutIcon = layoutInflater.inflate(R.layout.about_icon_layout, parent, false);
                }
                return aboutIcon;
            case 2:
                if (aboutLibs == null){
                    aboutLibs = layoutInflater.inflate(R.layout.about_libs_layout, parent, false);
                    mrvLib = (RecyclerView) aboutLibs.findViewById(R.id.rv_lib);
                    mLibAdapter = new LibraryAdapter(parent.getContext());
                    mrvLib.setAdapter(mLibAdapter);

                }
                return aboutLibs;
        }
        throw new InvalidParameterException();
    }
}
