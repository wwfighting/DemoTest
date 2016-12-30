package com.ww.administrator.demotest.event;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.D12HomeAdapter;

/**
 * Created by Administrator on 2016/11/22.
 */
public class D12HomeActivity extends AppCompatActivity {

    Toolbar mtb;
    RecyclerView mrvShow;
    D12HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d12home_layout);
        initViews();
        loadDatas();
    }

    private void initViews(){
        mrvShow = (RecyclerView) findViewById(R.id.rv_d12_show);
        mtb = (Toolbar) findViewById(R.id.tb_common);
        mtb.setTitle("圣诞特惠主会场");
        setSupportActionBar(mtb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(D12HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        mrvShow.setLayoutManager(layoutManager);

    }

    private void loadDatas(){
        mAdapter = new D12HomeAdapter(D12HomeActivity.this);
        mrvShow.setAdapter(mAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
