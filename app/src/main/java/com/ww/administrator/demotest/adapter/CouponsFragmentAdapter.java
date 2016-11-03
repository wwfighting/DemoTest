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
import com.ww.administrator.demotest.model.CouponsResultInfo;
import com.ww.administrator.demotest.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CouponsFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_COUPONS = 1;
    private final static int VIEW_TYPE_SIGNIN = 2;
    private Context mContext;

    List<CouponsResultInfo.DataBean> mList;

    public CouponsFragmentAdapter(Context context, CouponsResultInfo info) {
        this.mContext = context;
        mList = info.getData();
    }

    @Override
    public int getItemViewType(int position) {

        /*if (mList.get(position).getLpGoodid().equals("3506")){  //签到礼券

        }else {
            return VIEW_TYPE_COUPONS;   //其他礼券
        }*/
        return VIEW_TYPE_SIGNIN;

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
                : null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        /*if (holder instanceof CouponsViewHolder){
            //mode：1：未使用 2：已使用 3：已过期

            Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + "uploads/1449817621.png")
                    .crossFade()
                    .into(((CouponsViewHolder) holder).mivShow);

            ((CouponsViewHolder) holder).mtvTitle.setText(mList.get(position).getTitle());
            ((CouponsViewHolder) holder).mtvCouponsNo.setText(mList.get(position).getName());
            ((CouponsViewHolder) holder).mtvUseTime.setText("有效期至：" + mList.get(position).getLastTime());
            ((CouponsViewHolder) holder).mtvUseRule.setText(mList.get(position).getMode());

        }*/

        if (holder instanceof CouponsSignViewHolder){
            //mode：1：未使用 2：已使用 3：已过期

            if (mList.get(position).getLpGoodid().equals("3505")){
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + "uploads/1478143594.jpg")
                        .crossFade()
                        .into(((CouponsSignViewHolder) holder).mivSigninShow);

            }else if (mList.get(position).getLpGoodid().equals("3506")){
                Glide.with(mContext)
                        .load(Constants.BASE_IMG_URL + "uploads/1478051956.jpg")
                        .crossFade()
                        .into(((CouponsSignViewHolder) holder).mivSigninShow);
            }

            ((CouponsSignViewHolder) holder).mtvSigninNo.setText(mList.get(position).getName());
            ((CouponsSignViewHolder) holder).mtvSigninUseTime.setText("有效期至：" + mList.get(position).getLastTime());
            String mode = mList.get(position).getMode().replaceAll("<br>", "\r\n");
            ((CouponsSignViewHolder) holder).mtvSigninUseRule.setText(mode);

        }


    }

    /**
     * 普通礼券
     */
    class CouponsViewHolder extends RecyclerView.ViewHolder{

        TextView mtvTitle, mtvCouponsNo, mtvUseTime, mtvUseRule;
        ImageView mivShow;

        public CouponsViewHolder(View itemView) {
            super(itemView);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_coupons_title);
            mtvCouponsNo = (TextView) itemView.findViewById(R.id.tv_coupons_no);
            mtvUseTime = (TextView) itemView.findViewById(R.id.tv_coupons_usetime);
            mtvUseRule = (TextView) itemView.findViewById(R.id.tv_coupons_use_rule);
            mivShow = (ImageView) itemView.findViewById(R.id.iv_coupons_show);

        }
    }

    /**
     * 签到礼券
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
