package com.ww.administrator.demotest.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ActyInfo {

    /**
     * code : 200
     * info : success
     * data : [{"id":"29","type":"1","title":"6月活动","imgurl":"uploads/1464942882.jpg","href":"http://www.jvawa.com/new/game/AprilSpecial/special.php","sort":"1","status":"1","createtime":"2016-06-03 16:22:32"},{"id":"20","type":"1","title":"大转盘","imgurl":"uploads/1452045879.jpg","href":"#","sort":"2","status":"0","createtime":"2015-10-13 14:17:19"},{"id":"22","type":"1","title":"双十一活动","imgurl":"uploads/1446538960.jpg","href":"#","sort":"3","status":"0","createtime":"2015-11-02 10:36:06"}]
     */

    private String code;
    private String info;
    /**
     * id : 29
     * type : 1
     * title : 6月活动
     * imgurl : uploads/1464942882.jpg
     * href : http://www.jvawa.com/new/game/AprilSpecial/special.php
     * sort : 1
     * status : 1
     * createtime : 2016-06-03 16:22:32
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
        private String id;
        private String type;
        private String title;
        private String imgurl;
        private String href;
        private String sort;
        private String status;
        private String createtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
