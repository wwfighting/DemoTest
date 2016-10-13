package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ww.administrator.demotest.MainActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;

import java.security.InvalidParameterException;

/**
 * Created by Administrator on 2016/10/12.
 */
public class GuideAdapter extends PagerAdapter {


    private View guide1, guide2, guide3, guide4;

    private LayoutInflater mInflater;
    AppCompatSpinner mspCity;
    Button mbtnGo;
    String mslectedCity = "";
    Context mContext;

    public GuideAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View layout = getPage(position, container);
        container.addView(layout);
        return layout;
    }

    private View getPage(int position, final ViewGroup parent) {
        switch (position) {
            case 0:
                if (guide1 == null){
                    guide1 = mInflater.inflate(R.layout.guide1_layout, parent, false);
                }
                return guide1;
            case 1:
                if (guide2 == null){
                    guide2 = mInflater.inflate(R.layout.guide2_layout, parent, false);
                }
                return guide2;
            case 2:
                if (guide3 == null){
                    guide3 = mInflater.inflate(R.layout.guide3_layout, parent, false);

                }
                return guide3;
            case 3:
                if (guide4 == null){
                    guide4 = mInflater.inflate(R.layout.guide4_layout, parent, false);
                    mspCity = (AppCompatSpinner) guide4.findViewById(R.id.sp_city);
                    mslectedCity = mspCity.getSelectedItem().toString();
                    mbtnGo = (Button) guide4.findViewById(R.id.btn_go);
                    mbtnGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mslectedCity.equals("")){
                                Toast.makeText(mContext, "请选择城市", Toast.LENGTH_SHORT).show();
                            }else {
                                SharedPreUtil.saveData(mContext, "locatCity", mslectedCity);
                                mContext.startActivity(new Intent(mContext, MainActivity.class));
                                ((Activity) mContext).finish();
                            }
                        }
                    });
                }
                return guide4;
        }
        throw new InvalidParameterException();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
