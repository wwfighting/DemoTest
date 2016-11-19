package com.ww.administrator.demotest.util;

/**
 * Created by Administrator on 2016/8/20.
 */
public class Constants {

    //api
    public static final String BASE_TEST_URL = "http://192.168.1.25/test/jvawa/";
    //public static final String BASE_URL = "http://192.168.1.25/test/jvawa/";
    public static final String BASE_URL = "http://www.jvawa.com/new/jvawaApi/";
    public static final String BASE_IMG_URL= "http://www.jvawa.com/";

    //file
    public static final String SD_ROOT = FileUtil.getRoot("/Jvawa/");
    public static final String SD_TMP = FileUtil.GetFolder(SD_ROOT + "/temp/");
    public static final String SD_NODE = ToolsUtil.GetFolder(SD_ROOT + "/node/");
    public static final String SD_LOGINUSERNODE = Constants.SD_NODE + "user";
    public static final String SD_DBINTENT = SD_TMP + "/intent";
    public static final String SD_TEMPIMG = SD_TMP + "temp.png";

    //refresh
    public static final String SHOPPING_CART_REFRESH = "SHOPPING_CART_REFRESH";
    public static final String MY_REFRESH = "MY_REFRESH";
    public static final String ADDRESS_REFRESH = "ADDRESS_REFRESH";
    public static final String ADD_SHOPPING_CART_REFRESH = "ADD_SHOPPING_CART_REFRESH";
    public static final String HOME_REFRESH = "HOME_REFRESH";
    public static final String CATE_REFRESH = "CATE_REFRESH";

    //params
    public static final String[] FLOW_LAYOUT_TITLE = {"小康美厨","尚品魅厨","精英悦厨","铂金丽厨",
            "名家雅厨","欧风御厨","至尊典厨"};

    public static final String[] CITY_ARRAY = new String[]{"南京","上海","兰州","沈阳"};
}
