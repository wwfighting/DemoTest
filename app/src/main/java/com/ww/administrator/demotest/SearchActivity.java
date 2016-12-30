package com.ww.administrator.demotest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.ww.administrator.demotest.adapter.SearchAdapter;
import com.ww.administrator.demotest.util.Constants;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public class SearchActivity extends AppCompatActivity {

    ImageButton mimBtnBack;
    TagFlowLayout mTagFlowLayout;
    RecyclerView mrvSearch;
    EditText metSearch;
    ProgressWheel mpgSearch;
    CoordinatorLayout mClContainer;

    private List<String> mList = new ArrayList<>();
    private SearchAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        searchEvents();
    }

    private void initViews() {
        mimBtnBack = (ImageButton) findViewById(R.id.imbtn_back);
        metSearch = (EditText) findViewById(R.id.et_search);
        mTagFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        mrvSearch = (RecyclerView) findViewById(R.id.rv_search_result);
        mpgSearch = (ProgressWheel) findViewById(R.id.pb_common);
        mClContainer = (CoordinatorLayout) findViewById(R.id.fl_container);
        mpgSearch.setVisibility(View.GONE);


        mimBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTagFlowLayout.setAdapter(new TagAdapter<String>(Constants.FLOW_LAYOUT_TITLE) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.flow_txt_layout, mTagFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                metSearch.setText(Constants.FLOW_LAYOUT_TITLE[position].toString());
                String keyName = metSearch.getText().toString();
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, ProductListActivity.class);
                intent.putExtra("keyName", keyName);
                intent.putExtra("isRecom", "-1");
                startActivity(intent);
                return true;
            }
        });

        metSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mrvSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

    }

    private void searchEvents() {

        metSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId ==EditorInfo.IME_ACTION_SEARCH){
                    // 先隐藏键盘
                    ((InputMethodManager) metSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                            getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    if (metSearch.getText().length() == 0) {
                        Snackbar.make(mClContainer, "关键字不能为空！", Snackbar.LENGTH_SHORT).show();
                    } else {
                        String keyWord = metSearch.getText().toString();
                        Intent intent = new Intent();
                        intent.setClass(SearchActivity.this, ProductListActivity.class);
                        intent.putExtra("keyName", keyWord);
                        intent.putExtra("isRecom", "-1");
                        startActivity(intent);
                        return true;
                    }

                }
                return false;
            }
        });
    }
}