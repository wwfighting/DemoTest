package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.ProductListInfo;
import com.ww.administrator.demotest.util.DisplayUtil;
import com.ww.administrator.demotest.util.ToolsUtil;

/**
 * Created by Administrator on 2016/9/26.
 */
public class CateContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE = 1;

    private Context mContext;
    private ProductListInfo mInfo;

    public CateContentAdapter(Context context, ProductListInfo info) {
        this.mContext = context;
        this.mInfo = info;
    }

    @Override
    public int getItemCount() {
        return mInfo.getData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new CateContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_home_goods, parent, false))
                : null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CateContentViewHolder){
            ((CateContentViewHolder) holder).mTvGoodsTitle.setText(mInfo.getData().get(position).getGoodsname());

            if (mInfo.getData().get(position).getPrice().equals("3000")){
                String nowPrice  = "预约金：￥" + mInfo.getData().get(position).getPrice();
                ((CateContentViewHolder) holder).mTvGoodsNowPrice.setText(nowPrice);
            }else {
                String nowPrice = "现价：￥" + mInfo.getData().get(position).getPrice();
                ((CateContentViewHolder) holder).mTvGoodsNowPrice.setText(nowPrice);
            }



            Glide.with(mContext)
                    .load(mInfo.getData().get(position).getPicurl())
                    .crossFade()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(((CateContentViewHolder) holder).mIvGoodsPic);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //将物品ID通过intent对象传值到详细页面
                    Intent intent = new Intent();
                    intent.setClass(mContext, DetailActivity.class);
                    intent.putExtra("gid", mInfo.getData().get(position).getId());
                    if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent);
                    } else {
                        // mContext.startActivity(intent);
                        View sharedView = ((CateContentViewHolder) holder).mIvGoodsPic;
                        String transitionName = "detail";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }
                }
            });
        }

    }

    private class CateContentViewHolder extends RecyclerView.ViewHolder{
        CardView mcvContainer;
        ImageView mIvGoodsPic;
        TextView mTvGoodsTip, mTvGoodsTitle, mTvGoodsNowPrice, mTvGoodsOrgPrice;

        public CateContentViewHolder(View itemView) {
            super(itemView);
            mcvContainer = (CardView) itemView.findViewById(R.id.card_container);
            mIvGoodsPic = (ImageView) itemView.findViewById(R.id.iv_goods_show);
            mTvGoodsTip = (TextView) itemView.findViewById(R.id.tv_goods_tip);
            mTvGoodsTitle = (TextView) itemView.findViewById(R.id.tv_goods_title);
            mTvGoodsNowPrice = (TextView) itemView.findViewById(R.id.tv_goods_nowprice);
            mTvGoodsOrgPrice = (TextView) itemView.findViewById(R.id.tv_goods_orgprice);
            mcvContainer.setRadius(DisplayUtil.dip2px(mContext, 3.0f));
            mTvGoodsTip.setVisibility(View.GONE);
            mTvGoodsOrgPrice.setVisibility(View.GONE);

        }
    }


}
