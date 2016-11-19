package com.ww.administrator.demotest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.adapter.DetailColAdapter;
import com.ww.administrator.demotest.adapter.DetailTaiAdapter;
import com.ww.administrator.demotest.adapter.TabVPAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.fragment.CommentFragment;
import com.ww.administrator.demotest.fragment.DetailFragment;
import com.ww.administrator.demotest.model.GoodsDetailInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.DisplayUtil;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/8/29.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    public interface ResultCallBack{
        void getData(GoodsDetailInfo info);
    }

    public ResultCallBack mResultCallBack;

    public void setResultCallBack(ResultCallBack resultCallBack){
        this.mResultCallBack = resultCallBack;
    }

    private String uid = "-1";
    String mGid;
    NestedScrollView mNsvDetail;
    private List<Fragment> mFralist = new ArrayList<>();
    private String[] mTitle = {"商品详情","商品评价"};

    Toolbar mTbDetail;
    ConvenientBanner mBanner;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    TabVPAdapter mVpAdapter;
    TabLayout mTlDetail;
    AutoHeightViewPager mVpDetail;
    List<String> networkImages = new ArrayList<>();
    GoodsDetailInfo mInfo;
    Gson mGson = new Gson();
    String[] picurl;
    ProgressWheel mPwBanner;
    TextView mTvDetailprice, mTvDetailOrgMoney, mTvDetailOrderMoney,mTvDetailOrderCount,
            mTvDetailColorName, mTvDetailColorSelectedName, mTvDetailTaimianSelectedName, mTvDetailMode;

    LinearLayout llCountContainer;
    RecyclerView mRvColor, mRvTaimian;

    LinearLayoutManager llColorManager;
    LinearLayoutManager llTaimianManager;

    DetailColAdapter mColorAdapter;

    DetailTaiAdapter mTaimianAdapter;

    ImageView ivColorSelected;  //选中的colorView
    ImageView ivColorCurrent;   //当前的colorView

    int mColorCurrentPos = -1;  //当前的colorPos

    ImageView ivTaimianSelected;  //选中的taimianView
    ImageView ivTaimianCurrent;   //当前的taimianView

    int mTaimianCurrentPos = -1;  //当前的taimianPos
    DetailFragment detailFragment;

    FloatingActionButton mfabShop, mfabOrder;

    CoordinatorLayout mclContainer;

    CardView mcvColor, mcvTaimian;

    Bitmap mBitmap; //保存第一张图片加载的bitmap，用于设置购物车动画

    String strGoodsDetailInfo = "";   //将GoodsDetailInfo对象通过String跳转

    MyApp mApp;

    MaterialDialog mDialog;

    FloatingActionsMenu mfabMenu;

    Button mbtnMinus, mbtnPlus;
    TextView mtvNum;
    int orderNum = 1;

    float partMoney = 0;
    String mode = "";   //记录商品的类别
    int num = 1;    //记录商品的数量
    LinearLayout llMode;
    LinearLayout llBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mApp = (MyApp) getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }
        mGid = getIntent().getExtras().getString("gid");
        initViews();
        loadDatas();

    }

    private void initViews() {
        mTbDetail = (Toolbar) findViewById(R.id.tb_detail);
        mTlDetail = (TabLayout) findViewById(R.id.tl_detail);
        llMode = (LinearLayout) findViewById(R.id.ll_mode);
        llBanner = (LinearLayout) findViewById(R.id.ll_banner_container);
        mVpDetail = (AutoHeightViewPager) findViewById(R.id.vp_detail);
        mBanner = (ConvenientBanner) findViewById(R.id.cb_detail);

        mBanner.setLayoutParams(new CollapsingToolbarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(DetailActivity.this, 220)));
        mNsvDetail = (NestedScrollView) findViewById(R.id.nsv_detail);
        mPwBanner = (ProgressWheel) findViewById(R.id.pw_banner);
        mfabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        mfabShop = (FloatingActionButton) findViewById(R.id.fab_detail_shopping_cart);
        mfabOrder = (FloatingActionButton) findViewById(R.id.fab_detail_order);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_detail);
        mclContainer = (CoordinatorLayout) findViewById(R.id.cl_detail_container);
        mTvDetailprice = (TextView) findViewById(R.id.tv_detail_nowprice);
        mTvDetailOrgMoney = (TextView) findViewById(R.id.tv_detail_orgprice);
        mTvDetailOrderMoney = (TextView) findViewById(R.id.tv_detail_orderprice);
        mTvDetailOrderCount = (TextView) findViewById(R.id.tv_detail_ordercount);
        mTvDetailColorName = (TextView) findViewById(R.id.tv_detail_color_name);
        mRvColor = (RecyclerView) findViewById(R.id.rv_detail_color);
        mRvTaimian = (RecyclerView) findViewById(R.id.rv_detail_taimian);
        mTvDetailColorSelectedName = (TextView) findViewById(R.id.tv_detail_color_choose);
        mTvDetailTaimianSelectedName = (TextView) findViewById(R.id.tv_detail_taimian_choose);
        mTvDetailMode = (TextView) findViewById(R.id.tv_detail_spec);
        mcvColor = (CardView) findViewById(R.id.cv_color_container);
        mcvTaimian = (CardView) findViewById(R.id.cv_taimian_container);
        llCountContainer = (LinearLayout) findViewById(R.id.ll_count_container);
        mbtnMinus = (Button) findViewById(R.id.btn_detail_minus);
        mbtnPlus = (Button) findViewById(R.id.btn_detail_plus);
        mtvNum = (TextView) findViewById(R.id.tv_detail_buy_num);
        llCountContainer.setVisibility(View.GONE);
        mCollapsingToolbarLayout.setTitle("商品详情");
        setSupportActionBar(mTbDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();

        mfabShop.setOnClickListener(this);
        mfabOrder.setOnClickListener(this);
        mbtnMinus.setOnClickListener(this);
        mbtnPlus.setOnClickListener(this);

        initDialog();

    }

    private void initDialog() {
        mDialog = new MaterialDialog(this);
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        mDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        //点击对话框外，对话框消失
        mDialog.setCanceledOnTouchOutside(true);
    }

    private void initTab(){
        detailFragment = new DetailFragment();
        mFralist.add(detailFragment);
        mFralist.add(new CommentFragment());

        mVpAdapter = new TabVPAdapter(getSupportFragmentManager(), mTitle, mFralist);
        mVpDetail.setAdapter(mVpAdapter);
        mTlDetail.addTab(mTlDetail.newTab().setText(mTitle[0]));
        mTlDetail.addTab(mTlDetail.newTab().setText(mTitle[1]));
        mTlDetail.setupWithViewPager(mVpDetail);

    }


    private void loadDatas() {
        HttpUtil.postAsyn(Constants.BASE_URL + "product_detail.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                mInfo = mGson.fromJson(response, GoodsDetailInfo.class);
                if (mInfo.getCode().equals("200")) {

                    if (mInfo.getData().getDetail().getSubtitle().equals("全屋定制")) {
                        mode = "0";
                        mTvDetailprice.setVisibility(View.GONE);
                        mTvDetailOrgMoney.setVisibility(View.GONE);
                        mcvColor.setVisibility(View.GONE);
                        mcvTaimian.setVisibility(View.GONE);
                        llMode.setVisibility(View.GONE);
                    }
                    if (mInfo.getData().getDetail().getSubtitle().equals("全屋定制") || mInfo.getData().getDetail().getSubtitle().equals("")
                            || mInfo.getData().getDetail().getSubtitle().equals("双十一活动")) {
                        mode = "0";
                        mfabOrder.setTitle("一键预约");

                    } else if (mInfo.getData().getDetail().getSubtitle().equals("配件")) {
                        mode = "1";
                        mTvDetailOrderMoney.setVisibility(View.GONE);
                        llMode.setVisibility(View.GONE);
                        mTvDetailOrgMoney.setVisibility(View.GONE);
                        mfabOrder.setTitle("立即购买");
                        mTvDetailOrderCount.setVisibility(View.GONE);

                    }
                    strGoodsDetailInfo = response;
                    initBanner();
                    initMoney();
                    initSpecMode();
                    initColor();
                    initTaimian();
                    mResultCallBack.getData(mInfo);
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("gid", mGid)
        });
    }

    /**
     * 初始化头部banner
     */
    private void initBanner(){

        mCollapsingToolbarLayout.setTitle(mInfo.getData().getDetail().getGoodsname());
        mPwBanner.setVisibility(View.GONE);
        picurl = mInfo.getData().getDetail().getPicurl().split(";");

        for (int i = 0; i < picurl.length; i++){
            networkImages.add(Constants.BASE_IMG_URL + picurl[i]);
        }
        mBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.page_indicator_normal, R.drawable.page_indicator_focused});

        Glide.with(this)
                .load(networkImages.get(0))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(250, 250) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBitmap = resource;
                    }
                });

    }

    /**
     * 初始化 现金、原价、预约金、预约人数
     */

    private void initMoney(){
        mTvDetailprice.setText("现价：￥" + mInfo.getData().getDetail().getPrice());
        String orgPrice = "原价：￥" + mInfo.getData().getDetail().getPrice_old();
        String orderMoney = "预约金：" + mInfo.getData().getDetail().getOrdermoney();
        partMoney = Float.parseFloat(mInfo.getData().getDetail().getPrice());
        mTvDetailOrgMoney.setText(TextUtil.setStrSpan(orgPrice));
        if (mInfo.getData().getDetail().getSubtitle().equals("配件")){
            mTvDetailOrderMoney.setVisibility(View.VISIBLE);
            mTvDetailOrderMoney.setTextColor(Color.parseColor("#999999"));
            mTvDetailOrderMoney.setText(mInfo.getData().getDetail().getOrdercount() + "人预约");
        }

        if (mInfo.getData().getDetail().getSubtitle().equals("全屋定制")){
            mTvDetailOrderMoney.setTextColor(Color.parseColor("#999999"));
            mTvDetailOrderMoney.setText(mInfo.getData().getDetail().getOrdercount() + "人预约");
            mTvDetailprice.setVisibility(View.VISIBLE);
            mTvDetailOrderCount.setVisibility(View.GONE);

            mTvDetailprice.setText(orderMoney);
        }

    }

    private void initSpecMode(){

        if (mInfo.getData().getDetail().getSubtitle().equals("配件")){
            llCountContainer.setVisibility(View.GONE);
            mcvColor.setVisibility(View.GONE);
            mcvTaimian.setVisibility(View.GONE);
            mTvDetailMode.setText(mInfo.getData().getDetail().getTag());
        }

        if (mInfo.getData().getDetail().getSubtitle().equals("")){
            llCountContainer.setVisibility(View.GONE);
            mcvColor.setVisibility(View.VISIBLE);
            mcvTaimian.setVisibility(View.VISIBLE);
            mTvDetailMode.setText("3米地板 + 3米台面 + 1米吊柜");
        }
    }
    /**
     * 初始化颜色
     */
    private void initColor(){
        mRvColor.setNestedScrollingEnabled(false);
        final String colorName = (mInfo.getData().getDetail().getTag().split(";"))[0];
        if (colorName.length() > 12){
            mTvDetailColorName.setText(colorName.substring(0, 10) + "... ：");
        }else {
            mTvDetailColorName.setText(colorName + "：");
        }
        llColorManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvColor.setLayoutManager(llColorManager);
        mColorAdapter = new DetailColAdapter(this, mInfo);
        mRvColor.setAdapter(mColorAdapter);

        mColorAdapter.setOnItemClickListener(new DetailColAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ivColorSelected = (ImageView) view.findViewById(R.id.iv_detail_img);
                ivColorSelected.setBackground(getResources().getDrawable(R.drawable.iv_background_shape));
                if (mColorCurrentPos != position) {
                    if (ivColorCurrent != null) {
                        ivColorCurrent.setBackground(getResources().getDrawable(R.drawable.iv_background_shape_normal));
                    }
                }
                mColorCurrentPos = position;
                ivColorCurrent = ivColorSelected;
                mTvDetailColorSelectedName.setText(colorName + "/" + mInfo.getData().getColors().get(position).getCname());
            }
        });

    }

    /**
     * 初始化台面
     */
    private void initTaimian(){
        mRvTaimian.setNestedScrollingEnabled(false);
        llTaimianManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mRvTaimian.setLayoutManager(llTaimianManager);
        mTaimianAdapter = new DetailTaiAdapter(this, mInfo);
        mRvTaimian.setAdapter(mTaimianAdapter);

        mTaimianAdapter.setOnItemClickListener(new DetailTaiAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                ivTaimianSelected = (ImageView) view.findViewById(R.id.iv_detail_img);
                ivTaimianSelected.setBackground(getResources().getDrawable(R.drawable.iv_background_shape));
                if (mTaimianCurrentPos != position) {
                    if (ivTaimianCurrent != null) {
                        ivTaimianCurrent.setBackground(getResources().getDrawable(R.drawable.iv_background_shape_normal));
                    }
                }
                mTaimianCurrentPos = position;
                ivTaimianCurrent = ivTaimianSelected;
                mTvDetailTaimianSelectedName.setText(mInfo.getData().getTaimian().get(position).getTname());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            //加入购物车
            case R.id.fab_detail_shopping_cart:
                if (mApp.getUser() == null){
                    mDialog.show();
                }else {
                    addShoppingCart();
                    addCartAnimation(1);
                }

                break;

            //预约
            case R.id.fab_detail_order:

                if (mApp.getUser() == null){
                    mDialog.show();
                }else {

                    Intent intent = new Intent(DetailActivity.this, OrderActivity.class);

                    intent.putExtra("imgurl", networkImages.get(0));
                    intent.putExtra("gid", mGid);
                    intent.putExtra("response", strGoodsDetailInfo);
                    startActivity(intent);
                    if (mfabMenu.isExpanded()){
                        mfabMenu.collapse();
                    }
                }

                break;

            //数量减少
            case R.id.btn_detail_minus:
                if (orderNum > 1){
                    orderNum--;
                    mtvNum.setText(orderNum + "");
                    mTvDetailprice.setText("现价：￥" + orderNum * partMoney);
                }
                break;

            //数量增加
            case R.id.btn_detail_plus:
                orderNum++;
                mtvNum.setText(orderNum + "");
                mTvDetailprice.setText("现价：￥" + orderNum * partMoney);
                break;
        }
    }

    private void addShoppingCart(){
        HttpUtil.postAsyn(Constants.BASE_URL + "add_shoppingcart.php", new HttpUtil.ResultCallback<ResultInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mclContainer, "添加失败，请检查您的网络！", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ResultInfo info) {
                if (info.getCode().equals("200")){
                    ((MyApp)getApplicationContext()).SetActivityIntent(Constants.ADD_SHOPPING_CART_REFRESH, 1);
                    Snackbar.make(mclContainer, "添加成功！", Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(mclContainer, "添加失败！", Snackbar.LENGTH_SHORT).show();
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("gid", mGid),
                new HttpUtil.Param("action", "add"),
                new HttpUtil.Param("mode", mode),
                new HttpUtil.Param("num", num + "")
        });
    }

    /**
     * 添加到购物车的动画
     * @param time
     */
    private void addCartAnimation(final int time){

        //计算父控件的位置
        int[] parent = new int[2];
        mclContainer.getLocationInWindow(parent);

        //计算起点位置
        int[] startLocation = new int[2];
        mBanner.getLocationInWindow(startLocation);

        //计算终点位置
        int[] endLocation = new int[2];
        mfabShop.getLocationInWindow(endLocation);

        final ImageView iv = new ImageView(DetailActivity.this);


        if (mBitmap != null){
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mBitmap.getWidth(), mBitmap.getHeight());
            iv.setImageBitmap(mBitmap);
            //确定ImageView的位置与startView相同
            lp.leftMargin= startLocation[0] - parent[0] - mclContainer.getPaddingLeft();
            lp.topMargin= startLocation[1] - parent[1] - mclContainer.getPaddingTop();

            mclContainer.addView(iv, lp);

            //计算两者的横向X轴的距离差
            int XtoX = endLocation[0] - startLocation[0] + mfabShop.getWidth() / 2 - mBanner.getWidth() / 2;

            //根据距离 时间 获取到对应的X轴的初速度
            final float xv = XtoX / time;

            //计算两者的横向X轴的距离差
            int YtoY = endLocation[1] - startLocation[1];

            //根据距离 时间 初始设置的Y轴初速度与X轴初速度相同 获取到竖直方向上的加速度
            final float g;
            if(xv>0) {
                g = (YtoY + xv * time) / time / time *2;
            }else{
                g = (YtoY - xv * time) / time / time *2;
            }


            ValueAnimator va = new ValueAnimator();
            va.setDuration(time * 1000);
            va.setObjectValues(new PointF(0, 0));

            ObjectAnimator anim = ObjectAnimator
                    .ofFloat(iv, "ww", 1.0f, 0.0f)
                    .setDuration(1000);
            //anim.start();

            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float cVal = (Float) animation.getAnimatedValue();
                    iv.setAlpha(cVal);
                    iv.setScaleX(cVal);
                    iv.setScaleY(cVal);
                    iv.setRotation(360 * cVal);


                }
            });
            //计算位置
            va.setEvaluator(new TypeEvaluator<PointF>() {
                @Override
                public PointF evaluate(float v, PointF pointF, PointF t1) {
                    PointF point = new PointF();
                    point.x = v * xv * time;
                    if (xv > 0) {
                        point.y = g * (v * time) * (v * time) / 2 - xv * v * time;
                    } else {
                        point.y = g * (v * time) * (v * time) / 2 + xv * v * time;

                    }
                    return point;
                }
            });

            //va.start();

            //设置动画
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PointF point = (PointF) valueAnimator.getAnimatedValue();
                    iv.setTranslationX(point.x);
                    iv.setTranslationY(point.y);

                }
            });

            //在动画结束时去掉动画添加的ImageView
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mclContainer.removeView(iv);
                    mfabShop.setImageResource(R.drawable.ic_add_shopping_cart_24dp);

                    mfabMenu.setAnimation(setShakeAnimation(3));
                    mfabShop.setAnimation(setShakeAnimation(3));
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(1000);
            animSet.setInterpolator(new AccelerateInterpolator());
            //两个动画同时执行
            animSet.playTogether(anim, va);
            animSet.start();
        }

    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     *
     * @return
     */
    private Animation setShakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mBanner.startTurning(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        mBanner.stopTurning();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            uid = ((MyApp) getApplicationContext()).getUser().getId();
        }
    }
}
