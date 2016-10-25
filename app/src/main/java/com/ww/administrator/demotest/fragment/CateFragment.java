package com.ww.administrator.demotest.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.About4Acitivity;
import com.ww.administrator.demotest.AboutActivity;
import com.ww.administrator.demotest.BaseFragment;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.SearchActivity;
import com.ww.administrator.demotest.adapter.CateContentAdapter;
import com.ww.administrator.demotest.adapter.CateExpandableListAdapter;
import com.ww.administrator.demotest.model.CateInfo;
import com.ww.administrator.demotest.model.ProductListInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/19.
 */
public class CateFragment extends BaseFragment {
    private Toolbar mtb;
    ProgressWheel mpbLoad, mpbContent;
    CateExpandableListAdapter mAdapter;

    private ArrayList<CateInfo.DataBean> mListTitle = new ArrayList<>();
    private ArrayList<CateInfo.DataBean> mListContent = new ArrayList<>();

    ExpandableListView mlvCate;

    RecyclerView mrvContent;
    CateContentAdapter mContentAdapter;
    TextView mtvNoLabel;

    RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    @Override
    protected void getArgs() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//在fragment中设置toolbar上的menuitem可见

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cate;
    }

    @Override
    protected void initViews(View view) {
        mtb = (Toolbar) view.findViewById(R.id.tb_common);
        mpbLoad = (ProgressWheel) view.findViewById(R.id.pb_common);
        mtb.setTitle("分类");
        //((MainActivity)getActivity()).setSupportActionBar(mtb);
        mtb.inflateMenu(R.menu.main);
        mtb.setNavigationIcon(R.mipmap.logo);
        mtb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_search:

                        Intent intent = new Intent(getActivity(), SearchActivity.class);
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(intent);
                        }else {
                            View sharedView = getView().findViewById(R.id.tb_common).findViewById(R.id.menu_search);
                            String transitionName = "img_back";
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                            startActivity(intent, transitionActivityOptions.toBundle());
                        }


                        return true;
                    /*case R.id.menu_locate:
                        //startActivity(new Intent(getActivity(), LocatCityActivity.class));
                        return true;*/

                    case R.id.menu_about:
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP){
                            startActivity(new Intent(getActivity(), About4Acitivity.class));
                        }else {

                            startActivity(new Intent(getActivity(), AboutActivity.class));
                        }

                        return true;
                }
                return false;
            }
        });
        mlvCate = (ExpandableListView) view.findViewById(R.id.elv_cate);
        mpbContent = (ProgressWheel) view.findViewById(R.id.pb_content);
        mrvContent = (RecyclerView) view.findViewById(R.id.rv_cate_show);
        mtvNoLabel = (TextView) view.findViewById(R.id.tv_cate_no_content);
        mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mrvContent.setLayoutManager(mlayoutManager);
        mpbLoad.setVisibility(View.VISIBLE);
        mrvContent.setVisibility(View.GONE);
        mlvCate.setVisibility(View.GONE);
        mpbContent.setVisibility(View.GONE);
        mtvNoLabel.setVisibility(View.GONE);

    }

    @Override
    protected void doBusiness() {
        loadDatas();
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.view_row, R.id.header_text, array);
    }


    private void loadDatas(){
        HttpUtil.postAsyn(Constants.BASE_URL + "get_class.php", new HttpUtil.ResultCallback<CateInfo>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CateInfo info) {
                mpbLoad.setVisibility(View.GONE);
                mlvCate.setVisibility(View.VISIBLE);
                if (info.getCode().toString().equals("200")) {
                    for (int i = 0; i < info.getData().size(); i++) {
                        if (info.getData().get(i).getParentid().equals("0")) {   //得到种类标题
                            mListTitle.add(info.getData().get(i));
                        } else { //得到该类别下的内容
                            mListContent.add(info.getData().get(i));
                        }
                    }

                    //将所有内容封装进map
                    Map<String, ArrayList<CateInfo.DataBean>> map = new HashMap<String, ArrayList<CateInfo.DataBean>>();
                    for (int i = 0; i < mListTitle.size(); i++) {

                        ArrayList<CateInfo.DataBean> list = new ArrayList<CateInfo.DataBean>();
                        for (int j = 0; j < mListContent.size(); j++) {
                            if (mListTitle.get(i).getId().equals(mListContent.get(j).getParentid())) {
                                list.add(mListContent.get(j));
                            }
                        }
                        map.put(mListTitle.get(i).getName(), list);
                    }

                    mAdapter = new CateExpandableListAdapter(getActivity(), mListTitle, map);
                    mlvCate.setAdapter(mAdapter);
                    mlvCate.expandGroup(0);
                    loadContentDatas(mAdapter.getChild(0, 0).getId());
                    mlvCate.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                            mtvNoLabel.setVisibility(View.GONE);
                            mrvContent.setVisibility(View.GONE);
                            mpbContent.setVisibility(View.VISIBLE);
                            String cid = mAdapter.getChild(groupPosition, childPosition).getId();
                            loadContentDatas(cid);
                            return false;
                        }
                    });


                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("action", "class")
        });
    }

    /**
     * 点击分类id载入分类商品列表
     * @param cid
     */
    private void loadContentDatas(String cid){

        HttpUtil.postAsyn(Constants.BASE_URL + "product_list.php", new HttpUtil.ResultCallback<ProductListInfo>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbContent.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(ProductListInfo info) {

                mpbContent.setVisibility(View.GONE);
                if (info.getCode().equals("200")){
                    if (info.getData().size() != 0){
                        mrvContent.setVisibility(View.VISIBLE);
                        mContentAdapter = new CateContentAdapter(getActivity(), info);

                        mrvContent.setAdapter(mContentAdapter);
                    }else {
                        mtvNoLabel.setVisibility(View.VISIBLE);
                    }
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("cid", cid)
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
       /* menu.removeItem(R.id.menu_locate);*/
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_search) {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);

    }


}
