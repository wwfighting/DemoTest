package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.GoodsDetailInfo;
import com.ww.administrator.demotest.util.Constants;

/**
 * Created by Administrator on 2016/9/28.
 */
public class DetailDoorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 为ItemView创建接口，进行点击事件的处理
     */
    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;
    private static final int VIEW_TYPE = 1;

    private Context mContext;
    private GoodsDetailInfo mInfoList;

    public DetailDoorAdapter(Context context, GoodsDetailInfo infoList) {
        this.mContext = context;
        this.mInfoList = infoList;
    }

    @Override
    public int getItemCount() {
        return mInfoList.getData().getDoor().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new ImgClassViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_detail_coltai_img, parent, false)):null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImgClassViewHolder){
            Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + mInfoList.getData().getDoor().get(position).getDpath())
                    .crossFade()
                    .into(((ImgClassViewHolder) holder).ivShow);

        }

        if (mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int layoutPosition = holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView,layoutPosition);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }

    private class ImgClassViewHolder extends RecyclerView.ViewHolder{

        ImageView ivShow;

        public ImgClassViewHolder(View itemView) {
            super(itemView);
            ivShow = (ImageView)itemView.findViewById(R.id.iv_detail_img);
        }
    }
}
