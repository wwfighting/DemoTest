package com.ww.administrator.demotest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/9/14.
 */
public class RegexUtil {

    /**
     * 手机号码Pattern
     */
    public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("\\d{11}");

    /**
     * 用户密码Pattern 只能是数字,字母或下划线并且长度小于20
     */
    public static final Pattern USER_PWD_PATTERN = Pattern.compile("[A-Za-z0-9_]+");


    /**
     * 手机号码是否正确
     *
     * @param s
     * @return
     */
    public static boolean isMobileNumber(String s) {
        Matcher m = MOBILE_NUMBER_PATTERN.matcher(s);
        return m.matches();
    }

    /**
     * 验证密码是否正确
     * @param s
     * @return
     */
    public static boolean isPwd(String s){
        Matcher m = USER_PWD_PATTERN.matcher(s);
        return ((m.matches()) && (s.length() < 20) && (s.length() > 4));
    }

}
