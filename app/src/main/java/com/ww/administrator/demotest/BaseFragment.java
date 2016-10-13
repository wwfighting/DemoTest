package com.ww.administrator.demotest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/7/19.
 */
public abstract class BaseFragment extends Fragment {

    //日志输出标识
    protected final String TAG = this.getClass().getSimpleName();

    //设置Fragment的布局
    protected View mParentView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Fragment_onCreateView()");
        getArgs();
        mParentView = inflater.inflate(getLayout(), null);
        initViews(mParentView);
        doBusiness();
        return mParentView;
    }
    /**
     * 获得传递的参数
     */
    protected abstract void getArgs();

    /**
     * 返回Fragment布局
     * @return
     */
    protected abstract int getLayout();

    /**
     * 初始化视图
     * @param view
     */
    protected abstract void initViews(View view);


    /**
     * 业务逻辑处理
     */
    protected abstract void doBusiness();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Fragment_onAttach()");
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment_onCreate()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"Fragment_onActivityCreated()");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment_onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment_onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Fragment_onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment_onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment_onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Fragment_onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment_onDetach()");
    }
}
