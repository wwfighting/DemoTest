package com.ww.administrator.demotest.util;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2016/8/20.
 */
public class FileUtil {

    public static String getRoot(String strRoot) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        else
        {
            sdDir = Environment.getRootDirectory();
        }

        String strDir = sdDir.toString() + strRoot;
        GetFolder(strDir);
        return strDir;
    }

    public static String GetFolder(String strFolder)
    {
        if(new File(strFolder).exists())
        {
            return strFolder;
        }

        new File(strFolder).mkdir();
        return strFolder;
    }

    public static boolean exists(String file) {
        return new File(file).exists();
    }


    public static Object ReadObject(String strFile) {
        try {
            FileInputStream freader = new FileInputStream(strFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(freader);
            Object obj = objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean WriteObject(String strFile, Object oc) {
        try {
            FileOutputStream outStream = new FileOutputStream(strFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    outStream);

            objectOutputStream.writeObject(oc);
            objectOutputStream.flush();
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
