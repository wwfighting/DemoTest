package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ww.administrator.demotest.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE = 1;
    public Context mContext;
    public List<String> mList;



    public SearchAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    @Override
    public int getItemCount() {

        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder){
            ((SearchViewHolder) holder).mTvSearch.setText(mList.get(position));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new SearchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_search, parent, false)) : null;
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder{

        TextView mTvSearch;
        public SearchViewHolder(View itemView) {
            super(itemView);
            mTvSearch = (TextView) itemView.findViewById(R.id.tv_search);
        }
    }
}
