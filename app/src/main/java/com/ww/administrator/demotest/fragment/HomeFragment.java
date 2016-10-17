package com.ww.administrator.demotest.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.About4Acitivity;
import com.ww.administrator.demotest.AboutActivity;
import com.ww.administrator.demotest.BannerConActivity;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.NetworkImageHolderView;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.SearchActivity;
import com.ww.administrator.demotest.adapter.RecyclerViewHFAdapter;
import com.ww.administrator.demotest.model.BannerInfo;
import com.ww.administrator.demotest.model.GoodsInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final int NEW_GOODS = 1;
    public static final int HOT_GOODS = 2;

    Toolbar mtbHome;
    BannerInfo mBannerInfo;
    private Gson mGson = new Gson();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;
    private ArrayList<String> mDatas = new ArrayList<String>();
    private RecyclerViewHFAdapter adapter;
    private ConvenientBanner convenientBanner;

    private List<String> networkImages = new ArrayList<>();
    private GoodsInfo mNewList = new GoodsInfo();
    private GoodsInfo mHotList = new GoodsInfo();


    ProgressWheel mpbHome;

    @Override
    protected void getArgs() {

    }

    @Override
    protected int getLayout() {

        return R.layout.fragment_home;
    }


    @Override
    protected void initViews(View view) {
        mtbHome = (Toolbar) view.findViewById(R.id.tb_common);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        listView = (RecyclerView) view.findViewById(R.id.recyclerView);
        convenientBanner = (ConvenientBanner)LayoutInflater.from(getActivity()).inflate(R.layout.adapter_header_cb, null);
        convenientBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 410));
        mpbHome = (ProgressWheel) view.findViewById(R.id.pb_common);
        mpbHome.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
        refreshLayout.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);
        setToolbar(view);
        mtbHome.setNavigationIcon(R.mipmap.logo);
    }

    @Override
    protected void doBusiness() {

        //得到banner的相关信息
        loadDatas();
       /* Glide.with(this)
                .load("http://www.jvawa.com/uploads/1446193540.jpg")
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
                        // Do something with bitmap here.
                        bitmap.getHeight(); //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                        bitmap.getWidth();
                        Toast.makeText(getActivity(), "width:" + bitmap.getWidth() + "height:" + bitmap.getHeight(), Toast.LENGTH_LONG).show();
                    }

                });*/

    }
    private void initEvents() {
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 得到新品推荐
     */
    private void getNewGoods(){

        HttpUtil.postAsyn(Constants.BASE_URL + "index_goods.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                mNewList = mGson.fromJson(response, GoodsInfo.class);
                System.out.println(mNewList.getData().get(0).getPicurl().toString());
                getHotGoods();
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", NEW_GOODS + "")
        });
    }


    /**
     * 得到热门商品
     */
    private void getHotGoods(){
        HttpUtil.postAsyn(Constants.BASE_URL + "index_goods.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {

                mHotList = mGson.fromJson(response, GoodsInfo.class);
                init(mBannerInfo, mNewList, mHotList);
                initEvents();
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", HOT_GOODS + "")
        });
    }

    private void init(BannerInfo bannerInfo, GoodsInfo newList, GoodsInfo hotList){
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewHFAdapter(getActivity(), newList, hotList);
        listView.setAdapter(adapter);
        for (int i = 0; i < bannerInfo.getData().size(); i++){
            networkImages.add(bannerInfo.getData().get(i).getImgurl());
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = mBannerInfo.getData().get(position).getHref();
                if (!url.contains("http")) {
                    url = "http://www.jvawa.com/app/" + url;
                } else if (url.equals("#")) {
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(getActivity(), BannerConActivity.class);
                intent.putExtra("bannerUrl", url);
                startActivity(intent);
            }
        });

        adapter.addHeader(convenientBanner);
        //loadTestDatas();
    }

    /* //加入测试Views
    private void loadTestDatas() {

        mDatas.add("test＝＝＝＝＝＝＝＝＝＝＝");
        adapter.notifyDataSetChanged();
    }*/

    private void setToolbar(View view){
        mtbHome = (Toolbar) view.findViewById(R.id.tb_common);
        mtbHome.setTitle("家瓦商城");
        //((MainActivity)getActivity()).setSupportActionBar(mtbHome);
        mtbHome.inflateMenu(R.menu.main);
        mtbHome.setNavigationIcon(R.mipmap.ic_launcher);
        mtbHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_search:
                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent);
                        } else {
                            View sharedView = getView().findViewById(R.id.tb_common).findViewById(R.id.menu_search);
                            String transitionName = "img_back";
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                            startActivity(intent, transitionActivityOptions.toBundle());
                        }


                        return true;
                   /* case R.id.menu_locate:
                        //startActivity(new Intent(getActivity(), LocatCityActivity.class));
                        return true;*/

                    case R.id.menu_about:
                       /* startActivity(new Intent(getActivity(), About4Acitivity.class));*/
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP){
                            startActivity(new Intent(getActivity(), About4Acitivity.class));
                        }else {

                            startActivity(new Intent(getActivity(), AboutActivity.class));
                        }

                        break;

                }
                return false;
            }
        });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//在fragment中设置toolbar上的menuitem可见

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }



    /**
     * 载入信息
     */
    public void loadDatas() {
        getBanner();
    }

    /**
     * 得到Banner信息
     */
    public void getBanner(){

        HttpUtil.postAsyn(Constants.BASE_URL + "index_banner.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
                mpbHome.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                mBannerInfo = mGson.fromJson(response, BannerInfo.class);
                getNewGoods();
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("status", "1")
        });
    }



    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);
    }

    // 停止自动翻页
    @Override
    public void onPause() {
        super.onPause();
        //停止翻页
        convenientBanner.stopTurning();
    }

    @Override
    public void onRefresh() {

        adapter.notifyDataSetChanged();

        refreshLayout.setRefreshing(false);
    }

}
