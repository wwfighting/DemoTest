package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.pay.PayActivity;
import com.ww.administrator.demotest.util.Constants;

/**
 * Created by Administrator on 2016/10/28.
 */
public class PayCarActivity extends AppCompatActivity{

    private TextView mtvOrderNo, mtvOrderTime;

    private ImageView mivShow;
    private TextView mtvGoodsName,mtvOrderMoney;
    private FloatingActionButton mfabCommit;
    Toolbar mtbCommit;

    String title = "";
    int ordNum;
    int payMoney;
    String imgurl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paycar_layout);
        initParams();
        initViews();

    }

    /**
     * 初始化参数
     */
    private void initParams(){
        title = getIntent().getExtras().getString("title");
        ordNum = getIntent().getExtras().getInt("ordNum");
        payMoney = getIntent().getExtras().getInt("payMoney");
        imgurl = getIntent().getExtras().getString("imgurl");
    }

    /**
     * 初始化视图
     */
    private void initViews(){
        mtvOrderNo = (TextView) findViewById(R.id.tv_commit_order_num);
        mtvOrderTime = (TextView) findViewById(R.id.tv_commit_order_time);
        mtvGoodsName = (TextView) findViewById(R.id.tv_commit_goods_name);
        mtvOrderMoney = (TextView) findViewById(R.id.tv_commit_goods_order_money);
        mfabCommit = (FloatingActionButton) findViewById(R.id.fab_commit);
        mivShow = (ImageView) findViewById(R.id.iv_commit_goods_show);
        mtbCommit = (Toolbar) findViewById(R.id.tb_commit);
        setSupportActionBar(mtbCommit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String picurl = (imgurl.split(";"))[0];
        Glide.with(this)
                .load(Constants.BASE_IMG_URL + picurl)
                .crossFade()
                .into(mivShow);
        mtvOrderNo.setText("订单号：" + ordNum);

        mtvGoodsName.setText(title);
        mtvOrderMoney.setText("￥" + payMoney / 100);

        mfabCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApp) getApplicationContext()).SetActivityIntent(Constants.ADD_SHOPPING_CART_REFRESH, 1);
                Intent intent = new Intent(PayCarActivity.this, PayActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("ordNum", ordNum);
                intent.putExtra("payMoney", payMoney);
                startActivityForResult(intent, 100);
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((MyApp) getApplicationContext()).SetActivityIntent(Constants.ADD_SHOPPING_CART_REFRESH, 1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
