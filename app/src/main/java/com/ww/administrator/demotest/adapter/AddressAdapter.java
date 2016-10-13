package com.ww.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ww.administrator.demotest.R;
import com.ww.administrator.demotest.model.AddressInfo;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE = 1;
    Context mContext;
    AddressInfo mInfo;

    public AddressAdapter(Context context, AddressInfo addressInfo) {
        this.mContext = context;
        this.mInfo = addressInfo;
    }

    public void deleteData(int pos){
        mInfo.getData().remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, mInfo.getData().size());
    }

    @Override
    public int getItemCount() {
        return mInfo.getData().size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == VIEW_TYPE ? new AddressViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_address_layout, parent, false)) : null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AddressViewHolder){
            ((AddressViewHolder) holder).mtvName.setText(mInfo.getData().get(position).getReceivername());
            ((AddressViewHolder) holder).mtvPhone.setText(mInfo.getData().get(position).getPhone());
            ((AddressViewHolder) holder).mtvAddress.setText(mInfo.getData().get(position).getAddress());
            if (mInfo.getData().get(position).getIsdefault().equals("1")){
                ((AddressViewHolder) holder).mcbDefault.setChecked(true);
            }else {
                ((AddressViewHolder) holder).mcbDefault.setChecked(false);
            }

            ((AddressViewHolder) holder).mtvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMyViewHolderClicks != null){
                        mMyViewHolderClicks.updateAddress(mInfo, position);
                    }
                }
            });

            ((AddressViewHolder) holder).mcbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mMyViewHolderClicks != null){
                        mMyViewHolderClicks.isSetDefault(mInfo, isChecked, position);
                    }
                }
            });

            ((AddressViewHolder) holder).mtvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMyViewHolderClicks != null){
                        mMyViewHolderClicks.deleteAddress(mInfo, position);
                    }
                }
            });
        }
    }



    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE;
    }


    private class AddressViewHolder extends RecyclerView.ViewHolder{

        TextView mtvName, mtvPhone, mtvAddress, mtvUpdate, mtvDelete;
        AppCompatCheckBox mcbDefault;

        public AddressViewHolder(View itemView) {
            super(itemView);
            mtvName = (TextView) itemView.findViewById(R.id.tv_name);
            mtvPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            mtvAddress = (TextView) itemView.findViewById(R.id.tv_address_content);
            mtvUpdate = (TextView) itemView.findViewById(R.id.tv_edit_address);
            mtvDelete = (TextView) itemView.findViewById(R.id.tv_del_address);
            mcbDefault = (AppCompatCheckBox) itemView.findViewById(R.id.cb_address_default);
        }
    }


    public interface IMyViewHolderClicks{

        void isSetDefault(AddressInfo info, boolean isDefault, int pos);

        void updateAddress(AddressInfo info, int pos);

        void deleteAddress(AddressInfo info, int pos);
    }

    public void setIMyViewHolderClicks(IMyViewHolderClicks iMyViewHolderClicks){
        this.mMyViewHolderClicks = iMyViewHolderClicks;
    }
    private IMyViewHolderClicks mMyViewHolderClicks;
}
