package com.ww.administrator.demotest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.ww.administrator.demotest.adapter.ProductListAdapter;
import com.ww.administrator.demotest.model.ProductListInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

/**
 * Created by Administrator on 2016/8/27.
 */
public class ProductListActivity extends AppCompatActivity {

    private String mIsRecom;
    private String mKeyName;

    ProductListInfo mList;

    ProductListAdapter mAdapter;

    Gson mGson = new Gson();

    RecyclerView mRvProList;
    Toolbar mtbSearch;
    SwipeRefreshLayout mSrlProList;
    Toolbar mTbPro;
    ProgressWheel mpbPro;
    FloatingActionButton mFabPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        initParams();
        initViews();
        loadDatas();
        refreshDatas();
    }

    private void initParams(){
        mIsRecom = getIntent().getStringExtra("isRecom");
        mKeyName = getIntent().getStringExtra("keyName");
        if (mKeyName == null){
            mKeyName = "";
        }
    }

    private void setToolbar(){
        mtbSearch = (Toolbar) findViewById(R.id.tb_common);
        mtbSearch.setTitle("商品列表");
        mtbSearch.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(){
        mRvProList = (RecyclerView) findViewById(R.id.rv_productlist);
        mSrlProList = (SwipeRefreshLayout) findViewById(R.id.srl_productlist);
        mTbPro = (Toolbar) findViewById(R.id.tb_common);
        mpbPro = (ProgressWheel) findViewById(R.id.pb_common);
        mFabPro = (FloatingActionButton) findViewById(R.id.fab_pro);
        setToolbar();
        mSrlProList.setVisibility(View.GONE);
        mFabPro.setVisibility(View.GONE);
        mpbPro.setVisibility(View.VISIBLE);
        mSrlProList.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);

        mFabPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this, SearchActivity.class);

                View sharedView = mFabPro;
                String transitionName = "transview";
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(ProductListActivity.this, sharedView, transitionName);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });

    }


    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "product_list.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                mSrlProList.setVisibility(View.VISIBLE);
                mFabPro.setVisibility(View.VISIBLE);
                mpbPro.setVisibility(View.GONE);
                System.out.println("=======================");
                System.out.println(response.toString());
                System.out.println("=======================");
                mList = mGson.fromJson(response, ProductListInfo.class);
                mAdapter = new ProductListAdapter(ProductListActivity.this, mList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProductListActivity.this, LinearLayoutManager.VERTICAL, false);
                mRvProList.setLayoutManager(layoutManager);
                mRvProList.setAdapter(mAdapter);
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", mIsRecom),
                new HttpUtil.Param("key", mKeyName)
        });
    }

    public void refreshDatas(){
        mSrlProList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
                mSrlProList.setRefreshing(false);
            }
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