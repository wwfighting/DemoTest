package com.ww.administrator.demotest.util;

import android.os.Environment;

import java.io.File;

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
}
