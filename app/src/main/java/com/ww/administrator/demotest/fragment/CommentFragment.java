package com.ww.administrator.demotest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ww.administrator.demotest.AutoHeightListView;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.DetailCommentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CommentFragment extends Fragment {

    AutoHeightListView mLvComment;
    View mHeadView;
    DetailCommentAdapter mAdapter;
    List<String> mList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, null);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        mLvComment = (AutoHeightListView) view.findViewById(R.id.lv_comment);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.comment_head_view, null);
        mLvComment.addHeaderView(mHeadView);
        mList.add("1");
        mList.add("2");

        mAdapter = new DetailCommentAdapter(getActivity(), mList);

        mLvComment.setAdapter(mAdapter);

    }
}
