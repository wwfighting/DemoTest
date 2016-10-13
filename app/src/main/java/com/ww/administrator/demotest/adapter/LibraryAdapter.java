package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.util.CircleTransform;

/**
 * Created by Administrator on 2016/9/20.
 */
public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Library[] libs = {
            new Library("Android support libs",
                    "https://android.googlesource.com/platform/frameworks/support/",
                    "https://raw.githubusercontent.com/wwfighting/BlogPic/master/Pictures/google_logo.png"),
            new Library("FlowLayout",
                    "https://github.com/hongyangAndroid/FlowLayout/",
                    "https://avatars3.githubusercontent.com/u/10704521?v=3&s=400"),
            new Library("smarttablayout",
                    "https://github.com/ogaclejapan/SmartTabLayout/",
                    "https://avatars0.githubusercontent.com/u/1496485?v=3&s=400"),
            new Library("Glide",
                    "https://github.com/bumptech/glide",
                    "https://avatars.githubusercontent.com/u/423539"),
            new Library("Material-ish Progress",
                    "https://github.com/pnikosis/materialish-progress/",
                    "https://avatars3.githubusercontent.com/u/6094153?v=3&s=400"),
            new Library("OkHttp",
                    "http://square.github.io/okhttp/",
                    "https://avatars.githubusercontent.com/u/82592")};

    private static final int VIEW_TYPE_INTRO = 0;
    private static final int VIEW_TYPE_LIBRARY = 1;

    Context mContext;

    private final CircleTransform circleCrop;
    public LibraryAdapter(Context context) {
        mContext = context;
        circleCrop = new CircleTransform(context);
    }

    @Override
    public int getItemCount() {
        return libs.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_INTRO : VIEW_TYPE_LIBRARY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE_INTRO ? new TitleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_lib_title_layout, parent, false))
                : new ContentViewHodler(LayoutInflater.from(mContext).inflate(R.layout.adapter_lib_content_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int postion) {
        if (holder instanceof ContentViewHodler){
            bindLibrary((ContentViewHodler)holder, libs[postion - 1]);
        }

    }

    private void bindLibrary(final ContentViewHodler holder, final Library lib){
        holder.mtvLib.setText(lib.name);
        Glide.with(mContext)
                .load(lib.imageUrl)
                .transform(circleCrop)
                .into(holder.mivLib);
        final View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(lib.link)));
            }
        };

        holder.itemView.setOnClickListener(clickListener);
        holder.mbtnLib.setOnClickListener(clickListener);
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder{

        TextView mtvIntro;
        public TitleViewHolder(View itemView) {
            super(itemView);
            mtvIntro = (TextView) itemView.findViewById(R.id.tv_lib_title);
        }
    }

    private class ContentViewHodler extends RecyclerView.ViewHolder{

        ImageView mivLib;
        TextView mtvLib;
        Button mbtnLib;

        public ContentViewHodler(View itemView) {
            super(itemView);
            mivLib = (ImageView) itemView.findViewById(R.id.iv_lib);
            mtvLib = (TextView) itemView.findViewById(R.id.tv_lib_name);
            mbtnLib = (Button) itemView.findViewById(R.id.btn_lib_link);
        }
    }

    private static class Library {
        public final String name;
        public final String link;
        public final String imageUrl;

        public Library(String name, String link, String imageUrl) {
            this.name = name;
            this.link = link;
            this.imageUrl = imageUrl;
        }
    }
}
