package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class CouponsPicInfo {

    private String code;
    private String info;

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

    public static class DataBean{
        private String id;
        private String picurl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }
    }
}
