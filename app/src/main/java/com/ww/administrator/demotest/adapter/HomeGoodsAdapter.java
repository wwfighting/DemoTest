package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.ProductListActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.GoodsInfo;
import com.ww.administrator.demotest.util.TextUtil;

/**
 * Created by Administrator on 2016/8/25.
 */
public class HomeGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_VIEW = 0;

    GoodsInfo mGoodsInfo;
    Context mContext;

    public HomeGoodsAdapter(Context context, GoodsInfo info){
        this.mContext = context;
        this.mGoodsInfo = info;
    }

    @Override
    public int getItemCount() {
        return mGoodsInfo.getData().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return viewType == TYPE_VIEW ? new GoodsContentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_goods, parent, false)) : null ;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //isrecom 1：新品推荐 2：热门商品
        if (holder instanceof GoodsContentHolder){

            Glide.with(mContext).load(mGoodsInfo.getData().get(position).getPicurl()).into(((GoodsContentHolder) holder).mIvGoodsPic);
            ((GoodsContentHolder) holder).mTvGoodsTip.setText(
                    mGoodsInfo.getData().get(position).getIsrecom().equals("1") ? "新品" : "热门");

            ((GoodsContentHolder) holder).mTvGoodsTitle.setText(mGoodsInfo.getData().get(position).getGoodsname());

            String nowPrice = "现价：￥" + mGoodsInfo.getData().get(position).getPrice() + "-" + mGoodsInfo.getData().get(position).getPrice1();
            ((GoodsContentHolder) holder).mTvGoodsNowPrice.setText(nowPrice);
            String orgPrice = "原价：￥" + mGoodsInfo.getData().get(position).getPrice_old() + "-" + mGoodsInfo.getData().get(position).getPrice_old1();
            ((GoodsContentHolder) holder).mTvGoodsOrgPrice.setText(TextUtil.setStrSpan(orgPrice));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( mGoodsInfo.getData().get(position).getSubtitle().equals("分类")){
                        //跳到物品列表页面
                        Intent intent = new Intent();
                        intent.setClass(mContext, ProductListActivity.class);
                        intent.putExtra("classId", mGoodsInfo.getData().get(position).getClassifyid());
                        intent.putExtra("keyName", mGoodsInfo.getData().get(position).getGoodsname());
                        intent.putExtra("isRecom", "-1");
                        mContext.startActivity(intent);
                    }else {
                        //直接跳到该物品的详细页面
                        Intent intent = new Intent();
                        intent.setClass(mContext, DetailActivity.class);
                        intent.putExtra("gid", mGoodsInfo.getData().get(position).getId());
                        View sharedView = ((GoodsContentHolder) holder).mIvGoodsPic;
                        String transitionName = "detail";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_VIEW;
    }

    class GoodsContentHolder extends RecyclerView.ViewHolder {
        ImageView mIvGoodsPic;
        TextView mTvGoodsTip, mTvGoodsTitle, mTvGoodsNowPrice, mTvGoodsOrgPrice;

        public GoodsContentHolder(View itemView) {
            super(itemView);

            mIvGoodsPic = (ImageView) itemView.findViewById(R.id.iv_goods_show);
            mTvGoodsTip = (TextView) itemView.findViewById(R.id.tv_goods_tip);
            mTvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goods_title);
            mTvGoodsNowPrice = (TextView) itemView.findViewById(R.id.tv_goods_nowprice);
            mTvGoodsOrgPrice = (TextView) itemView.findViewById(R.id.tv_goods_orgprice);
        }
    }


}
