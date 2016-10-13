package com.ww.administrator.demotest.cityselect;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.ww.administrator.demotest.cityselect.db.DBManager;
import com.ww.administrator.demotest.model.User;
import com.ww.administrator.demotest.util.Constants;
import com.ww.administrator.demotest.util.FileUtil;
import com.ww.administrator.demotest.util.RefreshUtil;

import java.io.File;

import cn.beecloud.BeeCloud;

/**
 * Created by Administrator on 206/7/27.
 */
public class MyApp extends Application{

    private DBManager dbHelper;

    User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化百度地图
        //SDKInitializer.initialize(this);
        //导入数据库
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();
//      dbHelper.closeDatabase();
        //初始化Stetho
        Stetho.initializeWithDefaults(this);

        //初始化二维码扫描库
        ZXingLibrary.initDisplayOpinion(this);

        //初始化BeeCloud
        BeeCloud.setSandbox(false);
        BeeCloud.setAppIdAndSecret("2cf805f4-3842-4ab2-871d-421251da961d",
                "5b813c99-80f3-4ca5-b725-78cc3c0773e8");
    }

    /**
     * 得到用户登录时的一个全局对象User
     * @return
     */
    public User getUser(){
        if (mUser == null && FileUtil.exists(Constants.SD_LOGINUSERNODE) == false){

            //mUser为空，没有用户登录
            return null;
        }

        if (mUser == null){
            mUser = (User) FileUtil.ReadObject(Constants.SD_LOGINUSERNODE);
        }

        return mUser;
    }

    /**
     * 设置用户登录时的一个全局对象User
     * @param user
     */
    public void setUser(User user){
        new File(Constants.SD_LOGINUSERNODE).delete();
        mUser = user;
        if (user != null){
            FileUtil.WriteObject(Constants.SD_LOGINUSERNODE, mUser);
            //若要定位，可以在此时启动GPS
        }else {
            new File(Constants.SD_LOGINUSERNODE).delete();
        }
    }

    /**
     * 设置是否对activity进行刷新
     * @param strKey
     * @param obj
     */
    public void SetActivityIntent(String strKey, Object obj) {
        RefreshUtil refresh = (RefreshUtil) FileUtil
                .ReadObject(Constants.SD_DBINTENT);
        if (null == refresh) {
            refresh = new RefreshUtil();
        }
        refresh.SetRefresh(strKey, obj);
        FileUtil.WriteObject(Constants.SD_DBINTENT, refresh);
    }

    /**
     * 根据key得到对象判断是否需要进行刷新
     * @param strKey
     * @return
     */
    public Object GetActivityIntent(String strKey) {
        RefreshUtil refresh = (RefreshUtil) FileUtil
                .ReadObject(Constants.SD_DBINTENT);
        if (null == refresh) {
            return null;
        }
        Object obj = refresh.GetRefresh(strKey);
        FileUtil.WriteObject(Constants.SD_DBINTENT, refresh);
        return obj;
    }

}
