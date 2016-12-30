package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.TextUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

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
                .inflate(R.layout.adapter_home_goods, parent, false)) : null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //isrecom 1：新品推荐 2：热门商品 3：全屋定制
        if (holder instanceof GoodsContentHolder){

            Glide.with(mContext)
                    .load(mGoodsInfo.getData().get(position).getPicurl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(((GoodsContentHolder) holder).mIvGoodsPic);
            if (mGoodsInfo.getData().get(position).getIsrecom().equals("1")){
                ((GoodsContentHolder) holder).mTvGoodsOrgPrice.setVisibility(View.VISIBLE);
                ((GoodsContentHolder) holder).mTvGoodsTip.setText("新品");
            }else if (mGoodsInfo.getData().get(position).getIsrecom().equals("2")){
                ((GoodsContentHolder) holder).mTvGoodsOrgPrice.setVisibility(View.VISIBLE);
                ((GoodsContentHolder) holder).mTvGoodsTip.setText("热门");
            }else if (mGoodsInfo.getData().get(position).getIsrecom().equals("3")){
                ((GoodsContentHolder) holder).mTvGoodsOrgPrice.setVisibility(View.GONE);
                ((GoodsContentHolder) holder).mTvGoodsTip.setText("热卖");
            }

            ((GoodsContentHolder) holder).mTvGoodsTitle.setText(mGoodsInfo.getData().get(position).getGoodsname());

            if (mGoodsInfo.getData().get(position).getIsrecom().equals("3")){
                String nowPrice = "预约金：￥" + mGoodsInfo.getData().get(position).getPrice();
                ((GoodsContentHolder) holder).mTvGoodsNowPrice.setText(nowPrice);
            }else {
                String nowPrice = "现价：￥" + mGoodsInfo.getData().get(position).getPrice();
                ((GoodsContentHolder) holder).mTvGoodsNowPrice.setText(nowPrice);
                String orgPrice = "原价：￥" + mGoodsInfo.getData().get(position).getPrice_old();
                ((GoodsContentHolder) holder).mTvGoodsOrgPrice.setText(TextUtil.setStrSpan(orgPrice));
            }

            if (!mGoodsInfo.getData().get(position).getImg1().isEmpty()){
                ((GoodsContentHolder) holder).mivLeftLogo.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + mGoodsInfo.getData().get(position).getImg1())
                        .crossFade()
                        .into(((GoodsContentHolder) holder).mivLeftLogo);
            }else {
                ((GoodsContentHolder) holder).mivLeftLogo.setVisibility(View.GONE);
            }

            if (!mGoodsInfo.getData().get(position).getImg2().isEmpty()){
                ((GoodsContentHolder) holder).mivLabel.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + mGoodsInfo.getData().get(position).getImg2())
                        .crossFade()
                        .into(((GoodsContentHolder) holder).mivLabel);
            }else {
                ((GoodsContentHolder) holder).mivLabel.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mGoodsInfo.getData().get(position).getSubtitle().equals("分类")) {
                        //跳到物品列表页面
                        Intent intent = new Intent();
                        intent.setClass(mContext, ProductListActivity.class);
                        intent.putExtra("classId", mGoodsInfo.getData().get(position).getClassifyid());
                        intent.putExtra("keyName", mGoodsInfo.getData().get(position).getGoodsname());
                        intent.putExtra("isRecom", "-1");
                        mContext.startActivity(intent);
                    } else {
                        //直接跳到该物品的详细页面
                        Intent intent = new Intent();
                        intent.setClass(mContext, DetailActivity.class);
                        intent.putExtra("gid", mGoodsInfo.getData().get(position).getId());
                        if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                            mContext.startActivity(intent);
                        } else {
                            View sharedView = ((GoodsContentHolder) holder).mIvGoodsPic;
                            String transitionName = "detail";
                            ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, sharedView, transitionName);
                            mContext.startActivity(intent, transitionActivityOptions.toBundle());
                        }

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
        ImageView mIvGoodsPic, mivLeftLogo, mivLabel;
        TextView mTvGoodsTip, mTvGoodsTitle, mTvGoodsNowPrice, mTvGoodsOrgPrice;

        public GoodsContentHolder(View itemView) {
            super(itemView);

            mivLabel = (ImageView) itemView.findViewById(R.id.iv_label);
            mivLeftLogo = (ImageView) itemView.findViewById(R.id.iv_home_left_logo);
            mIvGoodsPic = (ImageView) itemView.findViewById(R.id.iv_goods_show);
            mTvGoodsTip = (TextView) itemView.findViewById(R.id.tv_goods_tip);
            mTvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goods_title);
            mTvGoodsNowPrice = (TextView) itemView.findViewById(R.id.tv_goods_nowprice);
            mTvGoodsOrgPrice = (TextView) itemView.findViewById(R.id.tv_goods_orgprice);
        }
    }


}
