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
 * Created by Administrator on 2016/7/28.
 */
public class CateBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<String> mlist;
    private LayoutInflater mInflater;

    public CateBaseAdapter(Context context, List<String> list){
        this.mContext = context;
        this.mlist = list;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.cate_itemview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            ((MyViewHolder) holder).mtv.setText(mlist.get(position).toString());
        }
    }
}

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mtv;

        public MyViewHolder(View itemView) {
            super(itemView);
            mtv = (TextView) itemView.findViewById(R.id.tv_card);
        }

    }
