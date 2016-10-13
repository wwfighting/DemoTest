package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.adapter.AddressAdapter;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.AddressInfo;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AddressManageActivity extends AppCompatActivity implements View.OnClickListener{


    String uid = "-1";
    Toolbar mtbAddress;
    CardView mcvAddress;
    RecyclerView mrvShow;
    FloatingActionButton mfabAdd;
    RelativeLayout mrvToAdd;
    AddressAdapter mAdapter;
    ProgressWheel mpbShow;
    CoordinatorLayout mContainer;
    Gson mGson = new Gson();
    LinearLayoutManager mManager;
    MaterialDialog mDialog;
    TextView mtvDialogName ;
    ProgressWheel mpgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressmanage_layout);
        uid = ((MyApp) getApplicationContext()).getUser().getId();
        initViews();
        initEvents();
        loadDatas();

    }

    private void initViews() {
        mtbAddress = (Toolbar) findViewById(R.id.tb_address);
        mcvAddress = (CardView) findViewById(R.id.cv_address);
        mrvShow = (RecyclerView) findViewById(R.id.rv_address);
        mfabAdd = (FloatingActionButton) findViewById(R.id.fab_address);
        mrvToAdd = (RelativeLayout) findViewById(R.id.rv_address_add);
        mpbShow = (ProgressWheel) findViewById(R.id.pb_common);
        mContainer = (CoordinatorLayout) findViewById(R.id.cdl_container);
        setToolbar();

        mpbShow.setVisibility(View.VISIBLE);
        mcvAddress.setVisibility(View.GONE);
        mrvShow.setVisibility(View.GONE);
        mfabAdd.setVisibility(View.GONE);
    }

    private void setToolbar() {
        mtbAddress.setTitle("地址管理");
        mtbAddress.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbAddress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initEvents(){
        mrvToAdd.setOnClickListener(this);
        mfabAdd.setOnClickListener(this);
    }


    private void loadDatas() {
        HttpUtil.postAsyn(Constants.BASE_URL + "get_address.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mpbShow.setVisibility(View.GONE);
                Snackbar.make(mContainer, "请先检查您的网络！", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonRoot = new JSONObject(response);
                    String strCode = jsonRoot.getString("code");
                    if (strCode.equals("200")){
                        AddressInfo info = mGson.fromJson(response, AddressInfo.class);
                        //成功
                        mpbShow.setVisibility(View.GONE);
                        mcvAddress.setVisibility(View.GONE);
                        mrvShow.setVisibility(View.VISIBLE);
                        mfabAdd.setVisibility(View.VISIBLE);
                        mAdapter = new AddressAdapter(AddressManageActivity.this, info);
                        mManager = new LinearLayoutManager(AddressManageActivity.this, LinearLayoutManager.VERTICAL, false);
                        mrvShow.setLayoutManager(mManager);
                        mrvShow.setAdapter(mAdapter);
                        editAddress();
                    }else {
                        //有误
                        mpbShow.setVisibility(View.GONE);
                        mcvAddress.setVisibility(View.VISIBLE);
                        mrvShow.setVisibility(View.GONE);
                        mfabAdd.setVisibility(View.GONE);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid)
        });
    }


    private void editAddress(){

        mAdapter.setIMyViewHolderClicks(new AddressAdapter.IMyViewHolderClicks() {
            @Override
            public void isSetDefault(AddressInfo info, boolean isDefault, int pos) {
                setDefaultAddress(info, isDefault, pos);
            }

            @Override
            public void updateAddress(AddressInfo info, int pos) {
                Intent update = new Intent(AddressManageActivity.this, UpdateAddressActivity.class);
                String str = mGson.toJson(info.getData().get(pos));
                update.putExtra("data", str);
                startActivityForResult(update, 100);
            }

            @Override
            public void deleteAddress(AddressInfo info, int pos) {
                delAddress(info, pos);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rv_address_add:
                Intent rvAdd = new Intent(AddressManageActivity.this, AddAddressActivity.class);
                startActivityForResult(rvAdd, 100);
                break;
            case R.id.fab_address:
                Intent fabAdd = new Intent(AddressManageActivity.this, AddAddressActivity.class);
                startActivityForResult(fabAdd, 100);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200){
            loadDatas();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置默认地址
     * @param info
     * @param isDefault
     * @param pos
     */
    private void setDefaultAddress(AddressInfo info, boolean isDefault, int pos){

        //初始化Dialog
        initDialog("设置中...");
        //网络请求
        HttpUtil.postAsyn(Constants.BASE_URL + "edit_address.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mDialog.dismiss();
                Snackbar.make(mContainer, "设置失败，请检查您的网络！", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                mDialog.dismiss();
                ResultInfo resultInfo = mGson.fromJson(response, ResultInfo.class);
                if (resultInfo.getCode().equals("200")){
                    ((MyApp) getApplicationContext()).SetActivityIntent(Constants.ADDRESS_REFRESH, 1);
                    loadDatas();
                }else {
                    Snackbar.make(mContainer, resultInfo.getInfo(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("aid", info.getData().get(pos).getAid()),
                new HttpUtil.Param("isDefault", isDefault ? "1" : "0"),
                new HttpUtil.Param("action", "setDefault")

        });
    }

    private void initDialog(String str){
        mDialog = new MaterialDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_common, null);
        mtvDialogName = (TextView) view.findViewById(R.id.tv_dialog_name_show);
        mpgDialog = (ProgressWheel) view.findViewById(R.id.progress_image);
        mpgDialog.setVisibility(View.VISIBLE);
        mtvDialogName.setText(str);

        mDialog.setContentView(view);
        mDialog.show();
    }

    private void delAddress(AddressInfo info, final int pos){
        //初始化Dialog
        initDialog("删除中...");
        //网络请求
        HttpUtil.postAsyn(Constants.BASE_URL + "edit_address.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mDialog.dismiss();
                Snackbar.make(mContainer, "删除失败，请检查您的网络！", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                mDialog.dismiss();
                ResultInfo resultInfo = mGson.fromJson(response, ResultInfo.class);
                if (resultInfo.getCode().equals("200")){
                    ((MyApp) getApplicationContext()).SetActivityIntent(Constants.ADDRESS_REFRESH, 1);
                    mAdapter.deleteData(pos);
                    if (mAdapter.getItemCount() == 0){
                        mpbShow.setVisibility(View.GONE);
                        mcvAddress.setVisibility(View.VISIBLE);
                        mrvShow.setVisibility(View.GONE);
                        mfabAdd.setVisibility(View.GONE);
                    }
                }else {
                    Snackbar.make(mContainer, resultInfo.getInfo(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("aid", info.getData().get(pos).getAid()),
                new HttpUtil.Param("action", "delete")

        });
    }
}
