package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ww.administrator.demotest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class DetailCommentAdapter extends BaseAdapter {

    Context mContext;
    List<String> mList;

    public DetailCommentAdapter(Context context, List<String> list) {
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
        CommentViewHolder commentViewHolder;

        if (convertView == null){
            commentViewHolder = new CommentViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_comment_layout, null);
            commentViewHolder.mTvCommentName = (TextView) convertView.findViewById(R.id.tv_comment_name);
            commentViewHolder.mTvCommentTime = (TextView) convertView.findViewById(R.id.tv_comment_time);
            commentViewHolder.mTvCommentContent = (TextView) convertView.findViewById(R.id.tv_comment_content);
            commentViewHolder.mRbComment = (RatingBar) convertView.findViewById(R.id.rb_comment);
            convertView.setTag(commentViewHolder);
        }else {
            commentViewHolder = (CommentViewHolder) convertView.getTag();
        }
        return convertView;
    }

    private static class CommentViewHolder{
        TextView mTvCommentName, mTvCommentTime, mTvCommentContent;
        RatingBar mRbComment;
    }


}
