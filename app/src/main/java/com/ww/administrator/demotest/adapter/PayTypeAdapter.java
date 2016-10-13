package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ww.administrator.demotest.R;

/**
 * Created by Administrator on 2016/10/8.
 */
public class PayTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEWTYPE = 1;
    private Context mContext;

    PayType[] payTypes = {
            new PayType(R.mipmap.wechat, "微信支付", "使用微信支付，以人民币CNY计算"),
            new PayType(R.mipmap.alipay, "支付宝支付", "使用支付宝支付，以人民币CNY计算"),
            new PayType(R.mipmap.baidupay, "百度钱包", "使用百度钱包支付，以人民币CNY计算")
    };

    public interface TypeClickListener{
        void getItem(int pos);
    }

    private TypeClickListener mtypeClickListener;

    public void setOnTypeClickListener(TypeClickListener typeClickListener){
        this.mtypeClickListener = typeClickListener;
    }

    public PayTypeAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return payTypes.length;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEWTYPE;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEWTYPE ? new TypeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_pay_layout, parent, false))
                : null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof TypeViewHolder){
            ((TypeViewHolder) holder).mivIcon.setImageResource(payTypes[position].icon);
            ((TypeViewHolder) holder).mtvTitle.setText(payTypes[position].title);
            ((TypeViewHolder) holder).mtvContent.setText(payTypes[position].content);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mtypeClickListener.getItem(position);
                }
            });
        }

    }

    class TypeViewHolder extends RecyclerView.ViewHolder{

        ImageView mivIcon;
        TextView mtvTitle, mtvContent;

        public TypeViewHolder(View itemView) {
            super(itemView);
            mivIcon = (ImageView) itemView.findViewById(R.id.iv_pay_icon);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_pay_name);
            mtvContent = (TextView) itemView.findViewById(R.id.tv_pay_content);
        }
    }

    static class PayType{
        public int icon;
        public String title;
        public String content;

        public PayType(int icon, String title, String content) {
            this.icon = icon;
            this.title = title;
            this.content = content;
        }
    }
}
