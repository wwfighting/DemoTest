package com.ww.administrator.demotest.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/10.
 */
public class RefreshUtil implements Serializable {

    private static final long serialVersionUID = 5893243639891874744L;
    private Map<String, Object> mRefreshMap = new HashMap<String, Object>();
    public  void SetRefresh(String strKey, Object obj) {
        if(mRefreshMap.containsKey(strKey)) {
            mRefreshMap.remove(strKey);
        }

        mRefreshMap.put(strKey, obj);
    }

    public  Object GetRefresh(String strKey) {
        if(mRefreshMap.containsKey(strKey)) {
            Object obj = mRefreshMap.get(strKey);
            mRefreshMap.remove(strKey);
            return obj;
        }

        return null;
    }
}
