package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.ShoppingcartInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.ToolsUtil;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VITE_TYPE = 1;

    private Context mContext;
    private ShoppingcartInfo mInfo;
    Boolean mIsSelected = false;

    public ShoppingCartAdapter(Context context, ShoppingcartInfo info) {
        this.mContext = context;
        mInfo = info;

    }

    public void onRefresh(boolean isSelected){
        this.mIsSelected = isSelected;
        this.notifyDataSetChanged();
    }


    public void delItem(int pos){
        mInfo.getData().remove(pos);
        notifyItemRemoved(pos);
    }


    public ShoppingcartInfo.DataBean getItem(int pos){
        return mInfo.getData().get(pos);
    }

    @Override
    public int getItemCount() {
        return mInfo.getData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return VITE_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VITE_TYPE ? new ShoppingcartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_shoppingcart, parent, false)) : null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ShoppingcartViewHolder){
            ((ShoppingcartViewHolder) holder).mtvTitle.setText(mInfo.getData().get(position).getGoodsname());
            ((ShoppingcartViewHolder) holder).mtvMoney.setText(mInfo.getData().get(position).getPrice());
            ((ShoppingcartViewHolder) holder).mtvOrdMoney.setText("2000");

            Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + mInfo.getData().get(position).getImgurl())
                    .crossFade()
                    .into(((ShoppingcartViewHolder) holder).mivShow);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, DetailActivity.class);
                    intent.putExtra("gid", mInfo.getData().get(position).getId());
                    // mContext.startActivity(intent);
                    if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent);
                    }else {
                        View sharedView = holder.itemView.findViewById(R.id.iv_cart_pro_show);
                        String transitionName = "detail";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }

                }
            });

            ((ShoppingcartViewHolder) holder).mcbSelected.setChecked(mIsSelected);

            ((ShoppingcartViewHolder) holder).mcbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (monCartChecked != null){
                        monCartChecked.isSetChecked(mInfo, isChecked, position);
                    }
                }
            });
        }
    }


    private class ShoppingcartViewHolder extends RecyclerView.ViewHolder{

        AppCompatCheckBox mcbSelected;
        ImageView mivShow;
        TextView mtvTitle, mtvOrdMoney, mtvMoney;

        public ShoppingcartViewHolder(View itemView) {
            super(itemView);
            mcbSelected = (AppCompatCheckBox) itemView.findViewById(R.id.cb_cart_pro_selected);
            mivShow = (ImageView) itemView.findViewById(R.id.iv_cart_pro_show);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_cart_pro_title);
            mtvOrdMoney = (TextView) itemView.findViewById(R.id.tv_cart_pro_order_money);
            mtvMoney = (TextView) itemView.findViewById(R.id.tv_cart_pro_money);
        }
    }

    public interface OnCartChecked{
        void isSetChecked(ShoppingcartInfo info, boolean isChecked, int pos);
    }

    private OnCartChecked monCartChecked;

    public void setOnCartChecked(OnCartChecked onCartChecked){
        this.monCartChecked = onCartChecked;
    }
}
