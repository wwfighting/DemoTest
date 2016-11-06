package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.ShoppingcartInfo;
import com.ww.administrator.demotest.util.ButtonUtil;
import com.ww.administrator.demotest.util.Constants;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VITE_TYPE = 1;

    private Context mContext;
    private ShoppingcartInfo mInfo;

    // 用来控制CheckBox的选中状况
    public Map<Integer, Boolean> selectedMap;


    public ShoppingCartAdapter(Context context, ShoppingcartInfo info) {
        this.mContext = context;
        mInfo = info;

    }

    public void onRefresh(ShoppingcartInfo info){
        mInfo = info;
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
            ((ShoppingcartViewHolder) holder).mtvMoney.setText(Integer.parseInt(mInfo.getData().get(position).getPrice()) * Integer.parseInt(mInfo.getData().get(position).getBuycount()) + "");

            if (mInfo.getData().get(position).getSubtitle().equals("配件")){
                ((ShoppingcartViewHolder) holder).mtvOrderLabel.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvOrdMoney.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvOrderMinus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvOrderPlus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNumLabel.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNumMinus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNum.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNumPlus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNum.setText(mInfo.getData().get(position).getBuycount());

            }else if (mInfo.getData().get(position).getSubtitle().equals("全屋定制")){  //全屋定制
                ((ShoppingcartViewHolder) holder).mtvOrderLabel.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrdMoney.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrderMinus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrderPlus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNumLabel.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNumMinus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNum.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNumPlus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvOrdMoney.setText(mInfo.getData().get(position).getOrderMoney() + "");
            }else if (mInfo.getData().get(position).getSubtitle().equals("")){  //橱柜
                ((ShoppingcartViewHolder) holder).mtvOrderLabel.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrdMoney.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrderMinus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvOrderPlus.setVisibility(View.VISIBLE);
                ((ShoppingcartViewHolder) holder).mtvNumLabel.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNumMinus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNum.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvNumPlus.setVisibility(View.GONE);
                ((ShoppingcartViewHolder) holder).mtvOrdMoney.setText(mInfo.getData().get(position).getOrderMoney() + "");
            }

            Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + mInfo.getData().get(position).getImgurl())
                    .crossFade()
                    .into(((ShoppingcartViewHolder) holder).mivShow);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent intent = new Intent();
                    intent.setClass(mContext, DetailActivity.class);
                    intent.putExtra("gid", mInfo.getData().get(position).getId());
                    // mContext.startActivity(intent);
                    if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP) {
                        mContext.startActivity(intent);
                    } else {
                        View sharedView = holder.itemView.findViewById(R.id.iv_cart_pro_show);
                        String transitionName = "detail";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }*/

                }
            });

            //选中该商品
            ((ShoppingcartViewHolder) holder).mcbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    /*if(isChecked){
                        selectedMap.put(position, true);
                    }else{
                        selectedMap.put(position, false);
                    }*/

                    if (monCartChecked != null){
                        monCartChecked.isSetChecked(mInfo, isChecked, position);
                        mInfo.getData().get(position).setIsSelected(isChecked);  //该商品是否被选中
                    }

                }
            });

            //增加配件数量
            ((ShoppingcartViewHolder) holder).mtvNumPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ButtonUtil.isFastDoubleClick()){
                        if (monCountNum != null){
                            monCountNum.numPlus(mInfo, position);
                        }
                    }


                }
            });

            //减少配件数量
            ((ShoppingcartViewHolder) holder).mtvNumMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ButtonUtil.isFastDoubleClick()){
                        if (monCountNum != null) {
                            monCountNum.numMinus(mInfo, position);
                        }
                    }

                }
            });

            //增加预约金额
            ((ShoppingcartViewHolder) holder).mtvOrderPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monOrderMoneyListener != null) {
                        monOrderMoneyListener.orderMoneyPlus(mInfo, position);
                    }
                }
            });

            //减少预约金额
            ((ShoppingcartViewHolder) holder).mtvOrderMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monOrderMoneyListener != null) {
                        monOrderMoneyListener.orderMoneyMinus(mInfo, position);
                    }
                }
            });
        }
    }



    private class ShoppingcartViewHolder extends RecyclerView.ViewHolder{

        AppCompatCheckBox mcbSelected;
        ImageView mivShow;

        TextView mtvTitle, mtvMoney;

        //橱柜或全屋定制预约金
        TextView mtvOrderLabel, mtvOrderMinus, mtvOrdMoney, mtvOrderPlus;
        //配件数量
        TextView mtvNumLabel, mtvNumMinus, mtvNum, mtvNumPlus;

        public ShoppingcartViewHolder(View itemView) {
            super(itemView);
            mcbSelected = (AppCompatCheckBox) itemView.findViewById(R.id.cb_cart_pro_selected);
            mivShow = (ImageView) itemView.findViewById(R.id.iv_cart_pro_show);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_cart_pro_title);
            mtvMoney = (TextView) itemView.findViewById(R.id.tv_cart_pro_money);

            //橱柜或全屋定制预约金
            mtvOrderLabel = (TextView) itemView.findViewById(R.id.tv_pro_order_money_label);
            mtvOrderMinus = (TextView) itemView.findViewById(R.id.tv_cart_money_minus);
            mtvOrdMoney = (TextView) itemView.findViewById(R.id.tv_cart_pro_order_money);
            mtvOrderPlus = (TextView) itemView.findViewById(R.id.tv_cart_money_plus);

            //配件数量
            mtvNumLabel = (TextView) itemView.findViewById(R.id.tv_pro_order_num_label);
            mtvNumMinus = (TextView) itemView.findViewById(R.id.tv_cart_num_minus);
            mtvNum = (TextView) itemView.findViewById(R.id.tv_cart_pro_num);
            mtvNumPlus = (TextView) itemView.findViewById(R.id.tv_cart_num_plus);
        }
    }

    //商品选中接口
    public interface OnCartChecked{
        void isSetChecked(ShoppingcartInfo info, boolean isChecked, int pos);
    }

    private OnCartChecked monCartChecked;

    public void setOnCartChecked(OnCartChecked onCartChecked){
        this.monCartChecked = onCartChecked;
    }

    //配件数量增减接口
    public interface OnNumCountListener{
        void numMinus(ShoppingcartInfo info, int pos);
        void numPlus(ShoppingcartInfo info, int pos);
    }

    private OnNumCountListener monCountNum;

    public void setOnCountNumListener(OnNumCountListener onCountNum){
        this.monCountNum = onCountNum;
    }

    //预约金额增减接口
    public interface OnOrderMoneyListener{
        void orderMoneyMinus(ShoppingcartInfo info, int pos);
        void orderMoneyPlus(ShoppingcartInfo info, int pos);
    }

    private OnOrderMoneyListener monOrderMoneyListener;

    public void setOnOrderMoneyListener(OnOrderMoneyListener onOrderMoneyListener){
        this.monOrderMoneyListener = onOrderMoneyListener;
    }


}
