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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.About4Acitivity;
import com.ww.administrator.demotest.AboutActivity;
import com.ww.administrator.demotest.BannerConActivity;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.MainActivity;
import com.ww.administrator.demotest.NetworkImageHolderView;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.SearchActivity;
import com.ww.administrator.demotest.SelectCityActivity;
import com.ww.administrator.demotest.adapter.RecyclerViewHFAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.event.D12HomeActivity;
import com.ww.administrator.demotest.event.OneBuyEventActivity;
import com.ww.administrator.demotest.event.SignInEventActivity;
import com.ww.administrator.demotest.model.BannerInfo;
import com.ww.administrator.demotest.model.GoodsInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.DisplayUtil;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    public static final int NEW_GOODS = 1;  //1：新品推荐
    public static final int HOT_GOODS = 2;  //2：热门商品
    public static final int HOME_GOODS = 3; //3：全屋热卖
    public static final int D12_GOODS = 5; //5：圣诞特惠
    Toolbar mtbHome;
    BannerInfo mBannerInfo;
    private Gson mGson = new Gson();
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView listView;
    private RecyclerViewHFAdapter adapter;
    private ConvenientBanner convenientBanner;

    private List<String> networkImages = new ArrayList<>();
    private GoodsInfo mNewList = new GoodsInfo();
    private GoodsInfo mHotList = new GoodsInfo();
    private GoodsInfo mHomeList = new GoodsInfo();
    private GoodsInfo mD12List = new GoodsInfo();

    String gid = "";
    int iAnim = 1;

    ProgressWheel mpbHome;
    String cityName = "";

    MaterialDialog cityDialog;
    ListView mlvCity;

    private ImageView ivEvent, ivD12Event;

    EditText metToSearch;
    ImageButton imbtnSearch;
    TextView mtvLocate;
    ImageView mivAbout;
    FloatingActionButton mfabAround;

    @Override
    protected void getArgs() {

    }

    @Override
    protected int getLayout() {

        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setOnScrollListener(new MyScrollListener());

    }
    /**
     * RecyclerView的滑动事件(用于实现隐藏显示底部导航栏)
     */
    class MyScrollListener extends RecyclerView.OnScrollListener{
        boolean isSlidingToLast = false;
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                isSlidingToLast = true;

                ((MainActivity)getActivity()).mBottomNavBar.hide();
            } else {
                isSlidingToLast = false;
                ((MainActivity)getActivity()).mBottomNavBar.show();
            }
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }
    }
    @Override
    protected void initViews(View view) {
        mtbHome = (Toolbar) view.findViewById(R.id.tb_common);
        ivEvent = (ImageView) view.findViewById(R.id.iv_event);
        ivD12Event = (ImageView) view.findViewById(R.id.iv_event_signin);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        listView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mfabAround = (FloatingActionButton) view.findViewById(R.id.fab_around);
        convenientBanner = (ConvenientBanner) LayoutInflater.from(getActivity()).inflate(R.layout.adapter_header_cb, null);
        convenientBanner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getActivity(), 230)));

        mpbHome = (ProgressWheel) view.findViewById(R.id.pb_common);
        mpbHome.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
        refreshLayout.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);

        metToSearch = (EditText) view.findViewById(R.id.et_search);
        imbtnSearch = (ImageButton) view.findViewById(R.id.imbtn_search);
        mtvLocate = (TextView) view.findViewById(R.id.tv_locate);
        mivAbout = (ImageView) view.findViewById(R.id.iv_about);
        setEditTextReadOnly();
        /*mfabAround.attachToRecyclerView(listView);
        mfabAround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearActivity.class);
                //Intent intent = new Intent(getActivity(), RotateEventActivity.class);
                startActivity(intent);
            }
        });*/

    }

    private void setEditTextReadOnly(){
        metToSearch.setCursorVisible(false);      //设置输入框中的光标不可见
        metToSearch.setFocusable(false);           //无焦点
        metToSearch.setFocusableInTouchMode(false);     //触摸时也得不到焦点
    }


    @Override
    protected void doBusiness() {
        initCity();
        setToolbar();
        //得到banner的相关信息
        loadDatas();
        initDialog();
        /*Glide.with(this)
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

    private void initCity(){
        cityName = (String) SharedPreUtil.getData(getActivity(), "locatCity", "南京");
    }

    private void initDialog(){
        cityDialog = new MaterialDialog(getActivity());
        cityDialog.setTitle("选择城市");

        LinearLayout linearLayout = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getActivity(), 200));
        lp.setMargins(10, 10, 10, 0);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        mlvCity = new ListView(getActivity());
        AbsListView.LayoutParams ablp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getActivity(), 200));
        mlvCity.setLayoutParams(ablp);
        mlvCity.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Constants.CITY_ARRAY));

        linearLayout.addView(mlvCity);
        cityDialog.setContentView(linearLayout);
        cityDialog.setCanceledOnTouchOutside(true);
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
                //mfabAround.setVisibility(View.VISIBLE);
                mHotList = mGson.fromJson(response, GoodsInfo.class);
                if (!cityName.equals("南京")) {
                    getHomeGoods();

                } else {
                    init(mBannerInfo, mD12List, mNewList, mHotList);
                    initEvents();

                }
                /*ivD12Event.setVisibility(View.VISIBLE);
                ivD12Event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent d12Sign = new Intent(getActivity(), D12SignInEventActivity.class);
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(d12Sign);
                        } else {
                            String transitionName = "event_d12_share";
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), ivD12Event, transitionName);
                            startActivity(d12Sign, transitionActivityOptions.toBundle());
                        }

                    }
                });*/
                if (!ToolsUtil.isEventExpire()) { //判断活动是否过期
                    ivEvent.setVisibility(View.VISIBLE);
                    //playHeartbeatAnimation(ivEvent);
                    if (iAnim == 0) {
                        new Thread() {
                            public void run() {
                                while (true) {
                                    try {
                                        //增大900mm，减小1000mm，所以这里设置2000mm左右即可
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {
                                            playHeartbeatAnimation(ivEvent);
                                            playHeartbeatAnimation(ivD12Event);
                                        }
                                    });
                                }
                            }
                        }.start();
                    }

                    //只有在iAnim为0的时候才开启线程，防止刷新的时候，开启多个线程。
                    iAnim = 1;

                    Intent i = new Intent(getActivity(), OneBuyEventActivity.class);
                    startActivity(i);

                    ivEvent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), OneBuyEventActivity.class);
                            //startActivity(i);
                            if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                                startActivity(i);
                            } else {
                                String transitionName = "event_buy_share";
                                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), ivEvent, transitionName);
                                startActivity(i, transitionActivityOptions.toBundle());
                            }

                        }
                    });
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", HOT_GOODS + "")
        });
    }

    /**
     * 得到全屋商品
     */
    private void getHomeGoods(){
        HttpUtil.postAsyn(Constants.BASE_URL + "index_goods.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                mHomeList = mGson.fromJson(response, GoodsInfo.class);
                init(mBannerInfo, mD12List, mNewList, mHotList, mHomeList);
                initEvents();
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", HOME_GOODS + "")
        });
    }

    /**
     * 得到双十二商品
     */
    private void getD12Goods(){
        HttpUtil.postAsyn(Constants.BASE_URL + "index_goods.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                mD12List = mGson.fromJson(response, GoodsInfo.class);
                getNewGoods();
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("isrecom", D12_GOODS + "")
        });
    }

    private void init(BannerInfo bannerInfo, GoodsInfo d12List, GoodsInfo newList, GoodsInfo hotList, GoodsInfo homeList){
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewHFAdapter(getActivity(), d12List, newList, hotList, homeList, cityName);
        listView.setAdapter(adapter);
        networkImages.clear();
        for (int i = 0; i < bannerInfo.getData().size(); i++){
            networkImages.add(bannerInfo.getData().get(i).getImgurl());
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.page_indicator_normal, R.drawable.page_indicator_focused});

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = mBannerInfo.getData().get(position).getHref();
                if (url.equals("#")) {

                    return;
                }

                if (url.equals("qdyl.php")) {
                        Intent intent = new Intent(getActivity(), SignInEventActivity.class);
                        startActivity(intent);

                }else if (url.equals("game/AprilSpecial/decSpecial.php") || url.equals("game/AprilSpecial/njDecSpecial.php")){
                    Intent intent = new Intent(getActivity(), D12HomeActivity.class);
                    startActivity(intent);
                }else if (url.equals("game/AprilSpecial/yuandan.php")){
                    Intent banner = new Intent();
                    banner.setClass(getActivity(), BannerConActivity.class);
                    banner.putExtra("bannerUrl", "http://www.jvawa.com/new/game/AprilSpecial/yuandan.php");
                    startActivity(banner);
                } else {
                    String[] urlArr = mBannerInfo.getData().get(position).getHref().split("[?]");

                    if (urlArr.length == 1) { //证明不是商品购买

                        url = "http://www.jvawa.com/app/" + url;
                        Intent banner = new Intent();
                        banner.setClass(getActivity(), BannerConActivity.class);
                        banner.putExtra("bannerUrl", url);
                        startActivity(banner);
                    } else {
                        gid = urlArr[1].substring(3);
                        //Log.d("ww", "gid:" + gid + "=======");
                        if (gid != "") {
                            Intent detail = new Intent();
                            detail.setClass(getActivity(), DetailActivity.class);
                            detail.putExtra("gid", gid);
                            startActivity(detail);
                        }
                    }
                }

            }
        });

        adapter.addHeader(convenientBanner);
        //loadTestDatas();
    }

    /**
     * 红包跳动动画
     */
    private void playHeartbeatAnimation(final ImageView iv) {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(0.7f, 1.0f));
        animationSet.setDuration(1000);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.3f, 1.0f, 1.3f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(1.0f, 0.7f));
                animationSet.setDuration(900);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(true);
                // 实现跳动的View
                iv.startAnimation(animationSet);
            }
        });
        // 实现跳动的View
        iv.startAnimation(animationSet);
    }

    private void init(BannerInfo bannerInfo, GoodsInfo d12List, GoodsInfo newList, GoodsInfo hotList){
        listView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewHFAdapter(getActivity(),d12List, newList, hotList, cityName);
        listView.setAdapter(adapter);
        networkImages.clear();
        for (int i = 0; i < bannerInfo.getData().size(); i++){
            networkImages.add(bannerInfo.getData().get(i).getImgurl());
        }

        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages)
                .setPageIndicator(new int[]{R.drawable.page_indicator_normal, R.drawable.page_indicator_focused});

        convenientBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = mBannerInfo.getData().get(position).getHref();

                if (url.equals("#")){

                    return;
                }

                if (url.equals("qdyl.php")){
                    Intent intent = new Intent(getActivity(), SignInEventActivity.class);
                    startActivity(intent);

                }else if (url.equals("game/AprilSpecial/decSpecial.php") || url.equals("game/AprilSpecial/njDecSpecial.php")){
                    Intent intent = new Intent(getActivity(), D12HomeActivity.class);
                    startActivity(intent);
                }else if (url.equals("game/AprilSpecial/yuandan.php")){
                    Intent banner = new Intent();
                    banner.setClass(getActivity(), BannerConActivity.class);
                    banner.putExtra("bannerUrl", "http://www.jvawa.com/new/game/AprilSpecial/yuandan.php");
                    startActivity(banner);
                }else {
                    String[] urlArr = mBannerInfo.getData().get(position).getHref().split("[?]");

                    if (urlArr.length == 1){ //证明不是商品购买
                        url = "http://www.jvawa.com/app/" + url;
                        Intent banner = new Intent();
                        banner.setClass(getActivity(), BannerConActivity.class);
                        banner.putExtra("bannerUrl", url);
                        startActivity(banner);
                    }else {
                        gid = urlArr[1].substring(3);
                        //Log.d("ww", "gid:" + gid + "=======");
                        if (gid != ""){
                            Intent detail = new Intent();
                            detail.setClass(getActivity(), DetailActivity.class);
                            detail.putExtra("gid", gid);
                            startActivity(detail);
                        }
                    }
                }
            }
        });

        adapter.addHeader(convenientBanner);
    }


    private void setToolbar(){


        //mtbHome = (Toolbar) view.findViewById(R.id.tb_common);
        //mtbHome.setNavigationIcon(R.mipmap.logo);
        //mtbHome.setTitle("家瓦商城");
        //((MainActivity)getActivity()).setSupportActionBar(mtbHome);
        //mtbHome.inflateMenu(R.menu.main);
        //mtbHome.setNavigationIcon(R.mipmap.ic_launcher);

       /* mtbHome.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
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
                    case R.id.menu_locate:

                        startActivityForResult(new Intent(getActivity(), SelectCityActivity.class), 100);
                        *//*cityDialog.show();
                        mlvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0: //南京
                                        cityName = "南京";
                                        cityDialog.dismiss();

                                        break;
                                    case 1: //上海
                                        cityName = "上海";
                                        cityDialog.dismiss();

                                        break;
                                    case 2: //兰州
                                        cityName = "兰州";
                                        cityDialog.dismiss();

                                        break;
                                    case 3: //沈阳
                                        cityName = "沈阳";
                                        cityDialog.dismiss();

                                        break;
                                }
                                SharedPreUtil.saveData(getActivity(), "locatCity", cityName);
                                loadDatas();
                            }
                        });*//*

                        //startActivity(new Intent(getActivity(), LocatCityActivity.class));
                        return true;

                    case R.id.menu_about:
                       *//* startActivity(new Intent(getActivity(), About4Acitivity.class));*//*
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(new Intent(getActivity(), About4Acitivity.class));
                        } else {

                            startActivity(new Intent(getActivity(), AboutActivity.class));
                        }

                        break;

                }
                return false;
            }
        });*/
        mtvLocate.setText(cityName);
        mtvLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SelectCityActivity.class), 100);
                mlvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: //南京
                                cityName = "南京";
                                cityDialog.dismiss();

                                break;
                            case 1: //上海
                                cityName = "上海";
                                cityDialog.dismiss();

                                break;
                            case 2: //兰州
                                cityName = "兰州";
                                cityDialog.dismiss();

                                break;
                            case 3: //沈阳
                                cityName = "沈阳";
                                cityDialog.dismiss();

                                break;
                        }
                        SharedPreUtil.saveData(getActivity(), "locatCity", cityName);
                        loadDatas();
                    }
                });
            }
        });

        mivAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(new Intent(getActivity(), About4Acitivity.class));
                } else {

                    startActivity(new Intent(getActivity(), AboutActivity.class));
                }
            }
        });

        metToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent);
                } else {
                    View sharedView = imbtnSearch;
                    String transitionName = "img_back";
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                }

            }
        });
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);//在fragment中设置toolbar上的menuitem可见

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);

    }*/

    /**
     * 载入信息
     */
    public void loadDatas() {
        mtvLocate.setText((String)SharedPreUtil.getData(getActivity(), "locatCity", "南京"));
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

                System.out.println("=============");
                System.out.println(response.toString());
                System.out.println("=============");

                mpbHome.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                mBannerInfo = mGson.fromJson(response, BannerInfo.class);
                getD12Goods();



            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("status", "1"),
                new HttpUtil.Param("city", cityName)
        });
    }


    // 开始自动翻页
    @Override
    public void onResume() {
        super.onResume();
        //开始自动翻页
        convenientBanner.startTurning(5000);

        MyApp app =(MyApp) getActivity().getApplicationContext();
        Object obj = app.GetActivityIntent(Constants.HOME_REFRESH);
        if (null != obj) {
            int iValue = (Integer) obj;
            if (iValue == 1) {  //1为刷新
                cityName = (String) SharedPreUtil.getData(getActivity(), "locatCity", "南京");
                loadDatas();
            }
        }
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 600){

        }
    }
}
