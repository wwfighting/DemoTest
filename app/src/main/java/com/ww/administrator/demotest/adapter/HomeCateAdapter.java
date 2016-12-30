package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ww.administrator.demotest.ProductListActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.util.Constants;

/**
 * Created by Administrator on 2016/11/16.
 */
public class HomeCateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE = 1;


    private Context mContext;
    String city = "";

    public HomeCateAdapter(Context context) {
        mContext = context;
        city = (String) SharedPreUtil.getData(context, "locatCity", "南京");
    }


    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new HomeCateContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_home_cate_layout, parent, false))
                : null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof HomeCateContentViewHolder){
            ((HomeCateContentViewHolder) holder).mtvCateTitle.setText(Constants.CATE_TITLE[position]);

            ((HomeCateContentViewHolder) holder).mivCateIcon.setImageResource(Constants.CATE_ICON[position]);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0: //圣诞特惠
                        Intent d12 = new Intent();
                        d12.setClass(mContext, ProductListActivity.class);
                        if (city.equals("南京")){
                            d12.putExtra("isRecom", "5");
                        }else {
                            d12.putExtra("keyName", "圣诞特惠套餐");
                            d12.putExtra("isRecom", "-1");

                        }
                        mContext.startActivity(d12);
                        break;

                    case 1: //新品推荐
                        Intent newGoods = new Intent();
                        newGoods.setClass(mContext, ProductListActivity.class);
                        newGoods.putExtra("isRecom", "1");
                        mContext.startActivity(newGoods);
                        break;

                    case 2: //热卖商品
                        Intent hotGoods = new Intent();
                        hotGoods.setClass(mContext, ProductListActivity.class);
                        hotGoods.putExtra("isRecom","2");
                        mContext.startActivity(hotGoods);
                        break;

                    case 3: //精品橱柜
                        Intent kitchen = new Intent();
                        kitchen.setClass(mContext, ProductListActivity.class);
                        kitchen.putExtra("keyName", "精英");
                        kitchen.putExtra("isRecom", "-1");
                        mContext.startActivity(kitchen);
                        break;

                    case 4: //全屋定制
                        if (((String) SharedPreUtil.getData(mContext, "locatCity", "南京")).equals("南京")){
                            Toast.makeText(mContext, "抱歉，目前南京不参与全屋活动！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent fullHouse = new Intent();
                        fullHouse.setClass(mContext, ProductListActivity.class);
                        fullHouse.putExtra("keyName", "元全屋");
                        fullHouse.putExtra("isRecom", "-1");
                        mContext.startActivity(fullHouse);
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Constants.CATE_TITLE.length;
    }

    class HomeCateContentViewHolder extends RecyclerView.ViewHolder{
        ImageView mivCateIcon;
        TextView mtvCateTitle;
        public HomeCateContentViewHolder(View itemView) {
            super(itemView);
            mivCateIcon = (ImageView) itemView.findViewById(R.id.iv_cate_img);
            mtvCateTitle = (TextView) itemView.findViewById(R.id.tv_cate_title);
        }
    }
}
