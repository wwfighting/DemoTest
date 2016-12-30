package com.ww.administrator.demotest.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.CouponsFragmentAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.CouponsPicInfo;
import com.ww.administrator.demotest.model.CouponsResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CouponsOverFragment extends BaseFragment{

    String uid = "-1";
    SwipeRefreshLayout msrlOrder;
    RecyclerView mrvOrder;
    TextView mtvOrderNo;
    ProgressWheel mpbOrder;

    CouponsFragmentAdapter mAdapter;
    Gson mGson = new Gson();
    LinearLayoutManager manager;

    String gids = "";
    CouponsResultInfo info;

    @Override
    protected void getArgs() {
        uid = ((MyApp) getActivity().getApplicationContext()).getUser().getId();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_order_layout;
    }

    @Override
    protected void initViews(View view) {
        msrlOrder = (SwipeRefreshLayout) view.findViewById(R.id.srl_order);
        mrvOrder = (RecyclerView) view.findViewById(R.id.rv_order);
        mtvOrderNo = (TextView) view.findViewById(R.id.tv_order_no);
        mpbOrder = (ProgressWheel) view.findViewById(R.id.pb_common);
        mtvOrderNo.setVisibility(View.GONE);
        mpbOrder.setVisibility(View.VISIBLE);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrvOrder.setLayoutManager(manager);
        msrlOrder.setColorSchemeResources(R.color.day_colorPrimary, R.color.day_colorPrimaryDark,
                R.color.day_colorAccent);
        mtvOrderNo.setText("暂无优惠券");
    }

    @Override
    protected void doBusiness() {
        loadDatas();
        refreshDatas();
    }

    /**
     * 刷新
     */
    public void refreshDatas(){
        msrlOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDatas();
                msrlOrder.setRefreshing(false);
            }
        });
    }

    private void loadDatas() {
        HttpUtil.postAsyn(Constants.BASE_URL + "get_coupons_list1.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbOrder.setVisibility(View.GONE);
                mrvOrder.setVisibility(View.GONE);
                mtvOrderNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response) {
                mpbOrder.setVisibility(View.GONE);

                try {
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")) {
                        info = mGson.fromJson(response, CouponsResultInfo.class);
                        if (info.getCode().equals("200")) {
                            mtvOrderNo.setVisibility(View.GONE);
                            mrvOrder.setVisibility(View.VISIBLE);

                            for (int i = 0; i < info.getData().size(); i++) {
                                gids = gids + info.getData().get(i).getLpGoodid() + ",";
                            }

                            loadPic();

                        } else {
                            mrvOrder.setVisibility(View.GONE);
                            mtvOrderNo.setVisibility(View.VISIBLE);
                            mtvOrderNo.setText(info.getInfo());
                        }
                    } else {
                        //有误或者无数据
                        mrvOrder.setVisibility(View.GONE);
                        mtvOrderNo.setVisibility(View.VISIBLE);
                        mtvOrderNo.setText(jsonRoot.getString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("mode", "3")
        });
    }

    private void loadPic(){
        HttpUtil.postAsyn(Constants.BASE_URL + "get_coupons.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbOrder.setVisibility(View.GONE);
                mrvOrder.setVisibility(View.GONE);
                mtvOrderNo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(String response) {
                mpbOrder.setVisibility(View.GONE);

                try {
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")) {
                        CouponsPicInfo infos = mGson.fromJson(response, CouponsPicInfo.class);
                        if (infos.getCode().equals("200")) {
                            mtvOrderNo.setVisibility(View.GONE);
                            mrvOrder.setVisibility(View.VISIBLE);
                            mAdapter = new CouponsFragmentAdapter(getActivity(), info, infos);
                            mrvOrder.setAdapter(mAdapter);

                        }else {
                            mrvOrder.setVisibility(View.GONE);
                            mtvOrderNo.setVisibility(View.VISIBLE);
                            mtvOrderNo.setText(info.getInfo());
                        }
                    } else {
                        //有误或者无数据
                        mrvOrder.setVisibility(View.GONE);
                        mtvOrderNo.setVisibility(View.VISIBLE);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("gids", gids),
        });
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            loadDatas();
            refreshDatas();
        }
    }*/
}
