package com.ww.administrator.demotest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.ww.administrator.demotest.AutoHeightListView;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.DetailImgAdapter;
import com.ww.administrator.demotest.model.GoodsDetailInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/30.
 */
public class DetailFragment extends Fragment {

    AutoHeightListView mLvDetail;
    ProgressWheel mPbDetail ;
    List<String> mList = new ArrayList<>();
    DetailImgAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        mLvDetail = (AutoHeightListView) view.findViewById(R.id.lv_detail);
        mPbDetail = (ProgressWheel)  view.findViewById(R.id.pb_common);
        mLvDetail.setVisibility(View.GONE);
        mPbDetail.setVisibility(View.VISIBLE);

        ((DetailActivity)getActivity()).setResultCallBack(new DetailActivity.ResultCallBack() {
            @Override
            public void getData(GoodsDetailInfo info) {
                if (info != null){
                    mPbDetail.setVisibility(View.GONE);
                    mLvDetail.setVisibility(View.VISIBLE);
                    String[] str = info.getData().getDetail().getGoodsdesc().split(">");
                    for (int i = 0; i < str.length; i++){
                        Pattern p = Pattern.compile("\"([^\"]*)\"");
                        Matcher m = p.matcher(str[i]);
                        while (m.find()) {
                            mList.add(m.group(1).toString());
                        }
                    }
                    mAdapter = new DetailImgAdapter(getActivity(), mList);
                    mLvDetail.setAdapter(mAdapter);
                }

            }
        });
    }


}
