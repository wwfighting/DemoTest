package com.ww.administrator.demotest.util;

import android.graphics.Bitmap;

import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016/8/21.
 */
public class BitmapUtil {

    public static boolean ToFile(Bitmap bmp, String strFile) {
        try {
            FileOutputStream fos = new FileOutputStream(strFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            fos = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
