package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class StaffInfo {


    /**
     * code : 200
     * info : success
     * data : [{"salerno":"NJ023","truename":"李兰兰","storeId":"122","storeName":"南京公司光华门店"},{"salerno":"NJ214","truename":"王庆霞","storeId":"629","storeName":"南京公司光华门店"},{"salerno":"NJ213","truename":"杨向北","storeId":"628","storeName":"南京公司光华门店"},{"salerno":"NJ223","truename":"唐巍","storeId":"627","storeName":"南京公司光华门店"}]
     */

    private String code;
    private String info;
    /**
     * salerno : NJ023
     * truename : 李兰兰
     * storeId : 122
     * storeName : 南京公司光华门店
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String salerno;
        private String truename;
        private String storeId;
        private String storeName;

        public String getSalerno() {
            return salerno;
        }

        public void setSalerno(String salerno) {
            this.salerno = salerno;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }
    }
}
