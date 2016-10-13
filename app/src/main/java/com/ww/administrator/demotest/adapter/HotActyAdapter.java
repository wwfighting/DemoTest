package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.BannerConActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.ActyInfo;
import com.ww.administrator.demotest.util.Constants;

/**
 * Created by Administrator on 2016/9/18.
 */
public class HotActyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE = 1;
    Context mContext;
    ActyInfo mInfo;

    public HotActyAdapter(Context context, ActyInfo actyInfo) {
        this.mContext = context;
        this.mInfo = actyInfo;
    }

    @Override
    public int getItemCount() {
        return mInfo.getData().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new ActyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_acty_layout, parent,false)) : null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ActyViewHolder){

            //载入图片
            Glide.with(mContext)
                    .load(Constants.BASE_IMG_URL + mInfo.getData().get(position).getImgurl())
                    .crossFade()
                    .into(((ActyViewHolder) holder).mivShow);

            ((ActyViewHolder) holder).mtvBig.setText(mInfo.getData().get(position).getTitle());
            ((ActyViewHolder) holder).mtvSmall.setText(mInfo.getData().get(position).getTitle());
            if (mInfo.getData().get(position).getStatus().equals("1")){ //正在进行的活动
                ((ActyViewHolder) holder).mLine.setBackgroundColor(Color.parseColor("#E4393C"));
                ((ActyViewHolder) holder).mtvTitle.setTextColor(Color.parseColor("#E4393C"));
                ((ActyViewHolder) holder).mtvTitle.setText("正在进行");
                ((ActyViewHolder) holder).mbtnGo.setBackgroundColor(Color.parseColor("#E4393C"));
                ((ActyViewHolder) holder).mbtnGo.setText("进入活动");
                ((ActyViewHolder) holder).mbtnGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BannerConActivity.class);
                        intent.putExtra("bannerUrl", mInfo.getData().get(position).getHref());
                        mContext.startActivity(intent);

                    }
                });

            }else {
                ((ActyViewHolder) holder).mLine.setBackgroundColor(Color.parseColor("#666666"));
                ((ActyViewHolder) holder).mtvTitle.setTextColor(Color.parseColor("#666666"));
                ((ActyViewHolder) holder).mtvTitle.setText("往期活动");
                ((ActyViewHolder) holder).mbtnGo.setBackgroundColor(Color.parseColor("#9D9D9D"));
                ((ActyViewHolder) holder).mbtnGo.setText("已过期");
                ((ActyViewHolder) holder).mbtnGo.setOnClickListener(null);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }


    private class ActyViewHolder extends RecyclerView.ViewHolder{

        View mLine;
        TextView mtvBig, mtvSmall, mtvTitle;
        AppCompatButton mbtnGo;
        ImageView mivShow;

        public ActyViewHolder(View itemView) {
            super(itemView);
            mtvBig = (TextView) itemView.findViewById(R.id.tv_big);
            mtvSmall = (TextView) itemView.findViewById(R.id.tv_small);
            mbtnGo = (AppCompatButton) itemView.findViewById(R.id.btn_acty_go);
            mivShow = (ImageView) itemView.findViewById(R.id.iv_acty);
            mLine = itemView.findViewById(R.id.vertical_line);
            mtvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
