package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class DetailImgAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;

    public DetailImgAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImgViewHolder imgViewHolder;
        if (convertView == null){
            imgViewHolder = new ImgViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_detail_layout, null);
            imgViewHolder.mIvDetailShow = (ImageView) convertView.findViewById(R.id.iv_detail_show);
            convertView.setTag(imgViewHolder);
        }else {
            imgViewHolder = (ImgViewHolder) convertView.getTag();
        }

        //加载图片
        Glide.with(mContext)
                .load(mList.get(position))
                .crossFade()
                .into(imgViewHolder.mIvDetailShow);

        return convertView;
    }

    private static class ImgViewHolder{
        ImageView mIvDetailShow;

    }

}
