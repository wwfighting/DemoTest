package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.StoreInfo;

/**
 * Created by Administrator on 2016/9/22.
 */
public class StoreAdapter extends BaseAdapter {

    private StoreInfo mInfo;
    private Context mContext;

    public StoreAdapter(Context context, StoreInfo info) {
        this.mContext = context;
        this.mInfo = info;
    }

    @Override
    public int getCount() {
        return mInfo.getData().size();
    }

    @Override
    public StoreInfo.DataBean getItem(int position) {
        return mInfo.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new StoreViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_cart_store_layout, null);
            viewHolder.mtvStoreName = (TextView) convertView.findViewById(R.id.tv_store_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (StoreViewHolder) convertView.getTag();
        }

        viewHolder.mtvStoreName.setText(mInfo.getData().get(position).getStoreName());

        return convertView;
    }

    class StoreViewHolder{
        TextView mtvStoreName;
    }
}
