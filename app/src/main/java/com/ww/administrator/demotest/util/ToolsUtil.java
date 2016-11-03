package com.ww.administrator.demotest.util;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ToolsUtil {

    public static String getRoot(String strRoot) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        else {
            sdDir = Environment.getRootDirectory();
        }

        String strDir = sdDir.toString() + strRoot;
        GetFolder(strDir);
        return strDir;
    }

    public static String GetFolder(String strFolder) {
        if(new File(strFolder).exists())
        {
            return strFolder;
        }

        new File(strFolder).mkdir();
        return strFolder;
    }

    /**
     * 获得手机sdk版本
     * @return
     */
    public static int GetVersionSDK(){
        int version = 0;
        try {
            version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {

        }
        return version;
    }

    /**
     * 判断双11活动有没有过期
     * @return
     */
    public static boolean isEventExpire(){
        //格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endDate = "2016-11-11 23:59:59";
        Date d = new Date(System.currentTimeMillis());//获取当前时间
        String beginDate = CurrentTime.format(d);
        try {

            Date curDate = CurrentTime.parse(beginDate);
            Date endTime = CurrentTime.parse(endDate);

            if(((endTime.getTime() - curDate.getTime())/(24*60*60*1000))<=0) {
                return true;
            }else{
                return false;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return true;
        }

    }
}
