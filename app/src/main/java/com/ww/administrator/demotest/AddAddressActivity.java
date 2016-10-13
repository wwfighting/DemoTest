package com.ww.administrator.demotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.model.ResultInfo;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.HttpUtil;
import com.ww.administrator.demotest.util.RegexUtil;

/**
 * Created by Administrator on 2016/9/18.
 */
public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar mtbAdd;
    EditText metName, metPhone, metAddress;
    AppCompatCheckBox mcbSetDefault;
    AppCompatButton mbtnAdd;
    RelativeLayout mrlExpand;
    LinearLayout mllStreet;
    AppCompatSpinner mspCity, mspZone;
    String[] zoneArr = null;
    Gson mGson = new Gson();

    boolean isExpand = true;

    String uid = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaddress_layout);
        uid = ((MyApp) getApplicationContext()).getUser().getId();
        initViews();
        initEvents();
    }

    private void initViews() {
        mtbAdd = (Toolbar) findViewById(R.id.tb_common);
        setToolbar();
        metName = (EditText) findViewById(R.id.et_receivername);
        metPhone = (EditText) findViewById(R.id.et_receiverphone);
        metAddress = (EditText) findViewById(R.id.et_receiveraddress);
        mcbSetDefault = (AppCompatCheckBox) findViewById(R.id.cb_setdefault_address);
        mbtnAdd = (AppCompatButton) findViewById(R.id.btn_address_add);
        mrlExpand = (RelativeLayout) findViewById(R.id.rv_expand);
        mllStreet = (LinearLayout) findViewById(R.id.ll_street);
        mspCity = (AppCompatSpinner) findViewById(R.id.sp_city);
        mspZone = (AppCompatSpinner) findViewById(R.id.sp_zone);

        if (isExpand){
            mllStreet.setVisibility(View.VISIBLE);
        }else {
            mllStreet.setVisibility(View.GONE);
        }

        metAddress.setText(mspCity.getSelectedItem().toString() + mspZone.getSelectedItem().toString());
        mspCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metAddress.setText(mspCity.getSelectedItem() + mspZone.getSelectedItem().toString());
                switch (position){
                    case 0:
                        zoneArr  = getResources().getStringArray(R.array.nj_zone);

                        break;
                    case 1:
                        zoneArr  = getResources().getStringArray(R.array.lz_zone);

                        break;
                    case 2:
                        zoneArr  = getResources().getStringArray(R.array.sh_zone);

                        break;
                    case 3:
                        zoneArr  = getResources().getStringArray(R.array.sy_zone);

                        break;
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(AddAddressActivity.this, android.R.layout.simple_spinner_dropdown_item, zoneArr);
                mspZone.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mspZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metAddress.setText(mspCity.getSelectedItem() + mspZone.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setToolbar() {
        mtbAdd.setTitle("添加新地址");
        mtbAdd.setLogo(R.mipmap.logo);
        setSupportActionBar(mtbAdd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initEvents(){
        mrlExpand.setOnClickListener(this);
        mbtnAdd.setOnClickListener(this);
    }

    private void addAddress(){

        if (TextUtils.isEmpty(metName.getText().toString())){
            Snackbar.make(mbtnAdd, "收货人姓名有误！", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(metPhone.getText().toString()) || !RegexUtil.isMobileNumber(metPhone.getText().toString())){
            Snackbar.make(mbtnAdd, "收货人手机号有误！", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(metAddress.getText().toString()) || metAddress.getText().length() < 12){
            Snackbar.make(mbtnAdd, "收货人地址有误！", Snackbar.LENGTH_LONG).show();
            return;
        }


        HttpUtil.postAsyn(Constants.BASE_URL + "edit_address.php", new HttpUtil.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Snackbar.make(mbtnAdd, "请先检查您的网络！", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                
                ResultInfo info = mGson.fromJson(response, ResultInfo.class);
                if (info.getCode().equals("200")){
                    addSuccess();
                }else {
                    Snackbar.make(mbtnAdd, info.getInfo(), Snackbar.LENGTH_LONG).show();
                }
            }
        }, new HttpUtil.Param[]{
                new HttpUtil.Param("uid", uid),
                new HttpUtil.Param("reciverName", metName.getText().toString()),
                new HttpUtil.Param("phoneNum", metPhone.getText().toString()),
                new HttpUtil.Param("address", metAddress.getText().toString()),
                new HttpUtil.Param("isDefault", mcbSetDefault.isChecked() ? "1" : "0"),
                new HttpUtil.Param("action", "add")
        });
    }

    private void addSuccess(){
        ((MyApp) getApplicationContext()).SetActivityIntent(Constants.ADDRESS_REFRESH, 1);
        Intent intent = new Intent(AddAddressActivity.this, AddressManageActivity.class);
        setResult(200, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rv_expand:

                break;
            case R.id.btn_address_add:
                addAddress();
                break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
