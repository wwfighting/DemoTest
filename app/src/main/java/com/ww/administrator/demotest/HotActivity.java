package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.adapter.HotActyAdapter;
import com.ww.administrator.demotest.model.ActyInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

/**
 * Created by Administrator on 2016/9/14.
 */
public class HotActivity extends AppCompatActivity {


    SwipeRefreshLayout msrlActy;
    RecyclerView mrvActy;
    Toolbar mtbActy;
    ProgressWheel mpbShow;
    HotActyAdapter mAdapter;
    Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        initViews();
        loadDatas();
        refreshDatas();

    }

    private void initViews() {
        msrlActy = (SwipeRefreshLayout) findViewById(R.id.srl_activity);
        mrvActy = (RecyclerView) findViewById(R.id.rv_activity);
        mtbActy = (Toolbar) findViewById(R.id.tb_common);
        mpbShow = (ProgressWheel) findViewById(R.id.pb_common);
        mpbShow.setVisibility(View.VISIBLE);
        msrlActy.setVisibility(View.GONE);
        msrlActy.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);
        mtbActy.setTitle("活动中心");
        mtbActy.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbActy);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void refreshDatas(){
        msrlActy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
                msrlActy.setRefreshing(false);
            }
        });
    }
    private void loadDatas() {
        HttpUtil.postAsyn(Constants.BASE_URL + "get_activity_list.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mrvActy, "请先检查您的网络！", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                msrlActy.setVisibility(View.VISIBLE);
                ActyInfo info = mGson.fromJson(response, ActyInfo.class);
                if (info.getCode().equals("200")){
                    //获取数据成功
                    mpbShow.setVisibility(View.GONE);
                    LinearLayoutManager manager = new LinearLayoutManager(HotActivity.this, LinearLayoutManager.VERTICAL, false);
                    mrvActy.setLayoutManager(manager);
                    mAdapter = new HotActyAdapter(HotActivity.this, info);
                    mrvActy.setAdapter(mAdapter);
                }else {
                    //获取数据失败
                    Snackbar.make(mrvActy, info.getInfo(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("action", "activity")
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
