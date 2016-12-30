package com.ww.administrator.demotest;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.adapter.ProductListAdapter1;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.model.ProductListInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/12/19.
 */
public class ProductListActivity1 extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnMoreListener{

    SuperRecyclerView mRecyclerview;
    Toolbar mtbSearch;
    private String mIsRecom;
    private String mKeyName;
    private String mclassId = "";

    int page = 1;
    int pageSize = 10;

    ProductListInfo listData;
    ProgressWheel mpbPro;

    ProductListAdapter1 mAdapter;

    Gson mGson = new Gson();
    String city = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist1_layout);
        initParams();
        initViews();
        if (mclassId.equals("")){
            loadDatas(true);
        }else {
            loadDatas(mclassId);
        }
    }


    private void initParams(){
        mIsRecom = getIntent().getStringExtra("isRecom");
        mKeyName = getIntent().getStringExtra("keyName");
        mclassId = getIntent().getStringExtra("classId");
        city = (String) SharedPreUtil.getData(this, "locatCity", "南京");
        if (mclassId == null){
            mclassId = "";
        }
        if (mKeyName == null){
            mKeyName = "";
        }

    }

    private void initViews(){
        mRecyclerview = (SuperRecyclerView) findViewById(R.id.rv_productlist);
        mtbSearch = (Toolbar) findViewById(R.id.tb_common);
        mpbPro = (ProgressWheel) findViewById(R.id.pb_common);
        setToolbar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductListActivity1.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecyclerview.setRefreshListener(this);
        mRecyclerview.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecyclerview.setupMoreListener(this, 1);
        //mRecyclerview.setOnMoreListener(ProductListActivity1.this);

    }
    private void setToolbar(){
        mtbSearch = (Toolbar) findViewById(R.id.tb_common);
        mtbSearch.setTitle("商品列表");
        mtbSearch.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void loadDatas(final boolean fresh){
        if (fresh){
            page = 1;
        }else {
            page ++;
        }

        HttpUtil.postAsyn(Constants.BASE_URL + "product_list_page.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                Log.d("ww", response.toString());
                mpbPro.setVisibility(View.GONE);
                try{
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")){

                        ProductListInfo result = mGson.fromJson(response, ProductListInfo.class);
                        if (fresh) {
                            listData = result;
                            mAdapter = new ProductListAdapter1(ProductListActivity1.this, listData);

                            mRecyclerview.setAdapter(mAdapter);
                            mRecyclerview.setRefreshing(false);
                        } else {//上拉加载
                            if (result.getCode().equals("401")) {
                                Snackbar.make(mRecyclerview, "已是最后一条!", Snackbar.LENGTH_SHORT)
                                        .show();
                            } else {
                                mRecyclerview.setLoadingMore(true);
                                listData.getData().addAll(result.getData());
                                mAdapter.refresh(listData);
                            }

                        }


                    }else if(strCode.equals("401")) {

                        mRecyclerview.hideMoreProgress();
                        mRecyclerview.setLoadingMore(false);
                        Snackbar.make(mRecyclerview, "已是最后一条!", Snackbar.LENGTH_SHORT)
                                .show();

                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", mIsRecom),
                new HttpUtil.Param("key", mKeyName),
                new HttpUtil.Param("city", city),
                new HttpUtil.Param("page", page + ""),
                new HttpUtil.Param("pageSize", pageSize + "")
        });

    }
    private void loadDatas(String classId){

    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        loadDatas(false);
    }

    @Override
    public void onRefresh() {
        loadDatas(true);
    }
}
