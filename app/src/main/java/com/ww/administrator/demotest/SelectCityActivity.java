package com.ww.administrator.demotest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ww.administrator.demotest.cityselect.MyApp;
import com.ww.administrator.demotest.cityselect.utils.SharedPreUtil;
import com.ww.administrator.demotest.util.Constants;

/**
 * Created by Administrator on 2016/10/31.
 */
public class SelectCityActivity extends AppCompatActivity {

    private ListView mlvCity;
    boolean isDismissing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cityselect_layout);
        mlvCity = (ListView) findViewById(R.id.lv_city);
        mlvCity.setAdapter(new ArrayAdapter<String>(SelectCityActivity.this, android.R.layout.simple_list_item_1, Constants.CITY_ARRAY));

        mlvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MyApp app = (MyApp) getApplicationContext();

                switch (position) {
                    case 0: //南京
                        SharedPreUtil.saveData(SelectCityActivity.this, "locatCity", "南京");


                        break;
                    case 1: //上海
                        SharedPreUtil.saveData(SelectCityActivity.this, "locatCity", "上海");

                        break;
                    case 2: //兰州
                        SharedPreUtil.saveData(SelectCityActivity.this, "locatCity", "兰州");


                        break;
                    case 3: //沈阳
                        SharedPreUtil.saveData(SelectCityActivity.this, "locatCity", "沈阳");


                        break;
                }
                app.SetActivityIntent(Constants.HOME_REFRESH, 1);
                app.SetActivityIntent(Constants.CATE_REFRESH, 10);
                setResult(600);
                finish();
            }
        });

    }

    public void dismiss(View view) {
        isDismissing = true;
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }
}
