package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.AddressInfo;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ShoppingCartAddressAdapter extends BaseAdapter {

    private Context mContext;
    private AddressInfo mInfo;

    public ShoppingCartAddressAdapter(Context context, AddressInfo info) {
        this.mContext = context;
        this.mInfo = info;
    }

    @Override
    public int getCount() {
        return mInfo.getData().size();
    }

    @Override
    public Object getItem(int position) {
        return mInfo.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartAddressViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new CartAddressViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_cart_address_layout, parent, false);
            viewHolder.mtvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.mtvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.mtvAddress = (TextView) convertView.findViewById(R.id.tv_address_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CartAddressViewHolder) convertView.getTag();
        }

        viewHolder.mtvName.setText(mInfo.getData().get(position).getReceivername());
        viewHolder.mtvPhone.setText(mInfo.getData().get(position).getPhone());
        viewHolder.mtvAddress.setText(mInfo.getData().get(position).getAddress());

        return convertView;
    }

    static class CartAddressViewHolder{
        TextView mtvName, mtvPhone, mtvAddress;

    }
}
