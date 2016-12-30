package com.ww.administrator.demotest.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.ProductListInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.ToolsUtil;

/**
 * Created by Administrator on 2016/8/27.
 */
public class ProductListAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TITLE_TYPE = 0;
    private static final int ITEM_CONTENT_TYPE = 1;

    private Context mContext;
    private ProductListInfo mList;



    public ProductListAdapter1(Context context, ProductListInfo list) {

        this.mContext = context;
        this.mList = list;
    }

    public void refresh(ProductListInfo list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == ITEM_TITLE_TYPE ?
                new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_texttitle_view, parent, false)) :
                new ContentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_productlist_item_layout, parent, false)) ;
    }

    public void addAll(ProductListInfo list) {

        int startIndex = mList.getData().size();


        mList.getData().addAll(startIndex, list.getData());
        notifyItemRangeInserted(startIndex, list.getData().size());
    }

    public void add(ProductListInfo list) {
        insert(list, mList.getData().size());
    }

    public void insert(ProductListInfo list, int position) {
        mList.getData().add(position, list.getData().get(position));
        notifyItemInserted(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TitleViewHolder){
            if (mList.getData() == null){
                ((TitleViewHolder) holder).mTvCount.setText("为您找到" + 0 + "件商品");
            }else {
                ((TitleViewHolder) holder).mTvCount.setText("为您找到" + mList.getData().size()+ "件商品");
            }

        }else {
            if (mList.getData() != null){
                Log.d("piurl",mList.getData().get(position).getPicurl());
                Glide.with(mContext).load(mList.getData().get(position).getPicurl()).into(((ContentViewHolder) holder).mIvProShow);
                ((ContentViewHolder) holder).mTvProTitle.setText(mList.getData().get(position).getGoodsname());
                ((ContentViewHolder) holder).mTvProOrderCount.setText("已预约" + mList.getData().get(position ).getMyordercount() + "次");
                ((ContentViewHolder) holder).mTvProCommentCount.setText(mList.getData().get(position ).getMycommentcount() + "人评价");

                if (mList.getData().get(position).getImg1() != null){
                    if (!mList.getData().get(position ).getImg1().isEmpty()){
                        ((ContentViewHolder) holder).mivLeftLogo.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(Constants.BASE_IMG_URL + mList.getData().get(position ).getImg1())
                                .crossFade()
                                .into(((ContentViewHolder) holder).mivLeftLogo);
                    }else {
                        ((ContentViewHolder) holder).mivLeftLogo.setVisibility(View.GONE);
                    }
                }


                if (mList.getData().get(position ).getImg2() != null){
                    if (!mList.getData().get(position).getImg2().isEmpty()){
                        ((ContentViewHolder) holder).mivLabel.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(Constants.BASE_IMG_URL + mList.getData().get(position).getImg2())
                                .crossFade()
                                .into(((ContentViewHolder) holder).mivLabel);
                    }else {
                        ((ContentViewHolder) holder).mivLabel.setVisibility(View.GONE);
                    }
                }

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* String gid = "";
                    if (mList.getData().get(position - 1).getQid() != null && !mList.getData().get(position - 1).getQid().equals("0")){
                        gid = mList.getData().get(position - 1).getQid();
                    }else {
                        gid = mList.getData().get(position - 1).getId();
                    }*/

                    Intent intent = new Intent();
                    intent.setClass(mContext, DetailActivity.class);
                    intent.putExtra("gid", mList.getData().get(position ).getId());
                    //将物品ID通过intent对象传值到详细页面
                    if (ToolsUtil.GetVersionSDK() < Build.VERSION_CODES.LOLLIPOP){
                        mContext.startActivity(intent);
                    }else {
                        View sharedView = holder.itemView.findViewById(R.id.iv_pro_show);
                        String transitionName = "detail";
                        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity)mContext, sharedView, transitionName);
                        mContext.startActivity(intent, transitionActivityOptions.toBundle());
                    }


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.getData().size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_CONTENT_TYPE;
    }


    public class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView mTvCount;

        public TitleViewHolder(View itemView) {
            super(itemView);
            mTvCount = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder{

        ImageView mIvProShow, mivLabel, mivLeftLogo;
        TextView mTvProTitle, mTvProOrderCount, mTvProCommentCount;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mIvProShow = (ImageView) itemView.findViewById(R.id.iv_pro_show);
            mTvProTitle = (TextView) itemView.findViewById(R.id.tv_pro_title);
            mTvProOrderCount = (TextView) itemView.findViewById(R.id.tv_pro_ordercount);
            mTvProCommentCount = (TextView) itemView.findViewById(R.id.tv_pro_commentcount);
            mivLabel = (ImageView) itemView.findViewById(R.id.iv_label);
            mivLeftLogo = (ImageView) itemView.findViewById(R.id.iv_home_left_logo);
        }
    }
}
