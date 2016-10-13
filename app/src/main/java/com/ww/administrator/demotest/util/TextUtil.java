package com.ww.administrator.demotest.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;

/**
 * Created by Administrator on 2016/8/26.
 */
public class TextUtil {

    /**
     * 给文字设置中划线
     * @param s
     * @return
     */
    public static SpannableString setStrSpan(String s){
        StringBuffer stringBuffer = new StringBuffer(s);
        SpannableString spannableString = new SpannableString(stringBuffer);
        spannableString.setSpan(new StrikethroughSpan(), 0, stringBuffer.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}
