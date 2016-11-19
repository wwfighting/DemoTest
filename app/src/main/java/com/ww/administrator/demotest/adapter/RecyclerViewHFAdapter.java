package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ww.administrator.demotest.ProductListActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.GoodsInfo;
import com.ww.administrator.demotest.util.DisplayUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class RecyclerViewHFAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_FOOTER = 1;

    private static final int TYPE_CATE_TITLE= 2; //八个种类图标

    private static final int TYPE_EVENT_TITLE = 3; //Banner下的活动图

    private static final int TYPE_NEWGOODS_TITLE = 4; //新品推荐标题

    private static final int TYPE_NEWGOODS_CONTENT = 5; //新品推荐内容

    private static final int TYPE_HOTGOODS_TITLE = 6; //热门商品标题

    private static final int TYPE_HOTGOODS_CONTENT = 7; //热门商品内容

    private static final int TYPE_HOMEGOODS_TITLE = 8; //全屋定制标题

    private static final int TYPE_HOMEGOODS_CONTENT = 9; //全屋定制内容

    private View mHeaderView;

    private View mFooterView;

    private View mEmptyView;

    HomeGoodsAdapter mAdapter;
    HomeCateAdapter mCateAdapter;

    GoodsInfo mNewList, mHotList, mHomeList;
    Context mContext;
    String mCity = "";

    private List<String> items;

    public RecyclerViewHFAdapter(Context context,GoodsInfo newInfo, GoodsInfo hotInfo, String city) {
        mNewList = newInfo;
        mHotList = hotInfo;
        mContext = context;
        mCity = city;
    }

    public RecyclerViewHFAdapter(Context context,GoodsInfo newInfo, GoodsInfo hotInfo, GoodsInfo homeInfo, String city) {
        mNewList = newInfo;
        mHotList = hotInfo;
        mHomeList = homeInfo;
        mContext = context;
        mCity = city;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = mHeaderView;
            return new HeaderHolder(v);

        }else if (viewType == TYPE_EVENT_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_event, parent, false);
            return new EventViewHolder(v);

        }else if (viewType == TYPE_CATE_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_layout, parent, false);
            return new HomeCateViewHolder(v);

        }else if (viewType == TYPE_NEWGOODS_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_title, parent, false);
            return new NewGoodsTitleHolder(v);

        }else if (viewType == TYPE_NEWGOODS_CONTENT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_layout, parent, false);
            return new NewGoodsContentHolder(v);

        }else if (viewType == TYPE_HOTGOODS_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_title, parent, false);
            return new HotGoodsTitleHolder(v);

        }else if (viewType == TYPE_HOTGOODS_CONTENT) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_layout, parent, false);
            return new HotGoodsContentHolder(v);

        }else if (viewType == TYPE_HOMEGOODS_TITLE){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_title, parent, false);
            return new HomeGoodsTitleHolder(v);
        }else if (viewType == TYPE_HOMEGOODS_CONTENT){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_layout, parent, false);
            return new HomeGoodsContentHolder(v);

        }else if (viewType == TYPE_FOOTER) {
            View v = mFooterView;
            return new FooterHolder(v);
        }else if (viewType == TYPE_NEWGOODS_CONTENT) {
            View v = mEmptyView;
            return new EmptyHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {

        }else if (holder instanceof EventViewHolder) {
            ((EventViewHolder) holder).mivEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "跳转到活动页", Toast.LENGTH_SHORT).show();
                }
            });

        }else if (holder instanceof HomeCateViewHolder) {
            if (((HomeCateViewHolder) holder).mNewRecyclerView.getAdapter() == null){
                mCateAdapter = new HomeCateAdapter(mContext);
                ((HomeCateViewHolder) holder).mNewRecyclerView.setAdapter(mCateAdapter);
            }else {
                ((HomeCateViewHolder) holder).mNewRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }else if (holder instanceof NewGoodsTitleHolder) {
            ((NewGoodsTitleHolder) holder).mNewTvTitle.setText("新品推荐");
            ((NewGoodsTitleHolder) holder).mNewTvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新品推荐更多的点击跳转 isRecom = 1
                    Intent intent = new Intent();
                    intent.setClass(mContext, ProductListActivity.class);
                    intent.putExtra("isRecom", "1");
                    mContext.startActivity(intent);

                }
            });
        }else if (holder instanceof NewGoodsContentHolder) {
            if (((NewGoodsContentHolder) holder).mNewRecyclerView.getAdapter() == null){
                mAdapter = new HomeGoodsAdapter(mContext, mNewList);
                ((NewGoodsContentHolder) holder).mNewRecyclerView.setAdapter(mAdapter);
            }else {
                ((NewGoodsContentHolder) holder).mNewRecyclerView.getAdapter().notifyDataSetChanged();
            }


        }else if (holder instanceof HotGoodsTitleHolder) {
            ((HotGoodsTitleHolder) holder).mHotTvTitle.setText("热门商品");
            ((HotGoodsTitleHolder) holder).mHotTvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //热门商品更多的点击跳转 isRecom = 2
                    Intent intent = new Intent();
                    intent.setClass(mContext, ProductListActivity.class);
                    intent.putExtra("isRecom","2");
                    mContext.startActivity(intent);

                }
            });
        }else if (holder instanceof HotGoodsContentHolder) {
            if (((HotGoodsContentHolder) holder).mHotRecyclerView.getAdapter() == null){
                mAdapter = new HomeGoodsAdapter(mContext, mHotList);
                ((HotGoodsContentHolder) holder).mHotRecyclerView.setAdapter(mAdapter);
                if (getItemCount() == 7){
                    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, DisplayUtil.dip2px(mContext, 50));
                    ((HotGoodsContentHolder) holder).mHotRecyclerView.setLayoutParams(lp);
                }
            }else {
                ((HotGoodsContentHolder) holder).mHotRecyclerView.getAdapter().notifyDataSetChanged();
            }

        }else if (holder instanceof HomeGoodsTitleHolder) {
            ((HomeGoodsTitleHolder) holder).mHomeTvTitle.setText("全屋热卖");
            ((HomeGoodsTitleHolder) holder).mHomeTvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //全屋定制更多的点击跳转 isRecom = 3
                    Intent intent = new Intent();
                    intent.setClass(mContext, ProductListActivity.class);
                    intent.putExtra("isRecom","3");
                    mContext.startActivity(intent);

                }
            });
        }else if (holder instanceof HomeGoodsContentHolder) {
            if (((HomeGoodsContentHolder) holder).mHomeRecyclerView.getAdapter() == null){
                if (getItemCount() == 7){
                    RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, DisplayUtil.dip2px(mContext, 50));
                    ((HomeGoodsContentHolder) holder).mHomeRecyclerView.setLayoutParams(lp);
                }
                mAdapter = new HomeGoodsAdapter(mContext, mHomeList);
                ((HomeGoodsContentHolder) holder).mHomeRecyclerView.setAdapter(mAdapter);
            }else {
                ((HomeGoodsContentHolder) holder).mHomeRecyclerView.getAdapter().notifyDataSetChanged();
            }

        }else if (holder instanceof FooterHolder) {

        }else if (holder instanceof EmptyHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return 7;
       /* if (!mCity.equals("南京")){
            return 9;
        }else {
            return 7;
        }*/

    }

    @Override
    public int getItemViewType(int position) {
        if (!mCity.equals("南京")){
            if (position == 0){
                return TYPE_HEADER;
            }else if (position == 1){
                return TYPE_EVENT_TITLE;
            }else if (position == 2){
                return TYPE_CATE_TITLE;
            }else if (position == 3){
                return TYPE_NEWGOODS_TITLE;
            }else if (position == 4){
                return TYPE_NEWGOODS_CONTENT;
            }else if (position == 5){
                return TYPE_HOMEGOODS_TITLE;
                //return TYPE_HOTGOODS_TITLE;
            }else {
                return TYPE_HOMEGOODS_CONTENT;
                //return TYPE_HOTGOODS_CONTENT;
            }/*else if (position == 7){
                return TYPE_HOMEGOODS_TITLE;
            }else {
                return TYPE_HOMEGOODS_CONTENT;
            }*/
        }else {
            if (position == 0){
                return TYPE_HEADER;
            }else if (position == 1){
                return TYPE_EVENT_TITLE;
            }else if (position == 2){
                return TYPE_CATE_TITLE;
            } else if (position == 3){
                return TYPE_NEWGOODS_TITLE;
            }else if (position == 4){
                return TYPE_NEWGOODS_CONTENT;
            }else if (position == 5){
                return TYPE_HOTGOODS_TITLE;
            }else {
                return TYPE_HOTGOODS_CONTENT;
            }
        }

    }

    private int getHeadViewSize() {
        return mHeaderView == null ? 0 : 1;
    }

    private int getFooterViewSize() {
        return mFooterView == null ? 0 : 1;
    }

    private String getItem(int position) {
        return items.get(position - getHeadViewSize());
    }


    //add a header to the adapter
    public void addHeader(View header) {
        mHeaderView = header;
        notifyItemInserted(0);
    }

    //remove a header from the adapter
    public void removeHeader(View header) {
        notifyItemRemoved(0);
        mHeaderView = null;
    }

    //add a footer to the adapter
    public void addFooter(View footer) {
        mFooterView = footer;
        notifyItemInserted(getHeadViewSize() + items.size());
    }

    //remove a footer from the adapter
    public void removeFooter(View footer) {
        notifyItemRemoved(getHeadViewSize() + items.size());
        mFooterView = null;
    }

    //add data
    public void addDatas(List<String> data) {
        items.addAll(data);
        notifyItemInserted(getHeadViewSize() + items.size() - 1);
    }

    //add data
    public void addData(String data) {
        items.add(data);
        notifyItemInserted(getHeadViewSize() + items.size() - 1);
    }

    //refresh data
    public void refreshData(List<String> datas){
        items.clear();
        addDatas(datas);
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        notifyItemInserted(0);
    }

    //头部Banner的ViewHolder
    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);

        }
    }

    //Banner下活动图ViewHolder
    class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView mivEvent;

        public EventViewHolder(View itemView) {
            super(itemView);
            mivEvent = (ImageView) itemView.findViewById(R.id.iv_home_event);
        }
    }

    //首页种类ViewHolder
    class HomeCateViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mNewRecyclerView;

        public HomeCateViewHolder(View itemView) {
            super(itemView);
            mNewRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 4);
            layoutManager.setAutoMeasureEnabled(true);
            mNewRecyclerView.setLayoutManager(layoutManager);
        }
    }

    //新品推荐标题ViewHolder
    class NewGoodsTitleHolder extends RecyclerView.ViewHolder {
        TextView mNewTvTitle, mNewTvMore;

        public NewGoodsTitleHolder(View itemView) {
            super(itemView);
            mNewTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mNewTvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }

    //新品推荐内容ViewHolder
    class NewGoodsContentHolder extends RecyclerView.ViewHolder {
        RecyclerView mNewRecyclerView;

        public NewGoodsContentHolder(View itemView) {
            super(itemView);
            mNewRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            layoutManager.setAutoMeasureEnabled(true);
            mNewRecyclerView.setLayoutManager(layoutManager);
        }
    }
    //热门商品标题ViewHolder
    class HotGoodsTitleHolder extends RecyclerView.ViewHolder {
        TextView mHotTvTitle, mHotTvMore;

        public HotGoodsTitleHolder(View itemView) {
            super(itemView);
            mHotTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mHotTvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }
    //热门商品内容ViewHolder
    class HotGoodsContentHolder extends RecyclerView.ViewHolder {
        RecyclerView mHotRecyclerView;

        public HotGoodsContentHolder(View itemView) {
            super(itemView);
            mHotRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            mHotRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            layoutManager.setAutoMeasureEnabled(true);
            mHotRecyclerView.setLayoutManager(layoutManager);
        }
    }

    //全屋定制标题ViewHolder
    class HomeGoodsTitleHolder extends RecyclerView.ViewHolder {
        TextView mHomeTvTitle, mHomeTvMore;

        public HomeGoodsTitleHolder(View itemView) {
            super(itemView);
            mHomeTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mHomeTvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }
    //全屋定制内容ViewHolder
    class HomeGoodsContentHolder extends RecyclerView.ViewHolder {
        RecyclerView mHomeRecyclerView;

        public HomeGoodsContentHolder(View itemView) {
            super(itemView);
            mHomeRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            mHomeRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_common);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            layoutManager.setAutoMeasureEnabled(true);
            mHomeRecyclerView.setLayoutManager(layoutManager);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    class EmptyHolder extends RecyclerView.ViewHolder {
        public EmptyHolder(View itemView) {
            super(itemView);
        }
    }
}
