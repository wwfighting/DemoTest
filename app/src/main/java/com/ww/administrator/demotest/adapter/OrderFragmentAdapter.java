package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.OrderResultInfo;
import com.ww.administrator.demotest.util.Constants;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class OrderFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE = 1;
    private Context mContext;
    List<OrderResultInfo.Databean> mList;

    public OrderFragmentAdapter(Context context, OrderResultInfo info) {
        this.mContext = context;
        mList = info.getData();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new OrderFraViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_fragment_order_layout, parent, false))
                : null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof OrderFraViewHolder){
            ((OrderFraViewHolder) holder).mtvOrderTime.setText(mList.get(position).getCreatetime().substring(0, 10));
            ((OrderFraViewHolder) holder).mtvOrderNum.setText("订单号：" + mList.get(position).getSuperbillid());
            //status：订单状态 0未付款;1已预约;2已付款;3配送中;4待评价;5已完成; 6设计师已测量;-1 订单取消
            switch (mList.get(position).getStatus()){
                case "0":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("等待支付");
                    ((OrderFraViewHolder) holder).mtvOrderState.setVisibility(View.GONE);
                    ((OrderFraViewHolder) holder).mbtnToPay.setVisibility(View.VISIBLE);
                    break;
                case "1":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("已付款");
                    ((OrderFraViewHolder) holder).mtvOrderState.setVisibility(View.VISIBLE);
                    ((OrderFraViewHolder) holder).mbtnToPay.setVisibility(View.GONE);
                    break;
                case "2":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("已付款");
                    ((OrderFraViewHolder) holder).mtvOrderState.setVisibility(View.VISIBLE);
                    ((OrderFraViewHolder) holder).mbtnToPay.setVisibility(View.GONE);
                    break;
                case "3":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("配送中");
                    break;
                case "4":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("待评价");
                    break;
                case "5":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("已完成");
                    break;
                case "6":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("设计师已测量");
                    break;
                case "-1":
                    ((OrderFraViewHolder) holder).mtvOrderIsPay.setText("订单取消");
                    break;
            }


            ((OrderFraViewHolder) holder).mtvGoodsNum.setVisibility(mList.get(position).getYyid() == null ? View.GONE : View.VISIBLE);
            ((OrderFraViewHolder) holder).mtvGoodsNum.setText("商单号：" + mList.get(position).getYyid());
            String picurl = "";
            if (!mList.get(position).getPicurl().contains(";")){
                picurl = Constants.BASE_IMG_URL + mList.get(position).getPicurl();
            }else {
                picurl = Constants.BASE_IMG_URL + ((mList.get(position).getPicurl().split(";"))[0]);
            }
            Glide.with(mContext)
                    .load(picurl)
                    .crossFade()
                    .into(((OrderFraViewHolder) holder).mivGoods);

            ((OrderFraViewHolder) holder).mtvTitle.setText(mList.get(position).getGoodsname() + "(" + mList.get(position).getColor() + ")");

            ((OrderFraViewHolder) holder).mtvOrderMoney.setText("预约金：￥" + mList.get(position).getSchedprice());
            ((OrderFraViewHolder) holder).mtvAllMoney.setText("金额：￥" + mList.get(position).getBillprice());

            ((OrderFraViewHolder) holder).mbtnToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击去付款
                    mOnPayClickListener.getItem(mList.get(position));

                }
            });

        }

    }


    public interface OnPayClickListener{
        void getItem(OrderResultInfo.Databean info);
    }

    private OnPayClickListener mOnPayClickListener;

    public void setOnPayClickListener(OnPayClickListener onPayClickListener){
        this.mOnPayClickListener = onPayClickListener;
    }

    class OrderFraViewHolder extends RecyclerView.ViewHolder{

        TextView mtvOrderTime, mtvOrderNum, mtvOrderIsPay, mtvGoodsNum;
        ImageView mivGoods;
        TextView mtvTitle, mtvAllMoney, mtvOrderMoney;
        TextView mtvOrderState;
        Button mbtnToPay;

        public OrderFraViewHolder(View itemView) {
            super(itemView);
            mtvOrderTime = (TextView) itemView.findViewById(R.id.tv_order_time);
            mtvOrderNum = (TextView) itemView.findViewById(R.id.tv_order_num);
            mtvOrderIsPay = (TextView) itemView.findViewById(R.id.tv_order_is_pay);
            mtvGoodsNum = (TextView) itemView.findViewById(R.id.tv_order_goods_num);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_order_goods_title);
            mtvOrderMoney = (TextView) itemView.findViewById(R.id.tv_order_goods_money);
            mtvAllMoney = (TextView) itemView.findViewById(R.id.tv_order_goods_allmoney);
            mtvOrderState = (TextView) itemView.findViewById(R.id.tv_order_state);
            mivGoods = (ImageView) itemView.findViewById(R.id.iv_order_goods_show);
            mbtnToPay = (Button) itemView.findViewById(R.id.btn_order_to_pay);
        }
    }


}
