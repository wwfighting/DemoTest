package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.StaffInfo;

/**
 * Created by Administrator on 2016/9/22.
 */
public class StaffAdapter extends BaseAdapter {

    private StaffInfo mInfo;
    private Context mContext;

    public StaffAdapter(Context context, StaffInfo info) {
        this.mContext = context;
        this.mInfo = info;
    }

    @Override
    public int getCount() {
        return mInfo.getData().size();
    }

    @Override
    public StaffInfo.DataBean getItem(int position) {
        return mInfo.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StaffViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new StaffViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_cart_store_layout, null);
            viewHolder.mtvStoreName = (TextView) convertView.findViewById(R.id.tv_store_name);
            viewHolder.mtvStoreName.setTextSize(16);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (StaffViewHolder) convertView.getTag();
        }

        viewHolder.mtvStoreName.setText(mInfo.getData().get(position).getTruename() + "  (" + mInfo.getData().get(position).getSalerno() + ")");

        return convertView;
    }

    class StaffViewHolder{
        TextView mtvStoreName;
    }
}
