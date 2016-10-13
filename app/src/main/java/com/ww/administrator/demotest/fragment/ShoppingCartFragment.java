package com.ww.administrator.demotest.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.AboutActivity;
import com.ww.administrator.demotest.AddAddressActivity;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.LoginActivity;
import com.ww.administrator.demotest.OrderActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.SearchActivity;
import com.ww.administrator.demotest.adapter.ShoppingCartAdapter;
import com.ww.administrator.demotest.adapter.ShoppingCartAddressAdapter;
import com.ww.administrator.demotest.adapter.StaffAdapter;
import com.ww.administrator.demotest.adapter.StoreAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.AddressInfo;
import com.ww.administrator.demotest.model.DefaultAddress;
import com.ww.administrator.demotest.model.GoodsDetailInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.model.ShoppingcartInfo;
import com.ww.administrator.demotest.model.StaffInfo;
import com.ww.administrator.demotest.model.StoreInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/7/19.
 */
public class ShoppingCartFragment extends BaseFragment implements View.OnClickListener{


    private static final String SHOPPING_CART_REFRESH = "SHOPPING_CART_REFRESH";

    RelativeLayout mrlBottom;
    RecyclerView mrvCart;
    SwipeRefreshLayout msrlCart;
    ListView mlvAddress;
    ProgressWheel mpbLoad;
    Toolbar mtbCart;
    BottomSheetDialog botAddDialog, botStoreDialog;
    View bottomAddView, bottomStoreView;

    Button mbtnAddClose, mbtnStoreBack, mbtnBackStep;
    TextView mtvChooseStore, mtvChooseAdd, mtvAddNew, mtvSendAddress;
    TextView mtvCurrentCity, mtvCurrentStore, mtvStoreSub, mtvLoadAgain, mtvLoadStaff;
    ListView mlvStore, mlvStaff;
    ProgressWheel mpbLoadStore;
    CoordinatorLayout mContainer;

    AppCompatSpinner mcitySpinner;

    AppCompatCheckBox mcbAllSelected;

    ShoppingCartAdapter mAdapter;
    ShoppingCartAddressAdapter mAddAdapter;
    StoreAdapter mStoreAdapter;
    StaffAdapter mStaffAdapter;

    private String uid = "-1";
    AddressInfo mInfo;
    ShoppingcartInfo cartInfo;
    LinearLayoutManager manager;

    TextView mtvAllMoney, mtvSelCount;
    Button mbtnCount;
    Gson mGson = new Gson();

    String mcity = "";  //记录所选城市
    String mstoreName = "";  //记录选中的门店名
    String mstoreId = "";   //记录选中门店的id
    String mstaffName = "";  //记录选中的员工信息
    String msalerNo = "";   //记录选中员工的工号

    float mPrice = 0;
    int mCount = 0;

    String mGid = "";
    String picurl = "";
    String strGoodsDetailInfo = "";

    MaterialDialog mDialog;

    MyApp mApp;

    RelativeLayout mrlStore, mrlAddress;
    Button mbtnLogin;


    @Override
    protected void getArgs() {
        mApp = (MyApp) getActivity().getApplicationContext();
        if (mApp.getUser() != null){
            uid = mApp.getUser().getId();
        }

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_shoppingcart;
    }

    @Override
    protected void initViews(View view) {
        mbtnLogin = (Button) view.findViewById(R.id.btn_to_login);
        mrlStore = (RelativeLayout) view.findViewById(R.id.rl_store);
        mrlAddress = (RelativeLayout) view.findViewById(R.id.rl_address);
        mrvCart = (RecyclerView) view.findViewById(R.id.rv_shopping_cart);
        mpbLoad = (ProgressWheel) view.findViewById(R.id.pb_common);
        mtbCart = (Toolbar) view.findViewById(R.id.tb_cart);
        mrlBottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        mtvChooseStore = (TextView) view.findViewById(R.id.tv_choose_store);
        mtvChooseAdd = (TextView) view.findViewById(R.id.tv_choose_address);
        mbtnCount = (Button) view.findViewById(R.id.btn_account);
        msrlCart = (SwipeRefreshLayout) view.findViewById(R.id.srl_cartlist);
        msrlCart.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);
        mtvSendAddress = (TextView) view.findViewById(R.id.tv_send_address);
        mtvAllMoney = (TextView) view.findViewById(R.id.tv_all_money);
        mcbAllSelected = (AppCompatCheckBox) view.findViewById(R.id.cb_cart_selected);
        mtvSelCount = (TextView) view.findViewById(R.id.tv_choose_count);
        mtvSendAddress.setVisibility(View.GONE);
        mContainer = (CoordinatorLayout) view.findViewById(R.id.cdl_container);
        setToolbar();
        mpbLoad.setVisibility(View.VISIBLE);
        mrvCart.setOnScrollListener(new MyScollListener());
        botAddDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        botStoreDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);

        if (mApp.getUser() == null){
            initDialog();
            mDialog.show();
            mbtnLogin.setVisibility(View.VISIBLE);
            mrlStore.setVisibility(View.GONE);
            mrlAddress.setVisibility(View.GONE);
            msrlCart.setVisibility(View.GONE);
            mrvCart.setVisibility(View.GONE);
            mrlBottom.setVisibility(View.GONE);
        }else {
            mbtnLogin.setVisibility(View.GONE);
            mrlStore.setVisibility(View.VISIBLE);
            mrlAddress.setVisibility(View.VISIBLE);
            msrlCart.setVisibility(View.VISIBLE);
            mrvCart.setVisibility(View.VISIBLE);
            mrlBottom.setVisibility(View.VISIBLE);
        }

        initEvents();
    }


    private void initDialog(){
        mDialog = new MaterialDialog(getActivity());
        mDialog.setTitle("提示");
        mDialog.setMessage("检测到您还未登录，点击确定立刻前往登录！");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDialog.dismiss();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
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
    private void setBtnColor(){
        if (!mtvSelCount.getText().toString().equals("0")){
            mbtnCount.setBackgroundColor(Color.parseColor("#F5183C"));
        }else {
            mbtnCount.setBackgroundColor(Color.parseColor("#999999"));
        }
    }

    private void initEvents(){
        mtvChooseStore.setOnClickListener(this);
        mtvChooseAdd.setOnClickListener(this);
        mtvSendAddress.setOnClickListener(this);
        mbtnCount.setOnClickListener(this);
        mbtnLogin.setOnClickListener(this);
    }

    private void setToolbar() {
        mtbCart.setTitle("购物车");

        //((MainActivity)getActivity()).setSupportActionBar(mtbCart);
        mtbCart.inflateMenu(R.menu.main);
        mtbCart.setNavigationIcon(R.mipmap.logo);
        mtbCart.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_search:

                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        View sharedView = getView().findViewById(R.id.tb_cart).findViewById(R.id.menu_search);
                        String transitionName = "img_back";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                        getActivity().startActivity(intent, transitionActivityOptions.toBundle());
                        return true;
                    /*case R.id.menu_locate:
                        //startActivity(new Intent(getActivity(), LocatCityActivity.class));
                        return true;*/

                    case R.id.menu_about:
                        startActivity(new Intent(getActivity(), AboutActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
    public void refreshDatas(){
        msrlCart.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
                loadAddress();
                msrlCart.setRefreshing(false);
            }
        });
    }



    /**
     * 收货地址BottomSheetDialog
     */
    private void showBottomAddress(){

        bottomAddView = LayoutInflater.from(getActivity()).inflate(R.layout.sheet_cart_address_layout, null);
        mbtnAddClose = (Button) bottomAddView.findViewById(R.id.btn_sheet_close);
        mtvAddNew = (TextView) bottomAddView.findViewById(R.id.tv_cart_add_address);
        mlvAddress = (ListView) bottomAddView.findViewById(R.id.lv_address);
        mlvAddress.setVisibility(View.GONE);

        mbtnAddClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botAddDialog.isShowing()) {
                    botAddDialog.dismiss();
                }
            }
        });
        mtvAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botAddDialog.isShowing()) {
                    botAddDialog.dismiss();
                }
                startActivity(new Intent(getActivity(), AddAddressActivity.class));
            }
        });

        botAddDialog.setContentView(bottomAddView);

        if (mInfo != null){
            mlvAddress.setVisibility(View.VISIBLE);
            mAddAdapter = new ShoppingCartAddressAdapter(getActivity(), mInfo);
            mlvAddress.setAdapter(mAddAdapter);
            mlvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mtvChooseAdd.setText(mInfo.getData().get(position).getReceivername() + "   " + mInfo.getData().get(position).getPhone());
                    mtvSendAddress.setText(mInfo.getData().get(position).getAddress());
                    botAddDialog.dismiss();
                }
            });
        }

        botAddDialog.show();

    }

    /**
     * 门店BottomSheetDialog
     */
    private void showBottomStore(){

        bottomStoreView = LayoutInflater.from(getActivity()).inflate(R.layout.sheet_cart_store_layout, null);
        mbtnStoreBack = (Button) bottomStoreView.findViewById(R.id.btn_sheet_back);
        mbtnBackStep = (Button) bottomStoreView.findViewById(R.id.btn_sheet_back_step);
        mtvCurrentCity = (TextView) bottomStoreView.findViewById(R.id.tv_current_city);
        mcitySpinner = (AppCompatSpinner) bottomStoreView.findViewById(R.id.sp_city);
        mtvCurrentStore = (TextView) bottomStoreView.findViewById(R.id.tv_current_store);
        mtvStoreSub = (TextView) bottomStoreView.findViewById(R.id.tv_sub_title);
        mtvLoadAgain = (TextView) bottomStoreView.findViewById(R.id.tv_load_again);
        mtvLoadStaff = (TextView) bottomStoreView.findViewById(R.id.tv_load_again_staff);
        mpbLoadStore = (ProgressWheel) bottomStoreView.findViewById(R.id.pb_common);
        mlvStore = (ListView) bottomStoreView.findViewById(R.id.lv_store);
        mlvStaff = (ListView) bottomStoreView.findViewById(R.id.lv_stuff);
        mbtnBackStep.setVisibility(View.GONE);
        mpbLoadStore.setVisibility(View.VISIBLE);
        mlvStore.setVisibility(View.GONE);
        mlvStaff.setVisibility(View.GONE);
        mtvLoadAgain.setVisibility(View.GONE);
        mtvLoadStaff.setVisibility(View.GONE);
        mcitySpinner.setVisibility(View.VISIBLE);
        mtvLoadStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        mbtnBackStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtvCurrentCity.setVisibility(View.VISIBLE);
                mtvCurrentCity.setText("当前所在城市");
                mtvCurrentStore.setVisibility(View.GONE);
                mcitySpinner.setVisibility(View.VISIBLE);
                mbtnBackStep.setVisibility(View.GONE);
                mlvStore.setVisibility(View.VISIBLE);
                mlvStaff.setVisibility(View.GONE);
            }
        });

        mtvLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStores(mcitySpinner.getSelectedItem().toString());
            }
        });
        mbtnStoreBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botStoreDialog.isShowing()) {
                    botStoreDialog.dismiss();
                }
            }
        });


        botStoreDialog.setContentView(bottomStoreView);


        botStoreDialog.show();
        mcitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mcity = mcitySpinner.getSelectedItem().toString();
                loadStores(mcitySpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //loadStores(mcitySpinner.getSelectedItem().toString());
    }

    @Override
    protected void doBusiness() {
        if (mApp.getUser() != null){
            loadDatas();
            loadAddress();
            refreshDatas();
        }

    }

    /**
     * 载入购物车内容
     */
    private void loadDatas() {

        HttpUtil.postAsyn(Constants.BASE_URL + "get_shopping_cart.php", new HttpUtil.ResultCallback<ShoppingcartInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbLoad.setVisibility(View.GONE);
                Snackbar.make(mrvCart, "请先检查您的网络！", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(ShoppingcartInfo info) {
                mpbLoad.setVisibility(View.GONE);
                if (info.getCode().equals("200")) {
                    if (info.getData().size() != 0){
                        cartInfo = info;
                        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mrvCart.setLayoutManager(manager);
                        mAdapter = new ShoppingCartAdapter(getActivity(), info);
                        mrvCart.setAdapter(mAdapter);
                        mcbAllSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                mAdapter.onRefresh(isChecked);
                            }
                        });

                        //右滑移除Item
                        swipItem();
                        //item选中 计算选中的金额和数量 得到该商品id
                        mAdapter.setOnCartChecked(new ShoppingCartAdapter.OnCartChecked() {
                            @Override
                            public void isSetChecked(ShoppingcartInfo info, boolean isChecked, int pos) {
                                mGid = info.getData().get(pos).getId();
                                picurl = info.getData().get(pos).getImgurl();
                                calculateMoney(info.getData().get(pos).getPrice(), isChecked);
                                calculateCount(isChecked);
                                setBtnColor();
                            }
                        });
                    }else {
                        mrlBottom.setVisibility(View.GONE);
                    }

                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid)
        });
    }


    /**
     * 载入配送地址
     */
    private void loadAddress(){

        HttpUtil.postAsyn(Constants.BASE_URL + "get_address.php", new HttpUtil.ResultCallback<AddressInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mContainer, "请先检查您的网络！", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(AddressInfo info) {

                if (info.getCode().equals("200")) {
                    if (info.getData() != null) {
                        mInfo = info;
                        mtvSendAddress.setVisibility(View.VISIBLE);
                        DefaultAddress d = hasDefault(info);
                        mtvChooseAdd.setText(info.getData().get(d.getPos()).getReceivername() + "    " + info.getData().get(d.getPos()).getPhone());
                        mtvSendAddress.setText(info.getData().get(d.getPos()).getAddress());
                    }
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid)
        });
    }

    private DefaultAddress hasDefault(AddressInfo info){
        DefaultAddress d = new DefaultAddress();

        for (int i = 0; i < info.getData().size(); i++){
            if (info.getData().get(i).getIsdefault().equals("1")){
                d.setIsDefault(true);
                d.setPos(i);
                return d;
            }
        }
        d.setIsDefault(false);
        d.setPos(0);
        return d;
    }

    /**
     * 右滑Item移除
     */

    private void swipItem(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                //cancelCollect(coAdapter.getItem(position));
                //Toast.makeText(getActivity(), mAdapter.getItem(position).getId(), Toast.LENGTH_LONG).show();
                deleteCart(mAdapter.getItem(position).getId());
                mAdapter.delItem(position);

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //滑动时改变Item的透明度
                    final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
            }
        }).attachToRecyclerView(mrvCart);
    }

    /**
     * 根据RecyclerView的上下滑动是否显示底部layout
     */
    class MyScollListener extends RecyclerView.OnScrollListener{

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0){//表示RecyclerView下滑
                mrlBottom.setVisibility(View.GONE);
            }else {//表示RecyclerView上滑
                mrlBottom.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 删除购物车内容
     * @param goodsstoreid
     */
    private void deleteCart(String goodsstoreid){
        HttpUtil.postAsyn(Constants.BASE_URL + "delete_shoppingcart.php", new HttpUtil.ResultCallback<ResultInfo>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(ResultInfo info) {

                if (info.getCode().equals("200")) {
                    if (mAdapter.getItemCount() == 0){
                        mrlBottom.setVisibility(View.GONE);
                    }

                    Snackbar.make(mContainer, "删除成功！", Snackbar.LENGTH_SHORT).show();
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsstoreid", goodsstoreid)
        });
    }

    /**
     * 载入门店信息
     */
    private void loadStores(String city){
        HttpUtil.postAsyn(Constants.BASE_URL + "get_store_info.php", new HttpUtil.ResultCallback<StoreInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                mtvLoadAgain.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(StoreInfo info) {
                mpbLoadStore.setVisibility(View.GONE);
                if (info.getCode().equals("200")) {
                    mlvStore.setVisibility(View.VISIBLE);
                    mStoreAdapter = new StoreAdapter(getActivity(), info);
                    mlvStore.setAdapter(mStoreAdapter);

                    mlvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mstoreName = mStoreAdapter.getItem(position).getStoreName();
                            mstoreId = mStoreAdapter.getItem(position).getStoreId();
                            loadStaffs(mstoreName);
                            mlvStore.setVisibility(View.GONE);
                            mlvStaff.setVisibility(View.VISIBLE);
                            mbtnStoreBack.setVisibility(View.GONE);
                            mbtnBackStep.setVisibility(View.VISIBLE);
                            mtvCurrentStore.setVisibility(View.VISIBLE);
                            mtvCurrentStore.setText(mstoreName);
                            mtvCurrentCity.setText("当前所在门店：");
                            mcitySpinner.setVisibility(View.GONE);

                        }
                    });
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("cityName", city),
                new HttpUtil.Param("action", "store")
        });
    }

    /**
     * 载入店员信息
     * @param storeName
     */

    private void loadStaffs(String storeName){
        HttpUtil.postAsyn(Constants.BASE_URL + "get_store_info.php", new HttpUtil.ResultCallback<StaffInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbLoadStore.setVisibility(View.GONE);
                mtvLoadStaff.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(StaffInfo info) {

                mpbLoadStore.setVisibility(View.GONE);
                if (info.getCode().equals("200")) {

                    mStaffAdapter = new StaffAdapter(getActivity(), info);
                    mlvStaff.setAdapter(mStaffAdapter);

                    mlvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mstaffName = mStaffAdapter.getItem(position).getTruename();
                            msalerNo = mStaffAdapter.getItem(position).getSalerno();
                            mtvChooseStore.setText(mstoreName + "  " + mstaffName + "(" + msalerNo + ")");
                            botStoreDialog.dismiss();
                        }
                    });
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("storeName", storeName),
                new HttpUtil.Param("action", "staff")
        });
    }

    /**
     * 计算选中商品金额
     */
    private void calculateMoney(String money, boolean isSelected){

        float d = Float.parseFloat(money);
        if (isSelected){
            mPrice += d;
        }else {
            if (mPrice > 0){
                mPrice -= d;
            }

        }
        mtvAllMoney.setText("￥" + mPrice + "");
    }

    /**
     * 计算选中的商品数量
     */
    private void calculateCount(boolean isSelected){
        if (isSelected){
            mCount ++;
        }else {
            if (mCount > 0){
                mCount --;
            }
        }
        mtvSelCount.setText(mCount + "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_choose_store:
                showBottomStore();
                break;
            case R.id.tv_choose_address:
                showBottomAddress();
                break;
            case R.id.tv_send_address:
                showBottomAddress();
                break;
            case R.id.btn_account:

                if (mtvSelCount.getText().equals("0")){
                    return;
                }
                //结账
                toPay();

                break;
            case R.id.btn_to_login:
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), 100);
                break;
        }
    }

    private void toPay(){
        HttpUtil.postAsyn(Constants.BASE_URL + "product_detail.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                GoodsDetailInfo mInfo = mGson.fromJson(response, GoodsDetailInfo.class);
                if (mInfo.getCode().equals("200")) {

                    strGoodsDetailInfo = response;
                    Intent intent = new Intent(getActivity(), OrderActivity.class);

                    intent.putExtra("imgurl", Constants.BASE_IMG_URL + picurl);
                    intent.putExtra("gid", mGid);
                    intent.putExtra("response", strGoodsDetailInfo);

                    if (!mstoreName.equals("") && !mstaffName.equals("") && !msalerNo.equals("")){
                        intent.putExtra("storeName", mstoreName);
                        intent.putExtra("staffName", mstaffName);
                        intent.putExtra("salerNo", msalerNo);
                        intent.putExtra("storeId", mstoreId);
                        intent.putExtra("city", mcity);
                    }
                    startActivity(intent);
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("gid", mGid)
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            mDialog.dismiss();
            mbtnLogin.setVisibility(View.GONE);
            mrlStore.setVisibility(View.VISIBLE);
            mrlAddress.setVisibility(View.VISIBLE);
            msrlCart.setVisibility(View.VISIBLE);
            mrvCart.setVisibility(View.VISIBLE);
            mrlBottom.setVisibility(View.VISIBLE);
            uid = mApp.getUser().getId();
            loadDatas();
            loadAddress();
            refreshDatas();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        MyApp app =(MyApp) getActivity().getApplicationContext();
        Object obj = app.GetActivityIntent(Constants.SHOPPING_CART_REFRESH);
        if (null != obj) {
            int iValue = (Integer) obj;

            if (iValue == 1) {  //1为登录
                if (mDialog == null){
                    initDialog();
                }
                mDialog.dismiss();
                mbtnLogin.setVisibility(View.GONE);
                mrlStore.setVisibility(View.VISIBLE);
                mrlAddress.setVisibility(View.VISIBLE);
                msrlCart.setVisibility(View.VISIBLE);
                mrvCart.setVisibility(View.VISIBLE);
                mrlBottom.setVisibility(View.VISIBLE);
                uid = app.getUser().getId();
                loadDatas();
                loadAddress();
                refreshDatas();
            }

            if (iValue == 0){   //0为退出
                initDialog();
                mDialog.show();
                mbtnLogin.setVisibility(View.VISIBLE);
                mrlStore.setVisibility(View.GONE);
                mrlAddress.setVisibility(View.GONE);
                msrlCart.setVisibility(View.GONE);
                mrvCart.setVisibility(View.GONE);
                mrlBottom.setVisibility(View.GONE);
            }
        }

        MyApp addressApp =(MyApp) getActivity().getApplicationContext();
        Object obj2 = addressApp.GetActivityIntent(Constants.ADDRESS_REFRESH);
        if (null != obj2) {
            int iValue = (Integer) obj2;

            if (iValue == 1) {  //1为刷新地址

                mbtnLogin.setVisibility(View.GONE);
                mrlStore.setVisibility(View.VISIBLE);
                mrlAddress.setVisibility(View.VISIBLE);
                msrlCart.setVisibility(View.VISIBLE);
                mrvCart.setVisibility(View.VISIBLE);
                mrlBottom.setVisibility(View.VISIBLE);
                uid = app.getUser().getId();
                //loadDatas();
                loadAddress();
                //refreshDatas();
            }

        }

        MyApp addShoppingApp =(MyApp) getActivity().getApplicationContext();
        Object obj3 = addShoppingApp.GetActivityIntent(Constants.ADD_SHOPPING_CART_REFRESH);
        if (null != obj3) {
            int iValue = (Integer) obj3;

            if (iValue == 1) {  //1为刷新购物车

                mbtnLogin.setVisibility(View.GONE);
                mrlStore.setVisibility(View.VISIBLE);
                mrlAddress.setVisibility(View.VISIBLE);
                msrlCart.setVisibility(View.VISIBLE);
                mrvCart.setVisibility(View.VISIBLE);
                mrlBottom.setVisibility(View.VISIBLE);
                uid = app.getUser().getId();
                loadDatas();
                loadAddress();
                refreshDatas();
            }

        }

    }
}
