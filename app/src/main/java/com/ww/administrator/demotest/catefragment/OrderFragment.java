package com.ww.administrator.demotest.catefragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.adapter.OrderFragmentAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.OrderResultInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.pay.EventPayActivity;
import com.ww.administrator.demotest.pay.PayActivity;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/28.
 */
public class OrderFragment extends BaseFragment {

    String uid = "-1";
    SwipeRefreshLayout msrlOrder;
    RecyclerView mrvOrder;
    TextView mtvOrderNo;
    ProgressWheel mpbOrder;

    OrderFragmentAdapter mAdapter;
    Gson mGson = new Gson();
    LinearLayoutManager manager;
    OrderResultInfo.Databean mInfo;
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
        HttpUtil.postAsyn(Constants.BASE_URL + "get_userorder_list.php", new HttpUtil.ResultCallback<String>() {
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
                        OrderResultInfo info = mGson.fromJson(response, OrderResultInfo.class);
                        if (info.getCode().equals("200")) {
                            mtvOrderNo.setVisibility(View.GONE);
                            mrvOrder.setVisibility(View.VISIBLE);
                            mAdapter = new OrderFragmentAdapter(getActivity(), info);
                            mrvOrder.setAdapter(mAdapter);
                            mAdapter.setOnPayClickListener(new OrderFragmentAdapter.OnPayClickListener() {
                                @Override
                                public void getItem(OrderResultInfo.Databean info) {

                                    if (info.getGoodsname().equals("圣诞欢乐购")) {
                                        mInfo = info;
                                        hasBuy();

                                    } else {
                                        Intent intent = new Intent(getActivity(), PayActivity.class);
                                        intent.putExtra("title", info.getGoodsname());
                                        intent.putExtra("ordNum", Integer.parseInt(info.getSuperbillid()));
                                        intent.putExtra("payMoney", (int) (Float.parseFloat(info.getSchedprice()) * 100));
                                        startActivityForResult(intent, 100);
                                    }

                                }
                            });
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
                new HttpUtil.Param("action", "all")
        });
    }
    /**
     * 判断是否购买过礼品券
     */
    private void hasBuy(){
        HttpUtil.postAsyn(Constants.BASE_URL + "onebuy_has.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(getActivity(), "请检查您的网络！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    Intent intent = new Intent(getActivity(), EventPayActivity.class);
                    intent.putExtra("title", mInfo.getGoodsname());
                    intent.putExtra("ordNum", Integer.parseInt(mInfo.getSuperbillid()));
                    intent.putExtra("payMoney", (int) (Float.parseFloat(mInfo.getSchedprice()) * 100));
                    startActivityForResult(intent, 100);

                }else if (info.getCode().equals("401")){
                    Toast.makeText(getActivity(), "您已抢购过！可以在 '我的礼券' 查看", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getActivity(), info.getInfo().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("goodsId", 3593 + "")
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            loadDatas();
            refreshDatas();
        }
    }
}
