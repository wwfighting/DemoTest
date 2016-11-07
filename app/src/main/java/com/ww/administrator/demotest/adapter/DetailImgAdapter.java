package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bm.library.Info;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.util.DisplayUtil;
import com.ww.administrator.demotest.widget.PicShowAlertDialog;

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
            imgViewHolder.mIvDetailShow = (PhotoView) convertView.findViewById(R.id.iv_detail_show);
            convertView.setTag(imgViewHolder);
        }else {
            imgViewHolder = (ImgViewHolder) convertView.getTag();
        }

        //加载图片
        loadGlideBitmap(mList.get(position), imgViewHolder);

        return convertView;
    }

    private void loadGlideBitmap(String url,final ImgViewHolder viewHolder){
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(DisplayUtil.getScreenWidth(mContext), Target.SIZE_ORIGINAL) {

                    @Override
                    public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {

                        //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
                        System.out.println("=============");
                        System.out.println("width:" + bitmap.getWidth() + "height:" + bitmap.getHeight());
                        System.out.println("=============");
                       /* ViewGroup.LayoutParams params = viewHolder.mIvDetailShow.getLayoutParams();
                        params.height = bitmap.getHeight();
                        params.width = DisplayUtil.getScreenWidth(mContext);
                        viewHolder.mIvDetailShow.setLayoutParams(params);*/
                        viewHolder.mIvDetailShow.setImageBitmap(bitmap);
                        viewHolder.mIvDetailShow.disenable();
                        viewHolder.mIvDetailShow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        //viewHolder.mIvDetailShow.setScaleType(ImageView.ScaleType.CENTER);
                        viewHolder.mIvDetailShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popUpPicDialog(viewHolder.mIvDetailShow.getInfo(), bitmap);
                            }
                        });

                    }

                });

    }


    private void popUpPicDialog(Info info, Bitmap bitmap){
        PicShowAlertDialog dialog = new PicShowAlertDialog(mContext, info, bitmap);
        dialog.show();
    }

    private static class ImgViewHolder{
        PhotoView mIvDetailShow;
    }

}
