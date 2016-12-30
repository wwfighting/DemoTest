package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.CouponsPicInfo;
import com.ww.administrator.demotest.model.CouponsResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.DisplayUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CouponsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_COUPONS = 1;
    private final static int VIEW_TYPE_SIGNIN = 2;
    private Context mContext;

    List<CouponsResultInfo.DataBean> mList;
    List<CouponsPicInfo.DataBean> mPics;

    public CouponsFragmentAdapter(Context context, CouponsResultInfo info, CouponsPicInfo pics) {
        this.mContext = context;
        mList = info.getData();
        mPics = pics.getData();
    }

    @Override
    public int getItemViewType(int position) {

        if (mPics.get(position).getPicurl().equals("xjlq")){  //现金礼券
            return VIEW_TYPE_COUPONS;
        }else {
            return VIEW_TYPE_SIGNIN; //其他礼券
        }

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return viewType == VIEW_TYPE_SIGNIN ? new CouponsSignViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_fragment_sigin_coupons_layout, parent, false))
                : new CouponsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_fragment_coupons_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CouponsViewHolder){
            //mode：1：未使用 2：已使用 3：已过期

            /*Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + "uploads/1449817621.png")
                    .crossFade()
                    .into(((CouponsViewHolder) holder).mivShow);*/

            ((CouponsViewHolder) holder).mtvTitle.setText(mList.get(position).getTitle());
            ((CouponsViewHolder) holder).mtvCouponsNo.setText(mList.get(position).getName());
            ((CouponsViewHolder) holder).mtvUseTime.setText("有效期至：" + mList.get(position).getLastTime());
            String mode1 = mList.get(position).getMode().replaceAll("<br>", "\r\n");
            ((CouponsViewHolder) holder).mtvUseRule.setText(mode1);
            ((CouponsViewHolder) holder).mtvCouponsPrice.setText("￥" + mList.get(position).getLpPrice());

        }

        if (holder instanceof CouponsSignViewHolder){
            //mode：1：未使用 2：已使用 3：已过期

            if (mList.get(position).getLpGoodid().equals("3507")){
                ((CouponsSignViewHolder) holder).mivSigninShow.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + "uploads/1479110026.jpg")
                        .crossFade()
                        .override( DisplayUtil.dip2px(mContext, 200), DisplayUtil.dip2px(mContext, 100))
                        .into(((CouponsSignViewHolder) holder).mivSigninShow);
            }else {
                if (!mPics.get(position).getPicurl().equals("xjlq")){

                    if (!mList.get(position).getLpmode().isEmpty()){
                        if (Integer.parseInt(mList.get(position).getLpmode()) >= 3215 &&
                                Integer.parseInt(mList.get(position).getLpmode()) <= 3220){
                            ((CouponsSignViewHolder) holder).mivSigninShow.setScaleType(ImageView.ScaleType.CENTER);
                            Glide.with(mContext)
                                    .load(Constants.BASE_IMG_URL + mPics.get(position).getPicurl())
                                    .override(80, 80)
                                    .crossFade()
                                    .into(((CouponsSignViewHolder) holder).mivSigninShow);

                        }
                    } else {
                        ((CouponsSignViewHolder) holder).mivSigninShow.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(mContext)
                                .load(Constants.BASE_IMG_URL + mPics.get(position).getPicurl())
                                .crossFade()
                                .into(((CouponsSignViewHolder) holder).mivSigninShow);

                    }

                }

            }
            /*if (mList.get(position).getLpGoodid().equals("3505")){

            }else if (mList.get(position).getLpGoodid().equals("3506")){
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + "uploads/1478051956.jpg")
                        .crossFade()
                        .into(((CouponsSignViewHolder) holder).mivSigninShow);
            }else */

            ((CouponsSignViewHolder) holder).mtvSigninNo.setText(mList.get(position).getName());
            ((CouponsSignViewHolder) holder).mtvSigninUseTime.setText("有效期至：" + mList.get(position).getLastTime());
            String mode2 = mList.get(position).getMode().replaceAll("<br>", "\r\n");
            ((CouponsSignViewHolder) holder).mtvSigninUseRule.setText(mode2);

        }

    }

    /**
     * 现金礼券(无pic)
     */
    class CouponsViewHolder extends RecyclerView.ViewHolder{

        TextView mtvTitle, mtvCouponsNo, mtvUseTime, mtvUseRule, mtvCouponsPrice;
        //ImageView mivShow;

        public CouponsViewHolder(View itemView) {
            super(itemView);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_coupons_title);
            mtvCouponsNo = (TextView) itemView.findViewById(R.id.tv_coupons_no);
            mtvUseTime = (TextView) itemView.findViewById(R.id.tv_coupons_usetime);
            mtvUseRule = (TextView) itemView.findViewById(R.id.tv_coupons_use_rule);
            mtvCouponsPrice = (TextView) itemView.findViewById(R.id.tv_coupons_price);
           //mivShow = (ImageView) itemView.findViewById(R.id.iv_coupons_show);

        }
    }

    /**
     * 签到礼券（有pic）
     */
    class CouponsSignViewHolder extends RecyclerView.ViewHolder{

        TextView mtvSigninNo, mtvSigninUseTime, mtvSigninUseRule;
        ImageView mivSigninShow;

        public CouponsSignViewHolder(View itemView) {
            super(itemView);
            mtvSigninNo = (TextView) itemView.findViewById(R.id.tv_signin_no);
            mtvSigninUseTime = (TextView) itemView.findViewById(R.id.tv_signin_usetime);
            mtvSigninUseRule = (TextView) itemView.findViewById(R.id.tv_signin_use_rule);
            mivSigninShow = (ImageView) itemView.findViewById(R.id.iv_signin_show);
        }
    }

}
