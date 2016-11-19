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
 * Created by Administrator on 2016/11/16.
 */
public class HomeCateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE = 1;

    private final static String[] CATETITLE = {"Fuck", "U", "!", "Bitch", "Suck", "Suck", "My", "Dick"};

    private Context mContext;

    public HomeCateAdapter(Context context) {
        mContext = context;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HomeCateContentViewHolder){
            ((HomeCateContentViewHolder) holder).mtvCateTitle.setText(CATETITLE[position]);
        }
    }

    @Override
    public int getItemCount() {
        return CATETITLE.length;
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
