package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.CateInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/23.
 */
public class CateExpandableListAdapter implements ExpandableListAdapter  {

    private List<CateInfo.DataBean> mListGroup;
    private Context mContext;

    Map<String, ArrayList<CateInfo.DataBean>> mMap = new HashMap<>();

    public CateExpandableListAdapter(Context context, List<CateInfo.DataBean> group, Map<String, ArrayList<CateInfo.DataBean>> map) {
        this.mContext = context;
        this.mListGroup = group;
        this.mMap = map;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mListGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mMap.get(mListGroup.get(groupPosition).getName()).size();
    }

    @Override
    public CateInfo.DataBean getGroup(int groupPosition) {
        return mListGroup.get(groupPosition);
    }

    @Override
    public CateInfo.DataBean getChild(int groupPosition, int childPosition) {
        return mMap.get(getGroup(groupPosition).getName()).get(childPosition);
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cate_header_view, null);
        }

        ((TextView) convertView.findViewById(R.id.tv_cate_header)).setText(getGroup(groupPosition).getName());

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CateContentViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new CateContentViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cate_header_view, null);
            viewHolder.mtvContent = (TextView) convertView.findViewById(R.id.tv_cate_header);
            viewHolder.mllHead = (LinearLayout) convertView.findViewById(R.id.ll_cate_header);
            LinearLayout.LayoutParams p = (LinearLayout.LayoutParams)viewHolder.mtvContent.getLayoutParams();
            p.setMargins(0, 0, 0, 0);
            viewHolder.mtvContent.setGravity(Gravity.CENTER);
            viewHolder.mtvContent.setLayoutParams(p);
            viewHolder.mllHead.setBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.mtvContent.setTextColor(Color.parseColor("#333333"));
            viewHolder.mtvContent.setTextSize(14);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CateContentViewHolder) convertView.getTag();
        }

        viewHolder.mtvContent.setText((getChild(groupPosition, childPosition).getName()));

        return convertView;
    }
    static class CateContentViewHolder{
        TextView mtvContent;
        LinearLayout mllHead;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


}
