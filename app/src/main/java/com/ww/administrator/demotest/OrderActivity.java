package com.ww.administrator.demotest;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.adapter.DetailColAdapter;
import com.ww.administrator.demotest.adapter.DetailDoorAdapter;
import com.ww.administrator.demotest.adapter.DetailTaiAdapter;
import com.ww.administrator.demotest.adapter.ShoppingCartAddressAdapter;
import com.ww.administrator.demotest.adapter.StaffAdapter;
import com.ww.administrator.demotest.adapter.StoreAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.model.AddressInfo;
import com.ww.administrator.demotest.model.DefaultAddress;
import com.ww.administrator.demotest.model.GoodsDetailInfo;
import com.ww.administrator.demotest.model.StaffInfo;
import com.ww.administrator.demotest.model.StoreInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/9/2.
 */
public class OrderActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar mtbOrder;

    private String uid = "-1";
    AddressInfo mInfo;

    ShoppingCartAddressAdapter mAddAdapter;
    CoordinatorLayout mContainer;
    TextView mtvChooseStore, mtvChooseAdd, mtvSendAddress, mtvAddNew;
    TextView mtvCurrentCity, mtvCurrentStore, mtvStoreSub, mtvLoadAgain, mtvLoadStaff;
    ProgressWheel mpbLoadStore;
    View bottomAddView, bottomStoreView;

    BottomSheetDialog botAddDialog, botStoreDialog;
    Button mbtnAddClose, mbtnStoreBack, mbtnBackStep;

    AppCompatSpinner mcitySpinner;

    ListView mlvAddress;

    StoreAdapter mStoreAdapter;
    StaffAdapter mStaffAdapter;
    ListView mlvStore, mlvStaff;
    ImageView mivShow;

    GoodsDetailInfo mDetailInfo;

    Gson mGson = new Gson();

    TextView mtvTitle, mtvAllMoney, mTvDetailColorName, mTvDetailColorSelectedName, mTvDetailTaimianSelectedName, mTvDetailDoorSelectedName;

    RecyclerView mRvColor, mRvTaimian, mRvDoor;

    LinearLayoutManager llColorManager;
    LinearLayoutManager llTaimianManager;
    LinearLayoutManager llDoorManager;

    DetailColAdapter mColorAdapter;

    DetailTaiAdapter mTaimianAdapter;

    DetailDoorAdapter mDoorAdapter;


    ImageView ivColorSelected;  //选中的colorView
    ImageView ivColorCurrent;   //当前的colorView

    int mColorCurrentPos = -1;  //当前的colorPos

    ImageView ivTaimianSelected;  //选中的taimianView
    ImageView ivTaimianCurrent;   //当前的taimianView

    int mTaimianCurrentPos = -1;  //当前的taimianPos

    ImageView ivDoorSelected;  //选中的doorView
    ImageView ivDoorCurrent;   //当前的doorView

    int mDoorCurrentPos = -1;  //当前的doorPos

    String mGid = "";
    String mimgUrl = "";
    String mcity = "";  //记录所选城市
    String mstoreName = "";  //记录选中的门店名
    String mstaffName = "";  //记录选中的员工姓名
    String msalerNo = "";   //记录选中的员工工号
    String mstoreId = "";   //记录门店id
    String strGoodsName = "";   //记录商品名
    String strReceiverInfo = "";    //记录收货人信息
    String strDoor = "";    //记录所选门型
    String strDoorId = "";    //记录所选门型id
    String strColor = "";   //记录所选颜色
    String strColorId = ""; //记录所选颜色id
    String strTaimian = ""; //记录所选台面
    String strTaimianId = ""; //记录所选台面id
    String strStandard = ""; //规格
    int morderMoney = 2000;  //预约金
    String strallMoney = "";  //预估价
    String strTip = ""; //备注
    float allMoney;

    EditText metTip;
    Button mbtnCommit;

    TextView mtvBottomAllMoney, mtvOrderMoney;

    RelativeLayout mrlFloorContainer, mrlHangingContainer, mrlOrderContainer;
    CardView mcvDoorContainer, mcvColorContainer, mcvTaimianContainer;

    View lineFloor, lineHanging;

    TextView mtvOrderMinus, mtvOrderPlus, mtvOrderMoneyShow;

    TextView mtvNumMinus, mtvNumPlus, mtvNumCount;
    TextView mtvFloorMinus, mtvFloorPlus, mtvFloorCount;
    TextView mtvHangMinus, mtvHangPlus, mtvHangCount;

    TextView mtvLeftBracket, mtvRightBracket;
    TextView mtvOrderMoneyLabel, mtvOrderMoneyBottom;

    double mFloorMet = 3.0f;
    double mHangMet = 1.0f;

    LinearLayout mllNumContainer;
    int orderNum = 1;

    float partMoney = 0;

    int orderMode = -1; //代表订单类型

    RelativeLayout mrlStoreChoose;
    CardView mcvTipContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_layout);
        initParams();
        initViews();
        initEvents();
        loadAddress();

        loadDatas();

    }

    private void initParams() {
        uid = ((MyApp) getApplicationContext()).getUser().getId();
        mimgUrl = getIntent().getExtras().getString("imgurl");
        mGid = getIntent().getExtras().getString("gid");
        orderMode = getIntent().getExtras().getInt("orderMode", -1);
        mDetailInfo = mGson.fromJson(getIntent().getExtras().getString("response"), GoodsDetailInfo.class);
        partMoney = Float.parseFloat(mDetailInfo.getData().getDetail().getPrice());

        if (getIntent().getExtras().getString("storeName") != null){
            mcity = getIntent().getExtras().getString("city");
            mstoreId = getIntent().getExtras().getString("storeId");
            mstoreName = getIntent().getExtras().getString("storeName");
            msalerNo = getIntent().getExtras().getString("salerNo");
            mstaffName = getIntent().getExtras().getString("staffName");
        }
        if (mDetailInfo.getData().getDetail().getSubtitle().equals("全屋定制")){
            morderMoney = 3000;
        }

    }

    private void initViews() {
        mtbOrder = (Toolbar) findViewById(R.id.tb_order);
        setSupportActionBar(mtbOrder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mllNumContainer = (LinearLayout) findViewById(R.id.ll_count_container);
        mtvLeftBracket = (TextView) findViewById(R.id.tv_order_left_bracket);
        mtvRightBracket = (TextView) findViewById(R.id.tv_order_right_bracket);
        mtvOrderMinus = (TextView) findViewById(R.id.tv_order_money_minus);
        mtvOrderPlus = (TextView) findViewById(R.id.tv_order_money_plus);
        mtvOrderMoneyShow = (TextView) findViewById(R.id.tv_order_money_show);
        mtvOrderMoneyLabel = (TextView) findViewById(R.id.tv_order_money_label);
        mtvOrderMoneyBottom = (TextView) findViewById(R.id.tv_order_money_label_bottom);
        mContainer = (CoordinatorLayout) findViewById(R.id.cdl_container);
        mtvChooseStore = (TextView) findViewById(R.id.tv_choose_store);
        mtvChooseAdd = (TextView) findViewById(R.id.tv_choose_address);
        mtvSendAddress = (TextView) findViewById(R.id.tv_send_address);
        botAddDialog = new BottomSheetDialog(OrderActivity.this, R.style.BottomSheetDialog);
        botStoreDialog = new BottomSheetDialog(OrderActivity.this, R.style.BottomSheetDialog);
        mTvDetailColorName = (TextView) findViewById(R.id.tv_detail_color_name);
        mRvColor = (RecyclerView) findViewById(R.id.rv_detail_color);
        mRvTaimian = (RecyclerView) findViewById(R.id.rv_detail_taimian);
        mRvDoor = (RecyclerView) findViewById(R.id.rv_detail_door);
        mTvDetailColorSelectedName = (TextView) findViewById(R.id.tv_detail_color_choose);
        mTvDetailTaimianSelectedName = (TextView) findViewById(R.id.tv_detail_taimian_choose);
        mTvDetailDoorSelectedName = (TextView) findViewById(R.id.tv_detail_door_choose);
        metTip = (EditText) findViewById(R.id.et_order_tip);
        mbtnCommit = (Button) findViewById(R.id.btn_order_commit);
        mtvBottomAllMoney = (TextView) findViewById(R.id.tv_order_all_money);
        mtvOrderMoney = (TextView) findViewById(R.id.tv_order_money);
        mrlFloorContainer = (RelativeLayout) findViewById(R.id.rl_floor_container);
        mrlHangingContainer = (RelativeLayout) findViewById(R.id.rl_hanging_container);
        mrlOrderContainer = (RelativeLayout) findViewById(R.id.rl_order_money_container);
        mcvDoorContainer = (CardView) findViewById(R.id.cv_door_container);
        mcvColorContainer = (CardView) findViewById(R.id.cv_color_container);
        mcvTaimianContainer = (CardView) findViewById(R.id.cv_taimian_container);
        mivShow = (ImageView) findViewById(R.id.iv_order_show);
        lineFloor = findViewById(R.id.line_floor);
        lineHanging = findViewById(R.id.line_hanging);
        mrlStoreChoose = (RelativeLayout) findViewById(R.id.rl_order_store);
        mcvTipContainer = (CardView) findViewById(R.id.cv_tip_container);

        mtvNumMinus = (TextView) findViewById(R.id.tv_order_count_minus);
        mtvNumPlus = (TextView) findViewById(R.id.tv_order_count_plus);
        mtvNumCount = (TextView) findViewById(R.id.tv_detail_buy_num);

        mtvFloorMinus = (TextView) findViewById(R.id.tv_floor_minus);
        mtvFloorPlus = (TextView) findViewById(R.id.tv_floor_plus);
        mtvFloorCount = (TextView) findViewById(R.id.tv_floor_count);
        mtvHangMinus = (TextView) findViewById(R.id.tv_hanging_minus);
        mtvHangPlus = (TextView) findViewById(R.id.tv_hanging_plus);
        mtvHangCount = (TextView) findViewById(R.id.tv_hanging_count);

        Glide.with(this)
                .load(mimgUrl)
                .crossFade()
                .into(mivShow);

        mtvTitle = (TextView) findViewById(R.id.tv_order_goods_title);
        mtvAllMoney = (TextView) findViewById(R.id.tv_all_money);
        strGoodsName = mDetailInfo.getData().getDetail().getGoodsname();
        mtvTitle.setText(mDetailInfo.getData().getDetail().getGoodsname());
        allMoney = Float.valueOf(mDetailInfo.getData().getDetail().getPrice());
        mtvOrderMoneyShow.setText(mDetailInfo.getData().getDetail().getOrdermoney() + "");
        mtvOrderMoney.setText("￥" + mDetailInfo.getData().getDetail().getOrdermoney());
        mtvAllMoney.setText("￥" + mDetailInfo.getData().getDetail().getPrice());
        mtvBottomAllMoney.setText("￥" + mDetailInfo.getData().getDetail().getPrice());
        if (!mstoreName.equals("") && !mstaffName.equals("")){
            mtvChooseStore.setText(mstoreName + "  " + mstaffName + "(" + msalerNo + ")");
        }

        if (mDetailInfo.getData().getDetail().getSubtitle().equals("配件")){
            mtvOrderMinus.setVisibility(View.GONE);
            mtvOrderPlus.setVisibility(View.GONE);

            if (orderMode == 500){
                mtvNumMinus.setVisibility(View.GONE);
                mtvNumPlus.setVisibility(View.GONE);
                mcvTipContainer.setVisibility(View.GONE);
                mrlStoreChoose.setVisibility(View.GONE);
            }
        }

    }


    private void loadDatas(){

        if (mDetailInfo.getData().getDetail().getSubtitle().equals("配件")){
            mllNumContainer.setVisibility(View.VISIBLE);
            mtvOrderMoneyLabel.setText("支付金额：");
            mtvOrderMoneyBottom.setText("支付金额：");
            mtvOrderMoneyShow.setText(partMoney + "");
            mtvOrderMoney.setText("￥" + partMoney);
        }

        if (mDetailInfo.getData().getDetail().getSubtitle().equals("配件") || mDetailInfo.getData().getDetail().getSubtitle().equals("全屋定制")){
            mrlFloorContainer.setVisibility(View.GONE);
            mrlHangingContainer.setVisibility(View.GONE);
            mrlOrderContainer.setVisibility(View.GONE);
            mcvDoorContainer.setVisibility(View.GONE);
            mcvColorContainer.setVisibility(View.GONE);
            mcvTaimianContainer.setVisibility(View.GONE);
            mtvLeftBracket.setVisibility(View.GONE);
            mtvRightBracket.setVisibility(View.GONE);
            mtvBottomAllMoney.setVisibility(View.GONE);
            lineFloor.setVisibility(View.GONE);
            lineHanging.setVisibility(View.GONE);
        }

        if (mDetailInfo.getData().getDetail().getSubtitle().equals("") || mDetailInfo.getData().getDetail().getSubtitle().equals("双十一活动")){
            mrlFloorContainer.setVisibility(View.VISIBLE);
            mrlHangingContainer.setVisibility(View.VISIBLE);
            mcvDoorContainer.setVisibility(View.VISIBLE);
            mcvColorContainer.setVisibility(View.VISIBLE);
            mrlOrderContainer.setVisibility(View.VISIBLE);
            mcvTaimianContainer.setVisibility(View.VISIBLE);
            mtvBottomAllMoney.setVisibility(View.VISIBLE);
            mtvLeftBracket.setVisibility(View.VISIBLE);
            mtvRightBracket.setVisibility(View.VISIBLE);
            lineFloor.setVisibility(View.VISIBLE);
            lineHanging.setVisibility(View.VISIBLE);
            mllNumContainer.setVisibility(View.GONE);
            initDoor();
            initColor();
            initTaimian();
        }

    }

    private void initEvents(){
        mtvChooseStore.setOnClickListener(this);
        mtvChooseAdd.setOnClickListener(this);
        mtvSendAddress.setOnClickListener(this);
        mbtnCommit.setOnClickListener(this);
        mtvOrderPlus.setOnClickListener(this);
        mtvOrderMinus.setOnClickListener(this);
        mtvFloorMinus.setOnClickListener(this);
        mtvFloorPlus.setOnClickListener(this);
        mtvHangMinus.setOnClickListener(this);
        mtvHangPlus.setOnClickListener(this);
        mtvNumMinus.setOnClickListener(this);
        mtvNumPlus.setOnClickListener(this);

    }

    private void showBottomStore(){
        bottomStoreView = LayoutInflater.from(OrderActivity.this).inflate(R.layout.sheet_cart_store_layout, null);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderActivity.this, R.layout.spinner_tv_layout, Constants.CITY_ARRAY);
        mcitySpinner.setAdapter(adapter);
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


    private void showBottomAddress(){
        bottomAddView = LayoutInflater.from(OrderActivity.this).inflate(R.layout.sheet_cart_address_layout, null);
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
                startActivity(new Intent(OrderActivity.this, AddAddressActivity.class));
            }
        });
        mtvAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botAddDialog.isShowing()) {
                    botAddDialog.dismiss();
                }
                startActivity(new Intent(OrderActivity.this, AddAddressActivity.class));
            }
        });

        botAddDialog.setContentView(bottomAddView);

        if (mInfo != null){
            mlvAddress.setVisibility(View.VISIBLE);
            mAddAdapter = new ShoppingCartAddressAdapter(OrderActivity.this, mInfo);
            mlvAddress.setAdapter(mAddAdapter);
            mlvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mtvChooseAdd.setText(mInfo.getData().get(position).getReceivername() + "   " + mInfo.getData().get(position).getPhone());
                    mtvSendAddress.setText(mInfo.getData().get(position).getAddress());
                    strReceiverInfo = mInfo.getData().get(position).getReceivername() + ";" + mInfo.getData().get(position).getPhone() + ";"
                            + mInfo.getData().get(position).getAddress();
                    botAddDialog.dismiss();
                }
            });
        }

        botAddDialog.show();
    }

    /**
     * 载入配送地址
     */
    private void loadAddress(){

        HttpUtil.postAsyn(Constants.BASE_URL + "get_address.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mContainer, "请先检查您的网络！", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")) {
                        AddressInfo info = mGson.fromJson(response, AddressInfo.class);
                        if (info.getData() != null) {
                            mInfo = info;
                            mtvSendAddress.setVisibility(View.VISIBLE);
                            DefaultAddress d = hasDefault(info);
                            mtvChooseAdd.setText(info.getData().get(d.getPos()).getReceivername() + "    " + info.getData().get(d.getPos()).getPhone());
                            mtvSendAddress.setText(info.getData().get(d.getPos()).getAddress());
                            strReceiverInfo = info.getData().get(d.getPos()).getReceivername() + ";" + info.getData().get(d.getPos()).getPhone() + ";"
                                    + info.getData().get(d.getPos()).getAddress();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid)
        });

    }

    /**
     * 筛选出默认地址，没有就默认第一个地址为默认地址
     * @param info
     * @return
     */
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
     * 初始化门型
     */
    private void initDoor(){
        mRvDoor.setNestedScrollingEnabled(false);
        llDoorManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        mRvDoor.setLayoutManager(llDoorManager);
        mDoorAdapter = new DetailDoorAdapter(this, mDetailInfo);
        mRvDoor.setAdapter(mDoorAdapter);

        mDoorAdapter.setOnItemClickListener(new DetailDoorAdapter.OnItemClickListener() {

            @Override
            public void OnItemClick(View view, int position) {
                ivDoorSelected = (ImageView) view.findViewById(R.id.iv_detail_img);
                ivDoorSelected.setBackground(getResources().getDrawable(R.drawable.iv_background_shape));
                if (mDoorCurrentPos != position) {
                    if (ivDoorCurrent != null) {
                        ivDoorCurrent.setBackground(getResources().getDrawable(R.drawable.iv_background_shape_normal));
                    }
                }
                mDoorCurrentPos = position;
                ivDoorCurrent = ivDoorSelected;
                mTvDetailDoorSelectedName.setText(mDetailInfo.getData().getDoor().get(position).getDname());
                strDoor = mDetailInfo.getData().getDoor().get(position).getDname();
                strDoorId = mDetailInfo.getData().getDoor().get(position).getDid();
            }
        });
    }

    /**
     * 初始化颜色
     */
    private void initColor(){
        mRvColor.setNestedScrollingEnabled(false);
        final String colorName = (mDetailInfo.getData().getDetail().getTag().split(";"))[0];
        if (colorName.length() > 12){
            mTvDetailColorName.setText(colorName.substring(0, 10) + "... ：");
        }else {
            mTvDetailColorName.setText(colorName + "：");
        }
        llColorManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRvColor.setLayoutManager(llColorManager);
        mColorAdapter = new DetailColAdapter(this, mDetailInfo);
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
                mTvDetailColorSelectedName.setText(colorName + "/" + mDetailInfo.getData().getColors().get(position).getCname());
                strColor = mTvDetailColorSelectedName.getText().toString();
                strColorId = mDetailInfo.getData().getColors().get(position).getCid();
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
        mTaimianAdapter = new DetailTaiAdapter(this, mDetailInfo);
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
                mTvDetailTaimianSelectedName.setText(mDetailInfo.getData().getTaimian().get(position).getTname());
                strTaimian = mDetailInfo.getData().getTaimian().get(position).getTname();
                strTaimianId = mDetailInfo.getData().getTaimian().get(position).getTid();
            }
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
                    mStoreAdapter = new StoreAdapter(OrderActivity.this, info);
                    mlvStore.setAdapter(mStoreAdapter);

                    mlvStore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mstoreName = mStoreAdapter.getItem(position).getStoreName();
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

                    mStaffAdapter = new StaffAdapter(OrderActivity.this, info);
                    mlvStaff.setAdapter(mStaffAdapter);

                    mlvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mstaffName = mStaffAdapter.getItem(position).getTruename() + " (" +
                                    mStaffAdapter.getItem(position).getSalerno() + ")";
                            mstoreId = mStaffAdapter.getItem(position).getStoreId();
                            msalerNo = mStaffAdapter.getItem(position).getSalerno();
                            mtvChooseStore.setText(mstoreName + "   " + mstaffName);
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
            case R.id.btn_order_commit:

                if (mDetailInfo.getData().getDetail().getSubtitle().equals("配件")){
                    submitPartsOrder();
                }

                if (mDetailInfo.getData().getDetail().getSubtitle().equals("") && orderMode != 500){
                    submitMainOrder();
                }

                if (mDetailInfo.getData().getDetail().getSubtitle().equals("双十一活动") && orderMode != 500){
                    submitMainOrder();
                }

                if (mDetailInfo.getData().getDetail().getSubtitle().equals("全屋定制")){
                    submitHomeOrder();
                }

                if (orderMode == 500){
                    submitEventOrder();
                }

                break;
            case R.id.tv_order_money_minus:
                if (mDetailInfo.getData().getDetail().getSubtitle().equals("全屋定制")){
                    if (morderMoney > 3000){
                        morderMoney = morderMoney - 1000;
                    }
                }else {
                    if (morderMoney > 2000){
                        morderMoney = morderMoney - 1000;
                    }
                }
                mtvOrderMoney.setText("￥" + morderMoney);
                mtvOrderMoneyShow.setText(morderMoney + "");

                break;
            case R.id.tv_order_money_plus:

                morderMoney = morderMoney + 1000;
                mtvOrderMoney.setText("￥" + morderMoney);
                mtvOrderMoneyShow.setText(morderMoney + "");
                break;

            case R.id.tv_floor_minus:   //地柜至少3m
                floorMinus();

                break;

            case R.id.tv_floor_plus:
                floorPlus();

                break;

            case R.id.tv_hanging_minus: //吊柜至少1m
                hangMinus();

                break;

            case R.id.tv_hanging_plus:
                hangPlus();

                break;

            case R.id.tv_order_count_minus:
                if (orderNum > 1){
                    orderNum--;
                    mtvNumCount.setText(orderNum + "");
                    mtvOrderMoneyShow.setText(partMoney * orderNum + "");
                    mtvOrderMoney.setText("￥" + partMoney * orderNum);
                }
                break;

            case R.id.tv_order_count_plus:
                orderNum++;
                mtvNumCount.setText(orderNum + "");
                mtvOrderMoneyShow.setText(partMoney * orderNum + "");
                mtvOrderMoney.setText("￥" + partMoney * orderNum);
                break;
        }
    }

    private void prinrLine(){
        System.out.println("=============");
        System.out.println("吊柜米数：" + mtvHangCount.getText().toString() + "米");
        System.out.println("地柜米数：" + mtvFloorCount.getText().toString() + "米");
        System.out.println("总金额：" + mtvAllMoney.getText().toString());
        System.out.println("=============");
    }

    /**
     * 减少地柜米数
     */
    private void floorMinus(){

        if (mFloorMet > 3 && mFloorMet <= 6){
            mFloorMet = mFloorMet - 0.5;
            allMoney = allMoney -1000;

        }else if (mFloorMet > 6 && mFloorMet <= 10){
            mFloorMet = mFloorMet - 1.0;
            allMoney = allMoney -2000;
        }

        mtvFloorCount.setText(mFloorMet + "");
        mtvAllMoney.setText("￥" + allMoney);
        mtvBottomAllMoney.setText("￥" + allMoney);
    }
    /**
     * 增加地柜米数
     */
    private void floorPlus(){

        if (mFloorMet >= 3 && mFloorMet < 6){
            mFloorMet = mFloorMet + 0.5;
            allMoney = allMoney + 1000;

        }else if (mFloorMet >= 6 && mFloorMet < 10){
            mFloorMet = mFloorMet + 1.0;
            allMoney = allMoney + 2000;
        }
        mtvFloorCount.setText(mFloorMet + "");
        mtvAllMoney.setText("￥" + allMoney);
        mtvBottomAllMoney.setText("￥" + allMoney);
    }
    /**
     * 减少吊柜米数
     */
    private void hangMinus(){

        if (mHangMet > 1 && mHangMet <= 3){
            mHangMet = mHangMet - 0.5;
            allMoney = allMoney - 500;

        }else if (mHangMet > 3 && mHangMet <= 5){
            mHangMet = mHangMet - 1.0;
            allMoney = allMoney -1000;
        }

        mtvHangCount.setText(mHangMet + "");
        mtvAllMoney.setText("￥" + allMoney);
        mtvBottomAllMoney.setText("￥" + allMoney);
    }
    /**
     * 增加吊柜米数
     */
    private void hangPlus(){

        if (mHangMet >= 1 && mHangMet < 3){
            mHangMet = mHangMet + 0.5;
            allMoney = allMoney + 500;

        }else if (mHangMet >= 3 && mHangMet < 5){
            mHangMet = mHangMet + 1.0;
            allMoney = allMoney + 1000;
        }
        mtvHangCount.setText(mHangMet + "");
        mtvAllMoney.setText("￥" + allMoney);
        mtvBottomAllMoney.setText("￥" + allMoney);
    }

    /**
     * 提交全屋定制订单
     */
    private void submitHomeOrder(){

        if (mstaffName.equals("") || mstoreName.equals("")){
            showSnackbar("请选择门店导购！");
            return;
        }

        if (strReceiverInfo.equals("")){
            showSnackbar("请填写收货人信息！");
            return;
        }

        if (!TextUtils.isEmpty(metTip.getText().toString())){
            strTip = metTip.getText().toString();
        }

        //strorderMoney = mtvOrderMoney.getText().toString();
        strallMoney = mtvAllMoney.getText().toString();

        Intent intent = new Intent(OrderActivity.this, CommitOrderActivity.class);
        intent.putExtra("ordermode", 300);
        intent.putExtra("gid", mGid);
        intent.putExtra("goodsName", strGoodsName);
        intent.putExtra("imgurl", mimgUrl);
        intent.putExtra("receiverInfo", strReceiverInfo);
        intent.putExtra("staffName", mstaffName);
        intent.putExtra("storeName", mstoreName);
        intent.putExtra("orderMoney", mtvOrderMoney.getText().toString());
        intent.putExtra("allMoney", strallMoney);
        intent.putExtra("tip", strTip);
        intent.putExtra("storeid", mstoreId);
        intent.putExtra("salerNo", msalerNo);
        intent.putExtra("city", mcity);

        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent);
        }else {
            View sharedView = mivShow;
            String transitionName = "transview";
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(OrderActivity.this, sharedView, transitionName);
            startActivityForResult(intent, 100, transitionActivityOptions.toBundle());
        }

    }

    /**
     * 提交配件订单 配件的总价就是预约价
     */
    private void submitPartsOrder(){
        if (mstaffName.equals("") || mstoreName.equals("")){
            showSnackbar("请选择门店导购！");
            return;
        }

        if (strReceiverInfo.equals("")){
            showSnackbar("请填写收货人信息！");
            return;
        }

        if (!TextUtils.isEmpty(metTip.getText().toString())){
            strTip = metTip.getText().toString();
        }

        //strorderMoney = mtvOrderMoney.getText().toString();
        //strallMoney = mtvAllMoney.getText().toString();
        strallMoney = mtvOrderMoney.getText().toString();

        Intent intent = new Intent(OrderActivity.this, CommitOrderActivity.class);
        intent.putExtra("ordermode", 100);
        intent.putExtra("gid", mGid);
        intent.putExtra("goodsName", strGoodsName);
        intent.putExtra("imgurl", mimgUrl);
        intent.putExtra("receiverInfo", strReceiverInfo);
        intent.putExtra("staffName", mstaffName);
        intent.putExtra("storeName", mstoreName);
        intent.putExtra("orderMoney", mtvOrderMoney.getText().toString());
        intent.putExtra("allMoney", strallMoney);
        intent.putExtra("tip", strTip);
        intent.putExtra("storeid", mstoreId);
        intent.putExtra("salerNo", msalerNo);
        intent.putExtra("city", mcity);
        intent.putExtra("num", orderNum);

        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent);
        }else {
            View sharedView = mivShow;
            String transitionName = "transview";
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(OrderActivity.this, sharedView, transitionName);
            startActivityForResult(intent, 100, transitionActivityOptions.toBundle());
        }
    }

    /**
     * 提交橱柜订单
     */
    private void submitMainOrder(){

        if (mstaffName.equals("") || mstoreName.equals("")){
            showSnackbar("请选择门店导购！");
            return;
        }

        if (strReceiverInfo.equals("")){
            showSnackbar("请填写收货人信息！");
            return;
        }

        if (strDoor.equals("")){
            showSnackbar("请选择商品门型！");
            return;
        }

        if (strColor.equals("")){
            showSnackbar("请选择商品颜色！");
            return;
        }

        if (strTaimian.equals("")){
            showSnackbar("请选择商品台面！");
            return;
        }

        if (!TextUtils.isEmpty(metTip.getText().toString())){
            strTip = metTip.getText().toString();
        }

        //strorderMoney = mtvOrderMoney.getText().toString();
        strallMoney = mtvAllMoney.getText().toString();

        Intent intent = new Intent(OrderActivity.this, CommitOrderActivity.class);

        intent.putExtra("ordermode", 200);
        intent.putExtra("gid", mGid);
        intent.putExtra("goodsName", strGoodsName);
        intent.putExtra("imgurl", mimgUrl);
        intent.putExtra("receiverInfo", strReceiverInfo);
        intent.putExtra("staffName", mstaffName);
        intent.putExtra("storeName", mstoreName);
        intent.putExtra("door", strDoor);
        intent.putExtra("color", strColor);
        intent.putExtra("taimian", strTaimian);
        intent.putExtra("orderMoney", mtvOrderMoney.getText().toString());
        intent.putExtra("allMoney", strallMoney);
        intent.putExtra("tip", strTip);
        intent.putExtra("storeid", mstoreId);
        intent.putExtra("salerNo", msalerNo);
        intent.putExtra("doorid", strDoorId);
        intent.putExtra("colorid", strColorId);
        intent.putExtra("taimianid", strTaimianId);
        intent.putExtra("mishu1", mtvHangCount.getText().toString());
        intent.putExtra("mishu2", mtvFloorCount.getText().toString());
        intent.putExtra("city", mcity);
        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent);
        }else {
            View sharedView = mivShow;
            String transitionName = "transview";
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(OrderActivity.this, sharedView, transitionName);
            startActivityForResult(intent,100, transitionActivityOptions.toBundle());
        }

    }

    /**
     * 提交活动订单
     */
    private void submitEventOrder(){

        mstoreName = "无";
        mstaffName = "无";
        if (mstaffName.equals("") || mstoreName.equals("")){
            showSnackbar("请选择门店导购！");
            return;
        }

        if (strReceiverInfo.equals("")){
            showSnackbar("请填写收货人信息！");
            return;
        }

        if (!TextUtils.isEmpty(metTip.getText().toString())){
            strTip = metTip.getText().toString();
        }

        //strorderMoney = mtvOrderMoney.getText().toString();
        //strallMoney = mtvAllMoney.getText().toString();
        strallMoney = mtvOrderMoney.getText().toString();

        Intent intent = new Intent(OrderActivity.this, CommitOrderActivity.class);
        intent.putExtra("ordermode", 500);
        intent.putExtra("gid", mGid);
        intent.putExtra("goodsName", strGoodsName);
        intent.putExtra("imgurl", mimgUrl);
        intent.putExtra("receiverInfo", strReceiverInfo);
        intent.putExtra("staffName", mstaffName);
        intent.putExtra("storeName", mstoreName);
        intent.putExtra("orderMoney", mtvOrderMoney.getText().toString());
        intent.putExtra("allMoney", strallMoney);
        intent.putExtra("tip", strTip);
        intent.putExtra("storeid", "0");
        intent.putExtra("salerNo", "001");
        intent.putExtra("city", (String)SharedPreUtil.getData(OrderActivity.this, "localCity", "南京"));
        intent.putExtra("num", 1);

        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent);
        }else {
            View sharedView = mivShow;
            String transitionName = "transview";
            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(OrderActivity.this, sharedView, transitionName);
            startActivityForResult(intent, 100, transitionActivityOptions.toBundle());
        }
    }

    private void showSnackbar(String str){
        Snackbar.make(mContainer, str, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApp addressApp =(MyApp) getApplicationContext();
        Object obj2 = addressApp.GetActivityIntent(Constants.ADDRESS_REFRESH);
        if (null != obj2) {
            int iValue = (Integer) obj2;

            if (iValue == 1) {  //1为刷新地址

                uid = addressApp.getUser().getId();
                //loadDatas();
                loadAddress();
                //refreshDatas();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200 && data != null){
            finish();
        }
    }
}
