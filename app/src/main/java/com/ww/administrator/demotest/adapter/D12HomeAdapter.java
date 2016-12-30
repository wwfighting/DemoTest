package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ww.administrator.demotest.DetailActivity;
import com.ww.administrator.demotest.ProductListActivity;
import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;

/**
 * Created by Administrator on 2016/11/22.
 */
public class D12HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_HEAD = 0;
    private final static int VIEW_TYPE = 1;
    private final static int[] PICURL= {R.drawable.nanjing_4, R.drawable.nanjing_5, R.drawable.quanguo_9,
                                        R.drawable.quanguo_8, R.drawable.nanjing_6};

    private final static int[] PICURL1= {R.drawable.nanjing_4, R.drawable.nanjing_5, R.drawable.nanjing_6,
                                        R.drawable.nanjing_7};

    private Context mConext;

    String city = "";

    public D12HomeAdapter(Context context) {
        this.mConext = context;
        city = (String) SharedPreUtil.getData(mConext, "locatCity", "南京");
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_HEAD : VIEW_TYPE;
    }

    @Override
    public int getItemCount() {

        if (city.equals("南京")){
            return 5;
        }
        return 6;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof D12HomeHeadViewHolder){
            /*
                "3550" => 小康美厨_雕刻时光A
                "3551" => 尚品魅厨_地平线
                "3552" => 精英悦厨_简爱
                "3553" => 铂晶丽厨_穆萨
                "3554" => 名家雅厨_雷尔诺
                "3555" => 欧风御厨_普利亚
                "3556" => 至尊典厨_圣艾米伦
             */
            final Intent intent = new Intent();
            ((D12HomeHeadViewHolder) holder).mivXK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "3527");
                    intent.putExtra("gid", "3572");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivQW.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (city.equals("南京")){
                        Toast.makeText(mConext, "抱歉，目前南京不参与全屋活动！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    intent.setClass(mConext, ProductListActivity.class);
                    intent.putExtra("keyName", "元全屋");
                    intent.putExtra("isRecom", "-1");
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivSP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "3528");
                    intent.putExtra("gid", "3573");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivJY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "3533");
                    intent.putExtra("gid", "3574");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivBJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "3546");
                    intent.putExtra("gid", "3575");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivMJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //intent.putExtra("gid", "3540");
                    intent.putExtra("gid", "3576");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivOF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "3543");
                    intent.putExtra("gid", "3577");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });
            ((D12HomeHeadViewHolder) holder).mivZZ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //intent.putExtra("gid", "2888");
                    intent.putExtra("gid", "3578");
                    intent.setClass(mConext, DetailActivity.class);
                    mConext.startActivity(intent);
                }
            });

        }

        if (holder instanceof D12HomeViewHolder){

            if (city.equals("南京")){
                Glide.with(mConext)
                        .load(PICURL1[position - 1])
                        .crossFade()
                        .into(((D12HomeViewHolder) holder).mivD12Show);
            }else {
                Glide.with(mConext)
                        .load(PICURL[position - 1])
                        .crossFade()
                        .into(((D12HomeViewHolder) holder).mivD12Show);
            }


            //((D12HomeViewHolder) holder).mivD12Show.setImageResource(PICURL[position - 1]);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new D12HomeViewHolder(LayoutInflater.from(mConext).inflate(R.layout.adapter_d12home_layout, parent, false))
                : new D12HomeHeadViewHolder(LayoutInflater.from(mConext).inflate(R.layout.adapter_d12home_head_layout, parent, false));
    }

    private class D12HomeHeadViewHolder extends RecyclerView.ViewHolder{

        ImageView mivXK, mivQW, mivSP, mivJY, mivBJ, mivMJ, mivOF, mivZZ;

        public D12HomeHeadViewHolder(View itemView) {
            super(itemView);
            mivXK = (ImageView) itemView.findViewById(R.id.iv_d12_xiaokang);
            mivQW = (ImageView) itemView.findViewById(R.id.iv_d12_quanwu);
            mivSP = (ImageView) itemView.findViewById(R.id.iv_d12_shangpin);
            mivJY = (ImageView) itemView.findViewById(R.id.iv_d12_jingying);
            mivBJ = (ImageView) itemView.findViewById(R.id.iv_d12_bojing);
            mivMJ = (ImageView) itemView.findViewById(R.id.iv_d12_mingjia);
            mivOF = (ImageView) itemView.findViewById(R.id.iv_d12_oufeng);
            mivZZ = (ImageView) itemView.findViewById(R.id.iv_d12_zhizun);
        }
    }
    private class D12HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView mivD12Show;
        public D12HomeViewHolder(View itemView) {
            super(itemView);
            mivD12Show = (ImageView) itemView.findViewById(R.id.iv_d12);
        }
    }


}
